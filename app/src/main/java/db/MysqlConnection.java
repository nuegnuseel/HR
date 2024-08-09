package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

public class MysqlConnection {
    private static MysqlConnection instance;
    private static ConcurrentLinkedQueue<Connection> connections;
    private MysqlConnection() {
        connections = new ConcurrentLinkedQueue<>();
    }
    public static MysqlConnection from(MysqlConfig mysqlConfig) throws IOException {
        if(instance == null) {
            instance = new MysqlConnection();
            Properties properties = mysqlConfig.getInstance().getProperties();
            int size = Integer.parseInt(properties.getProperty("db.poolsize"));
            IntStream.range(0, size).forEach(i -> {
                try {
                    connections.add(DriverManager
                            .getConnection(
                                    properties.getProperty("db.url"),
                                    properties.getProperty("db.username"),
                                    properties.getProperty("db.password")));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return instance;
    }
    public Connection getConnection() {
        return connections.poll();
    }

    public void retrieveConnection(Connection connection) {
        connections.add(connection);
    }

    public void clear() {
        connections.forEach(connection -> {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
