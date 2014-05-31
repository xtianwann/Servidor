package tareas;

import java.io.IOException;
import java.net.Socket;

import accesoDatos.Dispositivo;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.PedidoPendiente;
import Conexion.Conexion;
import XMLServer.XMLCocinaOn;

/**
 * FINALIZADO
 * 
 * Clase encargada de ver si el dispositivo (tipo destino) que ha dado el aviso
 * de que est� encendido, tiene informaci�n acumulada que deba necesitar.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 *
 */
public class CocinaOn extends Thread{
	
	private Socket socket;
	private Oraculo oraculo;
	private Inserciones modificador;
	
	/**
	 * Constructor
	 * 
	 * @param socket Socket a trav�s del cu�l el emisor est� estableciendo la conexi�n
	 */
	public CocinaOn(Socket socket){
		this.socket = socket;
		this.oraculo = new Oraculo();
		this.modificador = new Inserciones();
	}
	
	public void run(){
		encenderCocina();
	}
	
	/**
	 * M�todo encargado de poner en estado encendido al dispositivo en la base de datos.
	 * A continuaci�n busca si dicho dispositivo tiene pedidos pendientes, en caso afirmativo
	 * env�a dicha infomaci�n
	 */
	private void encenderCocina(){
		XMLCocinaOn xmlCocinaOn = null;
		String ip = socket.getInetAddress()+"";
		ip = ip.substring(1);
		
		if(!oraculo.isHiloLanzado(ip)){
			/* Encendemos el dispositivo en la base de datos */
			int idDisp = oraculo.getIdDispositivoPorIp(ip);
			modificador.onOffDispositivo(1, idDisp);
			
			/* Comprobamos si tiene algo pendiente */
			Dispositivo dispositivo = new Dispositivo(ip);
			PedidoPendiente[] pedidosPendientes = oraculo.getPedidosPendientes(dispositivo);
			if(pedidosPendientes != null && pedidosPendientes.length > 0){
				xmlCocinaOn = new XMLCocinaOn("OK+", pedidosPendientes);
			} else {
				xmlCocinaOn = new XMLCocinaOn("OK");
			}
		} else {
			xmlCocinaOn = new XMLCocinaOn("OK");
		}
		
		/* Enviamos el mensaje al destinatario */
		String mensaje = xmlCocinaOn.xmlToString(xmlCocinaOn.getDOM());
		System.out.println(mensaje);
		Conexion conexion;
		try {
			conexion = new Conexion(socket);
			conexion.escribirMensaje(mensaje);
			conexion.cerrarConexion();
		} catch (NullPointerException | IOException e3) {
			e3.printStackTrace();
		}
		System.out.println("Enviado");
		
	}

}
