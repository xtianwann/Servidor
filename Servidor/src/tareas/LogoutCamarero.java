package tareas;

import java.io.IOException;
import java.net.Socket;

import org.w3c.dom.Document;

import accesoDatos.Inserciones;
import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLResultadoLogoutCamarero;

/**
 * Clase encargada de desloguear al camarero del sistema y poner su dispositivo como
 * apagado.
 * En caso de que tenga pedidos pendientes en el histórico, estos pedidos serán entregados
 * al camarero que tenga más comandas en secciones comunes. Si ningún camarero tuviese
 * comandas en alguna de las secciones que el que se está deslogueando, entonces los pedidos
 * serán asignados aleatoriamente.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 *
 */
public class LogoutCamarero extends Thread{
	
	private Socket socket;
	private String recibido;
	private Inserciones modificador;
	
	/**
	 * Constructor
	 * 
	 * @param socket [Socket] socket por el que se estableció la comunicación
	 * @param recibido [String] mensaje recibido
	 */
	public LogoutCamarero(Socket socket, String recibido){
		this.socket= socket;
		this.recibido = recibido;
		modificador = new Inserciones();
	}
	
	public void run(){
		desloguearCamarero();
	}
	
	/**
	 * Apaga el camarero en la base de datos y le responde para que se apague
	 */
	private void desloguearCamarero(){
		Document dom = XML.stringToXml(recibido);
		String usuario = dom.getElementsByTagName("usuario").item(0).getFirstChild().getNodeValue();
		String ip = socket.getInetAddress()+"";
		ip = ip.substring(1);
		/* Desvincula camarero y dispositivo y apaga el dispositivo */
		modificador.vinculoUsuarioDispositivo(usuario, ip, 0);
		
		/* Acuse para el camarero para que pueda cerrar la aplicación correctamente */
		XMLResultadoLogoutCamarero xmlLogout = new XMLResultadoLogoutCamarero("OK");
		String mensaje = xmlLogout.xmlToString(xmlLogout.getDOM());
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
