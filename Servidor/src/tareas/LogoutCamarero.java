package tareas;

import java.io.IOException;
import java.net.Socket;

import org.w3c.dom.Document;

import accesoDatos.Inserciones;
import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLResultadoLogoutCamarero;

public class LogoutCamarero extends Thread{
	
	private Socket socket;
	private String recibido;
	private Inserciones modificador;
	
	public LogoutCamarero(Socket socket, String recibido){
		this.socket= socket;
		this.recibido = recibido;
		modificador = new Inserciones();
	}
	
	public void run(){
		desloguearCamarero();
	}
	
	private void desloguearCamarero(){
		Document dom = XML.stringToXml(recibido);
		String usuario = dom.getElementsByTagName("usuario").item(0).getFirstChild().getNodeValue();
		String ip = socket.getInetAddress()+"";
		ip = ip.substring(1);
		System.out.println("ip logout: " + ip);
		modificador.vinculoUsuarioDispositivo(usuario, ip, 0); // desvincula camarero y dispositivo y apaga el dispositivo
		
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
