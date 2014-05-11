package accesoDatos;

import java.net.Socket;

public class Usuario {
	
	private int idUsu;
	private String nombre;
	private int dispositivoActual;
	private String ipDispositivo;
	private static Oraculo oraculo = new Oraculo();;
	
	public Usuario(int idUsu, String nombre, int dispositivoActual, String ipDispositivo){
		this.idUsu = idUsu;
		this.nombre = nombre;
		this.dispositivoActual = dispositivoActual;
		this.ipDispositivo = ipDispositivo;
	}
	
	public static Usuario getUsuario(int idUsu){
		return oraculo.getUsuarioById(idUsu);
	}
	
	public static Usuario getUsuario(Socket socket){
		return oraculo.getUsuarioByIp(socket);
	}

	public int getIdUsu() {
		return idUsu;
	}

	public void setIdUsu(int idUsu) {
		this.idUsu = idUsu;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getDispositivoActual() {
		return dispositivoActual;
	}

	public void setDispositivoActual(int dispositivoActual) {
		this.dispositivoActual = dispositivoActual;
	}

	public String getIpDispositivo() {
		return ipDispositivo;
	}

	public void setIpDispositivo(String ipDispositivo) {
		this.ipDispositivo = ipDispositivo;
	}

}
