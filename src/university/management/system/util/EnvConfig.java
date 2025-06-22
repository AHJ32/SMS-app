package university.management.system.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EnvConfig {
    private static final Properties properties = new Properties();
    private static final Map<String, String> userCredentials = new HashMap<>();
    private static long lastModified = 0;
    private static final String ENV_FILE = "config/.env";

    static {
        loadEnv();
    }

    private static void loadEnv() {
        try (InputStream input = new FileInputStream(ENV_FILE)) {
            properties.load(input);
            loadUserCredentials();
            lastModified = Paths.get(ENV_FILE).toFile().lastModified();
        } catch (IOException e) {
            System.err.println("Warning: Could not load .env file: " + e.getMessage());
            // Load default values
            properties.setProperty("ADMIN_USERNAME", "admin");
            properties.setProperty("ADMIN_PASSWORD", "admin123");
            properties.setProperty("USER_CREDENTIALS", "teacher:teacher123,student:student123");
            loadUserCredentials();
        }
    }

    private static void loadUserCredentials() {
        userCredentials.clear();
        // Add admin user
        userCredentials.put(
            properties.getProperty("ADMIN_USERNAME"),
            properties.getProperty("ADMIN_PASSWORD")
        );
        
        // Add other users
        String userCreds = properties.getProperty("USER_CREDENTIALS", "");
        if (!userCreds.isEmpty()) {
            for (String userPass : userCreds.split(",")) {
                String[] parts = userPass.trim().split(":");
                if (parts.length == 2) {
                    userCredentials.put(parts[0], parts[1]);
                }
            }
        }
    }

    private static void checkForUpdates() {
        long currentModified = Paths.get(ENV_FILE).toFile().lastModified();
        if (currentModified > lastModified) {
            loadEnv();
        }
    }

    public static boolean isValidUser(String username, String password) {
        checkForUpdates();
        String storedPassword = userCredentials.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    public static String get(String key) {
        checkForUpdates();
        return properties.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        checkForUpdates();
        return properties.getProperty(key, defaultValue);
    }
}
