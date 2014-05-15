package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLPedidosServidos;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.PedidoListo;

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
		ArrayList<PedidoListo> totalmenteServidos = new ArrayList<>();
		
		Document dom = XML.stringToXml(recibido);
		NodeList nodeListFinalizados = dom.getElementsByTagName("pedido");
		for (int pedido = 0; pedido < nodeListFinalizados.getLength(); pedido++) {
			Node nodePedido = nodeListFinalizados.item(pedido);
			Element elementoPedido = (Element) nodePedido;
			int idComanda = Integer.parseInt(elementoPedido.getAttribute("idCom"));
			int idMenu = Integer.parseInt(nodePedido.getChildNodes().item(0).getFirstChild().getNodeValue());
			int servidos = Integer.parseInt(nodePedido.getChildNodes().item(1).getFirstChild().getNodeValue());

			String[] idServido = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "servido");

			if (servidos < idServido.length) {
				int diferencia = idServido.length - servidos;
				String[] cambiar = new String[diferencia];
				for (int cambiado = 0; cambiado < diferencia; cambiado++) {
					cambiar[cambiado] = idServido[cambiado];
				}
				modificador.modificarEstadoPedido(cambiar, "listo");
			} else if (servidos > idServido.length) {
				String[] idListos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "listo");
				int nuevos = servidos - idServido.length;
				if (nuevos > idListos.length) { // no deber�a darse nunca
					modificador.modificarEstadoPedido(idListos, "servido");
					todosListos = true;
				} else {
					String[] idNuevos = new String[nuevos];
					for (int nuevo = 0; nuevo < nuevos; nuevo++) {
						idNuevos[nuevo] = idListos[nuevo];
					}
					modificador.modificarEstadoPedido(idNuevos, "servido");
					
					idServido = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "servido");
					idListos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "listo");
					String[] idPedidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "pedido");
					if(servidos == idServido.length + idListos.length + idPedidos.length){
						todosListos = true;
						totalmenteServidos.add(new PedidoListo(idComanda, idMenu, 0));
					}
				}
			}
		}
		
		if(todosListos){
			XMLPedidosServidos xmlFinalizados = new XMLPedidosServidos(totalmenteServidos.toArray(new PedidoListo[0]));
			String xml = xmlFinalizados.xmlToString(xmlFinalizados.getDOM());
			try {
				Conexion conexionDestino = new Conexion("192.168.1.2", 27000);
				conexionDestino.escribirMensaje(xml);
				conexionDestino.cerrarConexion();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
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
