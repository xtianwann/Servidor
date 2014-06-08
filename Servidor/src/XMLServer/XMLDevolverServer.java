package XMLServer;

import accesoDatos.PedidoListo;
import XML.XML;

/**
 * Clase encargada de generar la estructura XML con la informaci�n necesaria para cancelar un pedido
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLDevolverServer extends XML {

	/**
	 * Constructor: genera la estructura del XML con la informaci�n
	 * 
	 * @param pedido [PedidoListo] pedido que se va a devolver
	 */
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
