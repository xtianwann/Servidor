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

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Esta clase es la encargada de recibir la comanda enviada por el camarero, 
 * insertar la información correspondiente en la base de datos, divide los pedidos
 * según el destino (cocina, barra, etc), genera los mensajes y los envía a los
 * correspondientes destinos. Finalmente, devuelve al camarero un acuse de recibo
 * con la información necesaria para que el camarero tenga una lista de los pedidos
 * pendientes de servir.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class PedidosComanda extends Thread {

    private Socket socket;
    private String recibido;
    private ArrayList<String> destinos;
    private int idMes;
    private Dispositivo dispositivo; // acabar la clase
    private Oraculo oraculo;

    public PedidosComanda(Socket socket, String recibido) {
        oraculo = new Oraculo();
        this.socket = socket;
        this.recibido = recibido;
        this.destinos = new ArrayList<>();
        this.dispositivo = new Dispositivo(socket.getInetAddress()); // tengo el dispositivo tuneado para los test, deshacer
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
        NodeList nodeListPedido = dom.getElementsByTagName("pedido");
        for (int contadorPedidos = 0; contadorPedidos < nodeListPedido.getLength(); contadorPedidos++) {
            Node nodePedido = nodeListPedido.item(contadorPedidos);
            NodeList items = nodePedido.getChildNodes();
            int idMenu = Integer.parseInt(items.item(0).getChildNodes().item(0).getNodeValue());
            int unidades = Integer.parseInt(items.item(1).getChildNodes().item(0).getNodeValue());
            pedidos.add(new Pedido(idMenu, unidades));
        }

        /* Comprobamos si la mesa está activa */
        Inserciones insertor = new Inserciones();
        int idComanda = 0;
        if(mesa.isActiva()){
            idComanda = insertor.insertarPedidos(mesa, pedidos.toArray(new Pedido[0]));
        } else {
            insertor.insertarNuevaComanda(mesa, dispositivo);
            idComanda = insertor.insertarPedidos(mesa, pedidos.toArray(new Pedido[0]));
        }

        /* Dividimos los pedidos según su destino y los almacenamos en un ArrayList */
        ArrayList<XMLPedidoMesaServer> listaXMLPedidos = new ArrayList<>();
        ArrayList<Integer> destinos = new ArrayList<>();
        for (Pedido p : pedidos) {
            int idDest = p.getIdDestino();
            System.out.println("Destino: " + idDest);
            if (!destinos.contains(idDest)) {
                destinos.add(idDest);
            }
        }
        for (int contadorDestinos = 0; contadorDestinos < destinos.size(); contadorDestinos++) {
            ArrayList<Pedido> pedidosDestino = new ArrayList<>();
            for (int contadorPedidos = 0; contadorPedidos < pedidos.size(); contadorPedidos++) {
                if (pedidos.get(contadorPedidos).getIdDestino() == destinos.get(contadorDestinos)) {
                    pedidosDestino.add(pedidos.get(contadorPedidos));
                }
            }
            XMLPedidoMesaServer xmlPedidoMesaServer = new XMLPedidoMesaServer(mesa, nombreSeccion, pedidosDestino.toArray(new Pedido[0]));
            listaXMLPedidos.add(xmlPedidoMesaServer);
        }

        /* Se envía cada parte del pedido a su destino */
        for (int contadorPedidos = 0; contadorPedidos < listaXMLPedidos.size(); contadorPedidos++) {
            String mensaje = listaXMLPedidos.get(contadorPedidos).xmlToString(listaXMLPedidos.get(contadorPedidos).getDOM());
//            Cliente cliente = new Cliente(dispositivoDestino, mensaje);
//            cliente.run();
            System.out.println(mensaje); // mensaje de prueba para ver los distintos subpedidos, borrar al terminar de testear
            
        }

        /* Finalmente se envía acuse de recibo al que pidió la comanda */
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
