package accesoDatos;

import java.net.InetAddress;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Dispositivo {
    
    private int idDisp;
    private String ip;
    private int conectado;
    private String nombreDestino;
    private static Oraculo oraculo = new Oraculo();

    public Dispositivo(InetAddress address) {
        
        this.ip = "192.168.0.1";
        this.idDisp = oraculo.getIdDispositivoPorIp(ip);
    }
    
    public Dispositivo(int idDisp, int conectado, String ip, String nomDest){
    	this.idDisp = idDisp;
    	this.conectado = conectado;
    	this.ip = ip;
    	this.nombreDestino = nombreDestino;
    }
    
    public static Dispositivo getDispositivo(int idMenu){
    	return oraculo.getDispositivoPorIdMenu(idMenu);
    }
    
    public boolean getConectado() {
    	boolean retorno = false;
    	if(conectado == 1)
    		retorno = true;
		return retorno;
	}

	public void setConectado(int conectado) {
		this.conectado = conectado;
	}

	public String getNombreDestino() {
		return nombreDestino;
	}

	public void setNombreDestino(String nombreDestino) {
		this.nombreDestino = nombreDestino;
	}

	public void setIdDisp(int idDisp) {
		this.idDisp = idDisp;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getIdDisp(){
        return idDisp;
    }
    
    public String getIp(){
        return ip;
    }
    
}
