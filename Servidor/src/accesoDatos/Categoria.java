package accesoDatos;

/**
 * 
 * Esta clase permite manejar la informaci�n relativa a las distintas categor�as que 
 * haya en la base de datos.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class Categoria {
    
    private int idCat;
    private String nomCat;

    /**
     * Constructor
     * 
     * @param idCat [int] id de la categor�a
     * @param nomcat [String] nombre de la categor�a
     */
    public Categoria(int idCat, String nomcat) {
        this.idCat = idCat;
        this.nomCat = nomcat;
    }

    /**
     * Permite obtener la id de una categor�a
     * 
     * @return [int] id de la categor�a
     */
    public int getIdCat() {
        return idCat;
    }

    /**
     * Permite modificar la id de una categor�a
     * 
     * @param idCat [int] id de la categor�a
     */
    public void setIdCat(int idCat) {
        this.idCat = idCat;
    }

    /**
     * Permite obtener el nombre de una categor�a
     * 
     * @return [String] nombre de la categor�a
     */
    public String getNomCat() {
        return nomCat;
    }

    /**
     * Permite modificar el nombre de una categor�a
     * 
     * @param nomCat [String] nombre de la categor�a
     */
    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }

}
