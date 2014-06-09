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

import servidor.Servidor;
import servidor.Servidor.Estados;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLPedidosServidos;
import accesoDatos.Dispositivo;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.PedidoListo;

/**
 * Se encarga de cambiar el estado de los pedidos a servido en la base de datos
 * y comunica al destino que le interese si están todos las unidades de un pedido
 * servidas.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class PedidosServidos extends Thread {

	private Socket socket;
	private String recibido;
	private Inserciones modificador;
	private Oraculo oraculo;

	/**
	 * Constructor
	 * 
	 * @param socket [Socket] socket por el que el camarero estableció conexión con el servidor
	 * @param recibido [String] mensaje recibido
	 */
	public PedidosServidos(Socket socket, String recibido) {
		this.recibido = recibido;
		this.socket = socket;
		modificador = new Inserciones();
		oraculo = new Oraculo();
	}

	public void run() {
		actualizar();
	}

	/**
	 * Obtiene los datos del mensaje, realiza los cambios necesarios en la base de datos
	 * y si están todas las unidades de un pedido servidas informa al destino que le 
	 * interese para que lo borre del histórico.
	 */
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
		int comandaAnterior = 0;
		/* Extraemos la información del mensaje recibido */
		Document dom = XML.stringToXml(recibido);
		NodeList nodeListFinalizados = dom.getElementsByTagName("pedido");
		for (int pedido = 0; pedido < nodeListFinalizados.getLength(); pedido++) {
			Node nodePedido = nodeListFinalizados.item(pedido);
			Element elementoPedido = (Element) nodePedido;
			int idComanda = Integer.parseInt(elementoPedido.getAttribute("idCom"));
			int idMenu = Integer.parseInt(nodePedido.getChildNodes().item(0).getFirstChild().getNodeValue());
			int servidos = Integer.parseInt(nodePedido.getChildNodes().item(1).getFirstChild().getNodeValue());

			/* Vemós qué hay en la base de datos y procedemos a cambiar el estado de los pedidos */
			String[] idServido = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "servido");
			
			if (servidos < idServido.length) { // rectificacion
				int diferencia = idServido.length - servidos;
				String[] cambiar = new String[diferencia];
				for (int cambiado = 0; cambiado < diferencia; cambiado++) {
					cambiar[cambiado] = idServido[cambiado];
				}
				modificador.modificarEstadoPedido(cambiar, "listo");
			} else if (servidos > idServido.length) {
				String[] idListos = oraculo.getIdPedidoPorIdMenuYIdComanda(
						idMenu, idComanda, "listo");
				int nuevos = servidos - idServido.length;
				if (nuevos > idListos.length) { // no debería darse nunca
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
					int total = idServido.length + idListos.length + idPedidos.length;
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
			if(comandaAnterior != idComanda){
				comandaAnterior = idComanda;
				Servidor.escribirLog(Estados.info, "Existen pedidos servidos de la comanda "+idComanda);
			}
		}
		/* Si están todos servidos se lo comunica al destino */
		if (todosListos) {
			for (int contador = 0; contador < dispositivos.size(); contador++) {
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
						Inserciones modificador = new Inserciones();
						modificador.onOffDispositivo(0,dispositivo.getIdDisp());
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
