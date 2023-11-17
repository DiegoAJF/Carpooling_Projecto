import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DriverApp extends JFrame {


    public DriverApp(String username) {
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
                    // Aquí puedes realizar acciones con el nombre del amigo
                    // Por ejemplo, mostrar un mensaje con el nombre
                    JOptionPane.showMessageDialog(DriverApp.this, "Friend added: " + friendName);
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

    public static void main(String[] args) {
        // Ejemplo de uso
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DriverApp("Driver123").setVisible(true);
            }
        });
    }
}
