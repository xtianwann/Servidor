package accesoDatos;

/**
 * Clase que permite manejar informaci�n relativa a los pedidos listos
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class PedidoListo {
	
	int idComanda;
	int idMenu;
	int listos;
	
	/**
	 * Constructor
	 * 
	 * @param idComanda [int] id de la comanda
	 * @param idMenu [int] id del men�
	 * @param listos [int] unidades listas
	 */
	public PedidoListo(int idComanda, int idMenu, int listos){
		this.idComanda = idComanda;
		this.idMenu = idMenu;
		this.listos = listos;
	}

	/**
	 * Permite obtener la id de la comanda
	 * 
	 * @return [int] id de la comanda
	 */
	public int getIdComanda() {
		return idComanda;
	}

	/**
	 * Permite modificar la id de la comanda
	 * 
	 * @param idComanda [int] id de la comanda
	 */
	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
	}

	/**
	 * Permite obtener la id del men�
	 * 
	 * @return [int] id del men�
	 */
	public int getIdMenu() {
		return idMenu;
	}

	/**
	 * Permite modificar la id del men�
	 * 
	 * @param idMenu [int] id del men�
	 */
	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}

	/**
	 * Permite obtener el n�mero de unidades listas
	 * 
	 * @return [int] n�mero de unidades listas
	 */
	public int getListos() {
		return listos;
	}

	/**
	 * Permite modificar el n�mero de unidades listas
	 * 
	 * @param listos [int] n�mero de unidades listas
	 */
	public void setListos(int listos) {
		this.listos = listos;
	}

}
