package XMLServer;

import accesoDatos.PedidoListo;
import XML.XML;

public class XMLModificacionCamarero extends XML{
	
	public XMLModificacionCamarero(PedidoListo[] pedidos){
		init();
		addNodo("tipo", "ModificacionCamarero", "paquete");
		addNodo("salen", null, "paquete");
		for(int contadorPedidos = 0; contadorPedidos < pedidos.length; contadorPedidos++){
			addNodoConAtributos("pedido", new String[]{"idCom"}, new String[]{pedidos[contadorPedidos].getIdComanda()+""}, null, "salen");
			addNodo("idMenu", pedidos[contadorPedidos].getIdMenu()+"", "pedido");
			addNodo("listos", pedidos[contadorPedidos].getListos()+"", "pedido");
		}
	}

}
