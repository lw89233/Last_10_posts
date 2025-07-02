package pl.edu.uws.lw89233;

import pl.edu.uws.lw89233.managers.DatabaseManager;
import pl.edu.uws.lw89233.managers.MessageManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Last_10_posts {

    private final int PORT = Integer.parseInt(System.getenv("LAST_10_POSTS_MICROSERVICE_PORT"));
    private final String DB_HOST = System.getenv("DB_HOST");
    private final String DB_PORT = System.getenv("DB_PORT");
    private final String DB_NAME = System.getenv("DB_NAME");
    private final String DB_USER = System.getenv("DB_USER");
    private final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    private final DatabaseManager dbManager = new DatabaseManager(DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD);

    public void startService() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Last 10 Posts microervice is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Error starting Last 10 Posts microservice: " + e.getMessage());
        }
    }

    private class ClientHandler extends Thread {

        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request = in.readLine();
                if (request != null && request.contains("retrive_last_10_posts_request")) {
                    String response = handleRetriveLast10Posts(request);
                    out.println(response);
                }
            } catch (IOException e) {
                System.err.println("Error handling client request: " + e.getMessage());
            }
        }

        private String handleRetriveLast10Posts(String request) {
            MessageManager responseManager = new MessageManager(request);
            String sql = "SELECT p.id, p.content, p.created_at, u.login "
                    + "FROM posts p "
                    + "JOIN users u ON p.user_id = u.id "
                    + "ORDER BY p.created_at DESC "
                    + "LIMIT 10";
            StringBuilder response = new StringBuilder("type:retrive_last_10_posts_response#message_id:");
            response.append(responseManager.getAttribute("message_id")).append("#");
            response.append("posts:");

            try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql);
                 ResultSet resultSet = stmt.executeQuery()) {
                boolean first = true;

                while (resultSet.next()) {
                    if (!first) {
                        response.append("&");
                    }
                    response.append("post_id=").append(resultSet.getInt("id"))
                            .append(";content=").append(resultSet.getString("content"))
                            .append(";author=").append(resultSet.getString("login"))
                            .append(";created_at=").append(resultSet.getString("created_at"));
                    first = false;
                }
                response.append("#status:200#");
            } catch (SQLException e) {
                System.err.println("Error retrieving last 10 posts: " + e.getMessage());
                response.append("status:400#");
            }
            return response.toString();
        }
    }

    public static void main(String[] args) {
        new Last_10_posts().startService();
    }
}