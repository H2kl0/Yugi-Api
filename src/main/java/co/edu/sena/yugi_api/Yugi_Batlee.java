/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package co.edu.sena.yugi_api;

import java.awt.Image;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author LUIS FERNANDO
 */
public class Yugi_Batlee extends javax.swing.JFrame {

private int vidaJugador = 3000;
    private int vidaRival = 3000;
    private JSONObject[] cartasJugador = new JSONObject[3];
    private JSONObject[] cartasRival = new JSONObject[3];
    private int turnoActual = 0;
    private boolean puedeAtacar = true;
    private int cartaSeleccionada = -1;
    private boolean[] cartasUsadas = new boolean[3];

    private javax.swing.JLabel[] cartaLabels;

    public Yugi_Batlee() {
    initComponents();
    cartaLabels = new javax.swing.JLabel[] { jLabel1, jLabel2, jLabel3 };
    iniciarJuego();
    actualizarVida();

        // Seleccionar carta con clic
        JLabel[] labels = { jLabel1, jLabel2, jLabel3 };
        for (int i = 0; i < labels.length; i++) {
            final int index = i;
            labels[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    cartaSeleccionada = index;
                    jTextAreaLog.append("\nSeleccionaste la carta: " + cartasJugador[index].getString("name") + "\n");
                }
            });
        }
    }

    private void iniciarJuego() {
        obtenerCartasAleatorias();
        jTextAreaLog.setText("Que comience el duelo!\n");
    }
        private void mostrarDetallesCartasJugador() {
    jTextAreaLog.append("\nTus cartas:\n");
    for (int i = 0; i < cartasJugador.length; i++) {
        String nombre = cartasJugador[i].getString("name");
        int atk = cartasJugador[i].optInt("atk", 0);
        int def = cartasJugador[i].optInt("def", 0);
        jTextAreaLog.append("- " + nombre + " (ATK: " + atk + ", DEF: " + def + ")\n");
    }
}

    private void obtenerCartasAleatorias() {
        String url = "https://db.ygoprodeck.com/api/v7/cardinfo.php?num=6&offset=" + (int)(Math.random() * 1000);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                JSONObject obj = new JSONObject(response.body());
                JSONArray data = obj.getJSONArray("data");

                for(int i = 0; i < 3; i++) {
                    cartasJugador[i] = data.getJSONObject((int)(Math.random() * data.length()));
                    cartasRival[i] = data.getJSONObject((int)(Math.random() * data.length()));
                }

               mostrarCartas();
               mostrarDetallesCartasJugador();
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar cartas: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void mostrarCartas() {
    for (int i = 0; i < 3; i++) {
        try {
            String urlImagen = cartasJugador[i].getJSONArray("card_images").getJSONObject(0).getString("image_url");
            java.net.URL url = new java.net.URL(urlImagen);
            javax.swing.ImageIcon icono = new javax.swing.ImageIcon(new javax.swing.ImageIcon(url).getImage().getScaledInstance(100, 150, java.awt.Image.SCALE_SMOOTH));
            cartaLabels[i].setIcon(icono);
            cartaLabels[i].setEnabled(true); // Habilita por si reinicias
            final int index = i;
            cartaLabels[i].setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            cartaLabels[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (!cartasUsadas[index]) {
                        cartaSeleccionada = index;
                        jTextAreaLog.append("Seleccionaste: " + cartasJugador[index].getString("name") + "\n");
                    } else {
                        JOptionPane.showMessageDialog(null, "Esta carta ya fue usada");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    private void marcarCartaComoUsada(int index) {
    cartasUsadas[index] = true;
    cartaLabels[index].setEnabled(false);
    cartaLabels[index].setIcon(new javax.swing.ImageIcon(
        new javax.swing.ImageIcon(getClass().getResource("/Imagen/card_disabled.png")).getImage().getScaledInstance(100, 150, java.awt.Image.SCALE_SMOOTH)
    ));
}

    private void actualizarVida() {
        jLabelVidaJugador.setText("Tu Vida: " + vidaJugador);
        jLabelVidaRival.setText("Vida Rival: " + vidaRival);

        if(vidaJugador <= 0 || vidaRival <= 0 || turnoActual >= 3) {
            finDelJuego();
        }
    }

    private void finDelJuego() {
        puedeAtacar = false;
        String resultado;

        if(vidaJugador <= 0) {
            resultado = "Has perdido el duelo!";
        } else if(vidaRival <= 0) {
            resultado = "Has ganado el duelo!";
        } else {
            resultado = "Empate!";
        }

        jTextAreaLog.append("\n" + resultado + "\n");
        JOptionPane.showMessageDialog(this, resultado);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabelReload = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabelATK = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelDF = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaLog = new javax.swing.JTextArea();
        jLabelVidaJugador = new javax.swing.JLabel();
        jLabelVidaRival = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabelBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(51, 0, 51));
        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
        });
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelReload.setFont(new java.awt.Font("Roboto ExtraBold", 1, 14)); // NOI18N
        jLabelReload.setForeground(new java.awt.Color(255, 255, 255));
        jLabelReload.setText("RR");
        jPanel3.add(jLabelReload, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 230, 100, 20));

        jPanel2.setBackground(new java.awt.Color(51, 0, 51));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelATK.setFont(new java.awt.Font("Roboto ExtraBold", 1, 14)); // NOI18N
        jLabelATK.setForeground(new java.awt.Color(255, 255, 255));
        jLabelATK.setText("¡ATACAR!");
        jPanel2.add(jLabelATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 230, 100, 20));

        jPanel1.setBackground(new java.awt.Color(51, 0, 51));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelDF.setFont(new java.awt.Font("Roboto ExtraBold", 1, 14)); // NOI18N
        jLabelDF.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDF.setText("¡DEFENDER!");
        jPanel1.add(jLabelDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 100, 20));

        jTextAreaLog.setColumns(20);
        jTextAreaLog.setFont(new java.awt.Font("Roboto Condensed", 0, 12)); // NOI18N
        jTextAreaLog.setRows(5);
        jScrollPane1.setViewportView(jTextAreaLog);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 430, 140));

        jLabelVidaJugador.setFont(new java.awt.Font("Roboto Black", 0, 12)); // NOI18N
        jLabelVidaJugador.setForeground(new java.awt.Color(255, 255, 255));
        jLabelVidaJugador.setText("Vida");
        getContentPane().add(jLabelVidaJugador, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        jLabelVidaRival.setFont(new java.awt.Font("Roboto Black", 0, 12)); // NOI18N
        jLabelVidaRival.setForeground(new java.awt.Color(255, 255, 255));
        jLabelVidaRival.setText("Vida Rival");
        getContentPane().add(jLabelVidaRival, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 150, 200));
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 170, 190));
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 160, 200));

        jLabel4.setFont(new java.awt.Font("Roboto Black", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Tus cartas");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jButton2.setBackground(new java.awt.Color(51, 0, 51));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen/94510.png"))); // NOI18N
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 394, 90, 40));

        jLabelBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen/waos.png"))); // NOI18N
        getContentPane().add(jLabelBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 4, -1, 430));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
if(!puedeAtacar || cartaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una carta primero");
            return;
        }
        JSONObject cartaJugador = cartasJugador[cartaSeleccionada];
        JSONObject cartaRival = cartasRival[(int)(Math.random() * 3)];

        int defJugador = cartaJugador.optInt("def", 0);
        int atkRival = cartaRival.optInt("atk", 0);
        int danio = Math.max(0, atkRival - defJugador);
        vidaJugador -= danio;

        jTextAreaLog.append("\nTurno " + (turnoActual+1) + ": Defendiste con " + cartaJugador.getString("name") + " (DEF: " + defJugador + ")\n");
        jTextAreaLog.append("Rival atacó con " + cartaRival.getString("name") + " (ATK: " + atkRival + ")\n");
        jTextAreaLog.append("Recibiste " + danio + " puntos de daño\n");

        turnoActual++;
        cartaSeleccionada = -1;
        actualizarVida();
        marcarCartaComoUsada(cartaSeleccionada);
                                     

    }//GEN-LAST:event_jPanel1MouseClicked

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
    if(!puedeAtacar || cartaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una carta primero");
            return;
        }
        JSONObject cartaJugador = cartasJugador[cartaSeleccionada];
        JSONObject cartaRival = cartasRival[(int)(Math.random() * 3)];

        int atkJugador = cartaJugador.optInt("atk", 0);
        int defRival = cartaRival.optInt("def", 0);
        int danio = Math.max(0, atkJugador - defRival);
        vidaRival -= danio;

        jTextAreaLog.append("\nTurno " + (turnoActual+1) + ": Atacaste con " + cartaJugador.getString("name") + " (ATK: " + atkJugador + ")\n");
        jTextAreaLog.append("Rival defendió con " + cartaRival.getString("name") + " (DEF: " + defRival + ")\n");
        jTextAreaLog.append("Infligiste " + danio + " puntos de daño\n");

        turnoActual++;
        cartaSeleccionada = -1;
        actualizarVida();
        marcarCartaComoUsada(cartaSeleccionada);
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked
        vidaJugador = 3000;
        vidaRival = 3000;
        turnoActual = 0;
        puedeAtacar = true;
        cartaSeleccionada = -1;

        jTextAreaLog.setText("\u00a1Nuevo duelo comenzado!\n");
        obtenerCartasAleatorias();
        actualizarVida();
        cartasUsadas = new boolean[3];
        for (int i = 0; i < 3; i++) {
        cartaLabels[i].setEnabled(true);
}
    }//GEN-LAST:event_jPanel3MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Yugi_Batlee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Yugi_Batlee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Yugi_Batlee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Yugi_Batlee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Yugi_Batlee().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelATK;
    private javax.swing.JLabel jLabelBackground;
    private javax.swing.JLabel jLabelDF;
    private javax.swing.JLabel jLabelReload;
    private javax.swing.JLabel jLabelVidaJugador;
    private javax.swing.JLabel jLabelVidaRival;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaLog;
    // End of variables declaration//GEN-END:variables
}
