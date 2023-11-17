import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DriverApp extends JFrame {

    private MapaInteractivo mapaInteractivo;

    public DriverApp(String username) {
        setTitle("Welcome, " + username + "!");
        setSize(700, 700);
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

class MapaInteractivo extends JPanel {

    private Map<String, Punto> puntos;

    public MapaInteractivo(int numDestinos) {
        puntos = new HashMap<>();
        generarMapa(numDestinos);
    }

    private void generarMapa(int numDestinos) {
        // Crear posiciones caóticas para los destinos
        int[] posX = {80, 180, 280, 380, 480, 120, 220, 320, 420, 520, 80, 180, 280, 380, 480, 120, 220, 320, 420, 520, 80, 180, 280, 380, 480, 120, 220, 320, 420, 520};
        int[] posY = {50, 100, 50, 100, 50, 200, 200, 200, 200, 200, 350, 300, 350, 300, 350, 450, 400, 450, 400, 450, 550, 500, 550, 500, 550, 600, 600, 600, 600, 600};

        // Crear puntos (destinos) con posiciones caóticas
        for (int i = 0; i < numDestinos; i++) {
            String nombre = "Destino " + (i + 1);
            Punto punto = new Punto(nombre, posX[i] + getRandomVariation(), posY[i] + getRandomVariation());
            puntos.put(nombre, punto);
        }
    }

    private int getRandomVariation() {
        return (int) (Math.random() * 30 - 15); // Variación entre -15 y 15
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar puntos sin conexiones
        for (Punto punto : puntos.values()) {
            punto.dibujar(g);
        }
    }
}

class Punto {
    private String nombre;
    private int x, y;

    public Punto(String nombre, int x, int y) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
    }

    public void dibujar(Graphics g) {
        // Dibujar el punto
        g.setColor(Color.BLUE);
        g.fillOval(x - 10, y - 10, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString(nombre, x - 10, y - 20);
    }
}
