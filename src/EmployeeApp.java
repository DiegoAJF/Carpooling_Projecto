import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * La clase EmployeeApp representa una aplicación de empleado con interfaz gráfica.
 * Permite a los empleados interactuar con un mapa interactivo y realizar diversas acciones.
 */
public class EmployeeApp extends JFrame {
    private JComboBox<String> transportationComboBox;
    private MapaInteractivoE mapaInteractivo;

    /**
     * Constructor de la clase EmployeeApp.
     *
     * @param username El nombre de usuario del empleado.
     */
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

    /**
     * Obtiene la opción de transporte seleccionada.
     *
     * @return La opción de transporte seleccionada.
     */
    public String getTransportationOption() {
        return (String) transportationComboBox.getSelectedItem();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeApp("Employee123"));
    }
}

/**
 * La clase MapaInteractivoE representa un mapa interactivo con puntos de destino.
 * Permite generar un mapa con ubicaciones predefinidas.
 */
class MapaInteractivoE extends JPanel {
    private Map<String, PuntoE> puntos;

    /**
     * Constructor de la clase MapaInteractivoE.
     *
     * @param numDestinos El número de destinos en el mapa.
     */
    public MapaInteractivoE(int numDestinos) {
        puntos = new HashMap<>();
        generarMapa(numDestinos);
    }

    private void generarMapa(int numDestinos) {
        int[] posX = {80, 180, 280, 380, 480, 120, 220, 320, 420, 520, 80, 180, 280, 380, 480, 120, 220, 320, 420, 520, 80, 180, 280, 380, 480, 120, 220, 320, 420, 520};
        int[] posY = {50, 100, 50, 100, 50, 200, 200, 200, 200, 200, 350, 300, 350, 300, 350, 450, 400, 450, 400, 450, 550, 500, 550, 500, 550, 600, 600, 600, 600, 600};
        for (int i = 0; i < numDestinos; i++) {
            String nombre = "Destino " + (i + 1);
            PuntoE puntoe;
            if (i == 14) {
                // Ubicación número 15
                puntoe = new PuntoE(nombre, posX[i], posY[i], Color.RED);
            } else {
                puntoe = new PuntoE(nombre, posX[i], posY[i], Color.BLUE);
            }
            puntos.put(nombre, puntoe);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (PuntoE puntoe : puntos.values()) {
            puntoe.dibujar(g);
        }
    }
}

/**
 * La clase PuntoE representa un punto en el mapa interactivo.
 * Cada punto tiene un nombre, coordenadas y color.
 */
class PuntoE {
    private String nombre;
    private int x, y;
    private Color color;

    /**
     * Constructor de la clase PuntoE.
     *
     * @param nombre El nombre del punto.
     * @param x      La coordenada x del punto.
     * @param y      La coordenada y del punto.
     * @param color  El color del punto.
     */
    public PuntoE(String nombre, int x, int y, Color color) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Dibuja el punto en el componente gráfico dado.
     *
     * @param g El objeto Graphics para dibujar el punto.
     */
    public void dibujar(Graphics g) {
        g.setColor(color);
        g.fillOval(x - 10, y - 10, 20, 20);
        g.setColor(Color.RED);
        g.drawString(nombre, x - 10, y - 20);
    }
}
