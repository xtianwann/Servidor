package accesoDatos;

/**
 * FINALIZADA
 * 
 * Clase que permite manejar informaci�n relativa a los pedidos pendientes
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
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
	private Oraculo oraculo;
	
	/**
	 * Constructor
	 * 
	 * @param pedido [Pedido] objeto pedido con toda la informaci�n necesaria
	 * @param unidades [int] unidades que se est�n pidiendo
	 * @param cantidadPedidos [int] unidades en estado pedido del total
	 * @param cantidadListos [int] unidades en estado listo del total
	 * @param cantidadServidos [int] unidades en estado servido del total
	 */
	public PedidoPendiente(Pedido pedido, int unidades, int cantidadPedidos, int cantidadListos, int cantidadServidos){
		this.oraculo = new Oraculo();
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
	
	/**
	 * Constructor
	 * 
	 * @param idComanda [int] id de la comanda a la que pertenece
	 * @param pedido [Pedido] objeto pedido con toda la informaci�n necesaria
	 * @param unidades [int] unidades que se est�n pidiendo
	 * @param cantidadPedidos [int] unidades en estado pedido del total
	 * @param cantidadListos [int] unidades en estado listo del total
	 * @param cantidadServidos [int] unidades en estado servido del total
	 */
	public PedidoPendiente(int idComanda, Pedido pedido, int unidades, int cantidadPedidos, int cantidadListos, int cantidadServidos){
		this.oraculo = new Oraculo();
		this.idComanda = idComanda;
		this.idMenu = pedido.getIdMenu();
		this.nombreSeccion = oraculo.getNombreSeccion(idComanda);
		this.nombreMesa = oraculo.getNombreMesa(idComanda);
		this.nombreProducto = pedido.getNombreProducto();
		this.nombreCantidad = pedido.getNombreCantidad();
		this.unidades = unidades;
		this.cantidadPedidos = cantidadPedidos;
		this.cantidadListos = cantidadListos;
		this.cantidadServidos = cantidadServidos;
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
	 * Permite obtener la id de la comanda
	 * 
	 * @return [int] id de la comanda
	 */
	public int getIdComanda() {
		return idComanda;
	}

	/**
	 * Permite obtener el nombre del producto
	 * 
	 * @return [String] nombre del producto
	 */
	public String getNombreProducto() {
		return nombreProducto;
	}

	/**
	 * Permite obtener el nombre de la cantidad
	 * 
	 * @return [String] nombre de la cantidad
	 */
	public String getNombreCantidad() {
		return nombreCantidad;
	}

	/**
	 * Permite obtener el total de unidades
	 * 
	 * @return [int] total de unidades
	 */
	public int getUnidades() {
		return unidades;
	}

	/**
	 * Permite obtener el n�mero de unidades en estado pedido
	 * 
	 * @return [int] n�mero de unidades en estado pedido
	 */
	public int getCantidadPedidos() {
		return cantidadPedidos;
	}

	/**
	 * Permite obtener el n�mero de unidades en estado listo
	 * 
	 * @return [int] n�mero de unidades en estado listo
	 */
	public int getCantidadListos() {
		return cantidadListos;
	}

	/**
	 * Permite obtener el n�mero de unidades en estado servido
	 * 
	 * @return [int] n�mero de unidades en estado servido
	 */
	public int getCantidadServidos() {
		return cantidadServidos;
	}

	/**
	 * Permite obtener el nombre de la secci�n
	 * 
	 * @return [String] nombre de la secci�n
	 */
	public String getNombreSeccion() {
		return nombreSeccion;
	}

	/**
	 * Permite obtener el nombre de la mesa
	 * 
	 * @return [String] nombre de la mesa
	 */
	public String getNombreMesa() {
		return nombreMesa;
	}

}
