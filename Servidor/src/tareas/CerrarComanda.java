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
 * Pone el estado de una comanda a cerrada en caso de que exista para la mesa solicitada.
 * Informa si la comanda no existe.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class CerrarComanda extends Thread{
	
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
	public CerrarComanda(Socket socket, String recibido){
		this.socket = socket;
		this.recibido = recibido;
		this.oraculo = new Oraculo();
		this.modificador = new Inserciones();
	}
	
	public void run(){
		cerrar();
	}
	
	/**
	 * Comprueba que existe una comanda activa para la mesa solicitada. En caso
	 * de no haberla informa de la incidencia. Si todo est� correcto cambia el estado
	 * de la comanda a cerrada.
	 */
	private void cerrar(){
		Document dom = XML.stringToXml(recibido);
		int idMesa = Integer.parseInt(dom.getElementsByTagName("idMesa").item(0).getFirstChild().getNodeValue());
		
		int idComanda = oraculo.getIdComandaActiva(idMesa);
		if(idComanda != 0){
			acuse("SI", idComanda+"");
			Servidor.escribirLog(Estados.info, "Se ha cerrado la comanda "+idComanda);
			modificador.cerrarComanda(idComanda);
			
			/* Avisamos a los destinos que la comanda se ha cobrado */
			XMLComandaAcabada xml = new XMLComandaAcabada(idComanda);
			String[] ipDestinos = oraculo.getIpDestinos();
			for(int contadorDestino = 0; contadorDestino < ipDestinos.length; contadorDestino++){
				Dispositivo dispositivo = new Dispositivo(ipDestinos[contadorDestino]);
				Conexion conexionDestino = null;
				 /* Comprobamos en la base de datos si est� conectado */
				if (dispositivo.getConectado()) {
					/* Vemos si realmente est� conectado */
					try {
						conexionDestino = new Conexion(dispositivo.getIp(), 27000);
					} catch (NullPointerException | IOException e1) {
						/* Cambiamos el estado del dispositivo en la base de datos a desconectado */
						modificador.onOffDispositivo(0,dispositivo.getIdDisp());
						new HiloInsistente(dispositivo).start();
						modificador.setHiloLanzado(dispositivo.getIp(), 1);
					}
					
					/* Si todo est� bien se env�a el mensaje */
					String mensaje = xml.xmlToString(xml.getDOM());
					try {
						conexionDestino.escribirMensaje(mensaje);
						conexionDestino.cerrarConexion();
					} catch (NullPointerException e) {
					} catch (IOException e) {
					}
				}
			}
		} else {
			Servidor.escribirLog(Estados.error, "La comanda "+idComanda+" no se pudo cerrar porque noe sta abierta.");
			acuse("NO", "No hay ninguna comanda abierta para esa mesa");
		}
	}
	
	/**
	 * Establece conexi�n con el emisor del mensaje y le devuelve una respuesta
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
