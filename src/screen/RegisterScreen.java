package screen;
import engine.*;
import entity.Coin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.List;
import java.sql.*;
import engine.LoginManager;
import engine.DatabaseConnect;
import security.SHA256;
public class RegisterScreen implements ActionListener{

    private Connection conn;

    private JFrame frame;
    private JPanel panel;
    private JLabel idLabel;
    private JLabel pwdLabel;
    private JLabel nameLabel;
    private JLabel countryLabel;
    private JTextField idInput;
    private JPasswordField pwdInput;
    private JTextField nameInput;
    private JTextField countryInput;
    private JButton registerButton;

    private RegisterManager registerManager;


    // id
    private String id;
    // password
    private String password;
    private String password_sha;
    private String name;
    private String country;

    private boolean framinfo = true;

    public RegisterScreen(Connection conn, RegisterManager registerManager) {
        this.registerManager = registerManager;
        this.conn = conn;
        frame = new JFrame();
        panel = new JPanel();
        idLabel = new JLabel("ID");
        pwdLabel = new JLabel("Password");
        nameLabel = new JLabel("user");
        countryLabel = new JLabel("country");
        idInput = new JTextField(id);
        pwdInput = new JPasswordField(password);
        nameInput = new JTextField(name);
        countryInput = new JTextField(country);
        registerButton = new JButton("Sign up");
        panel.setLayout(null);

        // Specify location of Components
        idLabel.setBounds(20, 10, 60, 30);
        pwdLabel.setBounds(20, 50, 60, 30);
        nameLabel.setBounds(20, 90, 60, 30);
        countryLabel.setBounds(20, 130, 60, 30);
        idInput.setBounds(100, 10, 80, 30);
        pwdInput.setBounds(100, 50, 80, 30);
        nameInput.setBounds(100, 90, 80, 30);
        countryInput.setBounds(100, 130, 80, 30);

        registerButton.setBounds(200, 70, 80,35);

        // Add an ActionListener to the Login Button
        registerButton.addActionListener(this);

        // Add Components to Panel
        panel.add(idLabel);
        panel.add(pwdLabel);
        panel.add(nameLabel);
        panel.add(countryLabel);
        panel.add(idInput);
        panel.add(pwdInput);
        panel.add(nameInput);
        panel.add(countryInput);
        panel.add(registerButton);
        // Add Panel to Frame
        frame.add(panel);

        frame.setTitle("Invaders Sign up");					// name on the top of the frame
        frame.setSize(320, 260);								// size of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// call System.exit() on closing
        frame.setVisible(framinfo);									// display the frame on the screen

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == registerButton){
            SHA256 sha256 = new SHA256();
            id = idInput.getText();
            password = new String(pwdInput.getPassword());
            name = nameInput.getText();
            country = countryInput.getText();
            try {
                password_sha = sha256.encrypt(password);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }

            //check about user can sign up our database
            if(conn != null){
                if(registerManager.join_membership(id, password_sha, name, country)){
                    System.out.println("sign up Success");
                    framinfo = false;
                    frame.setVisible(framinfo);
                }
                else{
                    System.out.println("sign up fail");
                }
            }
        }


    }


}