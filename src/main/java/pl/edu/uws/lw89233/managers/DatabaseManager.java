package pl.edu.uws.lw89233.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseManager {

    private Connection dbConnection;
    private final String DB_HOST;
    private final String DB_PORT;
    private final String DB_NAME;
    private final String DB_USER;
    private final String DB_PASSWORD;

    public DatabaseManager(String DB_HOST, String DB_PORT, String DB_NAME, String DB_USER, String DB_PASSWORD) {
        this.DB_HOST = DB_HOST;
        this.DB_PORT = DB_PORT;
        this.DB_NAME = DB_NAME;
        this.DB_USER = DB_USER;
        this.DB_PASSWORD = DB_PASSWORD;

        connectWithRetries();
    }

    public void connectWithRetries() {
        String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
        int maxRetries = 5;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {

                this.dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                System.out.println("Pomyślnie połączono z bazą danych: " + DB_NAME);
                return;

            } catch (SQLException e) {

                retryCount++;
                System.err.println("Błąd przy połączeniu z bazą danych (próba " + retryCount + "/" + maxRetries + "): " + e.getMessage());

                if (retryCount < maxRetries) {
                    try {

                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        System.err.println("Nie udało się nawiązać połączenia z bazą danych po " + maxRetries + " próbach.");
    }

    public Connection getConnection() {
        return dbConnection;
    }
}