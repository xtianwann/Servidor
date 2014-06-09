package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import servidor.Servidor;
import servidor.Servidor.Estados;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLModificacionCamarero;
import accesoDatos.Dispositivo;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.PedidoListo;

/**
 * Clase encarda de cambiar los pedidos solicitados por el camarero de estado
 * pedido, listo o servidos (en ese orden) al estado cancelar. Esa información
 * se propaga a los dispositivos destino que les resulte relevante la información.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class ModificacionCamarero extends Thread {

	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;

	/**
	 * Constructor
	 * 
	 * @param socket [Socket] socket por el que se estableció la comuncación
	 * @param recibido [String] mensaje recibido
	 */
	public ModificacionCamarero(Socket socket, String recibido) {
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}

	public void run() {
		modificarYEnviar();
	}

	/**
	 * Realiza los cambios necesarios en la base de datos, comunica al emisor del
	 * mensaje que todo fue correcto y finalmente envía los cambios a los destinos
	 * para que ellos también los hagan.
	 */
	private void modificarYEnviar() {
		/* Obtenemos la información del mensaje recibido */
		Document dom = XML.stringToXml(recibido);
		NodeList nodeListModificado = dom.getElementsByTagName("modificado");
		Node nodeModificado = nodeListModificado.item(0);
		int idComanda = Integer.parseInt(nodeModificado.getChildNodes().item(0)
				.getFirstChild().getNodeValue());
		int idMenu = Integer.parseInt(nodeModificado.getChildNodes().item(1)
				.getFirstChild().getNodeValue());
		int unidades = Integer.parseInt(nodeModificado.getChildNodes().item(2)
				.getFirstChild().getNodeValue());

		/* Vemos qué es lo que hay en la base de datos */
		String[] idPedidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu,
				idComanda, "pedido");
		String[] idListos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu,
				idComanda, "listo");
		String[] idServidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu,
				idComanda, "servido");

		int total = idPedidos.length + idListos.length + idServidos.length;

		/* Cancelamos los pedidos en el orden lógico */
		int diferencia = total - unidades;
		if (diferencia == total) { // cancelar todos los pedidos
			if(idPedidos.length > 0)
				modificador.modificarEstadoPedido(idPedidos, "cancelado");
			if(idListos.length > 0)
				modificador.modificarEstadoPedido(idListos, "cancelado");
			if(idServidos.length > 0)
				modificador.modificarEstadoPedido(idServidos, "cancelado");
		} else {
			ArrayList<String> modificaciones = new ArrayList<>();
			for (String id : idPedidos) {
				if (diferencia > 0) {
					modificaciones.add(id);
					diferencia--;
				} else
					break;
			}
			for (String id : idListos) {
				if (diferencia > 0) {
					modificaciones.add(id);
					diferencia--;
				} else
					break;
			}
			for (String id : idPedidos) {
				if (diferencia > 0) {
					modificaciones.add(id);
					diferencia--;
				} else
					break;
			}

			modificador.modificarEstadoPedido(
					modificaciones.toArray(new String[0]), "cancelado");
		}
		
		/* Una vez están realizados los cambios vemos si lo que queda en esa comanda
		 * está totalmente servido */
		boolean finalizado = oraculo.getFinalizado(idComanda, idMenu);

		/* Acuse de recibo al emisor */
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

		/* Finalmente se le envía a cada destino la modificación */
		PedidoListo pedido = new PedidoListo(idComanda, idMenu, unidades);
		XMLModificacionCamarero xmlModificacion = new XMLModificacionCamarero(
				pedido, finalizado);
		String mensaje = xmlModificacion.xmlToString(xmlModificacion.getDOM());
		Dispositivo dispositivo = Dispositivo.getDispositivo(idMenu);
		Servidor.escribirLog(Estados.info, "Se han modificado algunos pedidos de la comanda "+idComanda);
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
