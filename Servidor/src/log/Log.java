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
import java.util.logging.Level;
import java.util.logging.Logger;

import servidor.Servidor;
import servidor.Servidor.Estados;

public class Log {

    private String diaActual;
    private File log;
    private Servidor servidor;
    public File getLog() {
        return log;
    }
    private BufferedReader lector;
    private BufferedWriter escritor;

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

    private String formatearFecha(int data, int digits) {
        String ret = "" + data;
        while (ret.length() < digits) {
            ret = "0" + ret;
        }
        return ret;
    }
}

