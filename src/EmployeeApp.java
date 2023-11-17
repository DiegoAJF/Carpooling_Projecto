import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EmployeeApp extends JFrame {
    private JComboBox<String> transportationComboBox;
    private MapaInteractivoE mapaInteractivo;

    public EmployeeApp(String username) {
        setTitle("Welcome, " + username + "!");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear y agregar JComboBox en la parte inferior
        String[] transportationOptions = {"Friend", "Driver"};
        transportationComboBox = new JComboBox<>(transportationOptions);
        add(transportationComboBox, BorderLayout.SOUTH);

        // Crear JMenuBar
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem viewRatingItem = new JMenuItem("View Average Rating");
        viewRatingItem.addActionListener(e -> JOptionPane.showMessageDialog(EmployeeApp.this, "Viewing Average Rating"));
        JMenuItem viewFriendshipsItem = new JMenuItem("View Friendships");
        optionsMenu.add(viewRatingItem);
        optionsMenu.add(viewFriendshipsItem);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);

        // Crear el mapa interactivo con 30 destinos
        mapaInteractivo = new MapaInteractivoE(30);
        add(mapaInteractivo, BorderLayout.CENTER);

        setVisible(true);
    }

    public String getTransportationOption() {
        return (String) transportationComboBox.getSelectedItem();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeApp("Employee123"));
    }
}

class MapaInteractivoE extends JPanel {
    private Map<String, Punto> puntos;

    public MapaInteractivoE(int numDestinos) {
        puntos = new HashMap<>();
        generarMapa(numDestinos);
    }

    private void generarMapa(int numDestinos) {
        int[] posX = {80, 180, 280, 380, 480, 120, 220, 320, 420, 520, 80, 180, 280, 380, 480, 120, 220, 320, 420, 520, 80, 180, 280, 380, 480, 120, 220, 320, 420, 520};
        int[] posY = {50, 100, 50, 100, 50, 200, 200, 200, 200, 200, 350, 300, 350, 300, 350, 450, 400, 450, 400, 450, 550, 500, 550, 500, 550, 600, 600, 600, 600, 600};
        for (int i = 0; i < numDestinos; i++) {
            String nombre = "Destino " + (i + 1);
            Punto punto = new Punto(nombre, posX[i], posY[i]);
            puntos.put(nombre, punto);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Punto punto : puntos.values()) {
            punto.dibujar(g);
        }
    }
}

class PuntoE {
    private String nombre;
    private int x, y;

    public PuntoE(String nombre, int x, int y) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(x - 10, y - 10, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString(nombre, x - 10, y - 20);
    }
}
