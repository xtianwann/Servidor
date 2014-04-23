package XMLServer;

import XML.XML;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLAcuseReciboServer extends XML{

    public XMLAcuseReciboServer(String respuesta, String explicacion) {
        init();
        addNodo("tipo", "AcuseRecibo", "paquete");
        addNodo("respuesta", respuesta, "paquete");
        if(respuesta.equals("NO")){
            addNodo("explicacion", explicacion, "paquete");
        }
    }
    
}
