package poneytoponey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Directory {
    private final String host;

    public Directory(String host) {
        // "http://localhost:8080"
        // ou "http://192.168.1.10:8080"
        if (host.endsWith("/")) {
            this.host = host.substring(0, host.length() - 1);
        } else {
            this.host = host;
        }
    }

    public List<Entry> list() {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(host + "/list");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = connection.getResponseCode();

            if (status == 204) {
                return new ArrayList<>();
            }

            if (status != 200) {
                throw new RuntimeException("list failed with HTTP status " + status);
            }

            String body = readStream(connection.getInputStream());
            List<Entry> result = new ArrayList<>();

            if (body.isBlank()) {
                return result;
            }

            String[] lines = body.split("\\R");
            for (String line : lines) {
                String[] parts = line.split("\\s*/\\s*", 2);
                if (parts.length == 2) {
                    result.add(new Entry(parts[0].trim(), parts[1].trim()));
                }
            }

            return result;

        } catch (IOException e) {
            throw new RuntimeException("Could not list directory entries", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void join(Entry entry) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(host + "/register");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

            byte[] body = entry.username().getBytes(StandardCharsets.UTF_8);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(body);
            }

            int status = connection.getResponseCode();

            if (status != 201) {
                throw new RuntimeException("join failed with HTTP status " + status);
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not join directory", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void leave(Entry entry) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(host + "/remove");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // le serveur ignore le contenu et supprime selon l'IP du client
            try (OutputStream os = connection.getOutputStream()) {
                os.write(new byte[0]);
            }

            int status = connection.getResponseCode();

            if (status != 200) {
                throw new RuntimeException("leave failed with HTTP status " + status);
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not leave directory", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;

            while ((line = reader.readLine()) != null) {
                if (!first) {
                    sb.append("\n");
                }
                sb.append(line);
                first = false;
            }
        }

        return sb.toString();
    }
}
