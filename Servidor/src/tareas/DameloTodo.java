package tareas;

import Conexion.Conexion;
import XMLServer.XMLDameloTodoServer;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase encargada de entregar la información necesaria para que la aplicación camarero
 * rellene la interfaz para poder realizar pedidos.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class DameloTodo extends Thread{
    
    private Socket socket;
    
    /**
     * Constructor
     * 
     * @param socket [Socket] socket con el que el camarero ha establecido conexión con el servidor
     */
    public DameloTodo(Socket socket){
        this.socket = socket;
    }
    
    public void run(){
        darTodo();
    }
    
    /**
     * Obtiene toda la información solicitada y la envía
     */
    private void darTodo(){
    	try {
            XMLDameloTodoServer xml = new XMLDameloTodoServer();
            String respuesta = xml.xmlToString(xml.getDOM());
            Conexion conn = new Conexion(socket);
            conn.escribirMensaje(respuesta);
            conn.cerrarConexion();
        } catch (IOException ex) {
            Logger.getLogger(DameloTodo.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
    
}
