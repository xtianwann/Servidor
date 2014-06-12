package XMLServer;

import XML.XML;

/**
 * clase encargada de generar el XML con la informaci�n necesaria para informar
 * que una comanda se ha acabado.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLComandaAcabada extends XML{
	
	/**
	 * Constructor: genera la estructura del XML con la informaci�n necesaria.
	 * 
	 * @param idComanda [int] id de la comanda acabada
	 */
	public XMLComandaAcabada(int idComanda){
		init();
		addNodo("tipo", "ComandaAcabada", "paquete");
		addNodo("idComanda", idComanda+"", "paquete");
	}

}
