package arep.webserver;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Base64;
import java.util.concurrent.*;

public class SimpleWebServer {
    private static final int PORT = 35000;
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator
            + "src" + File.separator
            + "main" + File.separator
            + "resources" + File.separator;

    public static void main(String[] args) throws IOException {
        // Crear directorio si no existe
        File webRootDir = new File(WEB_ROOT);
        if (!webRootDir.exists()) {
            if (!webRootDir.mkdirs()) {
                System.err.println("No se pudo crear el directorio: " + WEB_ROOT);
                return;
            }
        }

        // Verificar permisos
        if (!webRootDir.canRead()) {
            System.err.println("No hay permisos de lectura en: " + WEB_ROOT);
            return;
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado en http://localhost:" + PORT);
            System.out.println("Directorio base: " + WEB_ROOT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ClientHandler(clientSocket));
            }
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream())) {

            String requestLine = in.readLine();
            if (requestLine == null) return;

            String[] tokens = requestLine.split(" ");
            if (tokens.length < 2) {
                sendBadRequest(out);
                return;
            }

            String method = tokens[0];
            String fileRequested = tokens[1].replaceFirst("^/", "");  // Eliminar slash inicial

            // Redirigir directorio raíz a index.html
            if (fileRequested.isEmpty() || fileRequested.endsWith("/")) {
                fileRequested += "index.html";
            }

            if (method.equals("GET")) {
                handleGetRequest(fileRequested, out, dataOut);
            } else if (method.equals("POST")) {
                handlePostRequest(fileRequested, in, out);
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

    private void handleGetRequest(String fileRequested, PrintWriter out, BufferedOutputStream dataOut) {
        try {
            File file = new File(SimpleWebServer.WEB_ROOT, fileRequested);
            System.out.println("Solicitado: " + file.getAbsolutePath());

            if (!file.exists()) {
                sendNotFound(out, fileRequested);
                return;
            }

            if (file.isDirectory()) {
                sendForbidden(out);
                return;
            }

            if (!file.canRead()) {
                sendForbidden(out);
                return;
            }

            String contentType = getContentType(fileRequested);
            byte[] fileData = Files.readAllBytes(file.toPath());

            // Manejo especial para imágenes
            if (contentType.startsWith("image/")) {
                String base64Image = Base64.getEncoder().encodeToString(fileData);
                String htmlResponse = "<!DOCTYPE html>\r\n"
                        + "<html>\r\n"
                        + "    <head>\r\n"
                        + "        <title>Imagen</title>\r\n"
                        + "    </head>\r\n"
                        + "    <body>\r\n"
                        + "         <center><img src=\"data:" + contentType + ";base64," + base64Image + "\" alt=\"image\"></center>\r\n"
                        + "    </body>\r\n"
                        + "</html>";

                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println();
                out.println(htmlResponse);
            } else {
                // Enviar headers
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: " + contentType);
                out.println("Content-Length: " + fileData.length);
                out.println();

                // Enviar contenido
                dataOut.write(fileData);
                dataOut.flush();
            }

        } catch (IOException e) {
            sendInternalError(out);
        }
    }

    private String getContentType(String filename) {
        if (filename.endsWith(".html")) return "text/html";
        if (filename.endsWith(".css")) return "text/css";
        if (filename.endsWith(".js")) return "application/javascript";
        if (filename.endsWith(".png")) return "image/png";
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
        return "text/plain";
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