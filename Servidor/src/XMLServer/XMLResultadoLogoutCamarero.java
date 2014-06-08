package XMLServer;

import XML.XML;

/**
 * Clase encargada de generar la estructura del XML con el resultado del proceso de logout
 * de un dipositivo del tipo camarero
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLResultadoLogoutCamarero extends XML{
	
	/**
	 * Constructor: genera la estructura del XML con la informaci�n necesaria
	 * 
	 * @param resultado [String] resultado del proceso de logout
	 */
	public XMLResultadoLogoutCamarero(String resultado){
		init();
		addNodo("tipo", "ResultadoLogoutCamarero", "paquete");
		addNodo("resultado", resultado, "paquete");
	}

}
