package XMLServer;

import XML.XML;
import accesoDatos.Mesa;
import accesoDatos.Pedido;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLPedidoMesaServer extends XML{

    public XMLPedidoMesaServer(Mesa mesa, String seccion, Pedido[] pedidos) {
        init();
        addNodo("tipo", "PedidoMesa", "paquete");
        addNodoConAtributos("mesa", new String[]{"idMes"}, new String[]{mesa.getIdMes()+""}, mesa.getNomMes(), "paquete");
        addNodo("seccion", seccion, "paquete");
        addNodo("pedidos", null, "paquete");
        for(int contadorPedidos = 0; contadorPedidos < pedidos.length; contadorPedidos++){
            addNodoConAtributos("menu", new String[]{"idMenu"}, new String[]{pedidos[contadorPedidos].getIdMenu()+""}, null, "pedidos");
            addNodo("producto", pedidos[contadorPedidos].getNombreProducto(), "menu");
            addNodo("cantidad", pedidos[contadorPedidos].getNombreCantidad(), "menu");
            addNodo("unidades", pedidos[contadorPedidos].getUnidades()+"", "menu");
            addNodo("listos", "0", "menu");
        }
    }
    
}
