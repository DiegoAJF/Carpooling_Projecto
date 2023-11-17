import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * La clase DriverApp representa la aplicación principal que muestra una interfaz gráfica para interactuar con un mapa interactivo.
 */
public class DriverApp extends JFrame {

    private MapaInteractivo mapaInteractivo;

    /**
     * Constructor de la clase DriverApp.
     * @param username El nombre de usuario que se mostrará en el título de la ventana.
     */
    public DriverApp(String username) {
        setTitle("Welcome, " + username + "!");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear JMenuBar
        JMenuBar menuBar = new JMenuBar();

        // Crear JMenu "Options"
        JMenu optionsMenu = new JMenu("Options");

        // Crear JMenuItem "View Average Rating"
        JMenuItem viewRatingItem = new JMenuItem("View Average Rating");
        viewRatingItem.addActionListener(e -> JOptionPane.showMessageDialog(DriverApp.this, "Viewing Average Rating"));

        // Crear JMenuItem "View Friendships with Drivers"
        JMenuItem viewFriendshipsItem = new JMenuItem("View Friendships with Employees");
        viewFriendshipsItem.addActionListener(e -> JOptionPane.showMessageDialog(DriverApp.this, "Viewing Friendships with Employees"));

        // Crear JMenuItem "Add Friends"
        JMenuItem addFriendsItem = new JMenuItem("Add Friends");
        addFriendsItem.addActionListener(e -> {
            String friendName = JOptionPane.showInputDialog(DriverApp.this, "Enter friend's name:");
            if (friendName != null) {
                JOptionPane.showMessageDialog(DriverApp.this, "Friend added: " + friendName);
            }
        });

        // Agregar los JMenuItem al JMenu
        optionsMenu.add(viewRatingItem);
        optionsMenu.add(viewFriendshipsItem);
        optionsMenu.add(addFriendsItem);

        // Agregar el JMenu al JMenuBar
        menuBar.add(optionsMenu);

        // Establecer el JMenuBar en el JFrame
        setJMenuBar(menuBar);

        // Crear el mapa interactivo con 30 destinos
        mapaInteractivo = new MapaInteractivo(30);

        // Agregar el mapa a la ventana
        add(mapaInteractivo, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DriverApp("Driver123"));
    }
}

/**
 * La clase MapaInteractivo representa un mapa interactivo con puntos y un cuadrado negro.
 */
class MapaInteractivo extends JPanel {
    private Map<String, Punto> puntos;
    private BloqueNegro bloqueNegro;

    /**
     * Constructor de la clase MapaInteractivo.
     * @param numDestinos El número de destinos en el mapa.
     */
    public MapaInteractivo(int numDestinos) {
        puntos = new HashMap<>();
        bloqueNegro = new BloqueNegro(0, 0); // Inicializar en (0, 0)
        generarMapa(numDestinos);
    }

    /**
     * Genera el mapa con los destinos y el cuadrado negro en una ubicación aleatoria.
     * @param numDestinos El número de destinos en el mapa.
     */
    private void generarMapa(int numDestinos) {
        int[] posX = {80, 180, 280, 380, 480, 120, 220, 320, 420, 520, 80, 180, 280, 380, 480, 120, 220, 320, 420, 520, 80, 180, 280, 380, 480, 120, 220, 320, 420, 520};
        int[] posY = {50, 100, 50, 100, 50, 200, 200, 200, 200, 200, 350, 300, 350, 300, 350, 450, 400, 450, 400, 450, 550, 500, 550, 500, 550, 600, 600, 600, 600, 600};

        for (int i = 0; i < numDestinos; i++) {
            String nombre = "Destino " + (i + 1);
            Punto punto;
            if (i == 14) {
                punto = new Punto(nombre, posX[i] + getRandomVariation(), posY[i] + getRandomVariation(), true);
            } else {
                punto = new Punto(nombre, posX[i] + getRandomVariation(), posY[i] + getRandomVariation(), false);
            }
            puntos.put(nombre, punto);
        }

        // Crear el cuadrado negro en un punto aleatorio
        int randomIndex = (int) (Math.random() * numDestinos);
        bloqueNegro = new BloqueNegro(posX[randomIndex] + getRandomVariation(), posY[randomIndex] + getRandomVariation());
    }

    private int getRandomVariation() {
        return 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar puntos sin conexiones
        for (Punto punto : puntos.values()) {
            punto.dibujar(g);
        }

        // Dibujar el cuadrado negro
        bloqueNegro.dibujar(g);
    }
}

/**
 * La clase Punto representa un punto en el mapa con nombre, coordenadas y color.
 */
class Punto {
    private String nombre;
    private int x, y;
    private boolean esUbicacion15;

    /**
     * Constructor de la clase Punto.
     * @param nombre El nombre del punto.
     * @param x La coordenada x del punto.
     * @param y La coordenada y del punto.
     * @param esUbicacion15 Indica si este punto es el número 15 en el mapa.
     */
    public Punto(String nombre, int x, int y, boolean esUbicacion15) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
        this.esUbicacion15 = esUbicacion15;
    }

    /**
     * Dibuja el punto en el mapa.
     * @param g El objeto Graphics utilizado para dibujar.
     */
    public void dibujar(Graphics g) {
        // Dibujar el punto
        if (esUbicacion15) {
            g.setColor(Color.RED); // Cambiar color para la ubicación número 15
        } else {
            g.setColor(Color.BLUE);
        }
        g.fillOval(x - 10, y - 10, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString(nombre, x - 10, y - 20);
    }
}

/**
 * La clase BloqueNegro representa un "carro" en el mapa.
 */
class BloqueNegro {
    private int x, y;

    /**
     * Constructor de la clase BloqueNegro.
     * @param x La coordenada x del cuadrado negro.
     * @param y La coordenada y del cuadrado negro.
     */
    public BloqueNegro(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Dibuja el cuadrado negro en el mapa.
     * @param g El objeto Graphics utilizado para dibujar.
     */
    public void dibujar(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, 20, 20); // Puedes ajustar el tamaño del cuadrado según tus preferencias
    }
}
