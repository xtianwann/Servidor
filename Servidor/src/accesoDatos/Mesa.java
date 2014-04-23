package accesoDatos;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Mesa {
    
    private int idMes;
    private String nomMes;
    private int seccion;
    private boolean activa;

    public Mesa(int idMes, String nomMes, int seccion) {
        Oraculo oraculo = new Oraculo();
        this.idMes = idMes;
        this.nomMes = nomMes;
        this.seccion = seccion;
        this.activa = oraculo.getIsMesaActiva(this.idMes);
    }
    
    public Mesa(int idMes){
        Oraculo oraculo = new Oraculo();
        this.idMes = idMes;
        this.nomMes = oraculo.getNombreMesaPorIdMesa(this.idMes);
        this.seccion = Integer.parseInt(oraculo.getIdSeccionPorIdMesa(this.idMes));
        this.activa = oraculo.getIsMesaActiva(this.idMes);
    }

    public boolean isActiva() {
        return activa;
    }

    /* GETTERS y SETTERS */
    public int getIdMes() {
        return idMes;
    }

    public void setIdMes(int idMes) {
        this.idMes = idMes;
    }

    public String getNomMes() {
        return nomMes;
    }

    public void setNomMes(String nomMes) {
        this.nomMes = nomMes;
    }

    public int getSeccion() {
        return seccion;
    }

    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }
    
    
    
}
