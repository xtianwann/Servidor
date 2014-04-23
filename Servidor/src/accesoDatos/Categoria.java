package accesoDatos;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Categoria {
    
    private int idCat;
    private String nomCat;

    public Categoria(int idCat, String nomcat) {
        this.idCat = idCat;
        this.nomCat = nomcat;
    }

    /* GETTERS y SETTERS */
    public int getIdCat() {
        return idCat;
    }

    public void setIdCat(int idCat) {
        this.idCat = idCat;
    }

    public String getNomCat() {
        return nomCat;
    }

    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }
    
    
    
}
