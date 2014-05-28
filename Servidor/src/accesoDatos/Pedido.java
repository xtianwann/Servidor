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
    private int listos;
    private int servidos;

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
	
	public Pedido(int idPed, int idMenu, int comanda, String estado, int unidades, int listos, int servidos){
		this.idPed = idPed;
		this.idMenu = idMenu;
		this.comanda = comanda;
		this.estado = estado;
		this.nombreProducto = oraculo.getNombreProductoPorIdMenu(idMenu);
		this.nombreCantidad = oraculo.getNombreCantidadPorIdMenu(idMenu);
		this.unidades = unidades;
		this.listos = listos;
		this.servidos = servidos;
	}
	
	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public void setNombreCantidad(String nombreCantidad) {
		this.nombreCantidad = nombreCantidad;
	}

	public void setIdDestino(int idDestino) {
		this.idDestino = idDestino;
	}

	public void setNombreDestino(String nombreDestino) {
		this.nombreDestino = nombreDestino;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}

	public void setListos(int listos) {
		this.listos = listos;
	}

	public void setIdPed(int idPed) {
		this.idPed = idPed;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setComanda(int comanda) {
		this.comanda = comanda;
	}

	public void setOraculo(Oraculo oraculo) {
		this.oraculo = oraculo;
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
    
    public int getListos() {
		return listos;
	}
    
    public int getServidos() {
		return servidos;
	}

	public void setServidos(int servidos) {
		this.servidos = servidos;
	}
    
}
