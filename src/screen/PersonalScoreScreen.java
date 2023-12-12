package screen;

import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.List;

import engine.*;
import org.w3c.dom.DOMStringList;

import java.util.ArrayList;

public class PersonalScoreScreen extends Screen {

    /** List of past high scores. */
    private List<Score> highScores_EASY;
    private List<Score> highScores_NORMAL;
    private List<Score> highScores_HARD;
    private List<Score> highScores_HARDCORE;
    private int difficulty;
    private Cooldown SelectCooldown;

    // 데이터베이스 계정
    private DatabaseConnect dbConnect;
    private static Connection conn;

    // 개인 점수 열람할 계정

    private ArrayList<String> ranking_easy = new ArrayList<>();
    private ArrayList<String> ranking_normal = new ArrayList<>();
    private ArrayList<String> ranking_hard = new ArrayList<>();
    private ArrayList<String> ranking_hardcore = new ArrayList<>();
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
    public PersonalScoreScreen(final int width, final int height, final int fps, Connection conn, LoginManager loginManager) {
        super(width, height, 60);

        if(null == loginManager.get_id()){
            Title = null;
        }
        else{
            Title = "ID : " + loginManager.get_id() + " Name : " + loginManager.get_name();
        }

        for(int difficulty = 1; difficulty < 5; difficulty++) {
            String sql = "SELECT * FROM score WHERE difficulty = ? and id = ? order by score DESC";
            try(PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, difficulty);
                preparedStatement.setString(2, loginManager.get_id());
                ResultSet resultSet = preparedStatement.executeQuery();
                List<String> stringList = new ArrayList<>();
                while(resultSet.next()){
                    int score = resultSet.getInt("score");
                    String resultString = "Score : " + score;
                    stringList.add(resultString);
                }
                if (difficulty - 1 == 0) {
                        ranking_easy.addAll(stringList);
                } else if (difficulty - 1 == 1) {
                        ranking_normal.addAll(stringList);
                } else if (difficulty - 1 == 2) {
                        ranking_hard.addAll(stringList);
                } else {
                        ranking_hardcore.addAll(stringList);
                }

            }
            catch (SQLException e){
                e.printStackTrace();
            }

        }

        this.SelectCooldown = Core.getCooldown(200);
        this.SelectCooldown.reset();
        this.returnCode = 1;
        this.difficulty = 0;
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
        if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                && this.SelectCooldown.checkFinished()) {
            this.difficulty += 1;
            if (this.difficulty > 3)
                this.difficulty = 0;
            this.SelectCooldown.reset();
        }
        else if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
                && this.SelectCooldown.checkFinished()) {
            this.difficulty -= 1;
            if (this.difficulty < 0)
                this.difficulty = 3;
            this.SelectCooldown.reset();
        }
        else if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
                && this.inputDelay.checkFinished())
            this.isRunning = false;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);
        drawManager.drawTitle(this, Title);
        drawManager.drawDiffScore(this, this.difficulty);

        if (this.difficulty == 0)
            drawManager.drawRanking(this, this.ranking_easy);
        else if (this.difficulty == 1)
            drawManager.drawRanking(this, this.ranking_normal);
        else if (this.difficulty == 2)
            drawManager.drawRanking(this, this.ranking_hard);
        else
            drawManager.drawRanking(this, this.ranking_hardcore);

        drawManager.completeDrawing(this);
    }
}