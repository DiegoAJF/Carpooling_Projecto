import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONObject;
import org.json.XML;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Server {

    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection established with " + clientSocket.getInetAddress());

                new Thread(() -> handleConnection(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleConnection(Socket clientSocket) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            String operationType = reader.readLine();

            if ("REGISTER".equals(operationType)) {
                // Leer el JSON con la información del registro
                String jsonRegistrationData = reader.readLine();
                JSONObject registrationData = new JSONObject(jsonRegistrationData);

                // Obtener los campos del JSON
                String username = registrationData.getString("username");
                String password = registrationData.getString("password");
                String employeeId = registrationData.getString("employeeId");
                String location = registrationData.getString("location");

                // Almacenar la información del usuario como un archivo XML
                saveUserDataAsXML(username, password, employeeId, location);

                // Enviar confirmación de registro exitoso al cliente
                writer.write("Registration successful");
                writer.newLine();
                writer.flush();
            } else if ("LOGIN".equals(operationType)) {
                // Lógica para el inicio de sesión aquí
                // Puedes adaptar según tus necesidades
            } else {
                System.out.println("Unsupported operation type");
            }

            // Cerrar la conexión
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveUserDataAsXML(String username, String password, String employeeId, String location) {
        // Crea un objeto JSON con la información del usuario
        JSONObject userData = new JSONObject();
        userData.put("username", username);
        userData.put("password", password);
        userData.put("employeeId", employeeId);
        userData.put("location", location);

        // Convierte el objeto JSON a una cadena XML
        String xmlData = XML.toString(userData);

        // Guarda la cadena XML en un archivo en el sistema de archivos del servidor
        String fileName = username + "_userData.xml"; // Puedes ajustar el nombre del archivo según tus necesidades
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(xmlData);
            System.out.println("User data saved to: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            // Aquí puedes manejar la excepción según tus necesidades, por ejemplo, registrando el error
        }
    }
}