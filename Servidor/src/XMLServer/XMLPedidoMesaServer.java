package XMLServer;

import XML.XML;
import accesoDatos.Mesa;
import accesoDatos.Pedido;

/**
 * Clase encargada de generar la estructura XML con todos los pedidos que se han realizado
 * desde un dispositivo de tipo camarero.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLPedidoMesaServer extends XML{

	/**
	 * Constructor: genera la estructura XML con la información necesaria
	 * 
	 * @param mesa [Mesa] instancia de una mesa con toda la información necesaria
	 * @param seccion [String] nombre de la sección
	 * @param idComanda [int] id de la comanda a la que pertenecen todos los pedidos
	 * @param pedidos [Pedido[]] lista con todos los pedidos que se han insertado en la comanda de la mesa
	 */
    public XMLPedidoMesaServer(Mesa mesa, String seccion,int idComanda, Pedido[] pedidos) {
        init();
        addNodo("tipo", "PedidoMesa", "paquete");
        addNodoConAtributos("mesa", new String[]{"idMes"}, new String[]{mesa.getIdMes()+""}, mesa.getNomMes(), "paquete");
        addNodo("seccion", seccion, "paquete");
        addNodo("idComanda", idComanda+"", "paquete");
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
