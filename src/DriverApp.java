import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONObject;
import java.io.*;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.IOException;

public class DriverApp extends JFrame {

    private String username; // Agrega esta línea para declarar la variable username

    public DriverApp(String username) {
        this.username = username; // Agrega esta línea para asignar el valor del parámetro al campo de la clase
        setTitle("Welcome, " + username + "!");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

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
                JOptionPane.showMessageDialog(DriverApp.this, "Viewing Average Rating");
            }
        });

        // Crear JMenuItem "View Friendships with Drivers"
        JMenuItem viewFriendshipsItem = new JMenuItem("View Friendships with Employees");
        viewFriendshipsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para visualizar relaciones de amistad con conductores
                JOptionPane.showMessageDialog(DriverApp.this, "Viewing Friendships with Employees");
            }
        });

        // Crear JMenuItem "Add Friends"
        JMenuItem addFriendsItem = new JMenuItem("Add Friends");
        addFriendsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para añadir amigos
                String friendName = JOptionPane.showInputDialog(DriverApp.this, "Enter friend's name:");
                if (friendName != null) {
                    // Enviar solicitud de amistad al servidor usando JSON
                    sendFriendRequest(friendName);
                }
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

        setVisible(true);
    }

    // Método para enviar la solicitud de amistad al servidor
// Reemplaza el método sendFriendRequest con este código:

    // Método para enviar la solicitud de amistad al servidor
// Reemplaza el método sendFriendRequest con este código:
    private void sendFriendRequest(String friendUsername) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Obtener detalles adicionales de la solicitud (opcional)
            String requestDetails = JOptionPane.showInputDialog(DriverApp.this, "Enter details for the friend request (optional):");

            // Crear un objeto JSON con la información de la solicitud de amistad
            JSONObject friendRequestData = new JSONObject();
            friendRequestData.put("operationType", "FRIEND_REQUEST");
            friendRequestData.put("driverUsername", username);  // Utiliza el campo de la clase
            friendRequestData.put("friendUsername", friendUsername);
            friendRequestData.put("requestDetails", requestDetails);  // Agregar detalles de la solicitud

            // Resto del código...
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
