package accesoDatos;

/**
 * FINALIZADA
 * 
 * Clase que permite manejar información relativa a un producto de la base de datos
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Producto {
    
    private int idProd;
    private String nomProd;
    private int categoria;

    /**
     * Constructor
     * 
     * @param idProd [int] id del producto
     * @param nomProd [String] nombre del producto
     * @param categoria [int] id de la categoría a la que pertenece el prodcuto
     */
    public Producto(int idProd, String nomProd, int categoria) {
        this.idProd = idProd;
        this.nomProd = nomProd;
        this.categoria = categoria;
    }

    /**
     * Permite obtener la id del producto
     * 
     * @return [int] id del producto
     */
    public int getIdProd() {
        return idProd;
    }

    /**
     * Permite modificar la id del producto
     * 
     * @param idProd [int] id del producto
     */
    public void setIdProd(int idProd) {
        this.idProd = idProd;
    }

    /**
     * Permite obtener el nombre del producto
     * 
     * @return [String] nombre del producto
     */
    public String getNomProd() {
        return nomProd;
    }

    /**
     * Permite modificar el nombre del producto
     * 
     * @param nomProd [String] nombre del producto
     */
    public void setNomProd(String nomProd) {
        this.nomProd = nomProd;
    }

    /**
     * Permite obtener la id de la categoría a la que pertenece el producto
     * 
     * @return [int] id de la categoría
     */
    public int getCategoria() {
        return categoria;
    }

    /**
     * Permite modificar la id de la categoría a la que pertenece el producto
     * 
     * @param categoria [int] id de la categoría
     */
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    
    
    
}
