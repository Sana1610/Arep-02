package arep.webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import arep.webserver.http.Request;
import arep.webserver.http.Response;
import arep.webserver.routes.Route;
import arep.webserver.routes.RouteManager;

class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private String webRoot;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.webRoot = webRoot;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream())) {

            String requestLine = in.readLine();
            if (requestLine == null) return;
            String[] tokens = requestLine.split(" ");
            String method = tokens[0];
            String path = tokens[1];

            if (method.equals("GET")) {
                handleGetRequest(path, out, dataOut);
            } else if (method.equals("POST")) {
                handlePostRequest(path, in, out);
            }

        } catch (IOException e) {
            System.err.println("Error en la conexión: " + e.getMessage());
        }
    }

    private void handlePostRequest(String fileRequested, BufferedReader in, PrintWriter out) throws IOException {
        // Leer headers
        while (!in.readLine().isEmpty());

        // Leer cuerpo
        StringBuilder payload = new StringBuilder();
        while (in.ready()) {
            payload.append((char) in.read());
        }

        // Guardar archivo
        String fileName = "post_data_" + System.currentTimeMillis() + ".txt";
        Path filePath = Paths.get(SimpleWebServer.WEB_ROOT, fileName);
        Files.write(filePath, payload.toString().getBytes());

        // Respuesta
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/plain");
        out.println();
        out.println("Archivo guardado: " + fileName);
    }

    private void handleGetRequest(String path, PrintWriter out, BufferedOutputStream dataOut) throws IOException {
        String[] pathAndQuery = path.split("\\?");
        String pathOnly = pathAndQuery[0];

        // Si la ruta es la raíz, servir index.html por defecto
        if (pathOnly.equals("/")) {
            pathOnly = "/index.html";
        }

        Request request = new Request("GET", pathOnly);

        if (pathAndQuery.length > 1) {
            String queryString = pathAndQuery[1];
            String[] params = queryString.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    request.addQueryParam(keyValue[0], keyValue[1]);
                }
            }
        }

        Route route = RouteManager.getRoute(pathOnly);
        if (route != null) {
            Response response = new Response(out);
            String result = route.handle(request, response);
            response.send(result);
        } else {
            StaticFiles.serveStaticFile(pathOnly, dataOut);
        }
    }

    private void serveStaticFile(String fileRequested, PrintWriter out, BufferedOutputStream dataOut) throws IOException {
        File file = new File(webRoot, fileRequested);
        if (file.exists()) {
            String contentType = getContentType(fileRequested);
            byte[] fileData = readFileData(file);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-type: " + contentType);
            out.println("Content-length: " + fileData.length);
            out.println();
            out.flush();
            dataOut.write(fileData, 0, fileData.length);
            dataOut.flush();
        } else {
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-type: text/html");
            out.println();
            out.flush();
            out.println("<html><body><h1>File Not Found</h1></body></html>");
            out.flush();
        }
    }

    private void saveToFile(String body, PrintWriter out) throws IOException {
        String fileName = "received_data.txt";
        try (FileWriter fileWriter = new FileWriter(new File(webRoot, fileName))) {
            fileWriter.write(body);
        }
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/plain");
        out.println();
        out.println("File created: " + fileName);
        out.flush();
    }

    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".html")) return "text/html";
        else if (fileRequested.endsWith(".css")) return "text/css";
        else if (fileRequested.endsWith(".js")) return "application/javascript";
        else if (fileRequested.endsWith(".png")) return "image/png";
        else if (fileRequested.endsWith(".jpg")) return "image/jpeg";
        return "text/plain";
    }

    private byte[] readFileData(File file) throws IOException {
        byte[] fileData = new byte[(int) file.length()];
        try (FileInputStream fileIn = new FileInputStream(file)) {
            fileIn.read(fileData);
        }
        return fileData;
    }

    private void sendNotFound(PrintWriter out, String filename) {
        out.println("HTTP/1.1 404 Not Found");
        out.println("Content-Type: text/html");
        out.println();
        out.println("<h1>404 - Archivo no encontrado</h1>");
        out.println("<p>" + filename + " no existe</p>");
        out.flush();
    }

    private void sendForbidden(PrintWriter out) {
        out.println("HTTP/1.1 403 Forbidden");
        out.println("Content-Type: text/html");
        out.println();
        out.println("<h1>403 - Acceso prohibido</h1>");
        out.flush();
    }

    private void sendBadRequest(PrintWriter out) {
        out.println("HTTP/1.1 400 Bad Request");
        out.println("Content-Type: text/html");
        out.println();
        out.println("<h1>400 - Solicitud inválida</h1>");
        out.flush();
    }

    private void sendInternalError(PrintWriter out) {
        out.println("HTTP/1.1 500 Internal Server Error");
        out.println("Content-Type: text/html");
        out.println();
        out.println("<h1>500 - Error interno del servidor</h1>");
        out.flush();
    }
}