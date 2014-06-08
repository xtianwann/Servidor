package XMLServer;

import accesoDatos.PedidoListo;
import XML.XML;

/**
 * Clase encargada de generar la esructura del XML con los pedidos servidos por un
 * dispositivo del tipo camarero
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLPedidosServidos extends XML{
	
	/**
	 * Constructor: genera la estructura del XML con la información necesaria
	 * 
	 * @param pedidos [PedidoListo[]] lista de pedidos que ha servido el dispositivo camarero
	 */
	public XMLPedidosServidos(PedidoListo[] pedidos){
		init();
		addNodo("tipo", "TodosServidos", "paquete");
		addNodo("servidos", null, "paquete");
		for(PedidoListo p : pedidos){
			addNodo("pedido", null, "servidos");
			addNodo("idCom", p.getIdComanda()+"", "pedido");
			addNodo("idMenu", p.getIdMenu()+"", "pedido");
		}
	}

}
