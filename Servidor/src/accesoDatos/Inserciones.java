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

		String sentencia = "insert into COMANDAS (fechaCom, mesa, usuario, pagado, cerrada) values ('"
				+ fechaComanda + "', " + idMesa + ", " + idUsuario + ", 0, 0)";
		gestorBD.actualizar(sentencia);
	}

	/**
	 * Inserta un grupo de pedidos de una mesa y devuelve la id de la comanda a
	 * la que pertenecen.
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
			for (int unidad = 0; unidad < unidades; unidad++) {
				String sentencia = "insert into PEDIDOS (menu, comanda, estado) values ('"
						+ idMenu + "', '" + idCom + "', '" + estado + "')";
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
	public void modificarEstadoPedido(String[] idPedidos, String estado) {
		String sentencia = "";

		for (int pedido = 0; pedido < idPedidos.length; pedido++) {
			sentencia = "update PEDIDOS set estado = '" + estado
					+ "' where idPed = " + idPedidos[pedido];
			gestorBD.actualizar(sentencia);
		}
	}

	/**
	 * Actualiza el estado de un dispositivo en la base de datos 
	 * 0 = desconectado
	 * 1 = conectado
	 * 
	 * @param estado nuevo estado que se le va a asignar
	 * @param idDisp id del dispositivo en cuestión
	 */
	public void actualizarEstadoDispositivo(int estado, int idDisp) {
		if (estado == 0 || estado == 1) {
			String sentencia = "update DISPOSITIVOS set conectado = " + estado
					+ " where idDisp = " + idDisp;
			gestorBD.actualizar(sentencia);
		} else {
			System.err.println("[Clase Inserciones] ActualizarEstadoDispositivo(): estado incorrecto, debe ser 0 o 1");
		}
	}

	/**
	 * Cambia el estado de un dispositivo en la base de datos 
	 * 0 = desconectado 
	 * 1 = conectado
	 * 
	 * @param socket de aquí extrae la ip para cambiarle el estado
	 * @param estado nuevo estado que se le va a asignar
	 */
	public void onOffDispositivo(String ip, int estado) {
		if (estado == 0 || estado == 1) {
			String sentencia = "update DISPOSITIVOS set conectado = " + estado
					+ "where ip = '" + ip + "'";
			gestorBD.actualizar(sentencia);
		} else {
			System.err.println("[Clase inserciones] onOffDispositivo(): estado incorrecto, debe ser 0 o 1");
		}
	}

	/**
	 * Desvincula un dispositivo de un usuario
	 * 
	 * @param nomUsu nombre del usuario en cuestión
	 */
	public void vinculoUsuarioDispositivo(String nomUsu, String ip, int login) {
		if (login == 0) { // caso deslogueo: desvincula y apaga el dispositivo
			String sentencia = "update USUARIOS dispositivo = 0 where nomUsu = '" + nomUsu + "'";
			gestorBD.actualizar(sentencia);
			onOffDispositivo(ip, login);
		} else if(login == 1){ // caso login: vinvula y enciende el dispositivo
			String consulta = "select idDisp from DISPOSITIVOS where ip = '" + ip + "'";
			String idDisp = gestorBD.consulta(consulta)[0];
			String sentencia = "update USUARIOS dispositivo = " + idDisp + " where nomUsu = '" + nomUsu + "'";
			gestorBD.actualizar(sentencia);
			onOffDispositivo(ip, login);
		} else {
			System.err.println("[Clase inserciones] vinvuloUsuarioDispositivo(): login incorrecto, debe ser 0 o 1");
		}
	}
}
