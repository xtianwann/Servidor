package tareas;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLAcuseReciboServer;
import XMLServer.XMLModificacionCBServer;
import XMLServer.XMLModificacionCamarero;
import accesoDatos.Inserciones;
import accesoDatos.Oraculo;
import accesoDatos.PedidoListo;

public class ModificacionCamarero extends Thread{
	
	private Socket socket;
	private String recibido;
	private Oraculo oraculo;
	private Inserciones modificador;
	
	public ModificacionCamarero(Socket socket, String recibido){
		this.socket = socket;
		this.recibido = recibido;
		oraculo = new Oraculo();
		modificador = new Inserciones();
	}
	
	public void run(){
		modificarYEnviar();
	}
	
	private void modificarYEnviar(){
		ArrayList<String> ipDestino = new ArrayList<>();
		HashMap<String, ArrayList<PedidoListo>> mapaDestino = new HashMap<>();
		
		Document dom = XML.stringToXml(recibido);
		NodeList nodeListModificado = dom.getElementsByTagName("modificado");
		for(int contadorModificado = 0; contadorModificado < nodeListModificado.getLength(); contadorModificado++){
			Node nodeModificado = nodeListModificado.item(contadorModificado);
			int idComanda = Integer.parseInt(nodeModificado.getChildNodes().item(0).getFirstChild().getNodeValue());
			int idMenu = Integer.parseInt(nodeModificado.getChildNodes().item(1).getFirstChild().getNodeValue());
			int unidades = Integer.parseInt(nodeModificado.getChildNodes().item(2).getFirstChild().getNodeValue());
			
			String[] idPedidos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "pedido");
			String[] idListos = oraculo.getIdPedidoPorIdMenuYIdComanda(idMenu, idComanda, "listo");
			String[] posiblesModificaciones = new String[idPedidos.length + idListos.length];
			for(int id = 0; id < idPedidos.length; id++){
				posiblesModificaciones[id] = idPedidos[id]; 
			}
			for(int id = idPedidos.length; id < posiblesModificaciones.length; id++){
				posiblesModificaciones[id] = idListos[id];
			}
			
			/* Vemos dónde debe mandarse */
			String ip = oraculo.getIdDestinoPorIdMenu(idMenu);
			if(!ipDestino.contains(ip)){
				ipDestino.add(ip);
				mapaDestino.put(ip, new ArrayList<PedidoListo>());
			}
			mapaDestino.get(ip).add(new PedidoListo(idComanda, idMenu, unidades));
			
			/* Cambiamos el estado en la base de datos */
			if(unidades == 0){
				modificador.modificarEstadoPedido(posiblesModificaciones, "cancelado");
			} else {
				int cancelar = posiblesModificaciones.length - unidades;
				if(cancelar < 0){
					modificador.modificarEstadoPedido(posiblesModificaciones, "cancelado");
				} else if(cancelar > 0){
					String[] aModificar = new String[cancelar];
					for(int cancel = 0; cancel < aModificar.length; cancel++){
						aModificar[cancel] = posiblesModificaciones[cancel];
					}
				}
			}
			
			/* Finalmente se le envía a cada destino la modificación */
			
			
			for(int destino = 0; destino < ipDestino.size(); destino++){
				PedidoListo[] pedidos = mapaDestino.get(ipDestino.get(destino)).toArray(new PedidoListo[0]);
				XMLModificacionCamarero xmlModificacion = new XMLModificacionCamarero(pedidos);
				Conexion conexion = null;
				try {
					conexion = new Conexion("127.0.0.1",5051);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				conexion.escribirMensaje(xmlModificacion.xmlToString(xmlModificacion.getDOM()));
				
//				Cliente cliente = new Cliente();
//				cliente.run();
			}
			
			/* Acuse de recibo al emisor */
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

}
