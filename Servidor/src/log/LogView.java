package log;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cmarin
 */
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import javax.swing.JTextPane;

import servidor.Servidor;
import servidor.Servidor.Estados;

public class LogView extends javax.swing.JFrame {

    private Servidor principal;
    private BufferedReader lector;

    public LogView(Servidor servidor) {
        this.principal = servidor;
        GraphicsConfiguration graphicsConfiguration = this.getGraphicsConfiguration();
        Insets screenInsets = java.awt.Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfiguration);
        Rectangle pantalla = this.getGraphicsConfiguration().getBounds();
        initComponents();
        int posX = pantalla.width - screenInsets.right - this.getWidth() - 5;
        int posY = pantalla.height - screenInsets.bottom - this.getHeight() - 5;
        setLocation(new Point(posX, posY));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        //jTextPane1.setContentType("text/html");
        setTitle("Log de sucesos");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setBounds(new java.awt.Rectangle(0, 0, 600, 400));
        setFocusCycleRoot(false);
        setFocusable(false);
        setFocusableWindowState(false);
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(600, 400));
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        jTextPane1.setEditable(false);
        jTextPane1.setToolTipText("Log");
        jTextPane1.setFocusCycleRoot(false);
        jTextPane1.setFocusable(false);
        jScrollPane1.setViewportView(jTextPane1);

        jButton1.setText("Ocultar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Limpiar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(108, 108, 108)
                                        .addComponent(jButton1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton2)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1)
                                .addComponent(jButton2))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        limpiar();
    }//GEN-LAST:event_jButton2ActionPerformed

    public void limpiar() {
        jTextPane1.setText("");
    }

    public void leer(Servidor.Estados estadoEnum, String nombre) throws FileNotFoundException, IOException {
        limpiar();
        String estado ="["+estadoEnum+"]";
        lector = new BufferedReader(new InputStreamReader(new FileInputStream(nombre),"UTF-8"));
        boolean finFichero = false;
        try {
            while (!finFichero) {
                String linea = lector.readLine();
                String datos[] = linea.split(" ");
                String estadoLinea = datos[0].toLowerCase();
                if(estadoLinea.equals(estado) || estado.equals("[todo]")){
                    if(estadoLinea.equals("[error]")){
                        linea = linea;
                    }
                    escribirLinea(linea);
                }
            }
        } catch (NullPointerException | IOException e) {
            finFichero = true;
        }
        lector.close();
    }
    public void escribirLinea( String linea) {
        
        jTextPane1.setText( jTextPane1.getText()+linea+"\n" );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables

    public JTextPane getjTextPane1() {
        return jTextPane1;
    }

    public void setjTextPane1(JTextPane jTextPane1) {
        this.jTextPane1 = jTextPane1;
    }
}
