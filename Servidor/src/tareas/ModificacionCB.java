package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import accesoDatos.Dispositivo;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.PedidoListo;
import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLModificacionCBServer;

/**
 * FINALIZADO
 * 
 * Recibe uno o varios pedidos procedentes de cocina/barra para ser modificados.
 * Realiza los cambios pertinentes en la base de datos y finalmente envía la
 * información necesaria a los camareros implicados.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class ModificacionCB extends Thread {

	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;

	/**
	 * Constructor
	 * 
	 * @param socket [Socket] socket a través del cuál se estableción la conexión
	 * @param recibido [String] mensaje recibido
	 */
	public ModificacionCB(Socket socket, String recibido) {
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}

	public void run() {
		modificarYEnviar();
	}

	/**
	 * 
	 */
	private void modificarYEnviar() {
		/*
		 * Preparamos la lista de pedidos rectificados para cada camarero que
		 * originalmente lo envió
		 */
		Document dom = XML.stringToXml(recibido);
		NodeList nodeListPedido = dom.getElementsByTagName("pedido");
		Node nodePedido = nodeListPedido.item(0);
		Element elementoPedido = (Element) nodePedido;
		int idComanda = Integer.parseInt(elementoPedido.getAttribute("idCom"));
		int idMenu = Integer.parseInt(nodePedido.getChildNodes().item(0)
				.getFirstChild().getNodeValue());
		int listos = Integer.parseInt(nodePedido.getChildNodes().item(1)
				.getFirstChild().getNodeValue());

		/* Hacemos las modificaciones pertinentes en la base de datos */
		String[] idListos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "listo");
		String[] idServidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "servido");
		int total = idListos.length + idServidos.length;
		System.out.println("listos: " + idListos.length);
		System.out.println("listos cocina: " + listos);
		if (listos == 0) { // cocina/barra ha marcado el pedido que no es
			modificador.modificarEstadoPedido(idListos, "pedido");
		} else { // cocina/barra ha marcado más listos de los que son (me
					// manda la cantidad correcta)
			System.out.println("listos a modificar: " + (total-listos));
			if(total-listos <= idListos.length){
				/* Acuse de recibo para el emisor */
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
				
				String[] idPedidosAModificar = new String[total-listos];
				for (int pedido = 0; pedido < idPedidosAModificar.length; pedido++) {
					idPedidosAModificar[pedido] = idListos[pedido];
				}
				modificador.modificarEstadoPedido(idPedidosAModificar, "pedido");
			} else {
				/* Acuse de recibo para el emisor */
				XMLAcuseReciboServer xmlAcuse = new XMLAcuseReciboServer("NO", "Algunos pedidos están servidos y ya no se pueden deshacer. Máximo permitido: " + idListos.length);
				String acuse = xmlAcuse.xmlToString(xmlAcuse.getDOM());
				try {
					Conexion conn = new Conexion(socket);
					conn.escribirMensaje(acuse);
					conn.cerrarConexion();
				} catch (IOException ex) {
					Logger.getLogger(PedidosComanda.class.getName()).log(Level.SEVERE,
							null, ex);
				}
			}
		}

		/* Finalmente se le envía a cada camarero la modificación */
		if(total-listos <= idListos.length){
			PedidoListo pedido = new PedidoListo(idComanda, idMenu, listos);
			XMLModificacionCBServer xmlModificacionesCB = new XMLModificacionCBServer(pedido);
			String mensaje = xmlModificacionesCB.xmlToString(xmlModificacionesCB.getDOM());
			String ip = oraculo.getCamareroPorComanda(idComanda);
			Dispositivo dispositivo = new Dispositivo(ip);
			
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
}
