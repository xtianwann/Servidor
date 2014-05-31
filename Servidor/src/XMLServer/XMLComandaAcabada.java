package XMLServer;

import XML.XML;

public class XMLComandaAcabada extends XML{
	
	public XMLComandaAcabada(int idComanda){
		init();
		addNodo("tipo", "ComandaAcabada", "paquete");
		addNodo("idComanda", idComanda+"", "paquete");
	}

}
