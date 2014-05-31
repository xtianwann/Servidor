package XMLServer;

import XML.XML;

/**
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLAcuseReciboServer extends XML {

	public XMLAcuseReciboServer(String respuesta, String explicacion) {
		init();
		addNodo("tipo", "AcuseRecibo", "paquete");
		addNodo("respuesta", respuesta, "paquete");
		addNodo("explicacion", explicacion, "paquete");
	}

}
