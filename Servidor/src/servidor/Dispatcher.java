package servidor;

import Cola.ColaSincronizadaSocket;
import Excepciones.Cola.ExcepcionColaLlena;
import Excepciones.Cola.ExcepcionColaVacia;
import Excepciones.ExcepcionInesperada;
import accesoDatos.Oraculo;
import java.net.Socket;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Coge un socket de la lista y se lo da al gestor de mensajes
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Dispatcher extends Thread{
    
    private ColaSincronizadaSocket cola;
    private boolean parado;
    private Servidor.HiloPrincipal hiloPrincipal;
    
    public Dispatcher(){
        cola = new ColaSincronizadaSocket();
        hiloPrincipal = null;
    }
    
    public Dispatcher(Servidor.HiloPrincipal hiloPrincipal){
        this.hiloPrincipal = hiloPrincipal;
        cola = new ColaSincronizadaSocket();
    }
    
    public void addSocket(Socket socket){
        try {
            cola.addSocket(socket);
        } catch (ExcepcionColaLlena ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExcepcionInesperada ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        while(!parado){
            if(!cola.isListaVacia()){
                try {
                    System.out.println("Dispatcher: Socket!");
                    Socket socket = cola.getSocket();
                    new GestorMensajes(socket).run();
                } catch (TimeoutException ex) {
                    Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExcepcionColaVacia ex) {
                    Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExcepcionInesperada ex) {
                    Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public boolean isParado() {
        return parado;
    }

    public void setParado(boolean parado) {
        this.parado = parado;
    }
    
}
