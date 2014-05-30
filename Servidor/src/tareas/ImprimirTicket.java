package tareas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import accesoDatos.Oraculo;
import accesoDatos.Pedido;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;

/**
 * Imprime un ticket en caso de que exista una comanda en la mesa solicitada
 * Informa si no existe comanda o la comanda no tiene pedidos
 * 
 * @author Juan GabrielPérez Leo
 * @author Cristian Marín Honor
 */
public class ImprimirTicket extends Thread {

	private Socket socket;
	private String recibido;
	private Oraculo oraculo;

	public ImprimirTicket(Socket socket, String recibido) {
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
	}

	public void run() {
		imprimir();
	}

	private void imprimir(){
		HashMap<Integer, Integer> mapaUnidades = new HashMap<>();
		ArrayList<Pedido> pedidos = new ArrayList<>();
		
		Document dom = XML.stringToXml(recibido);
		
		int idMesa = Integer.parseInt(dom.getElementsByTagName("idMesa").item(0).getFirstChild().getNodeValue());
		String[] resultados = oraculo.getMenusPorIdMesa(idMesa);
		
		if(resultados != null){
			if(resultados.length > 0){
				acuse("OK" ,"");
				
				for(int contador = 0; contador < resultados.length; contador++){
					int idMenu = Integer.parseInt(resultados[contador]);
					int idComanda = oraculo.getIdComandaPorIdMesa(idMesa);
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
				
				String fechaTicket = oraculo.getFechaYHoraActual();
				fechaTicket = fechaTicket.replace("-", "");
				fechaTicket = fechaTicket.replace(":", "_");
				String nombreSeccion = oraculo.getNombreSeccionPorIdMesa(idMesa);
				String nombreMesa = oraculo.getNombreMesaPorIdMesa(idMesa);
				float totalTicket = 0;
				try {
					String rutaCarpeta = "./Tickets/";
					File fichero = new File(rutaCarpeta+fechaTicket+" S_"+nombreSeccion+" M_"+nombreMesa+".txt");
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
			} else { // no hay pedidos en la comanda
				acuse("NO", "No hay pedidos en la comanda");
			}
		} else { // no hay comanda abierta en esa mesa
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
