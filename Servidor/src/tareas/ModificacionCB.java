package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.PedidoListo;
import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLModificacionCBServer;

/**
 * Recibe uno o varios pedidos procedentes de cocina/barra para ser modificados.
 * Realiza los cambios pertinentes en la base de datos y finalmente env�a la informaci�n
 * necesaria a los camareros implicados. 
 * 
 * @author Juan Gabriel P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class ModificacionCB extends Thread{
	
	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;
	
	public ModificacionCB(Socket socket, String recibido){
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}
	
	public void run(){
		modificarYEnviar();
	}
	
	private void modificarYEnviar(){
		ArrayList<String> listaIp = new ArrayList<>();
		HashMap<String, ArrayList<PedidoListo>> mapaDestino = new HashMap<>();
		
		/* Preparamos la lista de pedidos rectificados para cada camarero que originalmente lo envi� */
		Document dom = XML.stringToXml(recibido);
		NodeList nodeListPedido = dom.getElementsByTagName("pedido");
		for(int contadorPedido = 0; contadorPedido < nodeListPedido.getLength(); contadorPedido++){
			Node nodePedido = nodeListPedido.item(contadorPedido);
			Element elementoPedido = (Element) nodePedido;
			int idComanda = Integer.parseInt(elementoPedido.getAttribute("idCom"));
			int idMenu = Integer.parseInt(nodePedido.getChildNodes().item(0).getFirstChild().getNodeValue());
			int listos = Integer.parseInt(nodePedido.getChildNodes().item(1).getFirstChild().getNodeValue());
			
			String ipCamarero = oraculo.getCamareroPorComanda(idComanda);
			
			if(!mapaDestino.containsKey(ipCamarero)){
				mapaDestino.put(ipCamarero, new ArrayList<PedidoListo>());
				listaIp.add(ipCamarero);
			}
			mapaDestino.get(ipCamarero).add(new PedidoListo(idComanda, idMenu, listos));
			
			/* Hacemos las modificaciones pertinentes en la base de datos */
			String[] idPedidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "listo");
			if(listos == 0){ // cocina/barra ha marcado el pedido que no es
				modificador.modificarEstadoPedido(idPedidos, "pedido");
			} else { // cocina/barra ha marcado m�s listos de los que son (me manda la cantidad correcta)
				String[] idPedidosAModificar = new String[listos];
				for(int pedido = 0; pedido < listos; pedido++){
					idPedidosAModificar[pedido] = idPedidos[pedido];
				}
				modificador.modificarEstadoPedido(idPedidosAModificar, "listo");
			}
		}
		
		/* Finalmente se le env�a a cada camarero la modificaci�n */
		for(int destino = 0; destino < listaIp.size(); destino++){
			PedidoListo[] pedidos = mapaDestino.get(listaIp.get(destino)).toArray(new PedidoListo[0]);
			XMLModificacionCBServer xmlModificacionesCB = new XMLModificacionCBServer(pedidos);
			Conexion conexion = null;
			try {
				//conexion = new Conexion("192.168.1.7",27000);
				conexion = new Conexion("localhost",5050);
				conexion.escribirMensaje(xmlModificacionesCB.xmlToString(xmlModificacionesCB.getDOM()));
			} catch (NullPointerException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
//			Cliente cliente = new Cliente();
//			cliente.run();
		}
		
		/* Acuse de recibo para el emisor */
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
