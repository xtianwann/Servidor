package accesoDatos;

/**
 * FINALIZADA
 * 
 * Clase que permite manejar la información referente a las secciones en la base de datos
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Seccion {
   
    private int idSec;
    private String nomSec;
    
    /**
     * Constructor
     * 
     * @param idSec [int] id de la sección
     * @param nomSec [String] nombre de la sección
     */
    public Seccion(int idSec, String nomSec){
        this.idSec = idSec;
        this.nomSec = nomSec;
    }

    /**
     * Permite obtener la id de la sección
     * 
     * @return [int] id de la sección
     */
    public int getIdSec() {
        return idSec;
    }
    /**
     * Permite modificar la id de la sección
     * 
     * @param idSec [int] id de la sección
     */
    public void setIdSec(int idSec) {
        this.idSec = idSec;
    }

    /**
     * Permite obtener el nombre de la sección
     * 
     * @return [String] nombre de la sección
     */
    public String getNomSec() {
        return nomSec;
    }

    /**
     * Permite modificar el nombre de la sección
     * @param nomSec [String] nombre de la sección
     */
    public void setNomSec(String nomSec) {
        this.nomSec = nomSec;
    }
    
    
    
}
