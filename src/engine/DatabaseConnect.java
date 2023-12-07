package engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseConnect {
    Map<String, String> env = System.getenv();
    private String dbHost = env.get("DB_HOST");
    private String dbUser = env.get("DB_USER");
    private String dbPassword = env.get("DB_PASSWORD");
    public Connection connect () {
        Connection conn = null ;
        try {
            conn = DriverManager.getConnection(dbHost,dbUser,dbPassword);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println("fail connect");
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void disconnect(Connection conn){
        try {
            conn.close();
            System.out.println("Connection is closed successfully.");
        } catch (SQLException e ){
            System.out.println(e.getMessage());
        }
    }
}
