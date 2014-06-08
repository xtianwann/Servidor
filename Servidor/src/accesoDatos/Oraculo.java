package accesoDatos;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase que agrupa funciones condistintos tipos de consultas necesarias
 * para el servidor en distintas tareas.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Oraculo {
    
    private GestorBD gestorBD = new GestorBD();
    
    /**
     * Obtiene todos los nombres de sección, nombres de mesa y la id de dicha mesa
     * ordenados por sección y luego por mesa en orden alfabético
     *
     * @return [String[]] resultados de la consulta
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
     * @return [String[]] resultados de la consulta
     */
    public String[] getCantidadesYProductosTodo(){
        String consulta = "select nomCant, nomProd, idMenu from CANTIDADES inner join MENUS on cantidad = idCant inner join PRODUCTOS on idProd = producto order by nomCant, nomProd asc";
        String[] resultado = gestorBD.consulta(consulta, 3);
        return resultado;
    }
    
    /**
     * Obtiene el nombre de un producto a partir de una id de Menu
     *
     * @param idMenu [int] id del menú por el que se quiere filtrar la consulta
     * @return [String] resultado de la consulta
     */
    public String getNombreProductoPorIdMenu(int idMenu){
        String consulta = "select nomProd from PRODUCTOS inner join MENUS on idProd = producto where idMenu = " + idMenu;
        String resultado = gestorBD.consulta(consulta)[0];
        return resultado;
    }
    
    /**
     * Obtiene el nombre de una cantidad a partir de una id de Menu
     *
     * @param idMenu [int] id del menú por el que se quiere filtrar la consulta
     * @return [String] resultado de la consulta
     */
    public String getNombreCantidadPorIdMenu(int idMenu){
        String consulta = "select nomCant from CANTIDADES inner join MENUS on idCant = cantidad where idMenu = " + idMenu;
        String resultado = gestorBD.consulta(consulta)[0];
        return resultado;
    }
    
    /**
     * Obtiene el id de un destino a partir de una id de Menu
     *
     * @param idMenu [int] id del menú por el que se quiere filtrar la consulta
     * @return [String] resultado de la consulta
     */
    public String getIdDestinoPorIdMenu(int idMenu){
        String consulta = "select destino from MENUS where idMenu = " + idMenu;
        String resultado = gestorBD.consulta(consulta)[0];
        return resultado;
    }
    
    /**
     * Obtiene el nombre de un destino a partir de una id de Menu
     *
     * @param idMenu [int] id del menú por el que se quiere filtrar la consulta
     * @return [String] resultado de la consulta
     */
    public String getNombreDestinoPorIdMenu(int idMenu){
        String consulta = "select nomDest from DESTINOS inner join MENUS on idDest = destino where idMenu = " + idMenu;
        String resultado = gestorBD.consulta(consulta)[0];
        return resultado;
    }
    
    /**
     * Obtiene el nombre de una mesa a partir de su id
     *
     * @param idMes [int] id de la mesa de la que se quiere obtener el nombre
     * @return [String] resultado de la consulta
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
     * @param idMes [int] id de la mesa de la que se quiere obtener su sección
     * @return [String] resultado de la consulta
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
     * @param idMes [int] id de la mesa de la que se quiere saber si está activa
     * @return [boolean] true si la mesa está sin pagar y sin cerrar
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
     * @param idMesa [int] id de la mesa
     * @return [int] id de la comanda. 0 en caso de que no haya ninguna comanda sin pagar
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
     * Obtiene la id del usuario que cogió la comanda
     * 
     * @param idComanda [int] id de la comanda
     * @return [int] id del usuario
     */
    public int getIdUsuPorIdComanda(int idComanda){
    	String consulta = "select usuario from COMANDAS where idCom = " + idComanda;
    	String[] resultado = gestorBD.consulta(consulta);
    	int idUsu = (resultado.length > 0) ? Integer.parseInt(resultado[0]) : 0;
    	return idUsu;
    }
    
    /**
     * Devuelve fecha y hora actual en el siguiente formato YYYY-MM-DD hh:mm
     *
     * @return [String] fecha y hora actual (YYYY-MM-DD hh:mm)
     */
    public String getFechaYHoraActual(){
        String resultado = gestorBD.consulta("select datetime('now')")[0];
        resultado = resultado.substring(0, resultado.length()-3);
        return resultado;
    }
    
    /**
     * Obtiene la id de un dispositivo a partir de su ip
     *
     * @param ip [String] ip del dispositivo 
     * @return [int] id del dispositivo
     */
    public int getIdDispositivoPorIp(String ip){
        String consulta = "select idDisp from DISPOSITIVOS where ip = '" + ip + "'";
        String resultado = gestorBD.consulta(consulta)[0];
        return Integer.parseInt(resultado);
    }
    
    /**
     * Genera una lista con los pedidos de una mesa
     *
     * @param idMesa [int] id de la mesa de la que queremos obtener los pedidos
     * @return [Pedido[]] lista de pedidos de la mesa pasada por parámetro
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
     * @param idSec [int] id de la sección
     * @return [String] nombre de la sección
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
     * @param idMenu [int] id del producto por el que se quiere filtrar
     * @param idComanda [int] id de la comanda por la que se quiere filtrar
     * @param estado [String] estado por el que se quiere filtrar
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
     * @param idComanda [int] id de la comanda de la que se quiere conocer el camarero que la creó
     * @return [String] ip del dispositivo del camarero
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
     * @param idUsu [int] id de usuario que queremos saber su dispositivo
     * @return [String] ip del dispositivo que esté usando, null si no está usando ninguno
     */
    public String getIpPorIdUsuario(int idUsu){
    	String consulta = "select ip from DISPOSITIVOS inner join USUARIOS on idDisp = dispositivo where idUsu = " + idUsu;
    	String[] resultado = gestorBD.consulta(consulta);
    	if(resultado.length > 0)
    		return resultado[0];
    	else
    		return null;
    }
    
    /**
     * Obtiene un objeto usuario a partir de una id de usuario
     * 
     * @param idUsu [int] id del usuario del que vamos a crear el objeto
     * @return [Usuario] objeto con toda la información necesaria
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
     * @param socket [Socket] socket por el que el usuario estableció conexión
     * @return [Usuario] objeto con toda la información necesaria
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
     * @param ip [String] ip del dispositivo
     * @return [Usuario] objeto con toda la información necesaria
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
     * Devuelve un objeto dispositivo del tipo destino a partir de una id de menú
     * 
     * @param idMenu [int] id de menú
     * @return [Dispositivo] dispositivo de tipo destino
     */
    public Dispositivo getDispositivoPorIdMenu(int idMenu){
    	String consulta = "select idDisp, conectado, ip, nomDest from DISPOSITIVOS d inner join DESTINOS on d.destino = idDest inner join MENUS m on m.destino = idDest where idMenu = " + idMenu;
    	String[] resultado = gestorBD.consulta(consulta,4);
    	return new Dispositivo(Integer.parseInt(resultado[0]), Integer.parseInt(resultado[1]), resultado[2], resultado[3]);
    }
    
    /**
     * Obtiene la id de todas las comandas activas
     * 
     * @return [int[ ]] lista de id de comandas activas
     */
    public int[] getIdComandasActivas(){
    	String consulta = "select idCom from COMANDAS where pagado = 0 and cerrada = 0";
    	String[] resultados = gestorBD.consulta(consulta);
    	int[] idComandas = new int[resultados.length];
    	for(int comanda = 0; comanda < resultados.length; comanda++){
    		idComandas[comanda] = Integer.parseInt(resultados[comanda]);
    	}
    	return idComandas;
    }
    
    /**
     * Devuelve una lista de pedidos pendientes de un dispositivo tipo destino (cocina o barra)
     * 
     * @param dispositivo [Dispositivo] del que se quiere obtener la lista
     * @return PedidoPendiente[] lista de todos los pedidos pendientes de ese dispositivo
     */
    public PedidoPendiente[] getPedidosPendientes(Dispositivo dispositivo){
    	ArrayList<PedidoPendiente> pendientes = new ArrayList<>();
    	int[] comandasActivas = getIdComandasActivas();
    	/* Obtenemos todos los pedidos pendientes de las comandas activas */
    	PedidoPendiente[] pedidosPendientes = getPedidos(comandasActivas);
    	/* Comprobamos que el dispositivo pasado es del tipo destino, en tal caso obtenemos su id */
    	String consulta = "select destino from DISPOSITIVOS where idDisp = " + dispositivo.getIdDisp();
    	String[] destino = gestorBD.consulta(consulta);
    	String idDest = (destino != null && destino.length > 0) ? destino[0] : null;
    	if(idDest != null){
    		/* Una vez obtenemos la id nos quedamos con los pedidos que le corresponden a ese dispositivo */
	    	for(PedidoPendiente pedido : pedidosPendientes){
	    		if(getIdDestinoPorIdMenu(pedido.getIdMenu()).equals(idDest)){
	    			pendientes.add(pedido);
	    		}
	    	}
    	}
    	
    	return pendientes.toArray(new PedidoPendiente[0]);
    }
    
    /**
     * Devuelve todos los pedidos de una lista de comandas activas.
     * 
     * @param idMenus [int[ ]] lista de id de menú
     * @return [Pedido[ ]] lista de pedidos que coinciden con los pasados por parámetro
     */
    public PedidoPendiente[] getPedidos(int[] idComandas){
    	ArrayList<PedidoPendiente> pedidosPendientes = new ArrayList<>();
    	String consulta = "";
    	for(int idCom : idComandas){
    		consulta = "select distinct menu from PEDIDOS where comanda = " + idCom;
    		String[] menus = gestorBD.consulta(consulta);
    		if(menus.length > 0){
    			for(String menu : menus){
    				consulta = "select count(*) from pedidos where menu = " + menu + " and estado != 'cancelado' and comanda = " + idCom;
    				int unidades = Integer.parseInt(gestorBD.consulta(consulta)[0]);
    				consulta = "select count(*) from pedidos where menu = " + menu + " and estado = 'pedido' and comanda = " + idCom;
    				int udPedidas = Integer.parseInt(gestorBD.consulta(consulta)[0]);
    				consulta = "select count(*) from pedidos where menu = " + menu + " and estado = 'servido' and comanda = " + idCom;
    				int udServidas = Integer.parseInt(gestorBD.consulta(consulta)[0]);
    				consulta = "select count(*) from pedidos where menu = " + menu + " and estado = 'listo' and comanda = " + idCom;
    				int udListas = Integer.parseInt(gestorBD.consulta(consulta)[0]);
    				
    				pedidosPendientes.add(new PedidoPendiente(idCom, new Pedido(Integer.parseInt(menu)), unidades, udPedidas, udListas+udServidas, udServidas));
    			}
    		}
    	}
    	return pedidosPendientes.toArray(new PedidoPendiente[0]);
    }
    
    /**
     * Obtiene el nombre de una mesa a partir de una comanda
     * 
     * @param idComanda [int] comanda que pertenece a la mesa de la que queremos obtener el nombre
     * @return [String] nombre de la mesa
     */
    public String getNombreMesa(int idComanda){
    	String consulta = "select nomMes from MESAS inner join COMANDAS on mesa = idMes where idCom = " + idComanda;
    	String resultado = gestorBD.consulta(consulta)[0];
    	return resultado;
    }
    
    /**
     * Obtiene el nombre de una sección a partir de una comanda
     * 
     * @param idComanda [int] comanda que pertenece a una mesa y sección concreta
     * @return [String] nombre de la sección
     */
    public String getNombreSeccion(int idComanda){
    	String consulta = "select nomSec from SECCIONES inner join MESAS on idSec = seccion inner join COMANDAS on mesa = idMes where idCom = " + idComanda;
    	String resultado = gestorBD.consulta(consulta)[0];
    	return resultado;
    }
    
    /**
     * Obtiene el nombre de una sección a partir de la id de una mesa
     * 
     * @param idMesa [int] id de la mesa
     * @return [String] nombre de la sección
     */
    public String getNombreSeccionPorIdMesa(int idMesa){
    	String consulta = "select nomSec from SECCIONES inner join MESAS on idSec = seccion where idMes = " + idMesa;
    	String resultado = gestorBD.consulta(consulta)[0];
    	return resultado;
    }
    
    /**
     * Obtiene todos los usuarios registrados en la base de datos
     * 
     * @return [String[]] lista con los nombres de todos los usuarios obtenidos
     */
    public String[] getCamareros(){
    	String consulta = "select nomUsu from USUARIOS";
    	String[] resultado = gestorBD.consulta(consulta);
    	return resultado;
    }
    
    /**
     * Obtiene la id de un usuario a partir de su nombre
     * 
     * @param nomUsu [String] nombre del usuario
     * @return [int] id del usuario
     */
    public int getIdUsuario(String nomUsu){
    	String consulta = "select idUsu from USUARIOS where nomUsu = '" + nomUsu + "'";
    	String idUsu = gestorBD.consulta(consulta)[0];
    	return Integer.parseInt(idUsu);
    }
    
    /**
     * Obtiene la id de los camareros que se encuentran vinculados a un dispositivo
     * 
     * @param ip [String] ip del dipositivo en cuestión
     * @return [int[]] lista con la id de los camareros
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
     * Obtiene una lista de todos los pedidos de las comandas de un usuario que se
     * encuentren en estado "pedido" y "listo"
     * 
     * @param idUsu [int] id del usuario del que queremos saber sus pedidos pendientes
     * @return [PedidoPendiente[]] lista de los pedidos pendientes
     */
    public Pedido[] getPedidosPendientes(int idUsu){
    	ArrayList<Pedido> listaPedidos = new ArrayList<>();
    	Pedido[] pedidos = null;
    	/* Obtenemos los datos */
    	String consulta = "select idPed, menu, comanda, estado from PEDIDOS inner join COMANDAS on comanda = idCom where pagado = 0 and cerrada = 0 and estado in ('listo', 'pedido', 'servido') and usuario = " + idUsu;
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
     * Obtiene si un dispositivo está conectado o no a partir de su id
     * 
     * @param idDisp [int] id del dispositivo
     * @return [int] 1 = conectado, 0 = desconectado
     */
    public int getConectado(int idDisp){
    	String consulta = "select conectado from DISPOSITIVOS where idDisp = " + idDisp;
    	String conectado = gestorBD.consulta(consulta)[0];
    	return Integer.parseInt(conectado);
    }
    
    /**
     * Obtiene si un dispositivo está conectado o no a partir de su ip
     * 
     * @param ip [String] ip del dispositivo
     * @return [int] 1 = conectado, 0 = desconectado
     */
    public int getConectado(String ip){
    	String consulta = "select conectado from DISPOSITIVOS where ip = '" + ip + "'";
    	String conectado = gestorBD.consulta(consulta)[0];
    	return Integer.parseInt(conectado);
    }
    
    /**
     * Comprueba si se ha lanzado un hilo para intentar conectar con un dispositivo
     * 
     * @param ip [String] ip del dispositivo
     * @return [boolean] true si está lanzado, false en caso contrario
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
     * @param idDisp [int] id del dispositivo vinculado
     * @return [String] nombre del destino
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
     * @param idMesa [int] id de la mesa de la que se quiere obtener la comanda
     * @return [int] id de la comanda, en caso de no haber ninguna comanda abierta y sin pagar devuelve 0
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
     * Obtiene todas las idMenu distintas de una mesa pasada por parámetro en caso de que
     * tenga alguna comanda abierta
     * 
     * @param idMesa [int] id de la mesa de la que se va a extraer la información
     * @return [String] lista con todas las idMenu distintas que haya encontrado en esa mesa, null en caso de que no haya ninguna comanda abierta en esa mesa
     */
    public String[] getMenusPorIdMesa(int idMesa){
    	int idCom = getIdComandaActiva(idMesa);
    	if(idCom != 0){
	    	String consulta = "select distinct idMenu from MENUS inner join PEDIDOS on idMenu = menu where comanda = " + idCom + " and estado != 'cancelado'";
	    	String[] resultado = gestorBD.consulta(consulta);
	    	return resultado;
    	} else
    		return null;
    }
    
    /**
     * Obtiene el precio de un menú
     * 
     * @param idMenu [int] id del menú del que se quiere obtener el precio
     * @return [float] valor del precio
     */
    public float getPrecio(int idMenu){
    	String consulta = "select precio from MENUS where idMenu = " + idMenu;
    	String resultado = gestorBD.consulta(consulta)[0];
    	return Float.parseFloat(resultado);
    }
    
    /**
     * Cuenta el número de pedidos que no están cancelados
     * 
     * @param idMenu [int] id del menú
     * @param idComanda [int] id de la comanda
     * @return [int] número de unidades
     */
    public int contarResultados(int idMenu, int idComanda){
    	String consulta = "select count(*) from PEDIDOS where menu = " + idMenu + " and comanda = " + idComanda + " and estado != 'cancelado'";
    	String resultado = gestorBD.consulta(consulta)[0];
    	return Integer.parseInt(resultado);
    }
    
    /**
     * Cuenta los pedidos de una comanda que no están servidos ni cancelados
     * 
     * @param idComanda [int] id de la comanda
     * @return [int] número de pedidos sin servir y sin cancelar
     */
    public int contarPedidosYListos(int idComanda){
    	String consulta = "select count(*) from PEDIDOS where comanda = " + idComanda + " and estado not in('servido', 'cancelado')";
    	String resultado = gestorBD.consulta(consulta)[0];
    	return Integer.parseInt(resultado);
    }
    
    /**
     * Permite saber si todos los pedidos de un menú en concreto están servidos
     * 
     * @param idComanda [int] id de la comanda
     * @param idMenu [int] id del menú
     * @return [boolean] true si todos los pedidos están servidos, false en caso contrario
     */
    public boolean getFinalizado(int idComanda, int idMenu){
    	String consulta = "select count(*) from PEDIDOS where comanda = " + idComanda + " and menu = " + idMenu + " and estado != 'cancelado'";
    	int totalPedidos = Integer.parseInt(gestorBD.consulta(consulta)[0]);
    	consulta = "select count(*) from PEDIDOS where comanda = " + idComanda + " and menu = " + idMenu + " and estado = 'servido'";
    	int totalServidos = Integer.parseInt(gestorBD.consulta(consulta)[0]);
    	return totalPedidos == totalServidos;
    }
    
    /**
     * Obtiene la ip de todos los destinos que consten en la base de datos
     * 
     * @return [String[ ]] ip de todos los dispositivos que actúan como destino
     */
    public String[] getIpDestinos(){
    	String consulta = "select ip from DISPOSITIVOS where destino is not null";
    	String[] resultado = gestorBD.consulta(consulta);
    	return resultado;
    }
}
