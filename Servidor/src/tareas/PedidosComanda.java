package tareas;

import Conexion.Conexion;
import XML.XML;
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
	private int idMes;
	private Usuario usuario;
	private Oraculo oraculo;

	/**
	 * Constructor
	 * 
	 * @param socket [Socket] socket en el que el camarero estableció la conexión con el servidor
	 * @param recibido [String] mensaje recibido
	 */
	public PedidosComanda(Socket socket, String recibido) {
		oraculo = new Oraculo();
		this.socket = socket;
		this.recibido = recibido;
		this.usuario = Usuario.getUsuario(socket);
	}

	public void run() {
		dividirYEnviarSubcomandas();
	}

	/**
	 * Obtiene la información del mensaje, envía acuse al emisor, divide según el destino de los
	 * pedidos y envía un mensaje con lo que le correspone a cada destino
	 */
	private void dividirYEnviarSubcomandas() {
		Document dom = XML.stringToXml(recibido);

		/* Obtenemos los datos de la mesa */
		NodeList nodeListIdMes = dom.getElementsByTagName("idMes");
		idMes = Integer.parseInt(nodeListIdMes.item(0).getChildNodes().item(0)
				.getNodeValue());
		Mesa mesa = new Mesa(idMes);
		String nombreSeccion = oraculo.getNombreSeccionPorIdSeccion(mesa
				.getSeccion());
		
		/* Comprobamos si quien realiza el pedido es el dueño de la comanda */
		int idUsuarioPedidor = usuario.getIdUsu();
		int idCom = oraculo.getIdComandaPorIdMesa(idMes);
		int idPropietario = (idCom != 0) ? oraculo.getIdUsuPorIdComanda(idCom) : idUsuarioPedidor;
		
		if(idUsuarioPedidor == idPropietario){
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
			
			/* Devolvemos acuse al camarero con los pedidos que acaba de hacer y la info adicional */
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
				e3.printStackTrace();
			}
	
			/* Intentamos conectar con el destino y enviarle la información */
			for (int contadorDestino = 0; contadorDestino < dispositivos.size(); contadorDestino++) {
				Dispositivo dispositivo = dispositivos.get(contadorDestino);
				Conexion conexionDestino = null;
				 /* Comprobamos en la base de datos si está conectado */
				if (dispositivo.getConectado()) {
					/* Vemos si realmente está conectado */
					try {
						conexionDestino = new Conexion(dispositivo.getIp(), 27000);
					} catch (NullPointerException | IOException e1) {
						/* Cambiamos el estado del dispositivo en la base de datos a desconectado */
						Inserciones modificador = new Inserciones();
						modificador.onOffDispositivo(0,dispositivo.getIdDisp());
						new HiloInsistente(dispositivo).start();
						modificador.setHiloLanzado(dispositivo.getIp(), 1);
					}
					
					/* Si todo está bien se envía el mensaje */
					String mensaje = listaXML.get(contadorDestino).xmlToString(listaXML.get(contadorDestino).getDOM());
					try {
						conexionDestino.escribirMensaje(mensaje);
						conexionDestino.cerrarConexion();
					} catch (NullPointerException e) {
	
					} catch (IOException e) {
	
					}
				}
			}
		} else {
			XMLPedidosPendientesCamarero xmlPendientes = new XMLPedidosPendientesCamarero("nopuedeshacerunpedido", "0", 0, new Pedido[0]);
			String acuse = xmlPendientes.xmlToString(xmlPendientes.getDOM());
			Conexion conexionCamarero;
			try {
				conexionCamarero = new Conexion(socket);
				conexionCamarero.escribirMensaje(acuse);
				conexionCamarero.cerrarConexion();
			} catch (NullPointerException | IOException e3) {
				e3.printStackTrace();
			}
		}
	}
}