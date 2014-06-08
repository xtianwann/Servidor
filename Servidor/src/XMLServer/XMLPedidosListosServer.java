package XMLServer;

import accesoDatos.PedidoListo;
import XML.XML;

/**
 * Clase encargada de generar la estructura XML con la informaci�n de los pedidos que un 
 * dispositivo de tipo destino ha marcado como listo
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLPedidosListosServer extends XML{
	
	/**
	 * Constructor: genera la estructura del XML con la informaci�n necesaria
	 * 
	 * @param pedidos [PedidoListo[]] lista con todos los pedidos que ha marcado el dispositivo destino como listo
	 */
	public XMLPedidosListosServer(PedidoListo[] pedidos){
		init();
		addNodo("tipo", "PedidosListos", "paquete");
		addNodo("salen", null, "paquete");
		for(int contadorPedidos = 0; contadorPedidos < pedidos.length; contadorPedidos++){
			addNodoConAtributos("pedido", new String[]{"idCom"}, new String[]{pedidos[contadorPedidos].getIdComanda()+""}, null, "salen");
			addNodo("idMenu", pedidos[contadorPedidos].getIdMenu()+"", "pedido");
			addNodo("listos", pedidos[contadorPedidos].getListos()+"", "pedido");
		}
	}
	
}
