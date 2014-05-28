package XMLServer;

import accesoDatos.PedidoListo;
import XML.XML;

public class XMLModificacionCBServer extends XML {

	public XMLModificacionCBServer(PedidoListo pedido) {
		init();
		addNodo("tipo", "ModificacionCB", "paquete");
		addNodo("salen", null, "paquete");
		addNodoConAtributos("pedido", new String[] { "idCom" },
				new String[] { pedido.getIdComanda() + "" }, null, "salen");
		addNodo("idMenu", pedido.getIdMenu() + "", "pedido");
		addNodo("listos", pedido.getListos() + "", "pedido");
	}

}
