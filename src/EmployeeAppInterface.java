import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONObject;
import java.io.*;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.IOException;

public class EmployeeAppInterface extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField employeeIdField;
    private JComboBox<String> locationComboBox;

    public EmployeeAppInterface() {
        // Configuración de la interfaz
        setTitle("EmployeeApp");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Componentes de la interfaz
        JLabel titleLabel = new JLabel("EmployeeApp");
        titleLabel.setBounds(150, 10, 100, 20);
        add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 80, 20);
        add(usernameLabel);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 80, 80, 20);
        add(passwordLabel);

        JLabel employeeIdLabel = new JLabel("Employee ID:");
        employeeIdLabel.setBounds(50, 110, 80, 20);
        add(employeeIdLabel);

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

        // Change to hold 30 numeric locations
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
                registrationData.put("userType", "employee");

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
                                new EmployeeApp(username).setVisible(true);
                            }
                        });

                        // Cierra la interfaz actual
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(EmployeeAppInterface.this, "Login failed. Please check your credentials.");
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Aquí puedes manejar la excepción según tus necesidades
                }
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EmployeeAppInterface().setVisible(true);
            }
        });
    }
}
