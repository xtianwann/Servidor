package XMLServer;

import XML.XML;

/**
 * Clase encargada de generar la estructura del XML respuesta al login de un dispositivo
 * tipo camarero.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLResultadoLoginCamarero extends XML{
	
	/**
	 * Constructor: genera la estructura del XML con la información necesaria
	 * 
	 * @param resultado [String] resultado del proceso login
	 */
	public XMLResultadoLoginCamarero(String resultado){
		init();
		addNodo("tipo", "ResultadoLoginCamarero", "paquete");
		addNodo("resultado", resultado, "paquete");
	}

}
