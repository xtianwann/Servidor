package accesoDatos;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Producto {
    
    private int idProd;
    private String nomProd;
    private int categoria;

    public Producto(int idProd, String nomProd, int categoria) {
        this.idProd = idProd;
        this.nomProd = nomProd;
        this.categoria = categoria;
    }

    /* GETTERS y SETTERS */
    public int getIdProd() {
        return idProd;
    }

    public void setIdProd(int idProd) {
        this.idProd = idProd;
    }

    public String getNomProd() {
        return nomProd;
    }

    public void setNomProd(String nomProd) {
        this.nomProd = nomProd;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    
    
    
}
