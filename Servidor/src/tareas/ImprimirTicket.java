package tareas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import servidor.Servidor;
import servidor.Servidor.Estados;

import accesoDatos.Oraculo;
import accesoDatos.Pedido;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;

/**
 * Imprime un ticket en caso de que exista una comanda en la mesa solicitada
 * Informa si no existe comanda o si la comanda no tiene pedidos
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class ImprimirTicket extends Thread {

	private Socket socket;
	private String recibido;
	private Oraculo oraculo;

	/**
	 * Constructor
	 * 
	 * @param socket [Socket] socket por el que se ha establecido la comunicación
	 * @param recibido [String] mensaje recibido
	 */
	public ImprimirTicket(Socket socket, String recibido) {
		this.socket = socket;
		this.recibido = recibido;
		this.oraculo = new Oraculo();
	}

	public void run() {
		imprimir();
	}

	/**
	 * Hace todas las comprobaciones necesarias para informar de cualquier incidencia
	 * al camarero. Si todo está correcto procede a imprimir el ticket de la comanda.
	 */
	private void imprimir(){
		HashMap<Integer, Integer> mapaUnidades = new HashMap<>();
		ArrayList<Pedido> pedidos = new ArrayList<>();
		
		Document dom = XML.stringToXml(recibido);
		
		/* Obtiene todos los menús de una mesa */
		int idMesa = Integer.parseInt(dom.getElementsByTagName("idMesa").item(0).getFirstChild().getNodeValue());
		String[] resultados = oraculo.getMenusPorIdMesa(idMesa);
		
		if(resultados != null){
			if(resultados.length > 0){
				/* Llegados aquí es que todo está correcto y se procede a imprimir el ticket */
				acuse("OK" ,"");
				int idComanda = 0;
				/* Obtenemos la información necesaria */
				for(int contador = 0; contador < resultados.length; contador++){
					int idMenu = Integer.parseInt(resultados[contador]);
					idComanda = oraculo.getIdComandaPorIdMesa(idMesa);
					int unidades = oraculo.contarResultados(idMenu, idComanda);
					mapaUnidades.put(idMenu, unidades);
				}
				
				for(int contador = 0; contador < resultados.length; contador++){
					int idMenu = Integer.parseInt(resultados[contador]);
					int unidades = mapaUnidades.get(idMenu);
					float precio = oraculo.getPrecio(idMenu);
					Pedido pedido = new Pedido(idMenu, unidades, precio);
					pedidos.add(pedido);
				}
				
				/* Preparamos los datos para el ticket */
				String fechaTicket = oraculo.getFechaYHoraActual();
				fechaTicket = fechaTicket.replace("-", "");
				fechaTicket = fechaTicket.replace(":", "_");
				String nombreSeccion = oraculo.getNombreSeccionPorIdMesa(idMesa);
				String nombreMesa = oraculo.getNombreMesaPorIdMesa(idMesa);
				float totalTicket = 0;
				try {
					/* Creamos la carpeta si no existe en el destino */
					File carpeta = new File("./Tickets/");
					if(!carpeta.exists())
						carpeta.mkdir();
					
					/* Creamos el fichero e introducimos los datos */
					File fichero = new File(carpeta.getPath()+"/"+fechaTicket+" S_"+nombreSeccion+" M_"+nombreMesa+".txt");
					BufferedWriter escritor = new BufferedWriter(new FileWriter(fichero));
					String linea = "";
					for(int contador = 0; contador < pedidos.size(); contador++){
						int unidades = pedidos.get(contador).getUnidades();
						String nombreCantidad = pedidos.get(contador).getNombreCantidad();
						String nombreProducto = pedidos.get(contador).getNombreProducto();
						float precioUnitario = pedidos.get(contador).getPrecio();
						float precioTotalProducto = precioUnitario*unidades;
						totalTicket += precioTotalProducto;
						
						linea = unidades + " --- " + nombreCantidad + " " + nombreProducto + " --- " + precioUnitario + " --- " + precioTotalProducto;
						escritor.append(linea + "\r\n");
					}
					
					linea = "TOTAL: " + totalTicket;
					escritor.append(linea + "\n");
					escritor.flush();
					escritor.close();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Servidor.escribirLog(Estados.info, "Se ha impreso el ticket de la comanda "+idComanda);
			} else {
				/* En este caso hay comanda pero no tiene pedidos */
				acuse("NO", "No hay pedidos en la comanda");
			}
		} else {
			/* En este caso no existe ninguna comanda activa para esa mesa */
			acuse("NO", "No hay ninguna comanda abierta para esa mesa");
		}
	}
	
	/**
	 * Establece conexión con el emisor del mensaje y le devuelve una respuesta
	 * 
	 * @param respuesta
	 */
	private void acuse(String respuesta, String motivo){
		XMLAcuseReciboServer xml = new XMLAcuseReciboServer(respuesta, motivo);
		String mensaje = xml.xmlToString(xml.getDOM());
		try {
			Conexion conn = new Conexion(socket);
			conn.escribirMensaje(mensaje);
			conn.cerrarConexion();
		} catch (IOException ex) {
			Logger.getLogger(PedidosComanda.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
}
