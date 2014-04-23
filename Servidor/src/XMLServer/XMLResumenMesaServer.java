package XMLServer;

import XML.XML;
import accesoDatos.Pedido;

/**
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLResumenMesaServer extends XML{

    public XMLResumenMesaServer(Pedido[] pedidos) {
        init();
        addNodo("tipo", "ResumenMesa", "paquete");
        addNodo("pedidos", null, "paquete");
        for(int contadorPedidos = 0; contadorPedidos < pedidos.length; contadorPedidos++){
            addNodoConAtributos("pedido", new String[]{"idMenu"}, new String[]{}, null, null);
        }
    }
    
    
    
}
