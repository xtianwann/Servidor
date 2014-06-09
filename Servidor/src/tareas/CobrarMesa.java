package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import servidor.Servidor;
import servidor.Servidor.Estados;

import accesoDatos.Dispositivo;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLComandaAcabada;

/**
 * Pone el estado de una comanda a pagada en caso de que exista para la mesa solicitada.
 * Informa si la comanda no existe, si la comanda no tiene pedidos o si los pedidos que
 * tiene aún no están totalmente servidos.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class CobrarMesa extends Thread{
	
	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;
	
	/**
	 * Constructor
	 * 
	 * @param socket
	 * @param recibido
	 */
	public CobrarMesa(Socket socket, String recibido){
		this.socket = socket;
		this.recibido = recibido;
		this.oraculo = new Oraculo();
		this.modificador = new Inserciones();
	}
	
	public void run(){
		cobrar();
	}
	
	/**
	 * Hace todas las comprobaciones necesarias para informar de cualquier incidencia
	 * al camarero. En caso de estar todo correcto procede a poner la comanda como pagada
	 * y avisa a todos los destinos.
	 */
	private void cobrar(){
		Document dom = XML.stringToXml(recibido);
		
		int idMesa = Integer.parseInt(dom.getElementsByTagName("idMesa").item(0).getFirstChild().getNodeValue());
		String[] resultados = oraculo.getMenusPorIdMesa(idMesa);
		
		if(resultados != null){
			if(resultados.length > 0){
				int idComanda = oraculo.getIdComandaPorIdMesa(idMesa);
				int pedidosYListos = oraculo.contarPedidosYListos(idComanda);
				if(pedidosYListos > 0){
					acuse("NO", "Hay pedidos sin servir");
				} else {
					acuse("SI", idComanda+"");
					Servidor.escribirLog(Estados.info, "Se ha cobrado la comanda "+idComanda);
					modificador.cobrarComanda(idComanda);
					
					/* Avisamos a los destinos que la comanda se ha cobrado */
					XMLComandaAcabada xml = new XMLComandaAcabada(idComanda);
					String[] ipDestinos = oraculo.getIpDestinos();
					for(int contadorDestino = 0; contadorDestino < ipDestinos.length; contadorDestino++){
						Dispositivo dispositivo = new Dispositivo(ipDestinos[contadorDestino]);
						Conexion conexionDestino = null;
						 /* Comprobamos en la base de datos si está conectado */
						if (dispositivo.getConectado()) {
							/* Vemos si realmente está conectado */
							try {
								conexionDestino = new Conexion(dispositivo.getIp(), 27000);
							} catch (NullPointerException | IOException e1) {
								/* Cambiamos el estado del dispositivo en la base de datos a desconectado */
								modificador.onOffDispositivo(0,dispositivo.getIdDisp());
								new HiloInsistente(dispositivo).start();
								modificador.setHiloLanzado(dispositivo.getIp(), 1);
							}
							
							/* Si todo está bien se envía el mensaje */
							String mensaje = xml.xmlToString(xml.getDOM());
							try {
								conexionDestino.escribirMensaje(mensaje);
								conexionDestino.cerrarConexion();
							} catch (NullPointerException e) {
							} catch (IOException e) {
							}
						}
					}
				}
				
			} else {
				acuse("NO", "No hay pedidos en la comanda");
			}
		} else {
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
