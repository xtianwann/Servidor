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
import XMLServer.XMLPedidosListosServer;

/**
 * Esta clase se encarga de modificar en la base de datos los pedidos a 
 * estado listo, devuelve un acuse de recibo al emisor y propaga el mensaje
 * al dispositivo del camarero que originalmente envió la comanda a la 
 * que pertenecen dichos pedidos.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class PedidosListos extends Thread{
	
	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;
	
	public PedidosListos(Socket socket, String recibido){
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}
	
	public void run(){
		anotarYComunicar();
	}
	
	private void anotarYComunicar(){
		Document dom = XML.stringToXml(recibido);
		NodeList nodeListPedidos = dom.getElementsByTagName("pedido");
		Node nodePedido = null;
		Element elementoPedido = null;
		int idComanda = 0;
		String ipCamarero = "";
		ArrayList<String> listaIp = new ArrayList<>();
		HashMap<String, ArrayList<PedidoListo>> mapaDestino = new HashMap<>();
		
		/* Almacenamos la ip de los distintos camareros que deben recibir los pedidos según comanda */
		for(int contadorPedidos = 0; contadorPedidos < nodeListPedidos.getLength(); contadorPedidos++){
			nodePedido = nodeListPedidos.item(contadorPedidos);
			elementoPedido = (Element) nodePedido;
			idComanda = Integer.parseInt(elementoPedido.getAttribute("idCom"));
			ipCamarero = oraculo.getCamareroPorComanda(idComanda);
			if(!mapaDestino.containsKey(ipCamarero)){
				mapaDestino.put(ipCamarero, new ArrayList<PedidoListo>());
				listaIp.add(ipCamarero);
			}
		}
		
		/* Obtenemos los datos de cada pedido */
		for(int contadorPedidos = 0; contadorPedidos < nodeListPedidos.getLength(); contadorPedidos++){
			nodePedido = nodeListPedidos.item(contadorPedidos);
			elementoPedido = (Element) nodePedido;
			idComanda = Integer.parseInt(elementoPedido.getAttribute("idCom"));
			int idMenu = Integer.parseInt(nodePedido.getChildNodes().item(0).getFirstChild().getNodeValue());
			int listos = Integer.parseInt(nodePedido.getChildNodes().item(1).getFirstChild().getNodeValue());
			
			/* Con estos datos modificamos el estado de lo que corresponda en la base de datos */
			String[] pedidosListos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda,"listo");
			String[] pedidosPendientes = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda,"pedido");
			String[] pedidosAModificar = new String[listos-pedidosListos.length];
			for(int pedido = 0; pedido < pedidosAModificar.length; pedido++){
				pedidosAModificar[pedido] = pedidosPendientes[pedido];
			}
			modificador.modificarEstadoPedido(pedidosAModificar, "listo");
			
			/* Lo añadimos a la lista de pedidos según la ip que le corresponda */
			PedidoListo pedidoListo = new PedidoListo(idComanda, idMenu, listos);
			ipCamarero = oraculo.getCamareroPorComanda(pedidoListo.getIdComanda());
			mapaDestino.get(ipCamarero).add(pedidoListo);
		}
		
		/* Generamos los mensajes para cada destino y lo enviamos */
		for(int destino = 0; destino < listaIp.size(); destino++){
			PedidoListo[] pedidos = mapaDestino.get(listaIp.get(destino)).toArray(new PedidoListo[0]);
			XMLPedidosListosServer xmlPedidosListos = new XMLPedidosListosServer(pedidos);
			Conexion conexion = null;
			try {
				conexion = new Conexion("192.168.1.7",27000);
				conexion.escribirMensaje(xmlPedidosListos.xmlToString(xmlPedidosListos.getDOM()));
			} catch (NullPointerException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				conexion.cerrarConexion();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		XMLPedidosListosServer xmlPedidosListos = new XMLPedidosListosServer(pedidos);
//		Conexion conexion = null;
//		try {
//			conexion = new Conexion("192.168.1.6",27012);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		conexion.escribirMensaje(xmlPedidosListos.xmlToString(xmlPedidosListos.getDOM()));
		
		/* Finalmente enviamos un acuse de recibo al emisor del mensaje recibido */
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
