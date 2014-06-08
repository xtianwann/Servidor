package XMLServer;

import accesoDatos.PedidoListo;
import XML.XML;

/**
 * Clase encargada de generar la estructura del XML con los datos de un pedido modificado por
 * un camarero. Informa si todos los pedidos de ese tipo est�n servidos
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLModificacionCamarero extends XML {

	/**
	 * Constructor: genera la estructura del XML con la informaci�n necesaria
	 * 
	 * @param pedido [PedidoListo] pedido que ha modificado el camarero
	 * @param todosServidos [boolean] true si est�n todos servidos, false en caso contrario
	 */
	public XMLModificacionCamarero(PedidoListo pedido, boolean todosServidos) {
		init();
		addNodo("tipo", "ModificacionCamarero", "paquete");
		addNodo("salen", null, "paquete");
		addNodoConAtributos("pedido", new String[] { "idCom" },
				new String[] { pedido.getIdComanda() + "" }, null, "salen");
		addNodo("idMenu", pedido.getIdMenu() + "", "pedido");
		addNodo("listos", pedido.getListos() + "", "pedido");
		addNodo("todosServidos", todosServidos? "1" : "0", "pedido");
	}

}
