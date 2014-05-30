package accesoDatos;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class Oraculo {
    
    private GestorBD gestorBD = new GestorBD();
    
    /**
     * Obtiene todos los nombres de secci�n, nombres de mesa y la id de dicha mesa
     * ordenados por secci�n y luego por mesa en orden alfab�tico
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
     * men� correspondiente ordenado por cantidades y luego por productos en 
     * orden alfab�tico
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
     * @param idMenu id del men� por el que se quiere filtrar la consulta
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
     * @param idMenu id del men� por el que se quiere filtrar la consulta
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
     * @param idMenu id del men� por el que se quiere filtrar la consulta
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
     * @param idMenu id del men� por el que se quiere filtrar la consulta
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
     * Obtiene la id de la secci�n a la que pertenece una mesa a partir de la id
     * de la mesa
     *
     * @param idMes id de la mesa de la que se quiere obtener su secci�n
     * @return String con el resultado de la consulta
     */
    public String getIdSeccionPorIdMesa(int idMes){
        String consulta = "select seccion from MESAS where idMes = " + idMes;
        String resultado = gestorBD.consulta(consulta)[0];
        return resultado;
    }
    
    /**
     * Dice si una mesa est� activa o no, se considera activa si no est� pagada y
     * no est� cerrada
     *
     * @param idMes id de la mesa de la que se quiere saber si est� activa
     * @return true si la mesa est� sin pagar y sin cerrar
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
     * @return Pedido[] de la mesa pasada por par�metro
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
     * Obtiene el nombre de una secci�n a partir de su id
     *
     * @param idSec id de la secci�n
     * @return String con el nombre de la secci�n
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
     * @return lista con las id de los pedidos que coincidan con la b�squeda
     */
    public String[] getIdPedidoPorIdMenuYIdComanda(int idMenu, int idComanda, String estado){
    	String consulta = "select idPed from PEDIDOS where comanda = " + idComanda + " and menu = " + idMenu + " and estado = '"+estado+"'";
    	String[] resultado = gestorBD.consulta(consulta);
    	return resultado;
    }
    
    /**
     * Obtiene el dispositivo desde el que se emiti� una comanda
     * 
     * @param idComanda id de la comanda de la que se quiere conocer el camarero que la cre�
     * @return ip del dispositivo del camarero
     */
    public String getCamareroPorComanda(int idComanda){
    	String consulta = "select idUsu from USUARIOS inner join COMANDAS on idUsu = usuario where idCom = " + idComanda;
    	String resultado = gestorBD.consulta(consulta)[0];
    	resultado = getIpPorIdUsuario(Integer.parseInt(resultado));
    	return resultado;
    }
    
    /**
     * Obtiene la ip del dispositivo en el que se encuentra un camarero
     * 
     * @param idUsu id de usuario que queremos saber su dispositivo
     * @return ip del dispositivo que est� usando, null si no est� usando ninguno
     */
    public String getIpPorIdUsuario(int idUsu){
    	String consulta = "select ip from DISPOSITIVOS inner join USUARIOS on idDisp = dispositivo where idUsu = " + idUsu;
    	String resultado = gestorBD.consulta(consulta)[0];
    	return resultado;
    }
    
    /**
     * Obtiene un objeto usuario a partir de una id de usuario
     * 
     * @param idUsu int id del usuario del que vamos a crear el objeto
     * @return Usuario con toda la informaci�n necesaria
     */
    public Usuario getUsuarioById(int idUsu){
    	String consulta = "select idUsu, nomUsu, dispositivo from USUARIOS where idUsu = " + idUsu;
    	String[] datosUsu = gestorBD.consulta(consulta,3);
    	consulta = "select ip from DISPOSITIVOS where idDisp = " + datosUsu[2];
    	String ip = gestorBD.consulta(consulta)[0];
    	return new Usuario(Integer.parseInt(datosUsu[0]), datosUsu[1], Integer.parseInt(datosUsu[2]), ip);
    }
    
    /**
     * Obtiene un usuario a partir de la ip que se extrae del socket
     * 
     * @param socket socket por el que el usuario estableci� conexi�n
     * @return Usuario con toda la informaci�n
     */
    public Usuario getUsuarioByIp(Socket socket){
    	String ip = socket.getInetAddress()+"";
    	ip = ip.substring(1);
    	String consulta = "select * from USUARIOS inner join DISPOSITIVOS on idDisp = dispositivo where ip = '" + ip + "'";
    	String[] resultado = gestorBD.consulta(consulta,3);
    	int idUsu = Integer.parseInt(resultado[0]);
    	String nomUsu = resultado[1];
    	int idDisp = Integer.parseInt(resultado[2]);
    	return new Usuario(idUsu, nomUsu, idDisp, ip);
    }
    
    /**
     * Obtiene un usuario a partir de la ip de un dispositivo
     * 
     * @param ip String ip del dispositivo
     * @return Usuario con toda la informaci�n
     */
    public Usuario getUsuarioByIp(String ip){
    	String consulta = "select * from USUARIOS inner join DISPOSITIVOS on idDisp = dispositivo where ip = '" + ip + "'";
    	String[] resultado = gestorBD.consulta(consulta,3);
    	int idUsu = Integer.parseInt(resultado[0]);
    	String nomUsu = resultado[1];
    	int idDisp = Integer.parseInt(resultado[2]);
    	return new Usuario(idUsu, nomUsu, idDisp, ip);
    }
    
    /**
     * Devuelve un objeto dispositivo del tipo destino a partir de una id de men�
     * 
     * @param idMenu int id de men�
     * @return Dispositivo del tipo destino
     */
    public Dispositivo getDispositivoPorIdMenu(int idMenu){
    	String consulta = "select idDisp, conectado, ip, nomDest from DISPOSITIVOS d inner join DESTINOS on d.destino = idDest inner join MENUS m on m.destino = idDest where idMenu = " + idMenu;
    	String[] resultado = gestorBD.consulta(consulta,4);
    	return new Dispositivo(Integer.parseInt(resultado[0]), Integer.parseInt(resultado[1]), resultado[2], resultado[3]);
    }
    
    /**
     * Devuelve una lista de pedidos pendientes de un dispositivo tipo destino (cocina o barra)
     * 
     * @param dispositivo Dispositivo del que se quiere obtener la lista
     * @return PedidoPendiente[] lista de todos los pedidos pendientes de ese dispositivo
     */
    public PedidoPendiente[] getPedidosPendientes(Dispositivo dispositivo){
    	ArrayList<PedidoPendiente> pedidosPendientes = new ArrayList<>();
    	HashMap<Integer, ArrayList<Pedido>> mapaPedidosComanda = new HashMap<>();
    	ArrayList<Integer> comandas = new ArrayList<>();
    	/* Obtenemos todos los men�s distintos de las comandas activas */
    	String consulta = "select distinct menu from PEDIDOS inner join MENUS on menu = idMenu inner join DESTINOS on destino = idDest where nomDest = '" + dispositivo.getNombreDestino() + "' order by idPed";
    	String[] idMenus = gestorBD.consulta(consulta);
    	/* Obtenemos todos los pedidos de las comandas activas y los separamos por comanda */
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
    			Pedido pEnv = null; // aqu� guardo la informaci�n de pedido que debo pasar a pedidoPendiente
    			for(int contadorPedido = 0; contadorPedido < pedidosComanda.size(); contadorPedido++){
    				Pedido p = pedidosComanda.get(contadorPedido);
    				if(p.getIdMenu() == Integer.parseInt(idMenus[contadorMenu])){
    					pEnv = p; // si hay coincidencia guardo la info y empiezo a contar
    					if(p.getEstado().equals("pedido")){
    						udPedido++;
    						udTotales++;
    					} else if(p.getEstado().equals("listo")){
    						udListo++;
    						udTotales++;
    					} else if(p.getEstado().equals("servido")){
    						udServido++;
    						udListo++;
    						udTotales++;
    					}
    				}
    			}
    			if(pEnv != null)
    				pedidosPendientes.add(new PedidoPendiente(pEnv, udTotales, udPedido, udListo, udServido));
    		}
    	}
    	return pedidosPendientes.toArray(new PedidoPendiente[0]);
    }
    
    /**
     * Devuelve todos los pedidos de un men� en concreto de aquellas comandas 
     * que est�n activas.
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
    		datos = gestorBD.consulta(consulta,4);
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
     * Obtiene el nombre de una secci�n a partir de una comanda
     * 
     * @param idComanda comanda que pertenece a una mesa y secci�n concreta
     * @return nombre de la secci�n
     */
    public String getNombreSeccion(int idComanda){
    	String consulta = "select nomSec from SECCIONES inner join MESAS on idSec = seccion inner join COMANDAS on mesa = idMes where idCom = " + idComanda;
    	String resultado = gestorBD.consulta(consulta)[0];
    	return resultado;
    }
    
    /**
     * Obtiene el nombre de una secci�n a partir de la id de una mesa
     * 
     * @param idMesa int id de la mesa
     * @return String nombre de la secci�n
     */
    public String getNombreSeccionPorIdMesa(int idMesa){
    	String consulta = "select nomSec from SECCIONES inner join MESAS on idSec = seccion where idMes = " + idMesa;
    	String resultado = gestorBD.consulta(consulta)[0];
    	return resultado;
    }
    
    /**
     * Obtiene todos los usuarios registrados en la base de datos
     * 
     * @return String[] con los nombres de todos los usuarios obtenidos
     */
    public String[] getCamareros(){
    	String consulta = "select nomUsu from USUARIOS";
    	String[] resultado = gestorBD.consulta(consulta);
    	return resultado;
    }
    
    /**
     * Obtiene la id de un usuario a partir de su nombre
     * 
     * @param nomUsu - String nombre del usuario
     * @return int id del usuario
     */
    public int getIdUsuario(String nomUsu){
    	String consulta = "select idUsu from USUARIOS where nomUsu = '" + nomUsu + "'";
    	String idUsu = gestorBD.consulta(consulta)[0];
    	return Integer.parseInt(idUsu);
    }
    
    /**
     * Obtiene la id de los camareros que se encuentran vinculados a un dispositivo
     * 
     * @param ip String ip del dipositivo en cuesti�n
     * @return int[] con la id de los camareros
     */
    public int[] getCamarerosEnDispositivo(String ip){
    	int[] idUsuarios = null;
    	String consulta = "select idUsu from USUARIOS inner join DISPOSITIVOS on idDisp = dispositivo where ip = '" + ip + "'";
    	String[] ids = gestorBD.consulta(consulta);
    	if(ids != null && ids.length > 0){
	    	idUsuarios = new int[ids.length];
	    	for(int usuario = 0; usuario < ids.length; usuario++){
	    		idUsuarios[usuario] = Integer.parseInt(ids[usuario]);
	    	}
    	}
    	return idUsuarios;
    }
    
    /**
     * revisar
     * 
     * Obtiene una lista de todos los pedidos de las comandas de un usuario que se
     * encuentren en estado "pedido" y "listo"
     * 
     * @param idUsu int id del usuario del que queremos saber sus pedidos pendientes
     * @return PedidoPendiente[] lista de los pedidos pendientes
     */
    public Pedido[] getPedidosPendientes(int idUsu){
    	ArrayList<Pedido> listaPedidos = new ArrayList<>();
    	Pedido[] pedidos = null;
    	/* Obtenemos los datos */
    	String consulta = "select idPed, menu, comanda, estado from PEDIDOS inner join COMANDAS on comanda = idCom where cerrada = 0 and estado in ('listo', 'pedido', 'servido') and usuario = " + idUsu;
    	String[] datos = gestorBD.consulta(consulta, 4);
    	/* Si obtenemos datos creamos una lista de pedidos con cada tupla */
    	if(datos.length > 0){
    		pedidos = new Pedido[datos.length/4];
	    	int numPedidos = 0;
	    	for(int i = 0; i < datos.length; i+=4){
	    		pedidos[numPedidos] = new Pedido(Integer.parseInt(datos[i]), Integer.parseInt(datos[i+1]), Integer.parseInt(datos[i+2]), datos[i+3]);
	    		numPedidos++;
	    	}
	    	/* Mejoramos la lista contando los pedidos y uniendo los que se corresponden */
	    	for(int i = 0; i < pedidos.length; i++){
	    		int unidades = 0;
	    		int listos = 0;
	    		int servidos = 0;
	    		Pedido pedido = pedidos[i];
	    		boolean existe = false;
	    		for(int j = 0; j < pedidos.length; j++){
		    		if(pedido.getComanda() == pedidos[j].getComanda() && pedido.getIdMenu() == pedidos[j].getIdMenu()){
		    			if(pedidos[j].getEstado().equals("listo")){
		    				listos++;
		    			} else if(pedidos[j].getEstado().equals("servido")){
		    				servidos++;
		    			}
		    			unidades++;
		    		}
	    		}
	    		int pos = 0;
	    		if(listaPedidos.size() > 0){
	    			for(int k = 0; k < listaPedidos.size(); k++){
	    				if(listaPedidos.get(k).getIdMenu() == pedido.getIdMenu() && listaPedidos.get(k).getComanda() == pedido.getComanda()){
	    					existe = true;
	    					pos = k;
	    					break;
	    				}
	    			}
	    		}
	    		if(!existe)
	    			listaPedidos.add(new Pedido(pedido.getIdPed(), pedido.getIdMenu(), pedido.getComanda(), pedido.getEstado(), unidades, listos, servidos));
	    	}
    	}
    	return listaPedidos.toArray(new Pedido[0]);
    }
    
    /**
     * Obtiene si un dispositivo est� conectado o no a partir de su id
     * 
     * @param idDisp int id del dispositivo
     * @return int 1 = conectado, 0 = desconectado
     */
    public int getConectado(int idDisp){
    	String consulta = "select conectado from DISPOSITIVOS where idDisp = " + idDisp;
    	String conectado = gestorBD.consulta(consulta)[0];
    	return Integer.parseInt(conectado);
    }
    
    /**
     * Obtiene si un dispositivo est� conectado o no a partir de su ip
     * 
     * @param ip String ip del dispositivo
     * @return int 1 = conectado, 0 = desconectado
     */
    public int getConectado(String ip){
    	String consulta = "select conectado from DISPOSITIVOS where ip = '" + ip + "'";
    	String conectado = gestorBD.consulta(consulta)[0];
    	return Integer.parseInt(conectado);
    }
    
    /**
     * Comprueba si se ha lanzado un hilo para intentar conectar con un dispositivo
     * 
     * @param ip String ip del dispositivo
     * @return boolean true si est� lanzado, false en caso contrario
     */
    public boolean isHiloLanzado(String ip){
    	boolean hiloLanzado = false;
    	String consulta = "select hilo from DISPOSITIVOS where ip = '" + ip + "'";
    	String hilo = gestorBD.consulta(consulta)[0];
    	if(hilo.equals("1"))
    		hiloLanzado = true;
    	return hiloLanzado;
    }
    
    /**
     * Obtiene el nombre de un destino a partir del dispositivo vinculado
     * 
     * @param idDisp id del dispositivo vinculado
     * @return String nombre del destino
     */
    public String getNombreDestino(int idDisp){
    	String resultado = null;
    	String consulta = "select nomDest from DESTINOS inner join DISPOSITIVOS on idDest = destino where idDisp = " + idDisp;
    	String[] resultados = gestorBD.consulta(consulta);
    	if(resultados != null && resultados.length > 0)
    		resultado = resultados[0];
    	return resultado;
    }
    
    /**
     * Obtiene una id de comanda a partir de la id de una mesa
     * 
     * @param idMesa int id de la mesa de la que se quiere obtener la comanda
     * @return id de la comanda, en caso de no haber ninguna comanda abierta y sin pagar devuelve 0
     */
    public int getIdComandaActiva(int idMesa){
    	String consulta = "select idCom from COMANDAS where mesa = " + idMesa + " and pagado = 0 and cerrada = 0";
    	String[] resultado = gestorBD.consulta(consulta);
    	if(resultado.length > 0)
    		return Integer.parseInt(resultado[0]);
    	else
    		return 0;
    }
    
    /**
     * Obtiene todas las idMenu distintas de una mesa pasada por par�metro en caso de que
     * tenga alguna comanda abierta
     * 
     * @param idMesa int id de la mesa de la que se va a extraer la informaci�n
     * @return una lista con todas las idMenu distintas que haya encontrado en esa mesa, null en caso de que no haya ninguna comanda abierta en esa mesa
     */
    public String[] getMenusPorIdMesa(int idMesa){
    	int idCom = getIdComandaActiva(idMesa);
    	if(idCom != 0){
	    	String consulta = "select distinct idMenu from MENUS inner join PEDIDOS on idMenu = menu where comanda = " + idCom;
	    	String[] resultado = gestorBD.consulta(consulta);
	    	return resultado;
    	} else
    		return null;
    }
    
    /**
     * Obtiene el precio de un men�
     * 
     * @param idMenu int id del men� del que se quiere obtener el precio
     * @return float con el valor del precio
     */
    public float getPrecio(int idMenu){
    	String consulta = "select precio from MENUS where idMenu = " + idMenu;
    	String resultado = gestorBD.consulta(consulta)[0];
    	return Float.parseFloat(resultado);
    }
    
    /**
     * Cuenta el n�mero de pedidos que no est�n cancelados
     * 
     * @param idMenu [int] id del men�
     * @param idComanda [int] id de la comanda
     * @return [int] n�mero de unidades
     */
    public int contarResultados(int idMenu, int idComanda){
    	String consulta = "select count(*) from PEDIDOS where menu = " + idMenu + " and comanda = " + idComanda + " and estado != 'cancelado'";
    	String resultado = gestorBD.consulta(consulta)[0];
    	return Integer.parseInt(resultado);
    }
}
