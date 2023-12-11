package engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreManager {
    private Connection conn;
    private String id;
    public ScoreManager(Connection conn, LoginManager loginManager){
        id = loginManager.get_id();
        this.conn = conn;
    }


    //ScoreScreen에 savescore에 사용하면 데이터 베이스에 들어갈듯.
    public void scoreUpdate(int score, int difficulty){
        if("".equals(id) || id == null) {
            System.out.println("Not Login Yet");
            return;
        }
        System.out.println("=======================");
        System.out.println("Score update");
        System.out.println("=======================");
        PreparedStatement pSt;

        //esay
        try {
            pSt = conn.prepareStatement("insert into score values(?,?,?)");
            pSt.setString(1, id);
            pSt.setInt(2,score);
            pSt.setInt(3,difficulty+1);
            pSt.executeUpdate();
            pSt.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
