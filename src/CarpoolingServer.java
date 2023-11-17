import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class CarpoolingServer {
    private static final int PORT = 12345;
    private static MapaInteractivo mapa;

    public static void main(String[] args) {
        gestionarMapaPrototipo();

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

    private static void gestionarMapaPrototipo() {
        mapa = new MapaInteractivo(30);
    }

    private static void handleConnection(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String operationType = reader.readLine();

            if ("REGISTER".equals(operationType)) {
                handleRegistration(reader, writer);
            } else if ("LOGIN".equals(operationType)) {
                handleLogin(reader, writer);
            } else if ("MAP_REQUEST".equals(operationType)) {
                enviarMapaAlCliente(writer);
            } else if ("ROUTE_REQUEST".equals(operationType)) {
                handleRouteRequest(reader, writer);
            } else {
                System.out.println("Unsupported operation type");
            }

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRegistration(BufferedReader reader, BufferedWriter writer) throws IOException {
        String jsonRegistrationData = reader.readLine();
        JSONObject registrationData = new JSONObject(jsonRegistrationData);
        String username = registrationData.getString("username");
        String password = registrationData.getString("password");
        String employeeId = registrationData.getString("employeeId");
        String location = registrationData.getString("location");
        saveUserDataAsXML(username, password, employeeId, location);
        writer.write("Registration successful");
        writer.newLine();
        writer.flush();
    }

    private static void handleLogin(BufferedReader reader, BufferedWriter writer) throws IOException {
        String jsonLoginData = reader.readLine();
        JSONObject loginData = new JSONObject(jsonLoginData);
        String username = loginData.getString("username");
        String password = loginData.getString("password");
        boolean loginSuccessful = checkCredentials(username, password);
        if (loginSuccessful) {
            writer.write("Login successful");
        } else {
            writer.write("Login failed");
        }
        writer.newLine();
        writer.flush();
    }

    private static void handleRouteRequest(BufferedReader reader, BufferedWriter writer) throws IOException {
        String jsonRouteData = reader.readLine();
        JSONObject routeData = new JSONObject(jsonRouteData);
        String origin = routeData.getString("origin");
        String destination = routeData.getString("destination");

        List<Punto> rutaMasCorta = mapa.encontrarRutaMasCorta(origin, destination);

        if (rutaMasCorta != null) {
            enviarRutaAlCliente(writer, rutaMasCorta);
        } else {
            writer.write("Error: No se pudo encontrar la ruta");
            writer.newLine();
            writer.flush();
        }
    }

    private static void enviarMapaAlCliente(BufferedWriter writer) throws IOException {
        JSONObject mapaJSON = new JSONObject();
        for (Punto punto : mapa.getPuntos().values()) {
            JSONObject puntoJSON = new JSONObject();
            puntoJSON.put("nombre", punto.getNombre());
            puntoJSON.put("x", punto.getX());
            puntoJSON.put("y", punto.getY());
            mapaJSON.put(punto.getNombre(), puntoJSON);
        }

        writer.write(mapaJSON.toString());
        writer.newLine();
        writer.flush();
    }

    private static void enviarRutaAlCliente(BufferedWriter writer, List<Punto> ruta) throws IOException {
        JSONObject rutaJSON = new JSONObject();
        JSONArray puntosArray = new JSONArray();

        for (Punto punto : ruta) {
            JSONObject puntoJSON = new JSONObject();
            puntoJSON.put("nombre", punto.getNombre());
            puntoJSON.put("x", punto.getX());
            puntoJSON.put("y", punto.getY());
            puntosArray.put(puntoJSON);
        }

        rutaJSON.put("ruta", puntosArray);

        writer.write(rutaJSON.toString());
        writer.newLine();
        writer.flush();
    }

    private static boolean checkCredentials(String username, String password) {
        try {
            String fileName = username + "_userData.xml";
            File file = new File(fileName);

            if (file.exists()) {
                String xmlData = new String(Files.readAllBytes(file.toPath()));
                JSONObject userData = XML.toJSONObject(xmlData);
                String storedUsername = userData.getString("username");
                String storedPassword = userData.get("password").toString();
                return username.equals(storedUsername) && password.equals(storedPassword);
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void saveUserDataAsXML(String username, String password, String employeeId, String location) {
        JSONObject userData = new JSONObject();
        userData.put("username", username);
        userData.put("password", password);
        userData.put("employeeId", employeeId);
        userData.put("location", location);

        String xmlData = XML.toString(userData);

        String fileName = username + "_userData.xml";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(xmlData);
            System.out.println("User data saved to: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class MapaInteractivo {
        private Map<String, Punto> puntos;

        public MapaInteractivo(int numDestinos) {
            puntos = new HashMap<>();
            generarMapa(numDestinos);
            establecerViasComunicacion();
            asignarDistancias();
        }

        private void generarMapa(int numDestinos) {
            int[] posX = {80, 180, 280, 380, 480, 120, 220, 320, 420, 520, 80, 180, 280, 380, 480, 120, 220, 320, 420, 520, 80, 180, 280, 380, 480, 120, 220, 320, 420, 520};
            int[] posY = {50, 100, 50, 100, 50, 200, 200, 200, 200, 200, 350, 300, 350, 300, 350, 450, 400, 450, 400, 450, 550, 500, 550, 500, 550, 600, 600, 600, 600, 600};

            for (int i = 0; i < numDestinos; i++) {
                String nombre = "Destino " + (i + 1);
                Punto punto = new Punto(nombre, posX[i] + getRandomVariation(), posY[i] + getRandomVariation());
                puntos.put(nombre, punto);
            }
        }

        private void establecerViasComunicacion() {
            Random random = new Random();
            List<Punto> puntosList = new ArrayList<>(puntos.values());

            for (Punto punto : puntosList) {
                int numConexiones = random.nextInt(puntosList.size() / 2) + 1;
                Collections.shuffle(puntosList);

                for (int i = 0; i < numConexiones; i++) {
                    Punto destino = puntosList.get(i);
                    int distancia = random.nextInt(10) + 1;

                    if (!punto.equals(destino)) {
                        punto.agregarConexion(destino, distancia);
                    }
                }
            }
        }

        private void asignarDistancias() {
            Random random = new Random();
            List<Punto> puntosList = new ArrayList<>(puntos.values());

            for (int i = 0; i < puntosList.size(); i++) {
                for (int j = i + 1; j < puntosList.size(); j++) {
                    Punto punto1 = puntosList.get(i);
                    Punto punto2 = puntosList.get(j);

                    int distancia = random.nextInt(10) + 1;

                    punto1.agregarConexion(punto2, distancia);
                    punto2.agregarConexion(punto1, distancia);
                }
            }
        }

        private int getRandomVariation() {
            return (int) (Math.random() * 30 - 15);
        }

        public Map<String, Punto> getPuntos() {
            return puntos;
        }

        public List<Punto> encontrarRutaMasCorta(String origen, String destino) {
            if (!puntos.containsKey(origen) || !puntos.containsKey(destino)) {
                return null; // Si el origen o el destino no existen en el mapa, devuelve null
            }

            Punto puntoOrigen = puntos.get(origen);
            Punto puntoDestino = puntos.get(destino);

            Map<Punto, Integer> distancias = new HashMap<>();
            Map<Punto, Punto> anteriores = new HashMap<>();
            PriorityQueue<Punto> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(distancias::get));

            // Inicializar distancias y anteriores
            for (Punto punto : puntos.values()) {
                distancias.put(punto, Integer.MAX_VALUE);
                anteriores.put(punto, null);
            }

            distancias.put(puntoOrigen, 0);
            colaPrioridad.add(puntoOrigen);

            while (!colaPrioridad.isEmpty()) {
                Punto actual = colaPrioridad.poll();

                for (Map.Entry<Punto, Integer> vecino : actual.getConexiones().entrySet()) {
                    Punto puntoVecino = vecino.getKey();
                    int nuevaDistancia = distancias.get(actual) + vecino.getValue();

                    if (nuevaDistancia < distancias.get(puntoVecino)) {
                        distancias.put(puntoVecino, nuevaDistancia);
                        anteriores.put(puntoVecino, actual);
                        colaPrioridad.add(puntoVecino);
                    }
                }
            }

            // Reconstruir la ruta desde el destino hasta el origen
            List<Punto> ruta = new ArrayList<>();
            for (Punto punto = puntoDestino; punto != null; punto = anteriores.get(punto)) {
                ruta.add(punto);
            }

            Collections.reverse(ruta); // Invertir la lista para que vaya desde el origen hasta el destino
            return ruta;
        }
    }

    private static class Punto {
        private String nombre;
        private int x, y;
        private Map<Punto, Integer> conexiones;

        public Punto(String nombre, int x, int y) {
            this.nombre = nombre;
            this.x = x;
            this.y = y;
            this.conexiones = new HashMap<>();
        }

        public void agregarConexion(Punto destino, int distancia) {
            conexiones.put(destino, distancia);
        }

        public String getNombre() {
            return nombre;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Map<Punto, Integer> getConexiones() {
            return conexiones;
        }
    }
}
