import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONObject;
import java.io.*;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Clase que representa la interfaz de usuario para la aplicación de conductor (DriverApp).
 * Permite a los usuarios realizar operaciones de inicio de sesión y registro.
 */
public class DriverAppInterface extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField employeeIdField;
    private JComboBox<String> locationComboBox;

    /**
     * Constructor de la interfaz del usuario de DriverApp.
     * Configura y muestra la interfaz gráfica con campos de inicio de sesión y registro.
     */
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

        employeeIdField = new JTextField();
        employeeIdField.setBounds(150, 110, 150, 20);
        add(employeeIdField);

        String[] locations = new String[30];
        for (int i = 1; i <= 30; i++) {
            locations[i - 1] = "Location " + i;
        }

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
                // Obtén los valores de los campos
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);
                String employeeId = employeeIdField.getText();
                String location = (String) locationComboBox.getSelectedItem();

                // Crea un objeto JSON con la información del registro
                JSONObject registrationData = new JSONObject();
                registrationData.put("username", username);
                registrationData.put("password", password);
                registrationData.put("employeeId", employeeId);
                registrationData.put("location", location);

                // Agrega el campo "userType" con el valor "employee"
                registrationData.put("userType", "Driver");

                try (Socket socket = new Socket("localhost", 12345); // Cambia "localhost" por la IP del servidor
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
                ) {
                    // Indica al servidor que la operación es de registro
                    writer.write("REGISTER");
                    writer.newLine();

                    // Envía el JSON al servidor
                    writer.write(registrationData.toString());
                    writer.newLine();
                    writer.flush();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Aquí puedes manejar la excepción según tus necesidades
                }
            }
        });

        // Acción del botón de inicio de sesión
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtén los valores de los campos
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                // Crea un objeto JSON con la información de inicio de sesión
                JSONObject loginData = new JSONObject();
                loginData.put("username", username);
                loginData.put("password", password);

                try (Socket socket = new Socket("localhost", 12345);
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ) {
                    // Indica al servidor que la operación es de inicio de sesión
                    writer.write("LOGIN");
                    writer.newLine();

                    // Envía el JSON al servidor
                    writer.write(loginData.toString());
                    writer.newLine();
                    writer.flush();

                    // Lee la respuesta del servidor
                    String response = reader.readLine();

                    // Muestra el mensaje de resultado en la interfaz
                    if ("Login successful".equals(response)) {
                        // Abre la nueva interfaz
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new DriverApp(username).setVisible(true);
                            }
                        });

                        // Cierra la interfaz actual
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(DriverAppInterface.this, "Login failed. Please check your credentials.");
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Aquí puedes manejar la excepción según tus necesidades
                }
            }
        });
    }

    /**
     * Método principal que inicia la aplicación DriverAppInterface.
     * Crea una instancia de la interfaz y la hace visible.
     * @param args Los argumentos de la línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DriverAppInterface().setVisible(true);
            }
        });
    }
}
