package XMLServer;

import accesoDatos.PedidoListo;
import XML.XML;

public class XMLPedidosServidos extends XML{
	
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
