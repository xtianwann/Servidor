package tareas;

import Conexion.Conexion;
import XMLServer.XMLDameloTodoServer;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase entrega la información necesaria para que la aplicación camarero
 * rellene la interfaz para poder realizar pedidos.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class DameloTodo extends Thread{
    
    private Socket socket;
    
    public DameloTodo(Socket socket){
        this.socket = socket;
    }
    
    public void run(){
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
