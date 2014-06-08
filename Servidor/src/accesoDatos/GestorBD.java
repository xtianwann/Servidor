package accesoDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase encargada de realizar todas las operaciones relacionadas con la base de
 * datos
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class GestorBD {

	String cadenaConexion = "";
	Connection conexion = null;

	/**
	 * Constructor: realiza la conexión con la base de datos
	 */
	public GestorBD() {
		try {
			Class.forName("org.sqlite.JDBC");
			cadenaConexion = "jdbc:sqlite:basedatos/restaurante.db";

		} catch (ClassNotFoundException ex) {
			Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	/**
	 * Permite realizar consultas a la base de datos
	 * 
	 * @param consulta [String] sentencia que ejecutará la consulta
	 * @param numeroCampos [int] indica cuantos campos hay en la selección
	 * @return [String[ ]] conjunto de resultados de la consulta en forma de array
	 */
	public String[] consulta(String consulta, int numeroCampos) {
		ArrayList<String> resultados = new ArrayList<>();

		try {
			conexion = DriverManager.getConnection(cadenaConexion);
			ResultSet resultado = conexion.createStatement().executeQuery(
					consulta);
			while (resultado.next()) {
				for (int campo = 1; campo <= numeroCampos; campo++) {
					resultados.add(resultado.getString(campo));
				}
			}
			conexion.close();
		} catch (SQLException ex) {
			Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		return resultados.toArray(new String[0]);
	}

	/**
	 * Permite realizar consultas de un solo campo a la base de datos
	 * 
	 * @param consulta [String] sentencia que ejecutará la consulta
	 * @param numeroCampos [int] indica cuantos campos hay en la selección
	 * @return [String[ ]] conjunto de resultados de la consulta en forma de array
	 */
	public String[] consulta(String consulta) {
		return consulta(consulta, 1);
	}

	/**
	 * Permite realizar inserciones, modificaciones y borrado de datos en la
	 * base de datos.
	 * 
	 * @param actualizacion [String] sentencia que se ejecutará para modificar o borrar datos
	 */
	public void ejecutarSentencia(String actualizacion) {
		try {
			conexion = DriverManager.getConnection(cadenaConexion);
			conexion.createStatement().executeUpdate(actualizacion);
			conexion.close();
		} catch (SQLException ex) {
			Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Permite realizar inserciones masivas en la entrada de pedidos
	 * 
	 * @param tablaYCols
	 * @param idCom
	 * @param pedidos
	 */
	public void insertarMultiple(int idCom, Pedido[] pedidos) {
		try {
			conexion = DriverManager.getConnection(cadenaConexion);
			conexion.setAutoCommit(false);
			String insertSql = "insert into PEDIDOS (menu, comanda, estado) values (?, ?, ?)";
			PreparedStatement ps = conexion.prepareStatement(insertSql);
			for (int i = 0; i < pedidos.length; i++) {
				System.out.println(pedidos[i].getUnidades());
				for (int unidad = 0; unidad < pedidos[i].getUnidades(); unidad++) {
					ps.setInt(1, pedidos[i].getIdMenu());
					ps.setInt(2, idCom);
					ps.setString(3, "pedido");
					ps.addBatch();
				}
			}
			ps.executeBatch();
			conexion.commit();
			conexion.close();

		} catch (SQLException ex) {
			Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
}
