package screen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import engine.FriendManager;

public class FriendAddScreen implements ActionListener{

    private Connection conn;

    private JFrame frame;
    private JPanel panel;
    private JLabel friend_idLabel;
    private JTextField friend_idInput;
    private JButton addButton;
    private JButton deleteButton;

    private FriendManager friendManager;

    private String friend_id;
    private String my_id;
    private boolean frameinfo = true;

    public FriendAddScreen(Connection conn, FriendManager friendManager, String my_id)  {
        this.friendManager = friendManager;
        this.conn = conn;
        this.my_id = my_id;
        frame = new JFrame();
        panel = new JPanel();
        friend_idLabel = new JLabel("Friend ID");
        friend_idInput = new JTextField(friend_id);
        addButton = new JButton("Friend Add");
        panel.setLayout(null);
        // Specify location of Components
        friend_idLabel.setBounds(20, 10, 60, 30);
        friend_idInput.setBounds(100, 10, 80, 30);
        addButton.setBounds(200, 10,  80, 35);

        // Add an ActionListener to the Login Button
        addButton.addActionListener(this);

        // Add Components to Panel
        panel.add(friend_idLabel);
        panel.add(friend_idInput);
        panel.add(addButton);
        // Add Panel to Frame
        frame.add(panel);

        frame.setTitle("Friend Add");					// name on the top of the frame
        frame.setSize(320, 100);								// size of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// call System.exit() on closing
        frame.setVisible(frameinfo);									// display the frame on the screen

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addButton){
            friend_id = friend_idInput.getText();
            frameinfo = false;
            frame.setVisible(frameinfo);
            friendManager.insertFriend(my_id, friend_id);
        }

    }


}