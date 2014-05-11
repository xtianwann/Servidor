package accesoDatos;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

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
        
        String consulta = "select menu from PEDIDOS where comanda = " + idCom;
        String[] resultados = gestorBD.consulta(consulta, 2);
        
        for(int i = 0; i < resultados.length; i++){
            int idMenu = Integer.parseInt(resultados[i]);
            listaPedidos.add(new Pedido(idMenu));
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
    
    /**
     * Obtiene una lista de pedidos pertenecientes a una comanda y de unos
     * productos concretos que se encuentren en estado "pedido"
     * 
     * @param idMenu id del producto por el que se quiere filtrar
     * @param idComanda id de la comanda por la que se quiere filtrar
     * @return lista con las id de los pedidos que coincidan con la búsqueda
     */
    public String[] getIdPedidoPorIdMenuYIdComanda(int idMenu, int idComanda, String estado){
    	String consulta = "select idPed from PEDIDOS where comanda = " + idComanda + " and menu = " + idMenu + " and estado = '"+estado+"'";
    	String[] resultado = gestorBD.consulta(consulta);
    	return resultado;
    }
    
    /**
     * Obtiene el dispositivo desde el que se emitió una comanda
     * 
     * @param idComanda id de la comanda de la que se quiere conocer el camarero que la creó
     * @return ip del dispositivo del camarero
     */
    public String getCamareroPorComanda(int idComanda){
    	String consulta = "select ip from DISPOSITIVOS inner join COMANDAS on idDisp = dispositivo where idCom = " + idComanda;
    	String resultado = gestorBD.consulta(consulta)[0];
    	return resultado;
    }
    
    /**
     * 
     * @param idUsu
     * @return
     */
    public Usuario getUsuarioById(int idUsu){
    	String consulta = "select * from USUARIOS where idUsu = " + idUsu;
    	String[] datosUsu = gestorBD.consulta(consulta);
    	consulta = "select ip from DISPOSITIVOS where idDisp = " + datosUsu[2];
    	String ip = gestorBD.consulta(consulta)[0];
    	
    	return new Usuario(Integer.parseInt(datosUsu[0]), datosUsu[1], Integer.parseInt(datosUsu[2]), ip);
    }
    
    /**
     * 
     * @param socket
     * @return
     */
    public Usuario getUsuarioByIp(Socket socket){
    	System.out.println(socket.getInetAddress());
    	String consulta = "select idUsu, nomUsu, dispositivo, ip USUARIOS inner join DISPOSITIVOS on idDisp = dispositivo where ip = '" + socket.getInetAddress() + "'";
    	String[] resultado = gestorBD.consulta(consulta);
    	
    	return new Usuario(Integer.parseInt(resultado[0]), resultado[1], Integer.parseInt(resultado[2]), resultado[3]);
    }
    
    /**
     * 
     * @param idMenu
     * @return
     */
    public Dispositivo getDispositivoPorIdMenu(int idMenu){
    	String consulta = "select idDisp, conectado, ip, nomDest from DISPOSITIVOS d inner join DESTINOS on destino = idDest inner join MENUS m on destino = idDest where idMenu = " + idMenu;
    	String[] resultado = gestorBD.consulta(consulta);
    	
    	return new Dispositivo(Integer.parseInt(resultado[0]), Integer.parseInt(resultado[1]), resultado[2], resultado[3]);
    }
    
    /**
     * 
     * 
     * @param dispositivo
     * @return
     */
    public PedidoPendiente[] getPedidosPendientes(Dispositivo dispositivo){
    	ArrayList<PedidoPendiente> pedidosPendientes = new ArrayList<>();
    	HashMap<Integer, ArrayList<Pedido>> mapaPedidosComanda = new HashMap<>();
    	ArrayList<Integer> comandas = new ArrayList<>();
    	
    	/* Obtenemos todos los menús distintos de las comandas activas */
    	String consulta = "select distinct menu from PEDIDOS inner join MENUS on menu = idMenu inner join DESTINOS on destino = idDest where nomDest = '" + dispositivo.getNombreDestino() + "'";
    	String[] idMenus = gestorBD.consulta(consulta);
    	
    	/* Obtenemos todos los pedidos de las comandas activas y los 
    	 * separamos por comanda */
    	Pedido[] pedidos = getPedidos(idMenus);
    	for(Pedido pedido : pedidos){
    		if(!mapaPedidosComanda.containsKey(pedido.getComanda())){
    			comandas.add(pedido.getComanda());
    			mapaPedidosComanda.put(pedido.getComanda(), new ArrayList<Pedido>());
    		}
    		mapaPedidosComanda.get(pedido.getComanda()).add(pedido);
    	}
    	
    	ArrayList<Pedido> pedidosComanda = new ArrayList<>();
    	for(int contadorComanda = 0; contadorComanda < comandas.size(); contadorComanda++){
    		pedidosComanda = mapaPedidosComanda.get(comandas.get(contadorComanda));
    		for(int contadorMenu = 0; contadorMenu < idMenus.length; contadorMenu++){
    			int udTotales = 0;
    			int udPedido = 0;
    			int udListo = 0;
    			int udServido = 0;
    			Pedido pEnv = null; // aquí guardo la información de pedido que debo pasar a pedidoPendiente
    			for(int contadorPedido = 0; contadorPedido < pedidosComanda.size(); contadorPedido++){
    				Pedido p = pedidosComanda.get(contadorPedido);
    				if(p.getIdMenu() == Integer.parseInt(idMenus[contadorMenu])){
    					pEnv = p; // si hay coincidencia guardo la info y empiezo a contar
    					if(p.getEstado().equals("Pedido")){
    						udPedido++;
    						udTotales++;
    					} else if(p.getEstado().equals("Listo")){
    						udListo++;
    						udTotales++;
    					} else if(p.getEstado().equals("Servido")){
    						udServido++;
    						udTotales++;
    					}
    				}
    			}
    			pedidosPendientes.add(new PedidoPendiente(pEnv, udTotales, udPedido, udListo, udServido));
    		}
    	}
    	
    	return pedidosPendientes.toArray(new PedidoPendiente[0]);
    }
    
    /**
     * Devuelve todos los pedidos de un menú en concreto de aquellas comandas 
     * que estén activas.
     * 
     * @return array de Pedido
     */
    public Pedido[] getPedidos(String[] idMenus){
    	String consulta = "";
    	String[] datos;
    	ArrayList<String> tuplas = new ArrayList<>();
    	ArrayList<Pedido> pedidos = new ArrayList<>();
    	
    	for(String menu : idMenus){
    		consulta = "select idPed, menu, comanda, estado from PEDIDOS where menu = " + menu;
    		datos = gestorBD.consulta(consulta);
    		for(int contador = 0; contador < datos.length; contador++){
    			tuplas.add(datos[contador]);
    		}
    	}
    	
    	for(int contador = 0; contador < tuplas.size(); contador+=4){
    		pedidos.add(new Pedido(Integer.parseInt(tuplas.get(contador)), Integer.parseInt(tuplas.get(contador+1)), Integer.parseInt(tuplas.get(contador+2)), tuplas.get(contador+3)));
    	}
    	
    	return pedidos.toArray(new Pedido[0]);
    }
    
    /**
     * Obtiene el nombre de una mesa a partir de una comanda
     * 
     * @param idComanda comanda que pertenece a la mesa de la que queremos obtener el nombre
     * @return nombre de la mesa
     */
    public String getNombreMesa(int idComanda){
    	String consulta = "select nomMes from MESAS inner join COMANDAS on mesa = idMes where idCom = " + idComanda;
    	String resultado = gestorBD.consulta(consulta)[0];
    	return resultado;
    }
    
    /**
     * Obtiene el nombre de una sección a partir de una comanda
     * 
     * @param idComanda comanda que pertenece a una mesa y sección concreta
     * @return nombre de la sección
     */
    public String getNombreSeccion(int idComanda){
    	String consulta = "select nomSec from SECCIONES inner join MESAS on idSec = seccion inner join COMANDAS on mesa = idMes where idCom = " + idComanda;
    	String resultado = gestorBD.consulta(consulta)[0];
    	return resultado;
    }
    
    /* Funcion de prueba, borrar al acabar los test */
    public void test(){
        
    }
    
}
