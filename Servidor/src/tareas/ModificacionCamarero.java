package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLModificacionCBServer;
import XMLServer.XMLModificacionCamarero;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.PedidoListo;

public class ModificacionCamarero extends Thread {

	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;

	public ModificacionCamarero(Socket socket, String recibido) {
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}

	public void run() {
		modificarYEnviar();
	}

	private void modificarYEnviar() {
		ArrayList<String> ipDestino = new ArrayList<>();
		HashMap<String, ArrayList<PedidoListo>> mapaDestino = new HashMap<>();

		Document dom = XML.stringToXml(recibido);
		NodeList nodeListModificado = dom.getElementsByTagName("modificado");
		for (int contadorModificado = 0; contadorModificado < nodeListModificado
				.getLength(); contadorModificado++) {
			Node nodeModificado = nodeListModificado.item(contadorModificado);
			int idComanda = Integer.parseInt(nodeModificado.getChildNodes()
					.item(0).getFirstChild().getNodeValue());
			int idMenu = Integer.parseInt(nodeModificado.getChildNodes()
					.item(1).getFirstChild().getNodeValue());
			int unidades = Integer.parseInt(nodeModificado.getChildNodes()
					.item(2).getFirstChild().getNodeValue());

			String[] idPedidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu,
					idComanda, "pedido");
			String[] idListos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu,
					idComanda, "listo");
			String[] idServidos = oraculo.getIdPedidoPorIdMenuYIdComanda(
					idMenu, idComanda, "servido");

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

			/* Vemos dónde debe mandarse */
			String ip = oraculo.getIdDestinoPorIdMenu(idMenu);
			if (!ipDestino.contains(ip)) {
				ipDestino.add(ip);
				mapaDestino.put(ip, new ArrayList<PedidoListo>());
			}
			mapaDestino.get(ip).add(new PedidoListo(idComanda, idMenu, unidades));
		}
		
		/* Finalmente se le envía a cada destino la modificación */
		for (int destino = 0; destino < ipDestino.size(); destino++) {
			PedidoListo[] pedidos = mapaDestino.get(ipDestino.get(destino)).toArray(new PedidoListo[0]);
			XMLModificacionCamarero xmlModificacion = new XMLModificacionCamarero(pedidos);
			Conexion conexion = null;
			try {
				conexion = new Conexion("192.168.43.55", 27013);
				conexion.escribirMensaje(xmlModificacion
						.xmlToString(xmlModificacion.getDOM()));
			} catch (NullPointerException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Cliente cliente = new Cliente();
			// cliente.run();
		}

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
	}

}
