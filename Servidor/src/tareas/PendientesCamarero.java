package tareas;

import java.io.IOException;
import java.net.Socket;

import org.w3c.dom.Document;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLPendientesCamareroAlEncender;
import accesoDatos.Oraculo;
import accesoDatos.Pedido;

/**
 * Clase encargada de ver si un camarero tiene pedidos pendientes y se los 
 * envía. En caso de no tener pedidos se le informa en el mensaje enviado
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 *
 */
public class PendientesCamarero extends Thread{
	
	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	
	/**
	 * Constructor
	 * 
	 * @param socket [Socket] socket por el que el camarero estableció conexión con el servidor
	 * @param recibido [String] mensaje recibido
	 */
	public PendientesCamarero(Socket socket, String recibido){
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
	}
	
	public void run(){
		obtenerPendientes();
	}
	
	/**
	 * Obtiene el nombre del camarero y la ip desde donde se está conectando,
	 * comprueba que no hay ningún hilo que esté intentando conectar a esa ip,
	 * si es que no, entonces obtiene todos los pedidos pendientes, si lo hay,
	 * y los envía
	 */
	private void obtenerPendientes(){
		/* Obtenemos la información proporcionada en el mensaje */
		Document dom = XML.stringToXml(recibido);
		String nomUsu = dom.getElementsByTagName("usuario").item(0).getFirstChild().getNodeValue();
		
		/* Vemos si ya hay un hilo lanzado intentando conectar con el camarero */
		Pedido[] pedidos = null;
		String ip = socket.getInetAddress()+"";
		ip = ip.substring(1);
		if(!oraculo.isHiloLanzado(ip)){
			int idUsu = oraculo.getIdUsuario(nomUsu);
			pedidos = oraculo.getPedidosPendientes(idUsu);
		}
		
		/* Generamos el mensaje y lo enviamos */
		XMLPendientesCamareroAlEncender xmlPendientes = new XMLPendientesCamareroAlEncender(pedidos);
		String mensaje = xmlPendientes.xmlToString(xmlPendientes.getDOM());
		
		Conexion conexion;
		try {
			conexion = new Conexion(socket);
			conexion.escribirMensaje(mensaje);
			conexion.cerrarConexion();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
