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

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLPedidosServidos;
import accesoDatos.Dispositivo;
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
		/* Acuse para el emisor */
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

		boolean todosListos = false;
		ArrayList<Dispositivo> dispositivos = new ArrayList<>();
		HashMap<Integer, ArrayList<PedidoListo>> mapaServidos = new HashMap<>();

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

			String[] idServido = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu,
					idComanda, "servido");
			System.out.println("idServidos antes k nada" + idServido.length);
			// Aqui esta el error
			if (servidos < idServido.length) { // rectificacion
				int diferencia = idServido.length - servidos;
				String[] cambiar = new String[diferencia];
				for (int cambiado = 0; cambiado < diferencia; cambiado++) {
					cambiar[cambiado] = idServido[cambiado];
				}
				modificador.modificarEstadoPedido(cambiar, "listo");
				System.out.println("Modificacion de pedidos servidos");
			} else if (servidos > idServido.length) {
				String[] idListos = oraculo.getIdPedidoPorIdMenuYIdComanda(
						idMenu, idComanda, "listo");
				int nuevos = servidos - idServido.length;
				if (nuevos > idListos.length) { // no debería darse nunca
					System.out.println("No debo entrar aqui");
					modificador.modificarEstadoPedido(idListos, "servido");
					todosListos = true;
				} else {
					String[] idNuevos = new String[nuevos];
					for (int nuevo = 0; nuevo < nuevos; nuevo++) {
						idNuevos[nuevo] = idListos[nuevo];
					}
					modificador.modificarEstadoPedido(idNuevos, "servido");

					idServido = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu,
							idComanda, "servido");
					idListos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu,
							idComanda, "listo");
					String[] idPedidos = oraculo
							.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda,
									"pedido");
					System.out
							.println("Servidos "
									+ servidos
									+ " Unidades servidas "
									+ (idServido.length + idListos.length + idPedidos.length));
					System.out.println("Listos " + idListos.length
							+ " pedidos " + idPedidos.length);
					int total = idServido.length + idListos.length
							+ idPedidos.length;
					if (idServido.length == total) {
						todosListos = true;
						Dispositivo d = Dispositivo.getDispositivo(idMenu);
						boolean dispEncontrado = false;
						for (Dispositivo disp : dispositivos) {
							if (disp.getIdDisp() == d.getIdDisp()) {
								dispEncontrado = true;
								break;
							}
						}
						if (!dispEncontrado) {
							dispositivos.add(d);
							mapaServidos.put(d.getIdDisp(), new ArrayList<PedidoListo>());
						}
						mapaServidos.get(d.getIdDisp()).add(new PedidoListo(idComanda, idMenu, 0));
					}
				}
			}
		}

		if (todosListos) {
			for (int contador = 0; contador < dispositivos.size(); contador++) {
				System.out.println("todos servidos");
				Dispositivo dispositivo = dispositivos.get(contador);
				XMLPedidosServidos xmlFinalizados = new XMLPedidosServidos(mapaServidos.get(dispositivo.getIdDisp()).toArray(new PedidoListo[0]));
				String mensaje = xmlFinalizados.xmlToString(xmlFinalizados.getDOM());

				Conexion conexion = null;
				if (dispositivo.getConectado()) {
					/* Vemos si realmente está conectado */
					try {
						conexion = new Conexion(dispositivo.getIp(), 27000);
					} catch (NullPointerException | IOException e1) {
						/* Cambiamos el estado del dispositivo en la base de datos a desconectado */
						System.out.println("entro en no esta conectado");
						Inserciones modificador = new Inserciones();
						modificador.actualizarEstadoDispositivo(0,dispositivo.getIdDisp());
						new HiloInsistente(dispositivo).start();
						modificador.setHiloLanzado(dispositivo.getIp(), 1);
					}
					
					/* Si todo está bien se envía el mensaje */
					try {
						conexion.escribirMensaje(mensaje);
						conexion.cerrarConexion();
					} catch (NullPointerException e) {

					} catch (IOException e) {

					}
				}
			}
		}
	}
}
