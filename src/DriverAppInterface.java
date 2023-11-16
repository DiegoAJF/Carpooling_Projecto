import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DriverAppInterface extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField driverIdField;
    private JComboBox<String> locationComboBox;

    public DriverAppInterface() {
        // Configuración de la interfaz
        setTitle("DriverApp Login and Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Componentes de la interfaz
        JLabel titleLabel = new JLabel("DriverApp");
        titleLabel.setBounds(150, 10, 100, 20);
        add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 80, 20);
        add(usernameLabel);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 80, 80, 20);
        add(passwordLabel);

        JLabel driverIdLabel = new JLabel("Driver ID:");
        driverIdLabel.setBounds(50, 110, 80, 20);
        add(driverIdLabel);

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setBounds(50, 140, 80, 20);
        add(locationLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 150, 20);
        add(usernameField);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 80, 150, 20);
        add(passwordField);

        driverIdField = new JTextField();
        driverIdField.setBounds(150, 110, 150, 20);
        add(driverIdField);

        String[] locations = {"Location A", "Location B", "Location C"};
        locationComboBox = new JComboBox<>(locations);
        locationComboBox.setBounds(150, 140, 150, 20);
        add(locationComboBox);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 180, 80, 30);
        add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 180, 100, 30);
        add(registerButton);

        // Acción del botón de registro
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para el registro aquí
                // Puedes obtener los valores de los campos usando usernameField.getText(), passwordField.getPassword(), etc.
                // Implementa la lógica para guardar la información en el servidor.
            }
        });

        // Acción del botón de inicio de sesión
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para el inicio de sesión aquí
                // Puedes obtener los valores de los campos usando usernameField.getText(), passwordField.getPassword(), etc.
                // Implementa la lógica para verificar la información en el servidor.
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DriverAppInterface().setVisible(true);
            }
        });
    }
}
