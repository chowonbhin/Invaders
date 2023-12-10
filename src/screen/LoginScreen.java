package screen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.*;
import engine.LoginManager;
import engine.DatabaseConnect;
public class LoginScreen implements ActionListener{

    private Connection conn;

    private JFrame frame;
    private JPanel panel;
    private JLabel idLabel;
    private JLabel pwdLabel;
    private JTextField idInput;
    private JPasswordField pwdInput;
    private JButton loginButton;
    private JButton registerButton;

    private LoginManager loginManager;


    // id
    private String id;
    // password
    private String password;


    private DatabaseConnect databaseConnect;

    public LoginScreen() {

        frame = new JFrame();
        panel = new JPanel();
        idLabel = new JLabel("ID");
        pwdLabel = new JLabel("Password");
        idInput = new JTextField(id);
        pwdInput = new JPasswordField(password);
        loginButton = new JButton("Sign in");
        registerButton = new JButton("Sign up");
        panel.setLayout(null);

        // Specify location of Components
        idLabel.setBounds(20, 10, 60, 30);
        pwdLabel.setBounds(20, 50, 60, 30);
        idInput.setBounds(100, 10, 80, 30);
        pwdInput.setBounds(100, 50, 80, 30);
        loginButton.setBounds(200, 10,  80, 35);
        registerButton.setBounds(200, 50, 80,35);

        // Add an ActionListener to the Login Button
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        // Add Components to Panel
        panel.add(idLabel);
        panel.add(pwdLabel);
        panel.add(idInput);
        panel.add(pwdInput);
        panel.add(loginButton);
        panel.add(registerButton);
        // Add Panel to Frame
        frame.add(panel);

        frame.setTitle("Invaders Login");					// name on the top of the frame
        frame.setSize(320, 130);								// size of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// call System.exit() on closing
        frame.setVisible(true);									// display the frame on the screen

    }

    //Update user status
    private void updateIsOnline(Connection conn, String userId, boolean isOnline) {
        String sql = "UPDATE Client SET is_online = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, isOnline);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == loginButton){
            id = idInput.getText();
            password = new String(pwdInput.getPassword());
            conn = databaseConnect.connect();

            //check about user can login our database
            if(conn != null){
                if(loginManager.loginCheck(conn, id, password)){
                    System.out.println("login Success");
                    updateIsOnline(conn, id, true);
                }
                else{
                    System.out.println("login fail");
                }
            }
        }
        //sign up to our database
        else if(e.getSource() == registerButton){
            RegisterScreen registerScreen = new RegisterScreen();
        }

    }


}