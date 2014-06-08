package accesoDatos;

import java.net.Socket;

/**
 * Permite manejar información relativa a los usuarios que hay en la base de datsos
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Usuario {
	
	private int idUsu;
	private String nombre;
	private int dispositivoActual;
	private String ipDispositivo;
	private static Oraculo oraculo = new Oraculo();;
	
	/**
	 * Constructor
	 * 
	 * @param idUsu [int] id de usuario
	 * @param nombre [String] nombre del usuario
	 * @param dispositivoActual [int] id del dispositivo en el que se encuentra actualmente
	 * @param ipDispositivo [String] ip del dispositivo que tiene el usuario
	 */
	public Usuario(int idUsu, String nombre, int dispositivoActual, String ipDispositivo){
		this.idUsu = idUsu;
		this.nombre = nombre;
		this.dispositivoActual = dispositivoActual;
		this.ipDispositivo = ipDispositivo;
	}
	
	/**
	 * Permite crear un objeto Usuario a partir de una id de usuario
	 * 
	 * @param idUsu [int] id de usuario
	 * @return Usuario usuario con toda la información necesaria
	 */
	public static Usuario getUsuario(int idUsu){
		return oraculo.getUsuarioById(idUsu);
	}
	
	/**
	 * Constructor: permite crear un usuario a partir del socket desde el que
	 * está estableciendo conexión con el servidor mediante consultas a la base
	 * de datos.
	 * 
	 * @param socket
	 * @return
	 */
	public static Usuario getUsuario(Socket socket){
		return oraculo.getUsuarioByIp(socket);
	}

	/**
	 * Permite obtener la id del usuario
	 * 
	 * @return [int] id del usuario
	 */
	public int getIdUsu() {
		return idUsu;
	}

	/**
	 * Permite modificar la id del usuario
	 * 
	 * @param idUsu [int] id del usuario
	 */
	public void setIdUsu(int idUsu) {
		this.idUsu = idUsu;
	}

	/**
	 * Permite obtener el nombre de un usuario
	 * 
	 * @return [String] nombre del usuario
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Permite modificar el nombre de un usuario
	 * 
	 * @param nombre [String] nombre del usuario
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Permite obtener la id del dipositivo que está usando el usuario
	 * 
	 * @return [int] id del dispositivo
	 */
	public int getDispositivoActual() {
		return dispositivoActual;
	}

	/**
	 * Permite modificar la id del dispositivo que está usando el usuario
	 * 
	 * @param dispositivoActual [int] id del dispositivo
	 */
	public void setDispositivoActual(int dispositivoActual) {
		this.dispositivoActual = dispositivoActual;
	}

	/**
	 * Permite obtener la ip del dispositivo que está usando el usuario
	 * 
	 * @return [String] ip del dipositivo
	 */
	public String getIpDispositivo() {
		return ipDispositivo;
	}

	/**
	 * Permite modificar la ip del dispositivo que está usando el usuario
	 * 
	 * @param ipDispositivo [String] ip del dispositivo
	 */
	public void setIpDispositivo(String ipDispositivo) {
		this.ipDispositivo = ipDispositivo;
	}

}
