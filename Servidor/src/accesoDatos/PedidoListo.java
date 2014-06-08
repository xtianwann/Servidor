package accesoDatos;

/**
 * Clase que permite manejar información relativa a los pedidos listos
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class PedidoListo {
	
	int idComanda;
	int idMenu;
	int listos;
	
	/**
	 * Constructor
	 * 
	 * @param idComanda [int] id de la comanda
	 * @param idMenu [int] id del menú
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
	 * Permite obtener la id del menú
	 * 
	 * @return [int] id del menú
	 */
	public int getIdMenu() {
		return idMenu;
	}

	/**
	 * Permite modificar la id del menú
	 * 
	 * @param idMenu [int] id del menú
	 */
	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}

	/**
	 * Permite obtener el número de unidades listas
	 * 
	 * @return [int] número de unidades listas
	 */
	public int getListos() {
		return listos;
	}

	/**
	 * Permite modificar el número de unidades listas
	 * 
	 * @param listos [int] número de unidades listas
	 */
	public void setListos(int listos) {
		this.listos = listos;
	}

}
