package accesoDatos;

import java.util.ArrayList;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Oraculo {
    
    private GestorBD gestorBD = new GestorBD();
    
    /**
     * Obtiene todos los nombres de sección, nombres de mesa y la id de dicha mesa
     * ordenados por sección y luego por mesa en orden alfabético
     *
     * @return String[] con los resultados de la consulta
     */
    public String[] getSeccionesYMesasTodo(){
        String consulta = "select nomSec, nomMes, idMes from SECCIONES inner join MESAS on idSec = seccion order by nomSec, nomMes asc";
        String[] resultado = gestorBD.consulta(consulta, 3);
        return resultado;
    }
    
    /**
     * Obtiene todos los nombres de cantidades, nombres de productos y la id del
     * menú correspondiente ordenado por cantidades y luego por productos en 
     * orden alfabético
     * 
     * @return String[] con los resultados de la consulta
     */
    public String[] getCantidadesYProductosTodo(){
        String consulta = "select nomCant, nomProd, idMenu from CANTIDADES inner join MENUS on cantidad = idCant inner join PRODUCTOS on idProd = producto order by nomCant, nomProd asc";
        String[] resultado = gestorBD.consulta(consulta, 3);
        return resultado;
    }
    
    /**
     * Obtiene el nombre de un producto a partir de una id de Menu
     *
     * @param idMenu id del menú por el que se quiere filtrar la consulta
     * @return String con el resultado de la consulta
     */
    public String getNombreProductoPorIdMenu(int idMenu){
        String consulta = "select nomProd from PRODUCTOS inner join MENUS on idProd = producto where idMenu = " + idMenu;
        String resultado = gestorBD.consulta(consulta)[0];
        return resultado;
    }
    
    /**
     * Obtiene el nombre de una cantidad a partir de una id de Menu
     *
     * @param idMenu id del menú por el que se quiere filtrar la consulta
     * @return String con el resultado de la consulta
     */
    public String getNombreCantidadPorIdMenu(int idMenu){
        String consulta = "select nomCant from CANTIDADES inner join MENUS on idCant = cantidad where idMenu = " + idMenu;
        String resultado = gestorBD.consulta(consulta)[0];
        return resultado;
    }
    
    /**
     * Obtiene el id de un destino a partir de una id de Menu
     *
     * @param idMenu id del menú por el que se quiere filtrar la consulta
     * @return String con el resultado de la consulta
     */
    public String getIdDestinoPorIdMenu(int idMenu){
        String consulta = "select destino from MENUS where idMenu = " + idMenu;
        String resultado = gestorBD.consulta(consulta)[0];
        return resultado;
    }
    
    /**
     * Obtiene el nombre de un destino a partir de una id de Menu
     *
     * @param idMenu id del menú por el que se quiere filtrar la consulta
     * @return String con el resultado de la consulta
     */
    public String getNombreDestinoPorIdMenu(int idMenu){
        String consulta = "select nomDest from DESTINOS inner join MENUS on idDest = destino where idMenu = " + idMenu;
        String resultado = gestorBD.consulta(consulta)[0];
        return resultado;
    }
    
    /**
     * Obtiene el nombre de una mesa a partir de su id
     *
     * @param idMes id de la mesa de la que se quiere obtener el nombre
     * @return String con el resultado de la consulta
     */
    public String getNombreMesaPorIdMesa(int idMes){
        String consulta = "select nomMes from MESAS where idMes = " + idMes;
        String resultado = gestorBD.consulta(consulta)[0];
        return resultado;
    }
    
    /**
     * Obtiene la id de la sección a la que pertenece una mesa a partir de la id
     * de la mesa
     *
     * @param idMes id de la mesa de la que se quiere obtener su sección
     * @return String con el resultado de la consulta
     */
    public String getIdSeccionPorIdMesa(int idMes){
        String consulta = "select seccion from MESAS where idMes = " + idMes;
        String resultado = gestorBD.consulta(consulta)[0];
        return resultado;
    }
    
    /**
     * Dice si una mesa está activa o no, se considera activa si no está pagada y
     * no está cerrada
     *
     * @param idMes id de la mesa de la que se quiere saber si está activa
     * @return true si la mesa está sin pagar y sin cerrar
     */
    public boolean getIsMesaActiva(int idMes){
        boolean isActiva = false;
        String consulta = "select pagado, cerrada from COMANDAS where mesa = " + idMes;
        String[] resultado = gestorBD.consulta(consulta, 2);
        for(int i = 0; i < resultado.length; i+=2){
            if(resultado[i].equals("0") && resultado[i+1].equals("0")){
                isActiva = true;
                break;
            }
        }
        return isActiva;
    }
    
    /**
     * Obtiene la id de una comanda activa a partir de la id de una mesa.
     *
     * @param idMesa
     * @return id de la comanda. 0 en caso de que no haya ninguna comanda sin pagar
     * y sin cerrar
     */
    public int getIdComandaPorIdMesa(int idMesa){
        int idComanda = 0;
        String consulta = "select idCom from COMANDAS where mesa = " + idMesa + " and pagado = 0 and cerrada = 0";
        String[] resultado = gestorBD.consulta(consulta);
        if(resultado.length > 0)
            idComanda = Integer.parseInt(resultado[0]);
        return idComanda;
    }
    
    /**
     * Devuelve fecha y hora actual en el siguiente formato YYYY-MM-DD hh:mm
     *
     * @return fecha y hora actual (YYYY-MM-DD hh:mm)
     */
    public String getFechaYHoraActual(){
        String resultado = gestorBD.consulta("select datetime('now')")[0];
        resultado = resultado.substring(0, resultado.length()-3);
        return resultado;
    }
    
    /**
     * Obtiene la id de un dispositivo a partir de su ip
     *
     * @param ip ip del dispositivo 
     * @return int con la id del dispositivo
     */
    public int getIdDispositivoPorIp(String ip){
        String consulta = "select idDisp from DISPOSITIVOS where ip = '" + ip + "'";
        String resultado = gestorBD.consulta(consulta)[0];
        return Integer.parseInt(resultado);
    }
    
    /**
     * Genera una lista con los pedidos de una mesa
     *
     * @param idMesa id de la mesa de la que queremos obtener los pedidos
     * @return Pedido[] de la mesa pasada por parámetro
     */
    public Pedido[] getPedidosPorIdMesa(int idMesa){
        ArrayList<Pedido> listaPedidos = new ArrayList<>();
        
        String consultaComandaActiva = "select idCom from COMANDAS where pagado = 0 and cerrada = 0 and mesa = " + idMesa;
        int idCom = Integer.parseInt(gestorBD.consulta(consultaComandaActiva)[0]);
        
        String consulta = "select menu, unidades from PEDIDOS where comanda = " + idCom;
        String[] resultados = gestorBD.consulta(consulta, 2);
        
        for(int i = 0; i < resultados.length; i+=2){
            int idMenu = Integer.parseInt(resultados[i]);
            int unidades = Integer.parseInt(resultados[i+1]);
            listaPedidos.add(new Pedido(idMenu, unidades));
        }
        
        return listaPedidos.toArray(new Pedido[0]);
    }
    
    /**
     * Obtiene el nombre de una sección a partir de su id
     *
     * @param idSec id de la sección
     * @return String con el nombre de la sección
     */
    public String getNombreSeccionPorIdSeccion(int idSec){
        String consulta = "select nomSec from SECCIONES where idSec = " + idSec;
        String resultado = gestorBD.consulta(consulta)[0];
        return resultado;
    }
    
    /* Funcion de prueba, borrar al acabar los test */
    public void test(){
        
    }
    
}
