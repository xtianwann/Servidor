package accesoDatos;

/**
 * FINALIZADA
 * 
 * Clase que permite manejar toda la informaci�n referente a los men�s en la base de datos
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
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
     * @param idMenu [int] id del men�
     * @param producto [int] id del producto
     * @param cantidad [int] id de la cantidad
     * @param precio [float] precio unitario del men�
     * @param destino [int] id del destino al que est� asociado
     */
    public Menu(int idMenu, int producto, int cantidad, float precio, int destino) {
        this.idMenu = idMenu;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.destino = destino;
    }

    /**
     * Permite obtener la id de un men�
     * 
     * @return [int] id del men�
     */
    public int getIdMenu() {
        return idMenu;
    }

    /**
     * Permite modificar la id de un men�
     * 
     * @param idMenu [int] id del men�
     */
    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    /**
     * Permite obtener la id del producto que forma est� men�
     * 
     * @return [int] id del producto
     */
    public int getProducto() {
        return producto;
    }

    /**
     * Permite modificar la id del producto que forma este men�
     * 
     * @param producto [int] id del producto
     */
    public void setProducto(int producto) {
        this.producto = producto;
    }

    /**
     * Permite obtener la id de la cantidad que forma este men�
     * 
     * @return [int] id de la cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Permite modificar la id de la cantidad que forma este men�
     * 
     * @param cantidad [int] id de la cantidad
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Permite obtener el precio del men�
     * 
     * @return [float] precio del men�
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Permite modificar el precio de un men�
     * 
     * @param precio [float] precio del men�
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    /**
     * Permite obtener la id del destino al que est� asociado el men�
     * 
     * @return [int] id del destino
     */
    public int getDestino() {
        return destino;
    }

    /**
     * Permite modificar la id del destino al que est� asociado un men�
     * 
     * @param destino [int] id del destino
     */
    public void setDestino(int destino) {
        this.destino = destino;
    }
    
}
