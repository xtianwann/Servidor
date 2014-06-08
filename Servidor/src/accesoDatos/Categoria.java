package accesoDatos;

/**
 * 
 * Esta clase permite manejar la información relativa a las distintas categorías que 
 * haya en la base de datos.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Categoria {
    
    private int idCat;
    private String nomCat;

    /**
     * Constructor
     * 
     * @param idCat [int] id de la categoría
     * @param nomcat [String] nombre de la categoría
     */
    public Categoria(int idCat, String nomcat) {
        this.idCat = idCat;
        this.nomCat = nomcat;
    }

    /**
     * Permite obtener la id de una categoría
     * 
     * @return [int] id de la categoría
     */
    public int getIdCat() {
        return idCat;
    }

    /**
     * Permite modificar la id de una categoría
     * 
     * @param idCat [int] id de la categoría
     */
    public void setIdCat(int idCat) {
        this.idCat = idCat;
    }

    /**
     * Permite obtener el nombre de una categoría
     * 
     * @return [String] nombre de la categoría
     */
    public String getNomCat() {
        return nomCat;
    }

    /**
     * Permite modificar el nombre de una categoría
     * 
     * @param nomCat [String] nombre de la categoría
     */
    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }

}
