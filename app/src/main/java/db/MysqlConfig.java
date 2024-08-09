package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MysqlConfig {
    private static MysqlConfig instance;
    private static Properties properties;

    private MysqlConfig() throws IOException {
        properties = new Properties();
        InputStream input = new FileInputStream("app/src/main/java/application.properties");
        properties.load(input);
    }

    public static MysqlConfig getInstance() throws IOException {
        if (instance == null) {
            instance = new MysqlConfig();
        }
        return instance;
    }

    public Properties getProperties() {
        return properties;
    }
}
