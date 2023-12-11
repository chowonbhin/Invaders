package screen;

import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.List;

import engine.*;

import java.util.ArrayList;

public class FriendListScreen extends Screen {

    private Cooldown SelectCooldown;

    // 데이터베이스 계정
    private DatabaseConnect dbConnect;
    private static Connection conn;

    // 개인 점수 열람할 계정

    private ArrayList<String> friend_data = new ArrayList<>();

    private String Title = "";

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width
     *            Screen width.
     * @param height
     *            Screen height.
     * @param fps
     *            Frames per second, frame rate at which the game is run.
     */
    public FriendListScreen(final int width, final int height, final int fps, Connection conn,String user_id, FriendManager manager) {
        super(width, height, 60);
        ArrayList<String> getFriend=manager.getFriendData(conn, user_id);
        Title = "Friend";
        List<String> stringList = new ArrayList<>();
        for(int i =0 ;i<getFriend.size();i++) {
            try {
                PreparedStatement pSt = conn.prepareStatement("select is_online,last_online_time from client where id =?");
                pSt.setString(1,getFriend.get(i));
                ResultSet rs = pSt.executeQuery();
                rs.next();
                System.out.println(rs.getBoolean(1));
                String online;
                if(rs.getBoolean(1)==true){
                    online="online";
                }
                else{
                    online="offline";
                }
                String resultString = "friend : " +getFriend.get(i) + ", status : " +online;
                friend_data.add(resultString);
                resultString ="last_online_time : "+rs.getString(2);
                friend_data.add(resultString);
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //friend_data.addAll(stringList);
        this.SelectCooldown = Core.getCooldown(200);
        this.SelectCooldown.reset();
        this.returnCode = 42;

    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        return this.returnCode;
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final void update() {
        super.update();
        draw();
        if(this.SelectCooldown.checkFinished() && this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                this.isRunning = false;
            }
        }
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);
        drawManager.drawTitle(this, Title);
        drawManager.drawFriend(this,this.friend_data);
        drawManager.completeDrawing(this);
    }


}