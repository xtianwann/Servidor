package XMLServer;

import accesoDatos.PedidoPendiente;
import XML.XML;

/**
 * Clase encargada de generar la estructura del XML con todos los pedidos pendientes
 * que pueda tener acumulados un dispositivo
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLInfoAcumulada extends XML{
	
	/**
	 * Constructor: genera la estructura del XML con la información necesaria
	 * 
	 * @param pedidos [PedidoPendiente[]] lista de pedidos pendientes que tiene el dispositivo
	 */
	public XMLInfoAcumulada(PedidoPendiente[] pedidos){
		init();
		addNodo("tipo", "InfoAcumulada", "paquete");
		addNodo("pedidos", null, "paquete");
		for(int contadorPedido = 0; contadorPedido < pedidos.length; contadorPedido++){
			addNodo("pedido", null, "pedidos");
			addNodo("idMenu", pedidos[contadorPedido].getIdMenu()+"", "pedido");
			addNodo("idComanda", pedidos[contadorPedido].getIdComanda()+"", "pedido");
			addNodo("nombreSeccion", pedidos[contadorPedido].getNombreSeccion(), "pedido");
			addNodo("nombreMesa", pedidos[contadorPedido].getNombreMesa(), "pedido");
			addNodo("nombreProducto", pedidos[contadorPedido].getNombreProducto(), "pedido");
			addNodo("nombreCantidad", pedidos[contadorPedido].getNombreCantidad(), "pedido");
			addNodo("unidades", pedidos[contadorPedido].getUnidades()+"", "pedido");
			addNodo("udPedido", pedidos[contadorPedido].getCantidadPedidos()+"", "pedido");
			addNodo("udListo", pedidos[contadorPedido].getCantidadListos()+"", "pedido");
			addNodo("udServido", pedidos[contadorPedido].getCantidadServidos()+"", "pedido");
		}
	}

}
