package XMLServer;

import XML.XML;

/**
 * Clase encargada de generar el XML de acuse de recibo
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLAcuseReciboServer extends XML {

	/**
	 * Constructor: genera la estructura del XML con la informaci�n
	 * 
	 * @param respuesta [String] tipo de respuesta
	 * @param explicacion [String] explicaci�n adicional si le hiciera falta al cliente 
	 */
	public XMLAcuseReciboServer(String respuesta, String explicacion) {
		init();
		addNodo("tipo", "AcuseRecibo", "paquete");
		addNodo("respuesta", respuesta, "paquete");
		addNodo("explicacion", explicacion, "paquete");
	}

}
