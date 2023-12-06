package engine;

import java.sql.*;

public class LoginManager {


    // client들의 닉네임 출력
    public void displayPlayer(Connection conn){
        System.out.println("=======================");
        System.out.println("Player");
        System.out.println("=======================");
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM client");
            while(rs.next()){
                System.out.println("player: "+rs.getString(3));
            }
            rs.close();
            st.close();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    //id 중복 체크 중복된다면 true 중복되지 않으면 false 반환
    public boolean idDuplicationCheck(Connection conn, String inputted_id){
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM client");
            String id_data = new String();
            while(rs.next()){
                id_data= rs.getString(1);
                if(inputted_id.equals(id_data)){
                    return true;
                }
            }
            rs.close();
            st.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    //client_name 중복 체크 중복시 true 중복되지 않으면 false
    public boolean nameDuplicationCheck(Connection conn, String inputted_name){
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM client");
            String name_data = new String();
            while(rs.next()){
                name_data= rs.getString(3);
                if(inputted_name.equals(name_data)){
                    return true;
                }
            }
            rs.close();
            st.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    //회원가입 정보 database에 추가 성공시 true 실패시 false
    public boolean join_membership(
            Connection conn,
            String inputted_id,String inputted_password ,String inputted_name,String inputted_country){

        if(!this.nameDuplicationCheck(conn,inputted_name) && !this.idDuplicationCheck(conn,inputted_id)){
            try{
                PreparedStatement pSt = conn.prepareStatement("insert into client values(?,?,?,?)");
                pSt.setString(1,inputted_id);
                pSt.setString(2,inputted_password);
                pSt.setString(3,inputted_name);
                pSt.setString(4,inputted_country);
                pSt.executeUpdate();
                return true;
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return false;
    }


    //로그인 실패시 fail 문자열 출력
    public String loginCheck(Connection conn, String id , String password){
        System.out.println("=======================");
        System.out.println("Login");
        System.out.println("=======================");
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM client");
            String id_data = new String();
            String password_data = new String();
            while(rs.next()){
                id_data= rs.getString(1);
                password_data = rs.getString(2);
                if(id.equals(id_data) && password.equals(password_data)){
                    return rs.getString(3);
                }
            }
            rs.close();
            st.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return "fail";
    }

    public static void main(String[] args) {
        DatabaseConnect data = new DatabaseConnect();
        Connection conn = data.connect();
        LoginManager login = new LoginManager();
        if(login.join_membership(conn,"1","2","3","4")){
            System.out.println("success");
        }
    }
}