package accesoDatos;

/**
 * Esta clase permite manejar la información relativa a los dispositivos.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Dispositivo {
    
    private int idDisp;
    private String ip;
    private int conectado;
    private String nombreDestino;
    private static Oraculo oraculo;
    
    /**
     * Constructor: solo necesita la ip de un dispositivo para obtener toda la información
     * restante a partir de consultas a la base de datos.
     * 
     * @param ip [String] ip del dispositivo
     */
    public Dispositivo(String ip){
    	this.oraculo = new Oraculo();
    	this.ip = ip;
    	this.idDisp = oraculo.getIdDispositivoPorIp(ip);
    	this.conectado = oraculo.getConectado(idDisp);
    	this.nombreDestino = oraculo.getNombreDestino(idDisp);
    }
    
    /**
     * Constructor:
     * 
     * @param idDisp [int] id del dispositivo
     * @param conectado [int] 1 si está conectado, 0 si no lo está
     * @param ip [String] ip del dispositivo
     * @param nombreDestino [String] nombre del destino, null si es un camarero
     */
    public Dispositivo(int idDisp, int conectado, String ip, String nombreDestino){
    	this.idDisp = idDisp;
    	this.conectado = conectado;
    	this.ip = ip;
    	this.nombreDestino = nombreDestino;
    }
    
    /**
     * Genera un objeto dispositivo a partir de una id de menú
     * 
     * @param idMenu [int] id de un menú
     * @return [Dispositivo] dispositivo con todo los datos necesarios
     */
    public static Dispositivo getDispositivo(int idMenu){
    	oraculo = new Oraculo();
    	return oraculo.getDispositivoPorIdMenu(idMenu);
    }
    
    /**
     * Permite saber si un dispositivo está conectado
     * 
     * @return [boolean] true si está conectado, false en caso contrario
     */
    public boolean getConectado() {
    	boolean retorno = false;
    	if(conectado == 1)
    		retorno = true;
		return retorno;
	}

    /**
     * Cambia el estado de un dispositivo
     * 
     * @param conectado [int] 1 para conectarlo, 0 para desconectarlo
     */
	public void setConectado(int conectado) {
		this.conectado = conectado;
	}

	/**
	 * Obtiene el nombre de destino de un dispositivo
	 * 
	 * @return [String] nombre de destino, null si el dispositivo es de un camarero
	 */
	public String getNombreDestino() {
		return nombreDestino;
	}

	/**
	 * Permite cambiar el nombre de destino de un dispositivo
	 * 
	 * @param nombreDestino [String] con el nuevo nombre de destino
	 */
	public void setNombreDestino(String nombreDestino) {
		this.nombreDestino = nombreDestino;
	}

	/**
	 * Permite modificar la id de un dispositivo
	 * 
	 * @param idDisp [int] id del dispositivo
	 */
	public void setIdDisp(int idDisp) {
		this.idDisp = idDisp;
	}

	/**
	 * Permite modificar la ip de un dispositivo
	 * 
	 * @param ip [String] ip del dispositivo
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Permite obtener la id de un dispositivo
	 * 
	 * @return [int] id del dispositivo
	 */
	public int getIdDisp(){
        return idDisp;
    }
    
	/**
	 * Permite obtener la ip de un dispositivo
	 * 
	 * @return [String] ip del dispositivo
	 */
    public String getIp(){
        return ip;
    }
    
}
