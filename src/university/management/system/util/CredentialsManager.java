package university.management.system.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CredentialsManager {
    private static final String CONFIG_FILE = "src/university/management/system/config/credentials.properties";
    private static Properties properties;
    private static long lastModified = 0;

    static {
        properties = new Properties();
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            lastModified = new java.io.File(CONFIG_FILE).lastModified();
        } catch (IOException ex) {
            System.err.println("Error loading credentials: " + ex.getMessage());
        }
    }

    private static void checkForUpdates() {
        long currentModified = new java.io.File(CONFIG_FILE).lastModified();
        if (currentModified > lastModified) {
            loadProperties();
        }
    }

    public static boolean isValidUser(String username, String password) {
        checkForUpdates(); // Check if the file has been modified
        String storedPassword = properties.getProperty(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    public static void addUser(String username, String password) {
        checkForUpdates();
        properties.setProperty(username, password);
        // Note: To persist changes, you'd need to write back to the file
        // This is a simplified version that only keeps changes in memory
    }
}
