package XMLServer;

import XML.XML;
import accesoDatos.Oraculo;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLDameloTodoServer extends XML{
    
    Oraculo consultas = new Oraculo();
    
    public XMLDameloTodoServer(){
        String[] seccionesYMesas = consultas.getSeccionesYMesasTodo();
        String[] cantidadesYProductos = consultas.getCantidadesYProductosTodo();
        
        init();
        addNodo("tipo", "DameloTodo", "paquete");
        
        addNodo("secciones", null, "paquete");
        String seccion = seccionesYMesas[0];
        addNodoConAtributos("seccion", new String[]{"nomSec"}, new String[]{seccion},null, "secciones");
        for(int i = 0; i < seccionesYMesas.length; i+=3){
            if(!seccion.equals(seccionesYMesas[i])){
                seccion = seccionesYMesas[i];
                addNodoConAtributos("seccion", new String[]{"nomSec"}, new String[]{seccion},null, "secciones");
            }
            addNodoConAtributos("mesa", new String[]{"idMes"}, new String[]{seccionesYMesas[i+2]}, seccionesYMesas[i+1], "seccion");
        }
        
        addNodo("cantidades", null, "paquete");
        String cantidad = cantidadesYProductos[0];
        addNodoConAtributos("cantidad", new String[]{"nomCant"}, new String[]{cantidad},null, "cantidades");
        for(int i = 0; i < cantidadesYProductos.length; i+=3){
            if(!cantidad.equals(cantidadesYProductos[i])){
                cantidad = cantidadesYProductos[i];
                addNodoConAtributos("cantidad", new String[]{"nomCant"}, new String[]{cantidad},null, "cantidades");
            }
            addNodoConAtributos("producto", new String[]{"idMenu"}, new String[]{cantidadesYProductos[i+2]}, cantidadesYProductos[i+1], "cantidad");
        }
    }

}
