package directory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class DirectoryServer {

    // username -> ip
    private static final Map<String, String> directory = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        int port = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/list", DirectoryServer::handleList);
        server.createContext("/register", DirectoryServer::handleRegister);
        server.createContext("/remove", DirectoryServer::handleRemove);

        server.setExecutor(Executors.newFixedThreadPool(4));
        server.start();

        String ip = InetAddress.getLocalHost().getHostAddress();
        System.out.println("Server started on http://" + ip + ":" + port);
    }

    // ---------------- LIST ----------------

    private static void handleList(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        if (directory.isEmpty()) {
            exchange.sendResponseHeaders(204, -1); // No Content
            return;
        }

        StringBuilder response = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : directory.entrySet()) {
            if (!first)
                response.append("\n");
            response.append(entry.getKey())
                    .append(" / ")
                    .append(entry.getValue());
            first = false;
        }

        sendText(exchange, 200, response.toString());
    }

    // ---------------- REGISTER ----------------

    private static void handleRegister(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String username = readBody(exchange.getRequestBody()).trim();
        if (username.isEmpty()) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        String ip = exchange.getRemoteAddress().getAddress().getHostAddress();

        // username unique
        if (directory.containsKey(username)) {
            exchange.sendResponseHeaders(409, -1);
            return;
        }

        // ip unique
        if (directory.containsValue(ip)) {
            exchange.sendResponseHeaders(409, -1);
            return;
        }

        directory.put(username, ip);

        String response = username + " / " + ip;
        sendText(exchange, 201, response);
    }

    // ---------------- REMOVE ----------------

    private static void handleRemove(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String ip = exchange.getRemoteAddress().getAddress().getHostAddress();

        String usernameToRemove = null;

        for (Map.Entry<String, String> entry : directory.entrySet()) {
            if (entry.getValue().equals(ip)) {
                usernameToRemove = entry.getKey();
                break;
            }
        }

        if (usernameToRemove == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        directory.remove(usernameToRemove);

        String response = usernameToRemove + " / " + ip;
        sendText(exchange, 200, response);
    }

    // ---------------- UTILS ----------------

    private static String readBody(InputStream is) throws IOException {
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private static void sendText(HttpExchange exchange, int status, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}