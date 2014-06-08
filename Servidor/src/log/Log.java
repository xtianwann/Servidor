package log;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import servidor.Servidor;

/**
 * Clase encargada de los mensajes del log del servidor
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Log {

    private String diaActual;
    private File log;
    private Servidor servidor;
    private BufferedReader lector;
    private BufferedWriter escritor;
    
    /**
     * Constructor
     * 
     * @param servidor [Servidor] instancia del servidor
     * @throws IOException excepción lanzada en caso de error al crear el fichero
     * @throws ParseException excepción lanzada en caso de haber problemas de formateo
     */
    public Log(Servidor servidor) throws IOException, ParseException {
        this.servidor = servidor;
        Locale formatoLocal = new Locale("ES");
        SimpleDateFormat formateadorLocal = new SimpleDateFormat("EEEE", formatoLocal);
        Calendar calendario = Calendar.getInstance();
        diaActual = formateadorLocal.format(calendario.getTime());
        log = new File("Log-" + diaActual + ".log");
        boolean finFichero = false;
        try {
            lector = new BufferedReader(new InputStreamReader(new FileInputStream(log.getName())));
            String fecha = lector.readLine().split(" ")[1];
            fecha = fecha.substring(1, fecha.length()).split(" ")[0];
            SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy", formatoLocal);
            Date fechaFichero = formateadorFecha.parse(fecha);
            Date fechaActual = formateadorFecha.parse(formateadorFecha.format(new Date()));
            if (fechaActual.after(fechaFichero)) {
                lector.close();
                log.delete();
                log.createNewFile();
                lector = new BufferedReader(new InputStreamReader(new FileInputStream(log.getName())));
            }
            lector.close();
        } catch (FileNotFoundException ex) {
            log.createNewFile();
        }
    }

    /**
     * Permite escribir un mensaje en el log
     * 
     * @param estado [Estados] estado al que pertence el mensaje que se va a escribir
     * @param mensaje [String] mensaje que se desea escribir
     * @throws FileNotFoundException excepción lanzada cuando no se encuentra el fichero del log
     * @throws IOException excepción lanzada si ocurren problemas durante la escritura del mensaje
     */
    public synchronized void escribir(Servidor.Estados estado, String mensaje) throws FileNotFoundException, IOException {
		Calendar cal = Calendar.getInstance();
		String fecha = formatearFecha(cal.get(Calendar.DAY_OF_MONTH), 2) + "/"
				+ formatearFecha(cal.get(Calendar.MONTH) + 1, 2) + "/"
				+ formatearFecha(cal.get(Calendar.YEAR), 4) + " "
				+ formatearFecha(cal.get(Calendar.HOUR_OF_DAY), 2) + ":"
				+ formatearFecha(cal.get(Calendar.MINUTE), 2) + ":"
				+ formatearFecha(cal.get(Calendar.SECOND), 2);
		escritor = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(log.getName(), true), "UTF-8"));
		String linea = "[" + estado + "]"+" [" + fecha + "] [" + mensaje + "]";
		escritor.append(linea + "\n");
		escritor.close();
    }

    /**
     * Permite formatear un fecha
     * 
     * @param data [int]
     * @param digits [int]
     * @return [String] fecha formateada
     */
    private String formatearFecha(int data, int digits) {
        String ret = "" + data;
        while (ret.length() < digits) {
            ret = "0" + ret;
        }
        return ret;
    }
    
    /**
     * Permite obtener una instancia del fichero log
     * 
     * @return [File] instancia del fichero log
     */
    public File getLog() {
        return log;
    }
}

