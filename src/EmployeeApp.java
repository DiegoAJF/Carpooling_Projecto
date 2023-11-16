import javax.swing.*;

public class EmployeeApp extends JFrame {
    public EmployeeApp(String username) {
        setTitle("Welcome, " + username + "!");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setVisible(true);
    }
}