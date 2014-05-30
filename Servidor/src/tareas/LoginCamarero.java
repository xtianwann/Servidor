package tareas;

import java.io.IOException;
import java.net.Socket;

import org.w3c.dom.Document;

import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.Pedido;
import accesoDatos.PedidoPendiente;
import accesoDatos.Usuario;
import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLResultadoLoginCamarero;

/**
 * Esta clase se encarga de la identificaci�n de un camarero.
 * Tiene tres funciones principales: 
 * 1.- Comprobar si es un nuevo usuario en un dispositivo nuevo.
 * 2.- Comprobar si es un nuevo camarero en un dispositivo en el que ya
 * hab�a otro camarero, en tal caso asigna los pedidos del camarero anterior al
 * nuevo.
 * 3.- Comprobar si el nuevo usuario tiene pedidos pendientes (a parte de
 * los que pueda haber heredado de otro camarero) y pasarlos a ese dispositivo.
 * 
 * @author Juan Gabriel P�rez Leo
 * @author Cristian Mar�n Honor
 * 
 */
public class LoginCamarero extends Thread {

	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;

	public LoginCamarero(Socket socket, String recibido) {
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}

	public void run() {
		loguear();
	}

	private void loguear() {
		String resultado = "";
		int idUsuario = 0;
		Pedido[] pedidosPendientes = null;

		/* Leemos los datos del mensaje */
		Document dom = XML.stringToXml(recibido);
		String usuario = dom.getElementsByTagName("usuario").item(0).getFirstChild().getNodeValue();

		/* Comprobamos si el camarero existe en la base de datos */
		boolean existe = false;
		String[] camareros = oraculo.getCamareros();
		for (int camarero = 0; camarero < camareros.length; camarero++) {
			if (usuario.equals(camareros[camarero].toLowerCase())) {
				existe = true;
				idUsuario = oraculo.getIdUsuario(usuario);
				break;
			}
		}

		if (existe) {
			resultado = "OK";

			/* Comprobamos si ya hab�a otro usuario en ese dispositivo */
			String ip = socket.getInetAddress() + "";
			ip = ip.substring(1);
			System.out.println("ip login: " + ip);
			int[] idCamareros = oraculo.getCamarerosEnDispositivo(ip);
			if (idCamareros != null && idCamareros.length > 0) {
				/* Asignamos los pedidos de esos usuarios al nuevo */
				for(int usu = 0; usu < idCamareros.length; usu++)
					modificador.cambiarPedidosDeUsuario(idCamareros[usu], idUsuario);
				/* Desvinculamos los usuarios de ese dispositivo */
				for (int user = 0; user < idCamareros.length; user++) {
					Usuario usuarioAnterior = oraculo.getUsuarioById(idCamareros[user]);
					modificador.vinculoUsuarioDispositivo(usuarioAnterior.getNombre(), ip, 0);
				}
			}

			/*
			 * Asignamos el dispositivo al nuevo camarero y lo ponemos como
			 * encendido en la base de datos si no lo estaba ya
			 */
			modificador.vinculoUsuarioDispositivo(usuario, ip, 1);
		} else
			resultado = "NO";

		/* Finalmente enviamos el mensaje con el resultado del login */
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