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
    
    /**
     * Constructor
     */
    public Dispatcher(){
        cola = new ColaSincronizadaSocket();
        hiloPrincipal = null;
    }
    
    /**
     * Constructor
     * 
     * @param hiloPrincipal [Servidos.HiloPrincipal] instancia del hilo principal
     */
    public Dispatcher(Servidor.HiloPrincipal hiloPrincipal){
        this.hiloPrincipal = hiloPrincipal;
        cola = new ColaSincronizadaSocket();
    }
    
    /**
     * Añade un socket a la cola de conexiones.
     * 
     * @param socket [Socket] socket a través del cual un disositivo se conecta con el servidor.
     */
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
                    new GestorMensajes(socket).start();
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

    /**
     * Permite saber si el Dispatcer está parado o no.
     * 
     * @return [boolean] true si está parado, false en caso contrario.
     */
    public boolean isParado() {
        return parado;
    }

    /**
     * Permite modificar el estado del Dispatcher a parado o corriendo.
     * 
     * @param parado [boolean] true para pararlo, false para que funcione.
     */
    public void setParado(boolean parado) {
        this.parado = parado;
    }
    
}
