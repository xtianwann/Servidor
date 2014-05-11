package accesoDatos;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Pedido {

    private int idMenu;
    private String nombreProducto;
    private String nombreCantidad;
    private int idDestino;
    private String nombreDestino;
    private int unidades;
    
    private int idPed;
    private String estado;
    private int comanda;
    
    Oraculo oraculo = new Oraculo();
    
    public Pedido(int idMenu){
        this.idMenu = idMenu;
        this.nombreProducto = oraculo.getNombreProductoPorIdMenu(idMenu);
        this.nombreCantidad = oraculo.getNombreCantidadPorIdMenu(idMenu);
        this.idDestino = Integer.parseInt(oraculo.getIdDestinoPorIdMenu(idMenu));
        this.nombreDestino = oraculo.getNombreDestinoPorIdMenu(idMenu);
        this.unidades = 0;
    }

	public Pedido(int idMenu, int unidades) {
        this.idMenu = idMenu;
        this.nombreProducto = oraculo.getNombreProductoPorIdMenu(idMenu);
        this.nombreCantidad = oraculo.getNombreCantidadPorIdMenu(idMenu);
        this.idDestino = Integer.parseInt(oraculo.getIdDestinoPorIdMenu(idMenu));
        this.nombreDestino = oraculo.getNombreDestinoPorIdMenu(idMenu);
        this.unidades = unidades;
    }
	
	public Pedido(int idPed, int idMenu, int comanda, String estado){
		this.idPed = idPed;
		this.idMenu = idMenu;
		this.comanda = comanda;
		this.estado = estado;
		this.nombreProducto = oraculo.getNombreProductoPorIdMenu(idMenu);
		this.nombreCantidad = oraculo.getNombreCantidadPorIdMenu(idMenu);
	}

    public int getIdPed() {
		return idPed;
	}

	public String getEstado() {
		return estado;
	}

	public int getComanda() {
		return comanda;
	}

	public int getIdDestino() {
        return idDestino;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getNombreCantidad() {
        return nombreCantidad;
    }
    
    public int getUnidades() {
		return unidades;
	}
    
}
