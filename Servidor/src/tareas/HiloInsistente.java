package tareas;

import java.io.IOException;

import Conexion.Conexion;
import XMLServer.XMLInfoAcumulada;
import accesoDatos.Dispositivo;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.PedidoPendiente;

public class HiloInsistente extends Thread {

	private Dispositivo dispositivo;
	private Conexion conectado;
	private Oraculo oraculo = new Oraculo();

	public HiloInsistente(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
		conectado = null;
	}

	public void run() {
		do {
			try {
				conectado = new Conexion(dispositivo.getIp(), 27000);
			} catch (NullPointerException | IOException e1) {
				System.out.println("Enviado infotodo a " + dispositivo.getIp());
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (conectado == null);
		/* Ponemos el dispositivo como conectado en la base de datos */
		Inserciones modificador = new Inserciones();
		modificador.actualizarEstadoDispositivo(1,dispositivo.getIdDisp());

		/* Consultamos los pedidos pendientes del destino */
		PedidoPendiente[] pedidosPendientes = null;
		if (dispositivo.getNombreDestino() != null) { // caso camarero -->
														// cocina
			pedidosPendientes = oraculo.getPedidosPendientes(dispositivo);
		} else { // caso cocina --> camarero
					// otra consulta
		}

		/* Generamos el xml con la información actualizada */
		XMLInfoAcumulada xml = new XMLInfoAcumulada(pedidosPendientes);
		/* Finalmente envía el mensaje generado */
		Conexion conn;
		try {
			conectado.escribirMensaje(xml.xmlToString(xml.getDOM()));
			conectado.cerrarConexion();
		} catch (NullPointerException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
