package XMLServer;

import accesoDatos.Oraculo;
import accesoDatos.Pedido;
import XML.XML;

public class XMLPendientesCamareroAlEncender extends XML{
	
	private Oraculo oraculo = new Oraculo();
	
	public XMLPendientesCamareroAlEncender(Pedido[] pedidos){
		init();
		addNodo("tipo", "PendientesAlEncender", "paquete");
		if(pedidos == null || pedidos.length == 0){
			addNodo("hayPedidos", "no", "paquete");
		} else {
			addNodo("hayPedidos", "si", "paquete");
		}
		
		if(pedidos != null && pedidos.length > 0){
			addNodo("pendientes", null, "paquete");
			for(int i = 0; i < pedidos.length; i++){
				addNodo("pedido", null, "pendientes");
				addNodo("seccion", oraculo.getNombreSeccion(pedidos[i].getComanda()), "pedido");
				addNodo("mesa", oraculo.getNombreMesa(pedidos[i].getComanda()), "pedido");
				addNodo("comanda", pedidos[i].getComanda()+"", "pedido");
				addNodo("idMenu", pedidos[i].getIdMenu()+"", "pedido");
				addNodo("nombreProducto", pedidos[i].getNombreProducto(), "pedido");
				addNodo("nombreCantidad", pedidos[i].getNombreCantidad(), "pedido");
				addNodo("unidades", pedidos[i].getUnidades()+"", "pedido");
				addNodo("listos", pedidos[i].getListos()+"", "pedido");
				addNodo("servidos", pedidos[i].getServidos()+"", "pedido");
			}
		}
	}

}
