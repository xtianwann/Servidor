package accesoDatos;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Inserciones {

    private GestorBD gestorBD = new GestorBD();
    private Oraculo oraculo = new Oraculo();

    /**
     * Crea una nueva comanda en la base de datos.
     * 
     * @param mesa objeto Mesa al que está asociada la comanda
     * @param dispositivo objeto Dispositivo desde el que se tomó la comanda
     */
    public void insertarNuevaComanda(Mesa mesa, Dispositivo dispositivo) {
        String fechaComanda = oraculo.getFechaYHoraActual();
        int idMesa = mesa.getIdMes();
        int idDispositivo = dispositivo.getIdDisp();
        
        String sentencia = "insert into COMANDAS (fechaCom, mesa, dispositivo, pagado, cerrada) values ('" + fechaComanda + "', " + idMesa + ", " + idDispositivo + ", 0, 0)";
        gestorBD.actualizar(sentencia);
    }

    /**
     * Inserta un grupo de pedidos de una mesa y devuelve la id de la comanda a la que pertenecen.
     * 
     * @param mesa objeto Mesa para el que se han realizado los pedidos
     * @param pedidos lista de objetos Pedido con los datos a insertar
     * 
     * @return id de la comanda a la que pertenecen los pedidos
     */
    public int insertarPedidos(Mesa mesa, Pedido[] pedidos) {
        int idMenu;
        int idCom = oraculo.getIdComandaPorIdMesa(mesa.getIdMes());
        String estado = "pedido";

        for (Pedido p : pedidos) {
            idMenu = p.getIdMenu();
            int unidades = p.getUnidades();
            for(int unidad = 0; unidad < unidades; unidad++){
	            String sentencia = "insert into PEDIDOS (menu, comanda, estado) values (" + idMenu + ", " + idCom + ", " + estado + "')";
	            gestorBD.actualizar(sentencia);
            }
        }
        
        return idCom;
    }
}
