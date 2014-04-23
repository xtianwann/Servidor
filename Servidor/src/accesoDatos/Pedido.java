package accesoDatos;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Pedido {

    private int idMenu;
    private int unidades;
    private String nombreProducto;
    private String nombreCantidad;
    private int idDestino;
    private String nombreDestino;

    public Pedido(int idMenu, int unidades) {
        Oraculo oraculo = new Oraculo();
        this.idMenu = idMenu;
        this.unidades = unidades;
        this.nombreProducto = oraculo.getNombreProductoPorIdMenu(this.idMenu);
        this.nombreCantidad = oraculo.getNombreCantidadPorIdMenu(this.idMenu);
        this.idDestino = Integer.parseInt(oraculo.getIdDestinoPorIdMenu(this.idMenu));
        this.nombreDestino = oraculo.getNombreDestinoPorIdMenu(this.idMenu);
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

    public int getUnidades() {
        return unidades;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getNombreCantidad() {
        return nombreCantidad;
    }
    
}
