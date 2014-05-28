package XMLServer;

import accesoDatos.Oraculo;
import accesoDatos.Pedido;
import XML.XML;

public class XMLResultadoLoginCamarero extends XML{
	
	public XMLResultadoLoginCamarero(String resultado){
		init();
		addNodo("tipo", "ResultadoLoginCamarero", "paquete");
		addNodo("resultado", resultado, "paquete");
	}

}
