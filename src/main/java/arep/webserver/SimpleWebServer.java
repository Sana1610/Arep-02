package arep.webserver;

import java.io.*;
import java.net.*;
import arep.webserver.routes.RouteManager;

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
        StaticFiles.setLocation(WEB_ROOT);

        RouteManager.get("/", (req, res) -> {
            // Redirigir a index.html
            return "<html><body><h1>Welcome to the Home Page</h1></body></html>";
        });

        RouteManager.get("/hello", (req, res) -> "Hello " + req.getValues("name"));
        RouteManager.get("/pi", (req, res) -> String.valueOf(Math.PI));

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
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ClientHandler(clientSocket));
            }
        }
    }
}

