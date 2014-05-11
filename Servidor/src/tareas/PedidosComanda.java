package tareas;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLPedidoMesaServer;
import XMLServer.XMLPedidosPendientesCamarero;
import accesoDatos.Dispositivo;
import accesoDatos.Inserciones;
import accesoDatos.Mesa;
import accesoDatos.Oraculo;
import accesoDatos.Pedido;
import accesoDatos.Usuario;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Esta clase es la encargada de recibir la comanda enviada por el camarero, 
 * insertar la informaci�n correspondiente en la base de datos, divide los pedidos
 * seg�n el destino (cocina, barra, etc), genera los mensajes y los env�a a los
 * correspondientes destinos. Finalmente, devuelve al camarero un acuse de recibo
 * con la informaci�n necesaria para que el camarero tenga una lista de los pedidos
 * pendientes de servir.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class PedidosComanda extends Thread {

    private Socket socket;
    private String recibido;
    private ArrayList<String> destinos;
    private int idMes;
    private Usuario usuario; // acabar la clase
    private Oraculo oraculo;

    public PedidosComanda(Socket socket, String recibido) {
        oraculo = new Oraculo();
        this.socket = socket;
        this.recibido = recibido;
        this.destinos = new ArrayList<>();
        this.usuario = Usuario.getUsuario(socket);
    }

    public void run() {
        dividirYEnviarSubcomandas(recibido);
    }

    private void dividirYEnviarSubcomandas(String recibido) {
        Document dom = XML.stringToXml(recibido);
        String resultado = "";
        String explicacion = "";

        /* Obtenemos los datos de la mesa */
        NodeList nodeListIdMes = dom.getElementsByTagName("idMes");
        idMes = Integer.parseInt(nodeListIdMes.item(0).getChildNodes().item(0).getNodeValue());
        Mesa mesa = new Mesa(idMes);
        String nombreSeccion = oraculo.getNombreSeccionPorIdSeccion(mesa.getSeccion());

        /* Extraemos la lista de pedidos */
        ArrayList<Pedido> pedidos = new ArrayList<>();
        HashMap<Integer, ArrayList<Pedido>> mapaDestino = new HashMap<>();
        ArrayList<Dispositivo> dispositivos = new ArrayList<>();
        NodeList nodeListPedido = dom.getElementsByTagName("pedido");
        for (int contadorPedidos = 0; contadorPedidos < nodeListPedido.getLength(); contadorPedidos++) {
            Node nodePedido = nodeListPedido.item(contadorPedidos);
            NodeList items = nodePedido.getChildNodes();
            int idMenu = Integer.parseInt(items.item(0).getChildNodes().item(0).getNodeValue());
            int unidades = Integer.parseInt(items.item(1).getChildNodes().item(0).getNodeValue());
            
            Dispositivo dispositivo = Dispositivo.getDispositivo(idMenu);
            if(!dispositivos.contains(dispositivo)){
            	dispositivos.add(dispositivo);
            	mapaDestino.put(dispositivo.getIdDisp(), new ArrayList<Pedido>());
            }
            
            mapaDestino.get(dispositivo.getIdDisp()).add(new Pedido(idMenu, unidades));
            pedidos.add(new Pedido(idMenu, unidades));
        }
        
        /* Comprobamos si la mesa est� activa */
        Inserciones insertor = new Inserciones();
        int idComanda = 0;
        if(mesa.isActiva()){
            idComanda = insertor.insertarPedidos(mesa, pedidos.toArray(new Pedido[0]));
        } else {
            insertor.insertarNuevaComanda(mesa, usuario);
            idComanda = insertor.insertarPedidos(mesa, pedidos.toArray(new Pedido[0]));
        }
        
        /* Generamos una lista de xml seg�n destinos */
        ArrayList<XMLPedidoMesaServer> listaXML = new ArrayList<>();
        for(int contadorDestino = 0; contadorDestino < dispositivos.size(); contadorDestino++){
        	listaXML.add(new XMLPedidoMesaServer(mesa, nombreSeccion, idComanda, mapaDestino.get(dispositivos).toArray(new Pedido[0])));
        }
        
        /* Intentamos conectar con el destino y enviarle la informaci�n */
        for(int contadorDestino = 0; contadorDestino < dispositivos.size(); contadorDestino++){
        	Dispositivo dispositivo = dispositivos.get(contadorDestino);
        	
        	/* Comprobamos en la base de datos si est� conectado */
        	if(dispositivo.getConectado()){
        		/* Vemos si realmente est� conectado */
        		if(Conexion.hacerPing(dispositivo.getIp())){ // enviar tarea y acuse SI
        			resultado = "SI";
        		} else { // lanzar hilo y acuse NO
        			// modificar el estado en la bd como desconectado
        			new HiloInsistente(dispositivo).run();
        			resultado = "NO";
        			explicacion = dispositivo.getNombreDestino() + " est� desconectado";
        		}
        	} else { // enviar acuse con el fallo NO
        		new HiloInsistente(dispositivo).run();
    			resultado = "NO";
    			explicacion = dispositivo.getNombreDestino() + " est� desconectado";
        	}
        }

        /* Finalmente se env�a acuse de recibo al que pidi� la comanda */
        XMLPedidosPendientesCamarero xmlPendientes = new XMLPedidosPendientesCamarero(mesa.getNomMes(), nombreSeccion, idComanda, pedidos.toArray(new Pedido[0]));
        String acuse = xmlPendientes.xmlToString(xmlPendientes.getDOM());
        System.out.println("Acuse: " + acuse); // borrar al terminar de testear
        try {
            Conexion conn = new Conexion(socket);
            conn.escribirMensaje(acuse);
            conn.cerrarConexion();
        } catch (IOException ex) {
            Logger.getLogger(PedidosComanda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
