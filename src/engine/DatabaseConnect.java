package engine;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnect {
    Dotenv dotenv = Dotenv.load();
    String dbUrl = dotenv.get("DB_URL");
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    public Connection connect () {
        Connection conn = null ;
        try {
            conn = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
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
    public String getDbUrl(){ return dbUrl; }
    public String getDbUser(){ return  dbUser; }
    public String getDbPassword(){ return dbPassword;}
}
