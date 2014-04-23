package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Arranca el servidor
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Servidor {
    
    private HiloPrincipal hiloPrincipal;
    
    public Servidor(){
        try {
            hiloPrincipal = new HiloPrincipal(27015);
            hiloPrincipal.start();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new Servidor();
        System.out.println("Servidor iniciado...");
    }
    
    /* Hilo principal del servidor */
    public class HiloPrincipal extends Thread{
        
        private ServerSocket socketServidor;
        private Dispatcher dispatcher;
        private boolean parado;
        
        /**
         * Constructor de HiloPrincipal
         * 
         * @param puerto puerto de escucha
         * @throws IOException en caso de no poder crear el ServerSocket en
         * el puerto pasado por parámetro
         */
        public HiloPrincipal(int puerto) throws IOException{
            this.socketServidor = new ServerSocket(puerto);
            this.parado = true;
        }
        
        public void run(){
            Socket cliente;
            this.parado = false;
            dispatcher = new Dispatcher(this);
            dispatcher.start();
            
            while(!parado){
                try {
                    cliente = this.socketServidor.accept();
                    dispatcher.addSocket(cliente);
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        /* Para la ejecuciÃ³n del hilo */
        public void parar(){
            parado = true;
            dispatcher.setParado(true);
            try {
                socketServidor.close();
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}