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
    
    public Pedido(int idMenu){
    	Oraculo oraculo = new Oraculo();
        this.idMenu = idMenu;
        this.nombreProducto = oraculo.getNombreProductoPorIdMenu(this.idMenu);
        this.nombreCantidad = oraculo.getNombreCantidadPorIdMenu(this.idMenu);
        this.idDestino = Integer.parseInt(oraculo.getIdDestinoPorIdMenu(this.idMenu));
        this.nombreDestino = oraculo.getNombreDestinoPorIdMenu(this.idMenu);
        this.unidades = 0;
    }

	public Pedido(int idMenu, int unidades) {
        Oraculo oraculo = new Oraculo();
        this.idMenu = idMenu;
        this.nombreProducto = oraculo.getNombreProductoPorIdMenu(this.idMenu);
        this.nombreCantidad = oraculo.getNombreCantidadPorIdMenu(this.idMenu);
        this.idDestino = Integer.parseInt(oraculo.getIdDestinoPorIdMenu(this.idMenu));
        this.nombreDestino = oraculo.getNombreDestinoPorIdMenu(this.idMenu);
        this.unidades = unidades;
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
