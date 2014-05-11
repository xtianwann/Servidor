package XMLServer;

import accesoDatos.PedidoListo;
import XML.XML;

public class XMLDevolverServer extends XML{
	
	public XMLDevolverServer(PedidoListo[] pedidos){
		init();
		addNodo("tipo", "CancelarPedido", "paquete");
		addNodo("salen", null, "paquete");
		for(int contadorPedidos = 0; contadorPedidos < pedidos.length; contadorPedidos++){
                    addNodoConAtributos("pedido", new String[]{"idCom"}, new String[]{pedidos[contadorPedidos].getIdComanda()+""}, null, "salen");
                    addNodo("idMenu", pedidos[contadorPedidos].getIdMenu()+"", "pedido");
                    addNodo("unidades", pedidos[contadorPedidos].getListos()+"", "pedido");
		}
	}

}
