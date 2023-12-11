package engine;

import screen.FriendAddScreen;
import screen.FriendDeleteScreen;


import java.sql.*;
import java.util.ArrayList;

public class FriendManager {
    private String my_id;
    private Connection conn;
    public FriendManager(Connection conn){
        this.conn = conn;
    }

    public String insertFriend( String my_id , String friend_id) {

        PreparedStatement pSt;

        try {
            pSt = conn.prepareStatement("select * from friend_list where user_id = ? and friend_id = ? ");
            pSt.setString(1, my_id);
            pSt.setString(2, friend_id);

            ResultSet rs = pSt.executeQuery();
            if(!rs.next()){
                pSt = conn.prepareStatement("select * from client where id = ?");
                pSt.setString(1, friend_id);
                rs = pSt.executeQuery();
                if(!rs.next()){
                    return "id does not exist.";
                }
                else{
                    pSt = conn.prepareStatement("insert into friend_list values(?,?)");
                    pSt.setString(1, my_id);
                    pSt.setString(2, friend_id);
                    pSt.executeUpdate();
                    return "done adding friend.";
                }
            }
            else{
                return "already a friend.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Please check the database connection status.";
    }

    public String deleteFriend(String my_id , String friend_id) {

        PreparedStatement pSt;

        try {
            pSt = conn.prepareStatement("select * from friend_list where user_id = ? and friend_id = ? ");
            pSt.setString(1, my_id);
            pSt.setString(2, friend_id);

            ResultSet rs = pSt.executeQuery();
            if(rs.next()){
                pSt = conn.prepareStatement("delete from friend_list where user_id = ? and friend_id = ?");
                pSt.setString(1, my_id);
                pSt.setString(2, friend_id);
                pSt.executeUpdate();
                pSt = conn.prepareStatement("delete from friend_list where user_id = ? and friend_id = ?");
                pSt.setString(1, friend_id);
                pSt.setString(2, my_id);
                pSt.executeUpdate();
                return "complete friend delete.";
            }
            else{
                return "id is not in the friend list.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Please check the database connection status.";

    }

    public ArrayList<String> getFriendData(Connection conn, String my_id){
        ArrayList<String> friendData = new ArrayList<String>();

        try{
            PreparedStatement pSt = conn.prepareStatement("select friend_id from friend_list where user_id=?");
            pSt.setString(1,my_id);
            ResultSet rs =pSt.executeQuery();
            while(rs.next()){
                friendData.add(rs.getString(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return friendData;
    }
    public void getaddfriend(String my_id){
        FriendAddScreen friendManageScreen = new FriendAddScreen(conn, this, my_id);
    }
    public void getdeletefriend(String my_id){
        FriendDeleteScreen friendManageScreen = new FriendDeleteScreen(conn, this, my_id);
    }

}
