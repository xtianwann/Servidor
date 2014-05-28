package XMLServer;

import accesoDatos.PedidoPendiente;
import XML.XML;

public class XMLCocinaOn extends XML{
	
	public XMLCocinaOn(String respuesta){
		init();
		addNodo("tipo", "CocinaOn", "paquete");
		addNodo("respuesta", respuesta, "paquete");
	}
	
	public XMLCocinaOn(String respuesta, PedidoPendiente[] pendientes){
		init();
		addNodo("tipo", "CocinaOn", "paquete");
		addNodo("respuesta", respuesta, "paquete");
		addNodo("pendientes", null, "paquete");
		for(int contadorPendientes = 0; contadorPendientes < pendientes.length; contadorPendientes++){
			addNodo("pedido", null, "pendientes");
			addNodo("idComanda", pendientes[contadorPendientes].getIdComanda()+"", "pedido");
			addNodo("idMenu", pendientes[contadorPendientes].getIdMenu()+"", "pedido");
			addNodo("nombreSeccion", pendientes[contadorPendientes].getNombreSeccion(), "pedido");
			addNodo("nombreMesa", pendientes[contadorPendientes].getNombreMesa(), "pedido");
			addNodo("nombreProducto", pendientes[contadorPendientes].getNombreProducto(), "pedido");
			addNodo("nombreCantidad", pendientes[contadorPendientes].getNombreCantidad(), "pedido");
			addNodo("unidades", pendientes[contadorPendientes].getUnidades()+"", "pedido");
			addNodo("udPedido", pendientes[contadorPendientes].getCantidadPedidos()+"", "pedido");
			addNodo("udListo", pendientes[contadorPendientes].getCantidadListos()+"", "pedido");
			addNodo("udServido", pendientes[contadorPendientes].getCantidadServidos()+"", "pedido");
		}
	}

}
