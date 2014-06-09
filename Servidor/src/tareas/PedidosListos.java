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

import servidor.Servidor;
import servidor.Servidor.Estados;

import accesoDatos.Dispositivo;
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
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class PedidosListos extends Thread{
	
	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;
	
	/**
	 * Constructor
	 * 
	 * @param socket [Socket] socket por el que cocina estableció la conexión con el servidor
	 * @param recibido [String] mensaje recibido
	 */
	public PedidosListos(Socket socket, String recibido){
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}
	
	public void run(){
		anotarYComunicar();
	}
	
	/**
	 * Extrae los pedidos del mensaje, los separa según el destino y se lo envía 
	 * a cada camarero que le corresponda.
	 */
	private void anotarYComunicar(){
		/* Obtiene los datos pasados en el mensaje */
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
		int comandaAnterior = 0;
		/* Obtenemos los datos de cada pedido */
		for(int contadorPedidos = 0; contadorPedidos < nodeListPedidos.getLength(); contadorPedidos++){
			nodePedido = nodeListPedidos.item(contadorPedidos);
			elementoPedido = (Element) nodePedido;
			idComanda = Integer.parseInt(elementoPedido.getAttribute("idCom"));
			int idMenu = Integer.parseInt(nodePedido.getChildNodes().item(0).getFirstChild().getNodeValue());
			int listos = Integer.parseInt(nodePedido.getChildNodes().item(1).getFirstChild().getNodeValue());
			/* Con estos datos modificamos el estado de lo que corresponda en la base de datos */
			String[] pedidosListos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "listo");
			String[] pedidosPendientes = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "pedido");
			String[] pedidosServidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "servido");
			String[] pedidosAModificar = new String[listos-pedidosListos.length-pedidosServidos.length];
			for(int pedido = 0; pedido < pedidosAModificar.length; pedido++){
				pedidosAModificar[pedido] = pedidosPendientes[pedido];
			}
			PedidoListo pedidoListo = new PedidoListo(idComanda, idMenu, listos);
			modificador.modificarEstadoAListo(pedidoListo, pedidosAModificar.length);
			
			/* Lo añadimos a la lista de pedidos según la ip que le corresponda */
			
			ipCamarero = oraculo.getCamareroPorComanda(pedidoListo.getIdComanda());
			mapaDestino.get(ipCamarero).add(pedidoListo);
			if(comandaAnterior != idComanda){
				comandaAnterior = idComanda;
				Servidor.escribirLog(Estados.info, "Existen pedidos listos de la comanda "+idComanda);
			}
		}
		
		/* Enviamos un acuse de recibo al emisor del mensaje recibido */
		XMLAcuseReciboServer xmlAcuse = new XMLAcuseReciboServer("OK", "");
		String acuse = xmlAcuse.xmlToString(xmlAcuse.getDOM());
		try {
            Conexion conn = new Conexion(socket);
            conn.escribirMensaje(acuse);
            conn.cerrarConexion();
        } catch (IOException ex) {
            Logger.getLogger(PedidosComanda.class.getName()).log(Level.SEVERE, null, ex);
        }
		/* Generamos los mensajes para cada destino y lo enviamos */
		for(int destino = 0; destino < listaIp.size(); destino++){
			PedidoListo[] pedidos = mapaDestino.get(listaIp.get(destino)).toArray(new PedidoListo[0]);
			
			Dispositivo dispositivo = new Dispositivo(listaIp.get(destino));
			/* Comprobamos en la base de datos si está conectado */
			if(dispositivo.getConectado()){
				/* Vemos si realmente estáconectado */
				Conexion conexion = null;
				try {
					conexion = new Conexion(dispositivo.getIp(), 27000);
				} catch (NullPointerException | IOException e1) {
					/* Cambiamos el estado del dispositivo en la base de datos a desconectado */
					modificador.onOffDispositivo(0, dispositivo.getIdDisp());
					new HiloInsistente(dispositivo).start();
					modificador.setHiloLanzado(dispositivo.getIp(), 1);
				}
				
				/* Si todo está bien se envía el mensaje */
				XMLPedidosListosServer xmlPedidosListos = new XMLPedidosListosServer(pedidos);
				try {
					conexion.escribirMensaje(xmlPedidosListos.xmlToString(xmlPedidosListos.getDOM()));
					conexion.cerrarConexion();
				} catch (NullPointerException e) {
				} catch (IOException e) {
				}
			}
		}
	}
}
