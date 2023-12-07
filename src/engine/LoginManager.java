package engine;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginManager {

    private String id;
    private String password;
    private String name;
    private String country;

    public boolean loginCheck(Connection conn, String inputted_id , String inputted_password){
        System.out.println("=======================");
        System.out.println("Login");
        System.out.println("=======================");
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM client");
            String id_data = new String();
            String password_data = new String();
            if(rs.next()){
                id_data= rs.getString(1);
                password_data = rs.getString(2);
                if(inputted_id.equals(id_data) && inputted_password.equals(password_data)){
                    id=rs.getString(1);
                    password=rs.getString(2);
                    name=rs.getString(3);
                    country=rs.getString(4);
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

    public String get_id(){
        return id;
    }

    public String get_password(){
        return password;
    }

    public String get_name(){
        return name;
    }

    public String get_country(){
        return country;
    }

}
