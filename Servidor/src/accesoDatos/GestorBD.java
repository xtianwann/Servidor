package accesoDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class GestorBD {

    String cadenaConexion = "";
    Connection conexion = null;

    public GestorBD() {
        try {
            Class.forName("org.sqlite.JDBC");
            cadenaConexion = "jdbc:sqlite:basedatos/restaurante.db";

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Permite realizar consultas a la base de datos
     *
     * @param consulta Sentencia que ejecutar� la consulta
     * @param numeroCampos indica cuantos campos hay en la selecci�n
     * @return Conjunto de resultados de la consulta en forma de array
     */
    public String[] consulta(String consulta, int numeroCampos) {
        ArrayList<String> resultados = new ArrayList<>();

        try {
            conexion = DriverManager.getConnection(cadenaConexion);
            ResultSet resultado = conexion.createStatement().executeQuery(consulta);
            while (resultado.next()) {
                for (int campo = 1; campo <= numeroCampos; campo++) {
                    resultados.add(resultado.getString(campo));
                }
            }
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultados.toArray(new String[0]);
    }

    /**
     * Permite realizar consultas de un solo campo a la base de datos
     *
     * @param consulta Sentencia que ejecutar� la consulta
     * @param numeroCampos indica cuantos campos hay en la selecci�n
     * @return Conjunto de resultados de la consulta en forma de array
     */
    public String[] consulta(String consulta) {
        return consulta(consulta, 1);
    }

    /**
     * Permite realizar inserciones, modificaciones y borrado de datos en la
     * base de datos.
     *
     * @param actualizacion Sentencia que se ejecutar� para modificar o borrar
     * datos
     */
    public void actualizar(String actualizacion) {
        try {
            conexion = DriverManager.getConnection(cadenaConexion);
            conexion.createStatement().executeUpdate(actualizacion);
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
