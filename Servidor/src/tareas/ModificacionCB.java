package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Recibe uno o varios pedidos procedentes de cocina/barra para ser modificados.
 * Realiza los cambios pertinentes en la base de datos y finalmente envï¿½a la
 * informaciï¿½n necesaria a los camareros implicados.
 * 
 * @author Juan Gabriel Pï¿½rez Leo
 * @author Cristian Marï¿½n Honor
 */
public class ModificacionCB extends Thread {

	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;

	public ModificacionCB(Socket socket, String recibido) {
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}

	public void run() {
		modificarYEnviar();
	}

	private void modificarYEnviar() {
		
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

		String ipCamarero = oraculo.getCamareroPorComanda(idComanda);

		/* Hacemos las modificaciones pertinentes en la base de datos */
		String[] idPedidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "listo");
		if (listos == 0) { // cocina/barra ha marcado el pedido que no es
			modificador.modificarEstadoPedido(idPedidos, "pedido");
		} else { // cocina/barra ha marcado mï¿½s listos de los que son (me
					// manda la cantidad correcta)
			String[] idPedidosAModificar = new String[listos];
			for (int pedido = 0; pedido < listos; pedido++) {
				idPedidosAModificar[pedido] = idPedidos[pedido];
			}
			modificador.modificarEstadoPedido(idPedidosAModificar, "listo");
		}

		/* Finalmente se le envï¿½a a cada camarero la modificaciï¿½n */
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
				modificador.actualizarEstadoDispositivo(0,
						dispositivo.getIdDisp());
				new HiloInsistente(dispositivo).start();
				modificador.setHiloLanzado(dispositivo.getIp(), 1);
			}
		}
	}
}
