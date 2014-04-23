package accesoDatos;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Menu {
    
    private int idMenu;
    private int producto;
    private int cantidad;
    private float precio;
    private int destino;

    public Menu(int idMenu, int producto, int cantidad, float precio, int destino) {
        this.idMenu = idMenu;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.destino = destino;
    }

    /* GETTERS Y SETTERS */
    
    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public int getProducto() {
        return producto;
    }

    public void setProducto(int producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }
    
}
