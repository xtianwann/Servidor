package accesoDatos;

/**
 * Clase que permite manejar todos los datos relativos a una mesa en la base de datos 
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Mesa {
    
    private int idMes;
    private String nomMes;
    private int seccion;
    private boolean activa;
    private Oraculo oraculo;

    /**
     * Constructor
     * 
     * @param idMes [int] id de la mesa
     * @param nomMes [String] nombre de la mesa
     * @param seccion [int] id de la sección
     */
    public Mesa(int idMes, String nomMes, int seccion) {
    	this.oraculo = new Oraculo();
        this.idMes = idMes;
        this.nomMes = nomMes;
        this.seccion = seccion;
        this.activa = oraculo.getIsMesaActiva(this.idMes);
    }
    
    /**
     * Constructor: genera un objeto mesa a partir de su id. El resto de atributos los obtiene
     * a partir de consultas a la base de datos.
     * 
     * @param idMes [int] id de la mesa
     */
    public Mesa(int idMes){
    	this.oraculo = new Oraculo();
        this.idMes = idMes;
        this.nomMes = oraculo.getNombreMesaPorIdMesa(this.idMes);
        this.seccion = Integer.parseInt(oraculo.getIdSeccionPorIdMesa(this.idMes));
        this.activa = oraculo.getIsMesaActiva(this.idMes);
    }

    /**
     * Permite saber si una mesa está activa o no
     * 
     * @return [boolean] true si está activa, false en caso contrario
     */
    public boolean isActiva() {
        return activa;
    }

    /**
     * Permite obtener la id de una mesa
     * 
     * @return [int] id de la mesa
     */
    public int getIdMes() {
        return idMes;
    }

    /**
     * Permite modificar la id de una mesa
     * 
     * @param idMes [int] id de la mesa
     */
    public void setIdMes(int idMes) {
        this.idMes = idMes;
    }

    /**
     * Permite obtener el nombre de una mesa
     * 
     * @return [String] nombre de la mesa
     */
    public String getNomMes() {
        return nomMes;
    }

    /**
     * Permite modificar el nombre de una mesa
     * 
     * @param nomMes [String] nombre de la mesa
     */
    public void setNomMes(String nomMes) {
        this.nomMes = nomMes;
    }

    /**
     * Permite obtener la id de la sección a la que pertenece la mesa
     * 
     * @return [int] id de la sección
     */
    public int getSeccion() {
        return seccion;
    }

    /**
     * Permite modificar la id de la sección a la que pertenece la mesa
     * 
     * @param seccion [int] id de la sección
     */
    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }
    
}
