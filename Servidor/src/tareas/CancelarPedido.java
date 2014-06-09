package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import servidor.Servidor;
import servidor.Servidor.Estados;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLDevolverServer;
import accesoDatos.Dispositivo;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.PedidoListo;

/**
 * Clase encargada de cancelar los pedidos que solicite un camarero y ya estén servidos.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class CancelarPedido extends Thread {

	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;

	/**
	 * Constructor
	 * 
	 * @param socket [Socket] socket a través del cuál se enviará el acuse al camarero
	 * @param recibido [String] mensaje recibido en el que se idica qué pedido cancelar
	 */
	public CancelarPedido(Socket socket, String recibido) {
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}

	public void run() {
		cancelarYEnviar();
	}

	/**
	 * Método encargado de extraer la información del mensaje, cambiar el estado de los 
	 * pedidos en la base de datos y enviar los cambios al destinatario.
	 */
	private void cancelarYEnviar() {

		/* Acuse de recibo para el camarero */
		XMLAcuseReciboServer xmlAcuse = new XMLAcuseReciboServer("OK", "");
		String acuse = xmlAcuse.xmlToString(xmlAcuse.getDOM());
		try {
			Conexion conn = new Conexion(socket);
			conn.escribirMensaje(acuse);
			conn.cerrarConexion();
		} catch (IOException ex) {
			Logger.getLogger(PedidosComanda.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		/* Obtenemos la información del mensaje recibido */
		Document dom = XML.stringToXml(recibido);

		int idComanda = Integer.parseInt(dom.getElementsByTagName("idCom")
				.item(0).getFirstChild().getNodeValue());
		int idMenu = Integer.parseInt(dom.getElementsByTagName("idMenu")
				.item(0).getFirstChild().getNodeValue());
		int unidades = Integer.parseInt(dom.getElementsByTagName("unidades")
				.item(0).getFirstChild().getNodeValue());

		/* Cancelamos el pedido correspondiente en la base de datos */
		String[] idServidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu,
				idComanda, "servido");

		String[] aBorrar = new String[unidades];
		for (int contador = 0; contador < unidades; contador++) {
			aBorrar[contador] = idServidos[contador];
		}
		modificador.modificarEstadoPedido(aBorrar, "cancelado");

		/* Enviamos la información a cocina/barra */
		PedidoListo pedido = new PedidoListo(idComanda, idMenu, unidades);
		XMLDevolverServer xmlDevolver = new XMLDevolverServer(pedido);
		String mensaje = xmlDevolver.xmlToString(xmlDevolver.getDOM());
		Dispositivo dispositivo = Dispositivo.getDispositivo(idMenu);
		Servidor.escribirLog(Estados.info, "Se han devuelto pedidos de la comanda "+idComanda);
		Conexion conexion = null;
		/* Comprobamos en la base de datos si está conectado */
		if (dispositivo.getConectado()) {
			/* Vemos si realmente está conectado */
			try {
				conexion = new Conexion(dispositivo.getIp(), 27000);
				conexion.escribirMensaje(mensaje);
				conexion.cerrarConexion();
			} catch (NullPointerException | IOException e1) {
				/*
				 * Cambiamos el estado del dispositivo en la base de datos a
				 * desconectado
				 */
				Inserciones modificador = new Inserciones();
				modificador.onOffDispositivo(0,
						dispositivo.getIdDisp());
				new HiloInsistente(dispositivo).start();
				modificador.setHiloLanzado(dispositivo.getIp(), 1);
			}
		}

	}

}
