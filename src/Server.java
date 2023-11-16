import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONObject;
import org.json.XML;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

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
                // Leer el JSON con la información de inicio de sesión
                String jsonLoginData = reader.readLine();
                JSONObject loginData = new JSONObject(jsonLoginData);

                // Obtener los campos del JSON
                String username = loginData.getString("username");
                String password = loginData.getString("password");

                // Verificar las credenciales
                boolean loginSuccessful = checkCredentials(username, password);

                // Enviar resultado al cliente
                if (loginSuccessful) {
                    writer.write("Login successful");
                } else {
                    writer.write("Login failed");
                }
                writer.newLine();
                writer.flush();
            } else {
                System.out.println("Unsupported operation type");
            }

            // Cerrar la conexión
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static boolean checkCredentials(String username, String password) {
        try {
            String fileName = username + "_userData.xml";
            File file = new File(fileName);

            if (file.exists()) {
                // Lee el contenido del archivo XML y compara las credenciales
                String xmlData = new String(Files.readAllBytes(file.toPath()));
                JSONObject userData = XML.toJSONObject(xmlData);

                String storedUsername = userData.getString("username");
                String storedPassword = userData.get("password").toString(); // Convierte el valor a cadena

                return username.equals(storedUsername) && password.equals(storedPassword);
            } else {
                return false; // El usuario no existe
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void saveUserDataAsXML(String username, String password, String employeeId, String location) {
        // Crea un objeto JSON con la información del usuario
        JSONObject userData = new JSONObject();
        userData.put("username", username);
        userData.put("password", password); // Asegúrate de que password sea siempre una cadena
        userData.put("employeeId", employeeId);
        userData.put("location", location);

        // Convierte el objeto JSON a una cadena XML
        String xmlData = XML.toString(userData);

        // Guarda la cadena XML en un archivo en el sistema de archivos del servidor
        String fileName = username + "_userData.xml";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(xmlData);
            System.out.println("User data saved to: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            // Maneja la excepción según tus necesidades
        }
    }
}
