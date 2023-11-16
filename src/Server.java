import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


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

    // Reemplaza el método handleFriendRequest con este código:

    private static void handleFriendRequest(BufferedReader reader, BufferedWriter writer) throws IOException {
        // Lógica para manejar la solicitud de amistad
        String jsonFriendRequestData = reader.readLine();
        JSONObject friendRequestData = new JSONObject(jsonFriendRequestData);

        // Obtener los campos del JSON
        String driverUsername = friendRequestData.getString("driverUsername");
        String friendUsername = friendRequestData.getString("friendUsername");
        String requestDetails = friendRequestData.optString("requestDetails", "");  // Obtener detalles (puede ser una cadena vacía)

        // Verificar la solicitud de amistad y enviar la respuesta al cliente
        if (checkUserExists(driverUsername) && checkUserExists(friendUsername)) {
            // Almacenar la información de la solicitud en un archivo XML
            storeFriendshipRequest(driverUsername, friendUsername, requestDetails);

            writer.write("Friend request sent to " + friendUsername);
        } else {
            writer.write("User not found: " + friendUsername);
        }
        writer.newLine();
        writer.flush();
    }

    // Agregar este nuevo método para almacenar la solicitud de amistad en un archivo XML
    private static void storeFriendshipRequest(String driverUsername, String friendUsername, String requestDetails) {
        try (FileWriter writer = new FileWriter("friendship_requests.xml", true)) {
            // Formato XML para almacenar las solicitudes de amistad con detalles
            String xmlData = String.format(
                    "<friendship_request><driver>%s</driver><friend>%s</friend><requestDetails>%s</requestDetails></friendship_request>%n",
                    driverUsername, friendUsername, requestDetails);

            // Escribir la información en el archivo
            writer.write(xmlData);
            System.out.println(driverUsername + " sent a friend request to " + friendUsername);

        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
        }
    }

// Modificar el método getDriverFriendshipsWithDetails con este código:

    private static List<String> getDriverFriendshipsWithDetails(String driverUsername) {
        // Lógica para obtener las solicitudes y relaciones de amistad con detalles desde el archivo XML
        List<String> driverFriendships = new ArrayList<>();

        try {
            // Obtener las solicitudes de amistad
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = ((DocumentBuilder) builder).parse(new File("friendship_requests.xml"));

            NodeList requestNodes = doc.getElementsByTagName("friendship_request");
            for (int i = 0; i < ((NodeList) requestNodes).getLength(); i++) {
                Node requestNode = requestNodes.item(i);
                if (requestNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element requestElement = (Element) requestNode;
                    String friend = requestElement.getElementsByTagName("friend").item(0).getTextContent();
                    String requestDetails = requestElement.getElementsByTagName("requestDetails").item(0).getTextContent();
                    // Agregar el nombre del amigo y detalles de la solicitud a la lista
                    if (driverUsername.equals(friend)) {
                        driverFriendships.add(friend + " - Request Details: " + requestDetails);
                    }
                }
            }

            // Obtener las relaciones de amistad
            doc = builder.parse(new File("friendships.xml"));

            NodeList friendshipNodes = doc.getElementsByTagName("friendship");
            for (int i = 0; i < friendshipNodes.getLength(); i++) {
                Node friendshipNode = friendshipNodes.item(i);
                if (friendshipNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element friendshipElement = (Element) friendshipNode;
                    String friend = friendshipElement.getElementsByTagName("friend").item(0).getTextContent();
                    String requestDetails = friendshipElement.getElementsByTagName("requestDetails").item(0).getTextContent();
                    // Agregar el nombre del amigo y detalles de la solicitud a la lista
                    if (driverUsername.equals(friend)) {
                        driverFriendships.add(friend + " - Request Details: " + requestDetails);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
        }

        return driverFriendships;
    }

    private static boolean checkUserExists(String username) {
        try {
            String fileName = username + "_userData.xml";
            File file = new File(fileName);
            return file.exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

            } else if ("FRIEND_REQUEST".equals(operationType)) {
                // Lógica para manejar la solicitud de amistad
                handleFriendRequest(reader, writer);
            } else {
                System.out.println("Unsupported operation type");
            }

            // Cerrar la conexión
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Modificar el método storeFriendship
    private static void storeFriendship(String driverUsername, String friendUsername, String requestDetails) {
        try (FileWriter writer = new FileWriter("friendships.xml", true)) {
            // Formato XML para almacenar las relaciones de amistad con detalles de solicitud
            String xmlData = String.format(
                    "<friendship><driver>%s</driver><friend>%s</friend><requestDetails>%s</requestDetails></friendship>%n",
                    driverUsername, friendUsername, requestDetails);

            // Escribir la información en el archivo
            writer.write(xmlData);
            System.out.println(driverUsername + " and " + friendUsername + " are now friends.");

        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
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
