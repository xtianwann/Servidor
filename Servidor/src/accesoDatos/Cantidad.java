package accesoDatos;

/**
 * FINALIZADA
 * 
 * Clase que permite manejar toda la información referente a las cantidades en la base de datos.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Cantidad {
    
    private int idCant;
    private String nomCant;

    /**
     * Constructor:
     * 
     * @param idCant [int] id de la cantidad
     * @param nomCant [String] nombre de la cantidad
     */
    public Cantidad(int idCant, String nomCant) {
        this.idCant = idCant;
        this.nomCant = nomCant;
    }

    /**
     * Permite obtener la id de una cantidad
     * 
     * @return [int] id de la cantidad
     */
    public int getIdCant() {
        return idCant;
    }

    /**
     * Permite modificar la id de una cantidad
     * 
     * @param idCant [int] id de la cantidad
     */
    public void setIdCant(int idCant) {
        this.idCant = idCant;
    }

    /**
     * Permite obtener el nombre de una cantidad
     * 
     * @return [String] nombre de la cantidad
     */
    public String getNomCant() {
        return nomCant;
    }

    /**
     * Permite modificar el nombre de una cantidad
     * 
     * @param nomCant [String] nombre de la cantidad 
     */
    public void setNomCant(String nomCant) {
        this.nomCant = nomCant;
    }
    
}
