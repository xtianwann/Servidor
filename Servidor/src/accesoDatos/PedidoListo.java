package accesoDatos;

public class PedidoListo {
	
	int idComanda;
	int idMenu;
	int listos;
	
	public PedidoListo(int idComanda, int idMenu, int listos){
		this.idComanda = idComanda;
		this.idMenu = idMenu;
		this.listos = listos;
	}

	public int getIdComanda() {
		return idComanda;
	}

	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
	}

	public int getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}

	public int getListos() {
		return listos;
	}

	public void setListos(int listos) {
		this.listos = listos;
	}
	
	

}
