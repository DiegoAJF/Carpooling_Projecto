import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DriverApp extends JFrame {
    private JComboBox<String> transportationComboBox;

    public DriverApp(String username) {
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
                JOptionPane.showMessageDialog(DriverApp.this, "Viewing Average Rating");
            }
        });

        // Crear JMenuItem "View Friendships with Drivers"
        JMenuItem viewFriendshipsItem = new JMenuItem("View Friendships with Drivers");
        viewFriendshipsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para visualizar relaciones de amistad con conductores
                JOptionPane.showMessageDialog(DriverApp.this, "Viewing Friendships with Drivers");
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

}
