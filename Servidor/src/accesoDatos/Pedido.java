package accesoDatos;

/**
 * FINALIZADA
 * 
 * Clase que permite manejar toda la información referente a los pedidos en la base de datos
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Pedido {

	private int idMenu;
    private String nombreProducto;
    private String nombreCantidad;
    private int idDestino;
    private String nombreDestino;
    private float precio;
    private int unidades;
    private int listos;
    private int servidos;
	private int idPed;
    private String estado;
    private int comanda;
    private Oraculo oraculo;
    
    /**
     * Constructor: genera un objeto pedido a partir una id de menú. El resto de 
     * atributos los obtiene mediante consultas a la base de datos.
     * 
     * @param idMenu [int] id de menú
     */
    public Pedido(int idMenu){
    	this.oraculo = new Oraculo();
        this.idMenu = idMenu;
        this.nombreProducto = oraculo.getNombreProductoPorIdMenu(idMenu);
        this.nombreCantidad = oraculo.getNombreCantidadPorIdMenu(idMenu);
        this.idDestino = Integer.parseInt(oraculo.getIdDestinoPorIdMenu(idMenu));
        this.nombreDestino = oraculo.getNombreDestinoPorIdMenu(idMenu);
        this.unidades = 0;
    }

    /**
     * Constructor: genera un objeto pedido a partir de una id de menú. El resto de
     * atributos los obtiene mediante consultas a la base de datos, excepto el número de
     * unidades.
     * 
     * @param idMenu [int] id de menú
     * @param unidades [int] unidades del producto
     */
	public Pedido(int idMenu, int unidades) {
		this.oraculo = new Oraculo();
        this.idMenu = idMenu;
        this.nombreProducto = oraculo.getNombreProductoPorIdMenu(idMenu);
        this.nombreCantidad = oraculo.getNombreCantidadPorIdMenu(idMenu);
        this.idDestino = Integer.parseInt(oraculo.getIdDestinoPorIdMenu(idMenu));
        this.nombreDestino = oraculo.getNombreDestinoPorIdMenu(idMenu);
        this.unidades = unidades;
    }
	
	/**
	 * Constructor: genera un objeto pedido a partir de los parámetros pasados.
	 * El resto de los atributos los obtiene mediante consultas a la base de datos.
	 * 
	 * @param idMenu [int] id de menú
	 * @param unidades [int] unidades del pedido
	 * @param precio [float] precio del pedido
	 */
	public Pedido(int idMenu, int unidades, float precio){
		this.oraculo = new Oraculo();
		this.idMenu = idMenu;
        this.nombreProducto = oraculo.getNombreProductoPorIdMenu(idMenu);
        this.nombreCantidad = oraculo.getNombreCantidadPorIdMenu(idMenu);
        this.idDestino = Integer.parseInt(oraculo.getIdDestinoPorIdMenu(idMenu));
        this.nombreDestino = oraculo.getNombreDestinoPorIdMenu(idMenu);
        this.unidades = unidades;
        this.precio = precio;
	}
	
	/**
	 * Constructor: genera un objeto pedido a partir de los parámetros pasados.
	 * El resto de los atributos los obtiene mediante consultas a la base de datos.
	 * 
	 * @param idPed [int] id del pedido
	 * @param idMenu [int] id de menú
	 * @param comanda [int] id de la comanda a la que pertenece
	 * @param estado [String] estado del pedido (pedido, listo, servido, cancelado)
	 */
	public Pedido(int idPed, int idMenu, int comanda, String estado){
		this.oraculo = new Oraculo();
		this.idPed = idPed;
		this.idMenu = idMenu;
		this.comanda = comanda;
		this.estado = estado;
		this.nombreProducto = oraculo.getNombreProductoPorIdMenu(idMenu);
		this.nombreCantidad = oraculo.getNombreCantidadPorIdMenu(idMenu);
	}
	
	/**
	 * Constructor: genera un objeto pedido a partir de los parámetros pasados.
	 * El resto de los atributos los obtiene mediante consultas a la base de datos.
	 * 
	 * @param idPed [int] id del pedido
	 * @param idMenu [int] id de menú
	 * @param comanda [int] id de la comanda a la que pertenece
	 * @param estado [String] estado del pedido (pedido, listo, servido, cancelado)
	 * @param unidades [int] unidades del pedido
	 * @param listos [int] unidades listas del pedido
	 * @param servidos [int] unidades servidas del pedido
	 */
	public Pedido(int idPed, int idMenu, int comanda, String estado, int unidades, int listos, int servidos){
		this.oraculo = new Oraculo();
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
	
	/**
	 * Permite modificar la id del menú del pedido
	 * 
	 * @param idMenu [int] id de menú
	 */
	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}

	/**
	 * Permite modificar el nombre del producto
	 * 
	 * @param nombreProducto [String] nombre del producto
	 */
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	/**
	 * Permite modificar el nombre de la cantidad
	 * 
	 * @param nombreCantidad [String] nombre de la cantidad
	 */
	public void setNombreCantidad(String nombreCantidad) {
		this.nombreCantidad = nombreCantidad;
	}

	/**
	 * Permite modificar la id del destino al que irá el pedido
	 * 
	 * @param idDestino [int] id de destino
	 */
	public void setIdDestino(int idDestino) {
		this.idDestino = idDestino;
	}

	/**
	 * Permite modificar el nombre del destino al que irá el pedido
	 * 
	 * @param nombreDestino [String] nombre del destino
	 */
	public void setNombreDestino(String nombreDestino) {
		this.nombreDestino = nombreDestino;
	}

	/**
	 * Permite modificar las unidades del pedido
	 * 
	 * @param unidades [int] total de unidades del pedido
	 */
	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}

	/**
	 * Permite modificar las unidades listas del pedido
	 * 
	 * @param listos [int] número de unidades listas
	 */
	public void setListos(int listos) {
		this.listos = listos;
	}

	/**
	 * Permite modificar la id del pedido
	 * 
	 * @param idPed [int] id del pedido
	 */
	public void setIdPed(int idPed) {
		this.idPed = idPed;
	}

	/**
	 * Permite modificar el estado del pedido
	 * 
	 * @param estado [String] estado del pedido (pedido, listo, servido, cancelado)
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Permite modificar la id de la comanda a la que pertenece el pedido
	 * 
	 * @param comanda [int] id de la comanda
	 */
	public void setComanda(int comanda) {
		this.comanda = comanda;
	}

	/**
	 * Permite obtener la id del pedido
	 * 
	 * @return [int] id del pedido
	 */
    public int getIdPed() {
		return idPed;
	}

    /**
     * Permite obtener el estado del pedido
     * 
     * @return [String] estado del pedido
     */
	public String getEstado() {
		return estado;
	}

	/**
	 * Permite obtener la id de la comanda a la que pertenece el pedido
	 * 
	 * @return [int] id de la comanda
	 */
	public int getComanda() {
		return comanda;
	}

	/**
	 * Permite obtener la id del destino al que irá el pedido
	 * 
	 * @return [int] id del destino
	 */
	public int getIdDestino() {
        return idDestino;
    }

	/**
	 * Permite obtener el nombre del destino al que irá el pedido
	 * 
	 * @return [String] nombre del destino
	 */
    public String getNombreDestino() {
        return nombreDestino;
    }

    /**
     * Permite obtener la id del menú del pedido
     * 
     * @return [int] id del menú
     */
    public int getIdMenu() {
        return idMenu;
    }

    /**
     * Permite obtener el nombre del producto del pedido
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
     * Permite obtener el número de unidades del pedido
     * 
     * @return [int] unidades del pedido
     */
    public int getUnidades() {
		return unidades;
	}
    
    /**
     * Permite obtener las unidades listas del pedido
     * 
     * @return [int] unidades listas del pedido
     */
    public int getListos() {
		return listos;
	}
    
    /**
     * Permite obtener las unidades servidas del pedido
     * 
     * @return [int] unidades servidas del pedido
     */
    public int getServidos() {
		return servidos;
	}

    /**
     * Permite modificar el número de unidades servidas del pedido
     * 
     * @param servidos [int] unidades servidas del pedido
     */
	public void setServidos(int servidos) {
		this.servidos = servidos;
	}
    
	/**
	 * Permite obtener el precio del pedido
	 * 
	 * @return [float] precio del pedido
	 */
	public float getPrecio(){
		return precio;
	}
}
