package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLModificacionCamarero;
import accesoDatos.Dispositivo;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.PedidoListo;

/**
 * FINALIZADA
 * 
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
		Document dom = XML.stringToXml(recibido);
		NodeList nodeListModificado = dom.getElementsByTagName("modificado");
		Node nodeModificado = nodeListModificado.item(0);
		int idComanda = Integer.parseInt(nodeModificado.getChildNodes().item(0)
				.getFirstChild().getNodeValue());
		int idMenu = Integer.parseInt(nodeModificado.getChildNodes().item(1)
				.getFirstChild().getNodeValue());
		int unidades = Integer.parseInt(nodeModificado.getChildNodes().item(2)
				.getFirstChild().getNodeValue());

		String[] idPedidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu,
				idComanda, "pedido");
		String[] idListos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu,
				idComanda, "listo");
		String[] idServidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu,
				idComanda, "servido");

		int total = idPedidos.length + idListos.length + idServidos.length;

		int diferencia = total - unidades;
		if (diferencia == total) { // cancelar todos los pedidos
			modificador.modificarEstadoPedido(idPedidos, "cancelado");
			modificador.modificarEstadoPedido(idListos, "cancelado");
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
				System.out.println("entro en no esta conectado");
				Inserciones modificador = new Inserciones();
				modificador.onOffDispositivo(0,
						dispositivo.getIdDisp());
				new HiloInsistente(dispositivo).start();
				modificador.setHiloLanzado(dispositivo.getIp(), 1);
			}
		}

	}
}
