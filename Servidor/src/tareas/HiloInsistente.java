package tareas;

import Conexion.Conexion;
import accesoDatos.Dispositivo;
import accesoDatos.Oraculo;
import accesoDatos.PedidoPendiente;

public class HiloInsistente extends Thread {

	private Dispositivo dispositivo;
	private boolean conectado;
	private Oraculo oraculo = new Oraculo();

	public HiloInsistente(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
		conectado = false;
	}

	public void run() {
		do {
			conectado = Conexion.hacerPing(dispositivo.getIp());
			if (!conectado) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (!conectado);
		
		/* Consltamos los pedidos pendientes del destino */
		if(dispositivo.getNombreDestino() != null){ // caso camarero --> cocina
			PedidoPendiente[] pedidosPendientes = oraculo.getPedidosPendientes(dispositivo);
		}
		else{ // caso cocina --> camarero
			// otra consulta
		}
		
		/* Generamos el xml con la información actualizada */
		
		/* Finalmente envía el mensaje generado */
		
	}

}
