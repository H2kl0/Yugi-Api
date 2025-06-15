/*
 * JuegoFrame.java
 * Proyecto: Yu-Gi-Oh! Card Battle
 * Autor:  H2kl0
 */

package co.edu.sena.yugi_api;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.net.http.*;
import org.json.*;

public class JuegoFrame extends JFrame {

    // Atributos del juego
    private int vidaJugador = 3000;
    private int vidaRival = 3000;
    private JSONObject[] cartasJugador;
    private JSONObject[] cartasRival;

    // Componentes visuales
    private JLabel[] lblCartasJugador = new JLabel[3];
    private JLabel[] lblImagenesJugador = new JLabel[3];
    private JLabel[] lblImagenesRival = new JLabel[3];

    private JTextPane txtLog; // Cambiado de JTextArea a JTextPane
    private int turnoActual = 0;

    private static int partidaNumero = 1;
    private StringBuilder historial = new StringBuilder();

    private JComboBox<String> comboCartaJugador;
    private JButton btnAtacar;
    private JButton btnDefender;

    public JuegoFrame() {
        setTitle("Batalla Yu-Gi-Oh!");
        setSize(1200, 700);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Mostrar vida
        JLabel lblVida = new JLabel("Tu Vida: " + vidaJugador + " | Vida Rival: " + vidaRival);
        lblVida.setFont(new Font("Arial", Font.BOLD, 16));
        lblVida.setBounds(20, 20, 400, 30);
        add(lblVida);

        // Panel para las cartas del jugador (izquierda)
        JPanel panelCartasJugador = new JPanel();
        panelCartasJugador.setLayout(null);
        panelCartasJugador.setBounds(20, 60, 500, 400);
        panelCartasJugador.setBorder(BorderFactory.createTitledBorder("Tus Cartas"));
        add(panelCartasJugador);

        // Cartas del jugador
        for (int i = 0; i < 3; i++) {
            lblCartasJugador[i] = new JLabel("Carta " + (i + 1), SwingConstants.CENTER);
            lblCartasJugador[i].setBounds(20 + i * 150, 20, 130, 20);
            panelCartasJugador.add(lblCartasJugador[i]);

            lblImagenesJugador[i] = new JLabel();
            lblImagenesJugador[i].setBounds(20 + i * 150, 40, 130, 180);
            lblImagenesJugador[i].setHorizontalAlignment(SwingConstants.CENTER);
            panelCartasJugador.add(lblImagenesJugador[i]);
        }

        // Panel para las cartas del rival (izquierda abajo)
        JPanel panelCartasRival = new JPanel();
        panelCartasRival.setLayout(null);
        panelCartasRival.setBounds(20, 470, 500, 200);
        panelCartasRival.setBorder(BorderFactory.createTitledBorder("Cartas del Rival"));
        add(panelCartasRival);

        // Cartas del rival
        for (int i = 0; i < 3; i++) {
            JLabel nombreRival = new JLabel("Carta " + (i + 1), SwingConstants.CENTER);
            nombreRival.setBounds(20 + i * 150, 20, 130, 20);
            panelCartasRival.add(nombreRival);

            lblImagenesRival[i] = new JLabel();
            lblImagenesRival[i].setBounds(20 + i * 150, 40, 130, 150);
            lblImagenesRival[i].setHorizontalAlignment(SwingConstants.CENTER);
            panelCartasRival.add(lblImagenesRival[i]);
        }

        // Panel derecho: Log y botones
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(null);
        panelDerecho.setBounds(550, 20, 600, 630);
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Combate"));
        add(panelDerecho);

        // Log de combate
        txtLog = new JTextPane(); // Cambiado de JTextArea a JTextPane
        txtLog.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtLog);
        scroll.setBounds(20, 30, 550, 300);
        panelDerecho.add(scroll);

        // Selector de carta
        comboCartaJugador = new JComboBox<>();
        comboCartaJugador.setBounds(20, 340, 550, 30);
        panelDerecho.add(comboCartaJugador);

        // Botones de acción
        btnAtacar = new JButton("Atacar");
        btnDefender = new JButton("Defender");

        btnAtacar.setBounds(20, 380, 270, 40);
        btnDefender.setBounds(300, 380, 270, 40);

        btnAtacar.addActionListener(e -> ejecutarTurno(true));
        btnDefender.addActionListener(e -> ejecutarTurno(false));

        panelDerecho.add(btnAtacar);
        panelDerecho.add(btnDefender);

        // Botón Reiniciar
        JButton btnReiniciar = new JButton("Reiniciar Juego");
        btnReiniciar.setBounds(20, 440, 550, 40);
        btnReiniciar.addActionListener(e -> reiniciarJuego());
        panelDerecho.add(btnReiniciar);

        obtenerCartasAleatorias();

        try {
            txtLog.getDocument().insertString(txtLog.getDocument().getLength(), "¡Empieza el duelo!\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Carga cartas aleatorias del API
    private void obtenerCartasAleatorias() {
        String url = "https://db.ygoprodeck.com/api/v7/cardinfo.php?num=6&offset=" + (int) (Math.random() * 1000);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject obj = new JSONObject(response.body());
                JSONArray data = obj.getJSONArray("data");

                if (data.length() >= 6) {
                    java.util.Set<Integer> uniqueIndices = new java.util.HashSet<>();
                    while (uniqueIndices.size() < 6) {
                        uniqueIndices.add((int) (Math.random() * data.length()));
                    }

                    cartasJugador = new JSONObject[3];
                    cartasRival = new JSONObject[3];

                    int idx = 0;
                    for (int i : uniqueIndices) {
                        if (idx < 3) {
                            cartasJugador[idx++] = data.getJSONObject(i);
                        } else {
                            cartasRival[idx++ - 3] = data.getJSONObject(i);
                        }
                    }

                    // Cargar imágenes y nombres para el jugador
                    for (int i = 0; i < 3; i++) {
                        JSONObject carta = cartasJugador[i];
                        if (carta.has("card_images") && carta.getJSONArray("card_images").length() > 0) {
                            String urlImg = carta.getJSONArray("card_images").getJSONObject(0).getString("image_url");
                            ImageIcon icon = new ImageIcon(new URL(urlImg));
                            Image img = icon.getImage().getScaledInstance(130, 180, Image.SCALE_SMOOTH);
                            lblImagenesJugador[i].setIcon(new ImageIcon(img));
                            lblCartasJugador[i].setText(carta.getString("name"));
                        }
                    }

                    // Cargar imágenes y nombres para el rival
                    for (int i = 0; i < 3; i++) {
                        JSONObject carta = cartasRival[i];
                        if (carta.has("card_images") && carta.getJSONArray("card_images").length() > 0) {
                            String urlImg = carta.getJSONArray("card_images").getJSONObject(0).getString("image_url");
                            ImageIcon icon = new ImageIcon(new URL(urlImg));
                            Image img = icon.getImage().getScaledInstance(130, 150, Image.SCALE_SMOOTH);
                            lblImagenesRival[i].setIcon(new ImageIcon(img));
                        }
                    }

                    // Llenar combo de selección de carta
                    comboCartaJugador.removeAllItems();
                    for (int i = 0; i < 3; i++) {
                        comboCartaJugador.addItem(cartasJugador[i].getString("name"));
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No hay suficientes cartas disponibles.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar cartas.");
            e.printStackTrace();
        }
    }

    // Ejecuta una acción: ataque o defensa
    private void ejecutarTurno(boolean atacanteJugador) {
        int index = comboCartaJugador.getSelectedIndex();
        JSONObject cartaJugador = cartasJugador[index];

        int atkJugador = cartaJugador.optInt("atk", 0);
        int defJugador = cartaJugador.optInt("def", 0);

        // El rival elige una carta al azar
        int rivalIndex = (int)(Math.random() * 3);
        JSONObject cartaRival = cartasRival[rivalIndex];
        int atkRival = cartaRival.optInt("atk", 0);
        int defRival = cartaRival.optInt("def", 0);

        boolean atacanteRival = Math.random() > 0.5;

        try {
            StyledDocument doc = txtLog.getStyledDocument();
            doc.insertString(doc.getLength(), "\n--- Turno " + (turnoActual + 1) + " ---\n", null);
            doc.insertString(doc.getLength(), "Tú usaste: " + cartaJugador.getString("name") + "\n", null);
            doc.insertString(doc.getLength(), "Rival usó: " + cartaRival.getString("name") + "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        // Animación simple
        animacionAtaque(index);

        // Simular daño
        if (atacanteJugador && atacanteRival) {
            vidaRival -= Math.max(0, atkJugador - defRival);
            vidaJugador -= Math.max(0, atkRival - defJugador);
            mostrarMensajeConColor("Ambos atacaron directamente.", Color.RED);
        } else if (atacanteJugador && !atacanteRival) {
            vidaRival -= Math.max(0, atkJugador - defRival);
            mostrarMensajeConColor("Atacaste mientras el rival defendía.", Color.GREEN);
        } else if (!atacanteJugador && atacanteRival) {
            vidaJugador -= Math.max(0, atkRival - defJugador);
            mostrarMensajeConColor("Defendiste mientras el rival atacaba.", Color.YELLOW);
        } else {
            mostrarMensajeConColor("Ambos decidieron defender.", Color.BLUE);
        }

        // Actualizar vida
        ((JLabel)getContentPane().getComponent(0)).setText("Tu Vida: " + vidaJugador + " | Vida Rival: " + vidaRival);

        turnoActual++;

        if (turnoActual >= 3 || vidaJugador <= 0 || vidaRival <= 0) {
            String resultado;
            if (vidaJugador > vidaRival) {
                resultado = "🎉 ¡Has ganado!";
            } else if (vidaRival > vidaJugador) {
                resultado = "💀 Has perdido.";
            } else {
                resultado = "🤝 ¡Es un empate!";
            }

            mostrarMensajeConColor("\n" + resultado, vidaJugador > vidaRival ? Color.GREEN : Color.RED);
            guardarHistorial(resultado);
            deshabilitarBotones();
        }
    }

    // Muestra mensaje con color personalizado
    private void mostrarMensajeConColor(String mensaje, Color color) {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        AttributeSet attrSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        try {
            StyledDocument doc = txtLog.getStyledDocument();
            doc.setCharacterAttributes(doc.getLength(), 0, attrSet, false);
            doc.insertString(doc.getLength(), mensaje + "\n", attrSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Animación simple al atacar
    private void animacionAtaque(int index) {
        JLabel label = lblImagenesJugador[index];
        Point original = label.getLocation();

        Timer timer = new Timer(50, null);
        timer.setInitialDelay(0);
        timer.addActionListener(e -> {
            int x = label.getX();
            if (x < original.x + 10) {
                label.setLocation(x + 1, label.getY());
            } else {
                label.setLocation(original);
                ((Timer)e.getSource()).stop();
            }
        });

        timer.start();
    }

    // Reinicia el juego
    private void reiniciarJuego() {
        vidaJugador = 3000;
        vidaRival = 3000;
        turnoActual = 0;

        ((JLabel)getContentPane().getComponent(0)).setText("Tu Vida: " + vidaJugador + " | Vida Rival: " + vidaRival);
        txtLog.setText("¡Nuevo duelo empezando...\n");

        for (JLabel label : lblCartasJugador) label.setText("");
        for (JLabel label : lblImagenesJugador) label.setIcon(null);
        for (JLabel label : lblImagenesRival) label.setIcon(null);

        obtenerCartasAleatorias();
        habilitarBotones();
    }

    private void deshabilitarBotones() {
        btnAtacar.setEnabled(false);
        btnDefender.setEnabled(false);
    }

    private void habilitarBotones() {
        btnAtacar.setEnabled(true);
        btnDefender.setEnabled(true);
    }

    private void guardarHistorial(String resultado) {
        historial.append("Partida ").append(partidaNumero++)
                 .append(": ").append(resultado).append("\n");

        JOptionPane.showMessageDialog(this,
            "Historial de partidas:\n" + historial.toString(),
            "Historial", JOptionPane.INFORMATION_MESSAGE);
    }
}