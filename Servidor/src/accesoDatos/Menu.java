package accesoDatos;

/**
 * FINALIZADA
 * 
 * Clase que permite manejar toda la información referente a los menús en la base de datos
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Menu {
    
    private int idMenu;
    private int producto;
    private int cantidad;
    private float precio;
    private int destino;

    /**
     * Constructor
     * 
     * @param idMenu [int] id del menú
     * @param producto [int] id del producto
     * @param cantidad [int] id de la cantidad
     * @param precio [float] precio unitario del menú
     * @param destino [int] id del destino al que está asociado
     */
    public Menu(int idMenu, int producto, int cantidad, float precio, int destino) {
        this.idMenu = idMenu;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.destino = destino;
    }

    /**
     * Permite obtener la id de un menú
     * 
     * @return [int] id del menú
     */
    public int getIdMenu() {
        return idMenu;
    }

    /**
     * Permite modificar la id de un menú
     * 
     * @param idMenu [int] id del menú
     */
    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    /**
     * Permite obtener la id del producto que forma esté menú
     * 
     * @return [int] id del producto
     */
    public int getProducto() {
        return producto;
    }

    /**
     * Permite modificar la id del producto que forma este menú
     * 
     * @param producto [int] id del producto
     */
    public void setProducto(int producto) {
        this.producto = producto;
    }

    /**
     * Permite obtener la id de la cantidad que forma este menú
     * 
     * @return [int] id de la cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Permite modificar la id de la cantidad que forma este menú
     * 
     * @param cantidad [int] id de la cantidad
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Permite obtener el precio del menú
     * 
     * @return [float] precio del menú
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Permite modificar el precio de un menú
     * 
     * @param precio [float] precio del menú
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    /**
     * Permite obtener la id del destino al que está asociado el menú
     * 
     * @return [int] id del destino
     */
    public int getDestino() {
        return destino;
    }

    /**
     * Permite modificar la id del destino al que está asociado un menú
     * 
     * @param destino [int] id del destino
     */
    public void setDestino(int destino) {
        this.destino = destino;
    }
    
}
