package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;

public class PedidosServidos extends Thread{
	
	private Socket socket;
	private String recibido;
	private Inserciones modificador;
	private Oraculo oraculo;
	
	public PedidosServidos(Socket socket, String recibido){
		this.recibido = recibido;
		this.socket = socket;
		modificador = new Inserciones();
		oraculo = new Oraculo();
	}
	
	public void run(){
		actualizar();
	}
	
	private void actualizar(){
		Document dom = XML.stringToXml(recibido);
		NodeList nodeListFinalizados = dom.getElementsByTagName("pedido");
		for(int pedido = 0; pedido < nodeListFinalizados.getLength(); pedido++){
			Node nodePedido = nodeListFinalizados.item(pedido);
			Element elementoPedido = (Element) nodePedido;
			int idComanda = Integer.parseInt(elementoPedido.getAttribute("idCom"));
			int idMenu = Integer.parseInt(nodePedido.getChildNodes().item(0).getFirstChild().getNodeValue());
			int servidos = Integer.parseInt(nodePedido.getChildNodes().item(1).getFirstChild().getNodeValue());
			
			String[] idPedidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "servidos");
			String[] pedidosAModificar = new String[servidos - idPedidos.length];
			for(int servido = 0; servido < servidos; servido++){
				pedidosAModificar[servido] = idPedidos[servido];
			}
			modificador.modificarEstadoPedido(pedidosAModificar, "servido");
		}
		
		XMLAcuseReciboServer xmlAcuse = new XMLAcuseReciboServer("OK", "");
		String acuse = xmlAcuse.xmlToString(xmlAcuse.getDOM());
		try {
            Conexion conn = new Conexion(socket);
            conn.escribirMensaje(acuse);
            conn.cerrarConexion();
        } catch (IOException ex) {
            Logger.getLogger(PedidosComanda.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

}
