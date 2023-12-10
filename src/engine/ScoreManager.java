package engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreManager {
    private DatabaseConnect dbconnect;
    private Connection conn;
    private String id;
    public ScoreManager(){
        dbconnect = new DatabaseConnect();
        conn = dbconnect.connect();

    }


    //ScoreScreen에 savescore에 사용하면 데이터 베이스에 들어갈듯.
    public void scoreUpdate(int score, int difficulty){
        System.out.println("=======================");
        System.out.println("Score update");
        System.out.println("=======================");
        PreparedStatement pSt;
        //esay
        if(difficulty==0){
            try {
                pSt = conn.prepareStatement("insert into score values(?,?,?)");
                pSt.setString(1,id);
                pSt.setInt(2,score);
                pSt.setInt(3,difficulty+1);
                pSt.executeUpdate();
                pSt.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        //noraml
        if(difficulty==1){
            try {
                pSt = conn.prepareStatement("insert into score values(?,?,?)");
                pSt.setString(1,id);
                pSt.setInt(2,score);
                pSt.setInt(3,difficulty+1);
                pSt.executeUpdate();
                pSt.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        //hard
        if(difficulty==2){
            try {
                pSt = conn.prepareStatement("insert into score values(?,?,?)");
                pSt.setString(1,id);
                pSt.setInt(2,score);
                pSt.setInt(3,difficulty+1);
                pSt.executeUpdate();
                pSt.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        //hardcore
        if(difficulty==3){
            try {
                pSt = conn.prepareStatement("insert into score values(?,?,?)");
                pSt.setString(1,id);
                pSt.setInt(2,score);
                pSt.setInt(3,difficulty+1);
                pSt.executeUpdate();
                pSt.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }

    }
}
