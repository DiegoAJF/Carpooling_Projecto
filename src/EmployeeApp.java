import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.XML;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    private void viewDriverFriendships() {
        // Obtener el nombre de usuario del empleado actual
        String employeeUsername = "EmpleadoEjemplo";  // Reemplaza con el nombre de usuario real

        // Obtener y mostrar las amistades con conductores
        String friendships = getDriverFriendships(employeeUsername);
        JOptionPane.showMessageDialog(EmployeeApp.this, "Friendships with Drivers:\n" + friendships);
    }

    // Agregar un método para obtener las amistades con conductores
    private String getDriverFriendships(String employeeUsername) {
        try {
            String fileName = employeeUsername + "_friendships.xml";
            File file = new File(fileName);

            // Verificar si el archivo existe
            if (file.exists()) {
                // Lee el contenido del archivo XML
                String xmlData = new String(readFileContents(file));
                JSONObject friendshipsData = XML.toJSONObject(xmlData);

                // Extraer y formatear las amistades con conductores
                StringBuilder friendshipsStringBuilder = new StringBuilder();
                friendshipsStringBuilder.append("Driver\tStatus\n");

                JSONObject friendships = friendshipsData.getJSONObject("friendships");
                for (String driver : friendships.keySet()) {
                    String status = friendships.getString(driver);
                    friendshipsStringBuilder.append(driver).append("\t").append(status).append("\n");
                }

                return friendshipsStringBuilder.toString();
            } else {
                return "No friendships with drivers found.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error retrieving friendships with drivers.";
        }
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

