package accesoDatos;

public class PedidoPendiente {
	
	private int idMenu;
	private int idComanda;
	private String nombreProducto;
	private String nombreCantidad;
	private int unidades;
	private int cantidadPedidos;
	private int cantidadListos;
	private int cantidadServidos;
	private String nombreSeccion;
	private String nombreMesa;
	
	private Oraculo oraculo = new Oraculo();
	
	public PedidoPendiente(Pedido pedido, int unidades, int cantidadPedidos, int cantidadListos, int cantidadServidos){
		this.idMenu = pedido.getIdMenu();
		this.idComanda = pedido.getComanda();
		this.nombreSeccion = oraculo.getNombreSeccion(idComanda);
		this.nombreMesa = oraculo.getNombreMesa(idComanda);
		this.nombreProducto = pedido.getNombreProducto();
		this.nombreCantidad = pedido.getNombreCantidad();
		this.unidades = unidades;
		this.cantidadPedidos = cantidadPedidos;
		this.cantidadListos = cantidadListos;
		this.cantidadServidos = cantidadServidos;
	}

	public int getIdMenu() {
		return idMenu;
	}

	public int getIdComanda() {
		return idComanda;
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

	public int getCantidadPedidos() {
		return cantidadPedidos;
	}

	public int getCantidadListos() {
		return cantidadListos;
	}

	public int getCantidadServidos() {
		return cantidadServidos;
	}

	public String getNombreSeccion() {
		return nombreSeccion;
	}

	public String getNombreMesa() {
		return nombreMesa;
	}

	public Oraculo getOraculo() {
		return oraculo;
	}
	
	

}
