package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLDevolverServer;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.PedidoListo;

public class CancelarPedido extends Thread{
	
	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;
	
	public CancelarPedido(Socket socket, String recibido){
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}
	
	public void run(){
		cancelarYEnviar();
	}
	
	private void cancelarYEnviar(){
		ArrayList<String> ipDestino = new ArrayList<>();
		HashMap<String, ArrayList<PedidoListo>> mapaDestino = new HashMap<>();
		Document dom = XML.stringToXml(recibido);
		
		int idComanda = Integer.parseInt(dom.getElementsByTagName("idCom").item(0).getFirstChild().getNodeValue());
		int idMenu = Integer.parseInt(dom.getElementsByTagName("idMenu").item(0).getFirstChild().getNodeValue());
		int unidades = Integer.parseInt(dom.getElementsByTagName("unidades").item(0).getFirstChild().getNodeValue());
		
		/* Cancelamos el pedido correspondiente en la base de datos */
		String[] idServidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "servido");
		
		String[] aBorrar = new String[unidades];
		for(int contador = 0; contador < unidades; contador++){
			aBorrar[contador] = idServidos[contador];
		}
		modificador.modificarEstadoPedido(aBorrar, "cancelado");
		
		/* Enviamos la informaci�n a cocina/barra */
		String ip = oraculo.getIdDestinoPorIdMenu(idMenu);
		if (!ipDestino.contains(ip)) {
			ipDestino.add(ip);
			mapaDestino.put(ip, new ArrayList<PedidoListo>());
		}
		mapaDestino.get(ip).add(new PedidoListo(idComanda, idMenu, unidades));
		
		for (int destino = 0; destino < ipDestino.size(); destino++) {
			PedidoListo[] pedidos = mapaDestino.get(ipDestino.get(destino)).toArray(new PedidoListo[0]);
			XMLDevolverServer xmlDevolver = new XMLDevolverServer(pedidos);
			System.out.println(xmlDevolver.xmlToString(xmlDevolver.getDOM()));
			Conexion conexion = null;
			try {
				conexion = new Conexion("192.168.43.184", 27012);
				conexion.escribirMensaje(xmlDevolver
						.xmlToString(xmlDevolver.getDOM()));
			} catch (NullPointerException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			// Cliente cliente = new Cliente();
			// cliente.run();
		}
		
		/* Acuse de recibo para el camarero */
		XMLAcuseReciboServer xmlAcuse = new XMLAcuseReciboServer("OK", "");
		String acuse = xmlAcuse.xmlToString(xmlAcuse.getDOM());
		try {
            Conexion conn = new Conexion(socket);
            conn.escribirMensaje(acuse);
            conn.cerrarConexion();
        } catch (IOException ex) {
            Logger.getLogger(PedidosComanda.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

}
