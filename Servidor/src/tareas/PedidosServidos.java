package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;

public class PedidosServidos extends Thread {

	private Socket socket;
	private String recibido;
	private Inserciones modificador;
	private Oraculo oraculo;

	public PedidosServidos(Socket socket, String recibido) {
		this.recibido = recibido;
		this.socket = socket;
		modificador = new Inserciones();
		oraculo = new Oraculo();
	}

	public void run() {
		actualizar();
	}

	private void actualizar() {
		boolean todosListos = false;
		
		Document dom = XML.stringToXml(recibido);
		NodeList nodeListFinalizados = dom.getElementsByTagName("pedido");
		for (int pedido = 0; pedido < nodeListFinalizados.getLength(); pedido++) {
			Node nodePedido = nodeListFinalizados.item(pedido);
			Element elementoPedido = (Element) nodePedido;
			int idComanda = Integer.parseInt(elementoPedido
					.getAttribute("idCom"));
			int idMenu = Integer.parseInt(nodePedido.getChildNodes().item(0)
					.getFirstChild().getNodeValue());
			int servidos = Integer.parseInt(nodePedido.getChildNodes().item(1)
					.getFirstChild().getNodeValue());

			String[] idServido = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "servido");

			if (servidos < idServido.length) {
				int diferencia = idServido.length - servidos;
				String[] cambiar = new String[diferencia];
				for (int cambiado = 0; cambiado < diferencia; cambiado++) {
					cambiar[cambiado] = idServido[cambiado];
				}
				modificador.modificarEstadoPedido(cambiar, "listo");
			} else if (servidos > idServido.length) {
				String[] idListos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "listos");
				int nuevos = servidos - idServido.length;
				if (nuevos > idListos.length) { // no debería darse nunca
					modificador.modificarEstadoPedido(idListos, "servido");
					todosListos = true;
				} else {
					String[] idNuevos = new String[nuevos];
					for (int nuevo = 0; nuevo < nuevos; nuevo++) {
						idNuevos[nuevo] = idListos[nuevo];
						modificador.modificarEstadoPedido(idNuevos, "servido");
					}
					
					idServido = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "servido");
					idListos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "listos");
					String[] idPedidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "pedidos");
					if(servidos == idServido.length + idListos.length + idPedidos.length)
						todosListos = true;
				}
			}
			
			if(todosListos){
				// enviar a cocina/barra
			}
		}

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
	}

}
