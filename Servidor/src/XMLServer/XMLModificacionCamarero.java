package XMLServer;

import accesoDatos.PedidoListo;
import XML.XML;

public class XMLModificacionCamarero extends XML {

	public XMLModificacionCamarero(PedidoListo pedido) {
		init();
		addNodo("tipo", "ModificacionCamarero", "paquete");
		addNodo("salen", null, "paquete");
		addNodoConAtributos("pedido", new String[] { "idCom" },
				new String[] { pedido.getIdComanda() + "" }, null, "salen");
		addNodo("idMenu", pedido.getIdMenu() + "", "pedido");
		addNodo("listos", pedido.getListos() + "", "pedido");
	}

}
