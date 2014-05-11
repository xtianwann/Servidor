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
    public void insertarNuevaComanda(Mesa mesa, Usuario usuario) {
        String fechaComanda = oraculo.getFechaYHoraActual();
        int idMesa = mesa.getIdMes();
        int idUsuario = usuario.getIdUsu();
        
        String sentencia = "insert into COMANDAS (fechaCom, mesa, usuario, pagado, cerrada) values ('" + fechaComanda + "', " + idMesa + ", " + idUsuario + ", 0, 0)";
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
	            String sentencia = "insert into PEDIDOS (menu, comanda, estado) values ('" + idMenu + "', '" + idCom + "', '" + estado + "')";
	            gestorBD.actualizar(sentencia);
            }
        }
        
        return idCom;
    }
    
    /**
     * Cambia el estado de un pedido al que se le pase por parámetro
     * 
     * @param idPedidos lista de pedidos a modificar
     * @param estado nuevo estado que se le va a asignar a los pedidos de la lista
     */
    public void modificarEstadoPedido(String[] idPedidos, String estado){
    	String sentencia = "";
    	
    	for(int pedido = 0; pedido < idPedidos.length; pedido++){
    		sentencia = "update PEDIDOS set estado = '" + estado + "' where idPed = " + idPedidos[pedido];
    		gestorBD.actualizar(sentencia);
    	}
    }
    
    /**
     * Actualiza el estado de un dispositivo en la base de datos
     * 0 = desconectado
     * 1 = conectado
     * 
     * @param estado nuevo estado que se le va a asignar
     */
    public void actualizarEstadoDispositivo(int estado){
    	if(estado == 0 || estado == 1){
    		String sentencia = "update DISPOSITIVOS set conectado = " + estado;
    		gestorBD.actualizar(sentencia);
    	}
    	else {
    		System.err.println("[Clase Inserciones] ActualizarEstadoDispositivo(): estado incorrecto, debe ser 0 o 1");
    	}
    }
}
