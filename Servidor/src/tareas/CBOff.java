package tareas;

import java.net.Socket;

import accesoDatos.Inserciones;
import accesoDatos.Oraculo;

/**
 * FINALIZADA
 * 
 * Clase encargada de poner en estado apagado a los dispositivos que ejercen la
 * función de destino.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class CBOff extends Thread {

	private Socket socket;
	private Oraculo oraculo;
	private Inserciones modificador;

	/**
	 * Contructor
	 * 
	 * @param socket Socket que nos permite obtener la ip para saber qué
	 * dispositivo hay que poner en estado apagado
	 */
	public CBOff(Socket socket) {
		this.socket = socket;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}

	public void run() {
		desloguear();
	}

	/**
	 * Método encargado de extraer la ip del socket recibido y poner ese
	 * dispositivo como apagado en la base de datos
	 */
	private void desloguear() {
		String ip = socket.getInetAddress() + "";
		ip = ip.substring(1);
		int idDisp = oraculo.getIdDispositivoPorIp(ip);
		modificador.onOffDispositivo(0, idDisp);
	}

}
