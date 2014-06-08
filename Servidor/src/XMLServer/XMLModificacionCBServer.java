package XMLServer;

import accesoDatos.PedidoListo;
import XML.XML;

/**
 * Clase encargada de generar la estructura del XML con un pedido modificado por
 * un dispositivo de tipo destino.
 * 
 * @author Juan G. P�errez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLModificacionCBServer extends XML {

	/**
	 * Constructor: genera la estructura del XML con la informaci�n necesaria
	 * 
	 * @param pedido [PedidoListo] pedido que ha modificado el dispositivo destino
	 */
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
