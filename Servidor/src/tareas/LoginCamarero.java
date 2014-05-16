package tareas;

import java.io.IOException;
import java.net.Socket;

import org.w3c.dom.Document;

import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLResultadoLoginCamarero;

public class LoginCamarero extends Thread{
	
	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;
	
	public LoginCamarero(Socket socket, String recibido){
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}
	
	public void run(){
		loguear();
	}
	
	private void loguear(){
		Document dom = XML.stringToXml(recibido);
		String usuario = dom.getElementsByTagName("usuario").item(0).getFirstChild().getNodeValue();
		
		boolean existe = false;
		String[] camareros = oraculo.getCamareros();
		for(int camarero = 0; camarero < camareros.length; camarero++){
			if(usuario.equals(camareros[camarero].toLowerCase())){
				existe = true;
				break;
			}
		}
		
		String resultado = "";
		if(existe){ // vincula camarero y dispositivo y coloca el dispositivo como conectado
			resultado = "OK";
			String ip = socket.getInetAddress()+"";
			ip = ip.substring(1);
			System.out.println("ip login: " + ip);
			modificador.vinculoUsuarioDispositivo(usuario, ip, 1);
		}
		else
			resultado = "NO";
		
		XMLResultadoLoginCamarero xmlResultado = new XMLResultadoLoginCamarero(resultado);
		String mensaje = xmlResultado.xmlToString(xmlResultado.getDOM());
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
		System.out.println("Enviado");
		
	}

}
