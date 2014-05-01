package XMLServer;

import accesoDatos.PedidoListo;
import XML.XML;

public class XMLModificacionCBServer extends XML{
	
	public XMLModificacionCBServer(PedidoListo[] pedidos){
		init();
		addNodo("tipo", "ModificacionCB", "paquete");
		addNodo("salen", null, "paquete");
		for(int contadorPedidos = 0; contadorPedidos < pedidos.length; contadorPedidos++){
			addNodoConAtributos("pedido", new String[]{"idCom"}, new String[]{pedidos[contadorPedidos].getIdComanda()+""}, null, "salen");
			addNodo("idMenu", pedidos[contadorPedidos].getIdMenu()+"", "pedido");
			addNodo("listos", pedidos[contadorPedidos].getListos()+"", "pedido");
		}
	}

}
