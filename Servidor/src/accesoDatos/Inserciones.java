package accesoDatos;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Inserciones {

    private GestorBD gestorBD = new GestorBD();

    public void insertarNuevaComanda(Mesa mesa, Dispositivo dispositivo) {
        Oraculo oraculo = new Oraculo();
        String fechaComanda = oraculo.getFechaYHoraActual();
        int idMesa = mesa.getIdMes();
        int idDispositivo = dispositivo.getIdDisp();
        
        String sentencia = "insert into COMANDAS (fechaCom, mesa, dispositivo, pagado, cerrada) values ('" + fechaComanda + "', " + idMesa + ", " + idDispositivo + ", 0, 0)";
        gestorBD.actualizar(sentencia);
    }

    public void insertarPedidos(Mesa mesa, Pedido[] pedidos) {
        Oraculo oraculo = new Oraculo();
        int idMenu;
        int comanda;
        int unidades;
        String estado = "pedido";

        for (Pedido p : pedidos) {
            idMenu = p.getIdMenu();
            comanda = oraculo.getIdComandaPorIdMesa(mesa.getIdMes());
            unidades = p.getUnidades();

            String sentencia = "insert into PEDIDOS (menu, comanda, unidades, estado) values (" + idMenu + ", " + comanda + ", " + unidades + ", '" + estado + "')";
            gestorBD.actualizar(sentencia);
        }
    }
}
