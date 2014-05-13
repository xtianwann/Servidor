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
 * insertar la informaci�n correspondiente en la base de datos, divide los
 * pedidos seg�n el destino (cocina, barra, etc), genera los mensajes y los
 * env�a a los correspondientes destinos. Finalmente, devuelve al camarero un
 * acuse de recibo con la informaci�n necesaria para que el camarero tenga una
 * lista de los pedidos pendientes de servir.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
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
			if (!dispositivos.contains(dispositivo)) {
				dispositivos.add(dispositivo);
				mapaDestino.put(dispositivo.getIdDisp(),
						new ArrayList<Pedido>());
			}

			mapaDestino.get(dispositivo.getIdDisp()).add(
					new Pedido(idMenu, unidades));
			pedidos.add(new Pedido(idMenu, unidades));
		}

		/* Comprobamos si la mesa est� activa */
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

		/* Generamos una lista de xml seg�n destinos */
		ArrayList<XMLPedidoMesaServer> listaXML = new ArrayList<>();
		for (int contadorDestino = 0; contadorDestino < dispositivos.size(); contadorDestino++) {
			listaXML.add(new XMLPedidoMesaServer(mesa, nombreSeccion,
					idComanda, mapaDestino.get(
							dispositivos.get(contadorDestino).getIdDisp())
							.toArray(new Pedido[0])));
		}

		/* Intentamos conectar con el destino y enviarle la informaci�n */
		for (int contadorDestino = 0; contadorDestino < dispositivos.size(); contadorDestino++) {
			XMLPedidosPendientesCamarero xmlPendientes = new XMLPedidosPendientesCamarero(
					mesa.getNomMes(), nombreSeccion, idComanda,
					pedidos.toArray(new Pedido[0]));
			String acuse = xmlPendientes.xmlToString(xmlPendientes.getDOM());
			Conexion conexionCamarero;
			try {
				conexionCamarero = new Conexion(socket);
				conexionCamarero.escribirMensaje(acuse);
				conexionCamarero.cerrarConexion();
			} catch (NullPointerException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Enviado");
			return;
//			/* Comprobamos en la base de datos si est� conectado */
//			if (dispositivo.getConectado()) {
//				Conexion conexionDestino = null;
//				/* Vemos si realmente est� conectado */
//					try {
//						conexionDestino = new Conexion(dispositivo.getIp(),27000);
//						conexionDestino.escribirMensaje(listaXML.get(
//								contadorDestino).xmlToString(
//								listaXML.get(contadorDestino).getDOM()));
//						System.out.println("entro en esta conectado");
//						resultado = "SI";
//						try {
//							conexionDestino.cerrarConexion();
//						} catch (IOException e) {
//							
//						}
//					} catch (NullPointerException | IOException e) {
//						System.out.println("entro en no esta conectado");
//						/*
//						 * Cambiamos el estado del dispositivo en la base de datos a
//						 * desconectado
//						 */
//						Inserciones modificador = new Inserciones();
//						modificador.actualizarEstadoDispositivo(0);
//						resultado = "NO";
//						explicacion = dispositivo.getNombreDestino()
//								+ " est� desconectado";
//						XMLAcuseReciboServer xmlAcuse = new XMLAcuseReciboServer(resultado,
//								explicacion);
//						String acuse = xmlAcuse.xmlToString(xmlAcuse.getDOM());
//
//						Conexion conexionCamarero;
//						try {
//							conexionCamarero = new Conexion(socket);
//							conexionCamarero.escribirMensaje(acuse);
//							conexionCamarero.cerrarConexion();
//						} catch (NullPointerException | IOException ex) {
//							System.out.println("no se puede enviar acuse");
//						}
//						/* Dejamos un hilo comprobando si se conecta */
//						//new HiloInsistente(dispositivo).run();
//
//						/*
//						 * Preparamos la informaci�n a enviar en el acuse del
//						 * camarero
//						 */
//						
//					}
//			} else {
//				System.out.println("entro en no esta conectado en bd");
//				/* Dejamos un hilo comprobando si se conecta */
//				resultado = "NO";
//				explicacion = dispositivo.getNombreDestino()
//						+ " est� desconectado";
//				XMLAcuseReciboServer xmlAcuse = new XMLAcuseReciboServer(resultado,
//						explicacion);
//				String acuse = xmlAcuse.xmlToString(xmlAcuse.getDOM());
//
//				Conexion conexionCamarero;
//				try {
//					conexionCamarero = new Conexion(socket);
//					conexionCamarero.escribirMensaje(acuse);
//					conexionCamarero.cerrarConexion();
//					System.out.println("Enviado");
//				} catch (NullPointerException | IOException e) {
//					System.out.println("no se puede enviar acuse");
//				}
//				//new HiloInsistente(dispositivo).run();
//
//				/* Preparamos la informaci�n a enviar en el acuse del camarero */
//				
//			}
		}

//		/* Finalmente se env�a acuse de recibo al camarero que pidi� la comanda */
//		String acuse = "";
//		if (resultado.equals("SI")) {
//			XMLPedidosPendientesCamarero xmlPendientes = new XMLPedidosPendientesCamarero(
//					mesa.getNomMes(), nombreSeccion, idComanda,
//					pedidos.toArray(new Pedido[0]));
//			acuse = xmlPendientes.xmlToString(xmlPendientes.getDOM());
//		} else if (resultado.equals("NO")) {
//			XMLAcuseReciboServer xmlAcuse = new XMLAcuseReciboServer(resultado,
//					explicacion);
//			acuse = xmlAcuse.xmlToString(xmlAcuse.getDOM());
//		}
//
//		try {
//			Conexion conexionCamarero = new Conexion(socket);
//			conexionCamarero.escribirMensaje(acuse);
//			conexionCamarero.cerrarConexion();
//		} catch (NullPointerException e) {
//			e.printStackTrace();
//		} catch (IOException ex) {
//			Logger.getLogger(PedidosComanda.class.getName()).log(Level.SEVERE,
//					null, ex);
//		}
	}
}
