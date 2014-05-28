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
 * Esta clase se encarga de intentar conectar con un dispositivo que por algún motivo
 * ha perdido la conexión, ya que el hilo se llama cuando dicho dispositivo aparece 
 * como conectado en la base de datos. Una vez consigue establecer conexión con el dispositivo
 * le envía toda la información que se le ha acumulado en la ausencia mas la que tenía
 * anteriormente.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 *
 */
public class HiloInsistente extends Thread {

	private Dispositivo dispositivo;
	private Conexion conectado;
	private Oraculo oraculo;
	private Inserciones modificador;

	public HiloInsistente(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
		conectado = null;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}

	public void run() {
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
		} while (conectado == null);
		/* Ponemos el dispositivo como conectado en la base de datos */
		modificador.actualizarEstadoDispositivo(1,dispositivo.getIdDisp());

		/* Consultamos los pedidos pendientes del destino */
		PedidoPendiente[] pedidosPendientes = null;
		Pedido[] pendientesCamarero = null;
		if (dispositivo.getNombreDestino() != null) { // caso camarero --> cocina
			pedidosPendientes = oraculo.getPedidosPendientes(dispositivo);
		} else { // caso cocina --> camarero
			Usuario usuario = oraculo.getUsuarioByIp(dispositivo.getIp());
			pendientesCamarero = oraculo.getPedidosPendientes(usuario.getIdUsu());
		}

		/* Generamos el xml con la información actualizada */
		XMLInfoAcumulada xml = null;
		XMLPendientesCamareroAlEncender xmlCamarero = null;
		if(pedidosPendientes != null){
			xml = new XMLInfoAcumulada(pedidosPendientes);
		} else if(pendientesCamarero != null){
			xmlCamarero = new XMLPendientesCamareroAlEncender(pendientesCamarero);
		}
		
		/* Finalmente envía el mensaje generado */
		String mensaje = null;
		if(xml != null){
			mensaje = xml.xmlToString(xml.getDOM());
		} else if(xmlCamarero != null){
			mensaje = xmlCamarero.xmlToString(xmlCamarero.getDOM());
		}
		
		System.out.println("Enviando: " + mensaje);
		try {
			conectado.escribirMensaje(mensaje);
			conectado.cerrarConexion();
		} catch (NullPointerException | IOException e1) {
			e1.printStackTrace();
		}
		
		modificador.setHiloLanzado(dispositivo.getIp(), 0);
		modificador.actualizarEstadoDispositivo(1, dispositivo.getIdDisp());
	}

}
