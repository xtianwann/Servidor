package accesoDatos;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Seccion {
   
    private int idSec;
    private String nomSec;
    
    public Seccion(int idSec, String nomSec){
        this.idSec = idSec;
        this.nomSec = nomSec;
    }

    /* GETTERS y SETTERS */
    public int getIdSec() {
        return idSec;
    }

    public void setIdSec(int idSec) {
        this.idSec = idSec;
    }

    public String getNomSec() {
        return nomSec;
    }

    public void setNomSec(String nomSec) {
        this.nomSec = nomSec;
    }
    
    
    
}
