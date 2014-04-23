package accesoDatos;

import java.net.InetAddress;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Dispositivo {
    
    private int idDisp;
    private String ip;
    private int destino;

    public Dispositivo(InetAddress address) {
        Oraculo oraculo = new Oraculo();
//        this.ip = address.getHostAddress();
        this.ip = "192.168.0.1";
        this.idDisp = oraculo.getIdDispositivoPorIp(ip);
    }
    
    public Dispositivo(int destino){
        this.destino = destino;
        // Consultar en la base de datos para obtener los datos restantes a partir del destino
    }
    
    public int getIdDisp(){
        return idDisp;
    }
    
    public String getIp(){
        return ip;
    }
    
}
