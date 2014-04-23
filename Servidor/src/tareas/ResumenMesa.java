package tareas;

import Conexion.Conexion;
import XML.XML;
import XMLServer.XMLResumenMesaServer;
import accesoDatos.Mesa;
import accesoDatos.Oraculo;
import accesoDatos.Pedido;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class ResumenMesa extends Thread {

    private Socket socket;
    private String recibido;
    private int idMes;

    public ResumenMesa(Socket socket, String recibido) {
        this.socket = socket;
        this.recibido = recibido;
    }

    public void run() {
        generarResumenYEnviar();
    }

    private void generarResumenYEnviar() {
        Document dom = XML.stringToXml(recibido);

        /* Obtenemos idMes */
        NodeList nodeListIdMes = dom.getElementsByTagName("idMes");
        idMes = Integer.parseInt(nodeListIdMes.item(0).getChildNodes().item(0).getNodeValue());
        Mesa mesa = new Mesa(idMes);

        /* Generar el xml con todo lo que lleva la mesa */
        Oraculo oraculo = new Oraculo();
        Pedido[] pedidosMesa = oraculo.getPedidosPorIdMesa(idMes);
        XMLResumenMesaServer xmlResumen = new XMLResumenMesaServer(pedidosMesa);

        /* Finalmente enviamos el mensaje */
        try {
            String respuesta = xmlResumen.xmlToString(xmlResumen.getDOM());
            Conexion conn = new Conexion(socket);
            conn.escribirMensaje(respuesta);
            conn.cerrarConexion();
        } catch (IOException ex) {
            Logger.getLogger(ResumenMesa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
