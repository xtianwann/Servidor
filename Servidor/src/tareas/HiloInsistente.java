package tareas;

import java.io.IOException;

import Conexion.Conexion;
import XMLServer.XMLInfoAcumulada;
import XMLServer.XMLPendientesCamareroAlEncender;
import accesoDatos.Dispositivo;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.Pedido;
import accesoDatos.PedidoPendiente;
import accesoDatos.Usuario;

/**
 * FINALIZADA
 * 
 * Esta clase se encarga de intentar conectar con un dispositivo que por alg�n motivo
 * ha perdido la conexi�n, ya que el hilo se llama cuando dicho dispositivo aparece 
 * como conectado en la base de datos. Una vez consigue establecer conexi�n con el dispositivo
 * le env�a toda la informaci�n que se le ha acumulado en la ausencia mas la que ten�a
 * anteriormente.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class HiloInsistente extends Thread {

	private Dispositivo dispositivo;
	private Conexion conectado;
	private Oraculo oraculo;
	private Inserciones modificador;
	private boolean parar;

	/**
	 * Constructor
	 * 
	 * @param dispositivo [Dispositivo] dispositivo con el que debe reintentar la conexi�n
	 */
	public HiloInsistente(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
		this.conectado = null;
		this.oraculo = new Oraculo();
		this.modificador = new Inserciones();
		this.parar = false;
	}

	public void run() {
		reconexion();
	}

	/**
	 * Intenta conectar con el dispositivo cada dos segundos, cuando lo consigue
	 * env�a toda la informaci�n que necesita para estar actualizado y empezar a
	 * trbajar correctamente.
	 */
	private void reconexion(){
		do {
			try {
				conectado = new Conexion(dispositivo.getIp(), 27000);
			} catch (NullPointerException | IOException e1) {
				System.out.println(conectado + " Conectando con: " + dispositivo.getIp());
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			parar = oraculo.isHiloLanzado(dispositivo.getIp());
			
		} while (conectado == null && !parar);
		
		if(!parar){
			/* Ponemos el dispositivo como conectado en la base de datos */
			modificador.onOffDispositivo(1,dispositivo.getIdDisp());
	
			/* Consultamos los pedidos pendientes del destino */
			PedidoPendiente[] pedidosPendientes = null;
			Pedido[] pendientesCamarero = null;
			if (dispositivo.getNombreDestino() != null) { // caso camarero --> cocina
				pedidosPendientes = oraculo.getPedidosPendientes(dispositivo);
			} else { // caso cocina --> camarero
				Usuario usuario = oraculo.getUsuarioByIp(dispositivo.getIp());
				pendientesCamarero = oraculo.getPedidosPendientes(usuario.getIdUsu());
			}
	
			/* Generamos el xml con la informaci�n actualizada */
			XMLInfoAcumulada xml = null;
			XMLPendientesCamareroAlEncender xmlCamarero = null;
			if(pedidosPendientes != null){
				xml = new XMLInfoAcumulada(pedidosPendientes);
			} else if(pendientesCamarero != null){
				xmlCamarero = new XMLPendientesCamareroAlEncender(pendientesCamarero);
			}
			
			/* Finalmente env�a el mensaje generado */
			String mensaje = null;
			if(xml != null){
				mensaje = xml.xmlToString(xml.getDOM());
			} else if(xmlCamarero != null){
				mensaje = xmlCamarero.xmlToString(xmlCamarero.getDOM());
			}
			
			try {
				conectado.escribirMensaje(mensaje);
				conectado.cerrarConexion();
			} catch (NullPointerException | IOException e1) {
				e1.printStackTrace();
			}
			
			/* Cambia el estado de hilo a no lanzado y enciende el dispositivo en la base de datos */
			modificador.setHiloLanzado(dispositivo.getIp(), 0);
			modificador.onOffDispositivo(1, dispositivo.getIdDisp());
		}
	}
}
