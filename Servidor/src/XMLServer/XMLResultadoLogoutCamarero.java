package XMLServer;

import XML.XML;

public class XMLResultadoLogoutCamarero extends XML{
	
	public XMLResultadoLogoutCamarero(String resultado){
		init();
		addNodo("tipo", "ResultadoLogoutCamarero", "paquete");
		addNodo("resultado", resultado, "paquete");
	}

}
