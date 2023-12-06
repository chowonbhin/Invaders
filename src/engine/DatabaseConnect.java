package engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnect {
    private  final String url = "jdbc:postgresql://localhost:5432/invaders";
    private  final String user = "postgres";
    private  final String password = "89456951asd!";

    public Connection connect () {
        Connection conn = null ;
        try {
            conn = DriverManager.getConnection(url,user,password);
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
