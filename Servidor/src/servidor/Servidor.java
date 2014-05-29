package servidor;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import log.Log;
import log.LogView;

/**
 * Arranca el servidor
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Servidor {

	private HiloPrincipal hiloPrincipal;
	public static LogView logView;
	private CheckboxMenuItem cb3;
	public static Log log;
	private MenuItem pantallaLog;
	private CheckboxMenuItem checkboxTodo;
	private CheckboxMenuItem checkboxInfo;
	private CheckboxMenuItem checkboxError;
	private Estados estado;
	private static TrayIcon icono;

	public Servidor() {
		logView = new LogView(this);
		try {
			log = new Log(this);
		} catch (IOException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		escribirLog(Estados.info, "Servidor iniciado");
		
		// UIManager.setLookAndFeel(
		// "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			escribirLog(Estados.error,
					"No se puedo cargar la interfaz del servidor");

		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		// Schedule a job for the event-dispatching thread:
		// adding TrayIcon.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
		try {
			hiloPrincipal = new HiloPrincipal(27000);
			hiloPrincipal.start();
		} catch (IOException ex) {
			escribirLog(Estados.error, "No se pudo iniciar el servidor");
		}
	}

	public static void main(String[] args) {
		new Servidor();
		
	}

	/* Hilo principal del servidor */
	public class HiloPrincipal extends Thread {

		private ServerSocket socketServidor;
		private Dispatcher dispatcher;
		private boolean parado;

		/**
		 * Constructor de HiloPrincipal
		 * 
		 * @param puerto
		 *            puerto de escucha
		 * @throws IOException
		 *             en caso de no poder crear el ServerSocket en el puerto
		 *             pasado por parámetro
		 */
		public HiloPrincipal(int puerto) throws IOException {
			this.socketServidor = new ServerSocket(puerto);
			this.parado = true;
		}

		public void run() {
			Socket cliente;
			this.parado = false;
			dispatcher = new Dispatcher(this);
			dispatcher.start();

			while (!parado) {
				try {
					cliente = this.socketServidor.accept();
					dispatcher.addSocket(cliente);
				} catch (IOException ex) {

				}
			}
		}

		/* Para la ejecuciÃ³n del hilo */
		public void parar() {
			parado = true;
			dispatcher.setParado(true);
			try {
				socketServidor.close();
			} catch (IOException ex) {
				escribirLog(Estados.error, "No se pudo parar el servidor");
			}
		}

	}

	public void clearCB3() {
		cb3.setState(false);
	}

	public void limpiarCheckboxEstados() {
		checkboxTodo.setState(false);
		checkboxError.setState(false);
		checkboxInfo.setState(false);
	}

	public static enum Estados {

		todo, info, error
	}

	private void createAndShowGUI() {
		// Check the SystemTray support
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray no soportado");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		icono = new TrayIcon(createImage("images/bulb.gif", "tray icon"));
		
		final SystemTray tray = SystemTray.getSystemTray();
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			escribirLog(Estados.error, "No se pudo obtener la ip del servidor");
		}
		// Create a popup menu components
		MenuItem servidorInfo = new MenuItem("Servidor iniciado en: "
				+ ip.getHostAddress());
		pantallaLog = new MenuItem("Ver log de sucesos");
		MenuItem exitItem = new MenuItem("Salir");
		Menu displayMenuFiltro = new Menu("Filtro");
		checkboxTodo = new CheckboxMenuItem("Todo");
		checkboxInfo = new CheckboxMenuItem("Info");
		checkboxError = new CheckboxMenuItem("Error");

		// Add components to popup menu
		popup.add(servidorInfo);
		popup.addSeparator();
		popup.add(pantallaLog);
		popup.add(displayMenuFiltro);
		popup.addSeparator();

		displayMenuFiltro.add(checkboxTodo);
		displayMenuFiltro.add(checkboxInfo);
		displayMenuFiltro.add(checkboxError);
		popup.add(exitItem);
		icono.setPopupMenu(popup);
		checkboxTodo.setState(true);
		estado = Estados.todo;

		try {
			tray.add(icono);
		} catch (AWTException e) {
			try {
				log.escribir(Estados.error,
						"No se pudo añadir el icono del servidor");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
		pantallaLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					logView.leer(estado, log.getLog().getName());
				} catch (IOException ex) {
					icono.displayMessage("Servidor", "No se pudo leer el log",
							TrayIcon.MessageType.ERROR);
				}
				logView.setVisible(true);
			}
		});
		checkboxTodo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				limpiarCheckboxEstados();
				checkboxTodo.setState(true);
				estado = Estados.todo;
				if (logView.getjTextPane1().isVisible()) {
					try {
						logView.leer(estado, log.getLog().getName());
					} catch (IOException ex) {
						icono.displayMessage("Servidor",
								"No se pudo leer el log",
								TrayIcon.MessageType.ERROR);
					}
				}
			}
		});
		checkboxInfo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				limpiarCheckboxEstados();
				checkboxInfo.setState(true);
				estado = Estados.info;
				if (logView.getjTextPane1().isVisible()) {
					try {
						logView.leer(estado, log.getLog().getName());
					} catch (IOException ex) {
						icono.displayMessage("Servidor",
								"No se pudo leer el log",
								TrayIcon.MessageType.ERROR);
					}
				}
			}
		});
		checkboxError.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				limpiarCheckboxEstados();
				checkboxError.setState(true);
				estado = Estados.error;
				if (logView.getjTextPane1().isVisible()) {
					try {
						logView.leer(estado, log.getLog().getName());
					} catch (IOException ex) {
						icono.displayMessage("Servidor",
								"No se pudo leer el log",
								TrayIcon.MessageType.ERROR);
					}
				}
			}
		});

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(icono);
				hiloPrincipal.parar();
				System.exit(0);
			}
		});
	}

	// Obtain the image URL
	protected Image createImage(String path, String description) {
		URL imageURL = Servidor.class.getResource(path);

		if (imageURL == null) {
			// log.addMsg("No encuentro imagen para el tray: " + path);
			return null;
		} else {
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}

	public Estados getEstado() {
		return estado;
	}

	public void setEstado(Estados estado) {
		this.estado = estado;
	}

	public static void escribirLog(Estados estadoMensaje, String mensaje) {
		try {
			log.escribir(estadoMensaje, mensaje);
			if (estadoMensaje == Estados.error)
				icono.displayMessage("Servidor", mensaje,
						TrayIcon.MessageType.ERROR);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

}