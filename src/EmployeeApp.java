import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EmployeeApp extends JFrame {
    private JComboBox<String> transportationComboBox;

    public EmployeeApp(String username) {
        setTitle("Welcome, " + username + "!");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        // Crear y agregar JComboBox en la parte inferior
        String[] transportationOptions = {"Friend", "Driver"};
        transportationComboBox = new JComboBox<>(transportationOptions);
        add(transportationComboBox, BorderLayout.SOUTH);

        // Crear JMenuBar
        JMenuBar menuBar = new JMenuBar();

        // Crear JMenu "Options"
        JMenu optionsMenu = new JMenu("Options");

        // Crear JMenuItem "View Average Rating"
        JMenuItem viewRatingItem = new JMenuItem("View Average Rating");
        viewRatingItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para visualizar la calificación promedio
                JOptionPane.showMessageDialog(EmployeeApp.this, "Viewing Average Rating");
            }
        });

        // Crear JMenuItem "View Friendships with Drivers"
        JMenuItem viewFriendshipsItem = new JMenuItem("View Friendships with Drivers");
        viewFriendshipsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para visualizar relaciones de amistad con conductores
                String transportationOption = getTransportationOption();
                if ("Driver".equals(transportationOption)) {
                    // Lógica específica para visualizar amistades con conductores
                    viewDriverFriendships();
                } else {
                    // Lógica para otros tipos de transporte (si es necesario)
                    JOptionPane.showMessageDialog(EmployeeApp.this, "Viewing Friendships with " + transportationOption + "s");
                }
            }
        });

        // Agregar los JMenuItem al JMenu
        optionsMenu.add(viewRatingItem);
        optionsMenu.add(viewFriendshipsItem);

        // Agregar el JMenu al JMenuBar
        menuBar.add(optionsMenu);

        // Establecer el JMenuBar en el JFrame
        setJMenuBar(menuBar);

        setVisible(true);
    }

    // Agrega un método para obtener la opción seleccionada en el JComboBox
    public String getTransportationOption() {
        return (String) transportationComboBox.getSelectedItem();
    }
// Dentro de la clase EmployeeApp

    // Dentro de la clase EmployeeApp
    private void viewDriverFriendships() {
        // Lógica para obtener y mostrar las amistades con conductores
        List<String> driverFriendships = getDriverFriendshipsWithDetails();  // Obtener detalles de las amistades con conductores
        if (!driverFriendships.isEmpty()) {
            // Si hay conductores amigos, mostrar en un cuadro de diálogo
            String message = "Friendships with Drivers:\n" + String.join("\n", driverFriendships);
            JOptionPane.showMessageDialog(EmployeeApp.this, message);
        } else {
            JOptionPane.showMessageDialog(EmployeeApp.this, "No friendships with drivers.");
        }
    }

    // Agregar un método para obtener los detalles de las amistades con conductores
    private List<String> getDriverFriendshipsWithDetails() {
        // Lógica para obtener los detalles de las amistades con conductores desde el archivo XML
        List<String> driverFriendships = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("friendships.xml"));

            NodeList friendshipNodes = doc.getElementsByTagName("friendship");
            for (int i = 0; i < friendshipNodes.getLength(); i++) {
                Node friendshipNode = friendshipNodes.item(i);
                if (friendshipNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element friendshipElement = (Element) friendshipNode;
                    String friend = friendshipElement.getElementsByTagName("friend").item(0).getTextContent();
                    String requestDetails = friendshipElement.getElementsByTagName("requestDetails").item(0).getTextContent();
                    // Agregar el nombre del amigo y detalles de la solicitud a la lista
                    driverFriendships.add(friend + " - Request Details: " + requestDetails);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
        }

        return driverFriendships;
    }


    // Agregar un método para leer el contenido de un archivo
    private static String readFileContents(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}

