package XMLServer;

import accesoDatos.PedidoListo;
import XML.XML;

public class XMLDevolverServer extends XML {

	public XMLDevolverServer(PedidoListo pedido) {
		init();
		addNodo("tipo", "CancelarPedido", "paquete");
		addNodo("salen", null, "paquete");
		addNodoConAtributos("pedido", new String[] { "idCom" },
				new String[] { pedido.getIdComanda() + "" }, null, "salen");
		addNodo("idMenu", pedido.getIdMenu() + "", "pedido");
		addNodo("unidades", pedido.getListos() + "", "pedido");
	}

}
