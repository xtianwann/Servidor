package XMLServer;

import accesoDatos.Pedido;
import XML.XML;

/*
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLPedidosPendientesCamarero extends XML{
	
	public XMLPedidosPendientesCamarero(String nombreMesa, String nombreSeccion, int idCom, Pedido[] pedidos){
		init();
		addNodo("tipo", "PedidosPendientesCamarero", "paquete");
		addNodo("seccion", nombreSeccion, "paquete");
		addNodo("mesa", nombreMesa, "paquete");
		addNodoConAtributos("pedidos", new String[]{"idCom"}, new String[]{idCom+""}, null, "paquete");
		for(int pedido = 0; pedido < pedidos.length; pedido++){
			addNodoConAtributos("pedido", new String[]{"idMenu"}, new String[]{pedidos[pedido].getIdMenu()+""}, null, "pedidos");
			addNodo("nombre", pedidos[pedido].getNombreProducto(), "pedido");
			addNodo("cantidad", pedidos[pedido].getNombreCantidad(), "pedido");
			addNodo("unidades", pedidos[pedido].getUnidades()+"", "pedido");
			addNodo("listos", "0", "pedido");
			addNodo("servidos", "0", "pedido");
		}
	}

}
