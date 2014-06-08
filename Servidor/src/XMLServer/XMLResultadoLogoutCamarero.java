package XMLServer;

import XML.XML;

/**
 * Clase encargada de generar la estructura del XML con el resultado del proceso de logout
 * de un dipositivo del tipo camarero
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLResultadoLogoutCamarero extends XML{
	
	/**
	 * Constructor: genera la estructura del XML con la información necesaria
	 * 
	 * @param resultado [String] resultado del proceso de logout
	 */
	public XMLResultadoLogoutCamarero(String resultado){
		init();
		addNodo("tipo", "ResultadoLogoutCamarero", "paquete");
		addNodo("resultado", resultado, "paquete");
	}

}
