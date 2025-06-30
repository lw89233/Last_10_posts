package pl.edu.uws.lw89233.managers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class EnvManager {

    private static final Properties properties = loadEnvVariablesFromFile(".env");

    private EnvManager() {
    }

    private static Properties loadEnvVariablesFromFile(String envFilePath) {
        Properties props = new Properties();
        try (InputStream envFile = new FileInputStream(envFilePath)) {
            props.load(envFile);
        } catch (IOException e) {
            System.err.println("Błąd przy ładowaniu pliku konfiguracyjnego: " + e.getMessage());
        }

        return props;
    }

    public static String getEnvVariable(String key) {
        return properties.getProperty(key);
    }
}
