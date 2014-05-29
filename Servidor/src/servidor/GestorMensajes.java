package servidor;

import Conexion.Conexion;

import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import tareas.CBOff;
import tareas.CancelarPedido;
import tareas.CocinaOn;
import tareas.DameloTodo;
import tareas.ImprimirTicket;
import tareas.LoginCamarero;
import tareas.LogoutCamarero;
import tareas.ModificacionCB;
import tareas.ModificacionCamarero;
import tareas.PedidosComanda;
import tareas.PedidosListos;
import tareas.PedidosServidos;
import tareas.PendientesCamarero;
import tareas.ResumenMesa;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class GestorMensajes extends Thread {

    private Socket socket;
    Conexion conn;
    private String mensaje;

    public GestorMensajes(Socket socket) {
        this.socket = socket;
        try {
			conn = new Conexion(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
        do {
            try {
				this.mensaje = conn.leerMensaje();
			} catch (NullPointerException | IOException e) {
				e.printStackTrace();
			}
        } while (this.mensaje.length() == 0);
    }

    public void run() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            mensaje = mensaje.substring(mensaje.indexOf("<"));
            builder = factory.newDocumentBuilder();
            Document dom = builder.parse(new InputSource(new StringReader(mensaje)));

            NodeList nodo = dom.getElementsByTagName("tipo");
            String tipo = nodo.item(0).getChildNodes().item(0).getNodeValue();
            
            System.out.println("Mensaje recibido tipo: " + tipo);
            System.out.println(mensaje);
            switch (tipo) {
                case "DameloTodo":
                    DameloTodo dameloTodo = new DameloTodo(socket);
                    dameloTodo.start();
                    break;
                case "PedidosComanda":
                    PedidosComanda pedidosComanda = new PedidosComanda(socket, mensaje);
                    pedidosComanda.start();
                    break;
                case "ResumenMesa":
                    ResumenMesa resumenMesa = new ResumenMesa(socket, mensaje);
                    resumenMesa.start();
                    break;
                case "PedidosListos":
                	PedidosListos pedidosListos = new PedidosListos(socket, mensaje);
                	pedidosListos.start();
                	break;
                case "PedidosServidos":
                	PedidosServidos pedidosServidos = new PedidosServidos(socket, mensaje);
                	pedidosServidos.start();
                	break;
                case "ModificacionCB":
                	ModificacionCB modificacionCB = new ModificacionCB(socket, mensaje);
                	modificacionCB.start();
                	break;
                case "ModificacionCamarero":
                	ModificacionCamarero modificacionCamarero = new ModificacionCamarero(socket, mensaje);
                	modificacionCamarero.start();
                	break;
                case "CancelarPedido":
                	CancelarPedido cancelarPedido = new CancelarPedido(socket, mensaje);
                	cancelarPedido.start();
                	break;
                case "LoginCamarero":
                	LoginCamarero loginCamarero = new LoginCamarero(socket, mensaje);
                	loginCamarero.start();
                	break;
                case "LogoutCamarero":
                	LogoutCamarero logoutCamarero = new LogoutCamarero(socket, mensaje);
                	logoutCamarero.start();
                	break;
                case "DamePendientes":
                	PendientesCamarero pendientesCamarero = new PendientesCamarero(socket, mensaje);
                	pendientesCamarero.start();
                	break;
                case "CocinaOn":
                	CocinaOn cocinaOn = new CocinaOn(socket);
                	cocinaOn.start();
                	break;
                case "LogoutCB":
                	CBOff logoutCB = new CBOff(socket);
                	logoutCB.start();
                	break;
                case "ImprimirTicket":
                	ImprimirTicket imprimir = new ImprimirTicket(socket, mensaje);
                	imprimir.start();
                	break;
            }

        } catch (SAXException ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
