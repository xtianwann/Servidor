package accesoDatos;

/**
 * FINALIZADA
 * 
 * Clase que permite manejar la informaci�n referente a las secciones en la base de datos
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class Seccion {
   
    private int idSec;
    private String nomSec;
    
    /**
     * Constructor
     * 
     * @param idSec [int] id de la secci�n
     * @param nomSec [String] nombre de la secci�n
     */
    public Seccion(int idSec, String nomSec){
        this.idSec = idSec;
        this.nomSec = nomSec;
    }

    /**
     * Permite obtener la id de la secci�n
     * 
     * @return [int] id de la secci�n
     */
    public int getIdSec() {
        return idSec;
    }
    /**
     * Permite modificar la id de la secci�n
     * 
     * @param idSec [int] id de la secci�n
     */
    public void setIdSec(int idSec) {
        this.idSec = idSec;
    }

    /**
     * Permite obtener el nombre de la secci�n
     * 
     * @return [String] nombre de la secci�n
     */
    public String getNomSec() {
        return nomSec;
    }

    /**
     * Permite modificar el nombre de la secci�n
     * @param nomSec [String] nombre de la secci�n
     */
    public void setNomSec(String nomSec) {
        this.nomSec = nomSec;
    }
    
    
    
}
