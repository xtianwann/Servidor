package accesoDatos;

/**
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class Cantidad {
    
    private int idCant;
    private String nomCant;

    public Cantidad(int idCant, String nomCant) {
        this.idCant = idCant;
        this.nomCant = nomCant;
    }

    public int getIdCant() {
        return idCant;
    }

    public void setIdCant(int idCant) {
        this.idCant = idCant;
    }

    public String getNomCant() {
        return nomCant;
    }

    public void setNomCant(String nomCant) {
        this.nomCant = nomCant;
    }
    
}
