package tareas;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLPedidoMesaServer;
import XMLServer.XMLPedidosPendientesCamarero;
import accesoDatos.Dispositivo;
import accesoDatos.Inserciones;
import accesoDatos.Mesa;
import accesoDatos.Oraculo;
import accesoDatos.Pedido;
import accesoDatos.Usuario;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Esta clase es la encargada de recibir la comanda enviada por el camarero,
 * insertar la información correspondiente en la base de datos, divide los
 * pedidos según el destino (cocina, barra, etc), genera los mensajes y los
 * envía a los correspondientes destinos. Finalmente, devuelve al camarero un
 * acuse de recibo con la información necesaria para que el camarero tenga una
 * lista de los pedidos pendientes de servir.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class PedidosComanda extends Thread {

	private Socket socket;
	private String recibido;
	private ArrayList<String> destinos;
	private int idMes;
	private Usuario usuario; // acabar la clase
	private Oraculo oraculo;

	public PedidosComanda(Socket socket, String recibido) {
		oraculo = new Oraculo();
		this.socket = socket;
		this.recibido = recibido;
		this.destinos = new ArrayList<>();
		this.usuario = Usuario.getUsuario(socket);
	}

	public void run() {
		dividirYEnviarSubcomandas(recibido);
	}

	private void dividirYEnviarSubcomandas(String recibido) {
		Document dom = XML.stringToXml(recibido);
		String resultado = "";
		String explicacion = "";

		/* Obtenemos los datos de la mesa */
		NodeList nodeListIdMes = dom.getElementsByTagName("idMes");
		idMes = Integer.parseInt(nodeListIdMes.item(0).getChildNodes().item(0)
				.getNodeValue());
		Mesa mesa = new Mesa(idMes);
		String nombreSeccion = oraculo.getNombreSeccionPorIdSeccion(mesa
				.getSeccion());

		/* Extraemos la lista de pedidos */
		ArrayList<Pedido> pedidos = new ArrayList<>();
		HashMap<Integer, ArrayList<Pedido>> mapaDestino = new HashMap<>();
		ArrayList<Dispositivo> dispositivos = new ArrayList<>();
		NodeList nodeListPedido = dom.getElementsByTagName("pedido");
		for (int contadorPedidos = 0; contadorPedidos < nodeListPedido
				.getLength(); contadorPedidos++) {
			Node nodePedido = nodeListPedido.item(contadorPedidos);
			NodeList items = nodePedido.getChildNodes();
			int idMenu = Integer.parseInt(items.item(0).getChildNodes().item(0)
					.getNodeValue());
			int unidades = Integer.parseInt(items.item(1).getChildNodes()
					.item(0).getNodeValue());

			Dispositivo dispositivo = Dispositivo.getDispositivo(idMenu);
			boolean dispEncontrado = false;
			for (Dispositivo disp : dispositivos) {
				if (disp.getIdDisp() == dispositivo.getIdDisp()) {
					dispEncontrado = true;
				}
			}
			if (!dispEncontrado) {
				dispositivos.add(dispositivo);
				mapaDestino.put(dispositivo.getIdDisp(),
						new ArrayList<Pedido>());
			}
			mapaDestino.get(dispositivo.getIdDisp()).add(
					new Pedido(idMenu, unidades));
			pedidos.add(new Pedido(idMenu, unidades));
		}
		/* Comprobamos si la mesa está activa */
		Inserciones insertor = new Inserciones();
		int idComanda = 0;
		if (mesa.isActiva()) {
			idComanda = insertor.insertarPedidos(mesa,
					pedidos.toArray(new Pedido[0]));
		} else {
			insertor.insertarNuevaComanda(mesa, usuario);
			idComanda = insertor.insertarPedidos(mesa,
					pedidos.toArray(new Pedido[0]));
		}

		/* Generamos una lista de xml según destinos */
		ArrayList<XMLPedidoMesaServer> listaXML = new ArrayList<>();
		for (int contadorDestino = 0; contadorDestino < dispositivos.size(); contadorDestino++) {
			listaXML.add(new XMLPedidoMesaServer(mesa, nombreSeccion,
					idComanda, mapaDestino.get(
							dispositivos.get(contadorDestino).getIdDisp())
							.toArray(new Pedido[0])));
		}
		XMLPedidosPendientesCamarero xmlPendientes = new XMLPedidosPendientesCamarero(
				mesa.getNomMes(), nombreSeccion, idComanda,
				pedidos.toArray(new Pedido[0]));
		String acuse = xmlPendientes.xmlToString(xmlPendientes.getDOM());
		Conexion conexionCamarero;
		try {
			conexionCamarero = new Conexion(socket);
			conexionCamarero.escribirMensaje(acuse);
			conexionCamarero.cerrarConexion();
		} catch (NullPointerException | IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		System.out.println("Enviado");

		// /* Intentamos conectar con el destino y enviarle la información */
		for (int contadorDestino = 0; contadorDestino < dispositivos.size(); contadorDestino++) {
			Dispositivo dispositivo = dispositivos.get(contadorDestino);
			Conexion conexionDestino = null;
			// /* Comprobamos en la base de datos si está conectado */
			if (dispositivo.getConectado()) {
				/* Vemos si realmente está conectado */
				try {
					conexionDestino = new Conexion(dispositivo.getIp(), 27000);
				} catch (NullPointerException | IOException e1) {
					/*
					 * Cambiamos el estado del dispositivo en la base de datos a
					 * desconectado
					 */
					System.out.println("entro en no esta conectado");
					Inserciones modificador = new Inserciones();
					modificador.actualizarEstadoDispositivo(0,dispositivo.getIdDisp());
					new HiloInsistente(dispositivo).start();
				}
				String mensaje = listaXML.get(contadorDestino).xmlToString(
						listaXML.get(contadorDestino).getDOM());
				System.out.println(mensaje);
				try {
					conexionDestino.escribirMensaje(mensaje);
					conexionDestino.cerrarConexion();
				} catch (NullPointerException e) {

				} catch (IOException e) {

				}
			} else {
				System.out.println("entro en no esta conectado en bd");
				new HiloInsistente(dispositivo).start();
			}
		}

		/* Finalmente se envía acuse de recibo al camarero que pidió la comanda */
		// String acuse = "";
		// if (resultado.equals("SI")) {
		// XMLPedidosPendientesCamarero xmlPendientes = new
		// XMLPedidosPendientesCamarero(
		// mesa.getNomMes(), nombreSeccion, idComanda,
		// pedidos.toArray(new Pedido[0]));
		// acuse = xmlPendientes.xmlToString(xmlPendientes.getDOM());
		// } else if (resultado.equals("NO")) {
		// XMLAcuseReciboServer xmlAcuse = new XMLAcuseReciboServer(resultado,
		// explicacion);
		// acuse = xmlAcuse.xmlToString(xmlAcuse.getDOM());
		// }
		//
		// try {
		// Conexion conexionCamarero = new Conexion(socket);
		// conexionCamarero.escribirMensaje(acuse);
		// conexionCamarero.cerrarConexion();
		// } catch (NullPointerException e) {
		// e.printStackTrace();
		// } catch (IOException ex) {
		// Logger.getLogger(PedidosComanda.class.getName()).log(Level.SEVERE,
		// null, ex);
		// }
	}
}