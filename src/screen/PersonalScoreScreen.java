package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import engine.Cooldown;
import engine.Core;
import engine.Score;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class PersonalScoreScreen extends Screen {

    /** List of past high scores. */
    private List<Score> highScores_EASY;
    private List<Score> highScores_NORMAL;
    private List<Score> highScores_HARD;
    private List<Score> highScores_HARDCORE;
    private int difficulty;
    private Cooldown SelectCooldown;

    // 데이터베이스 계정
    private final String url = "jdbc:postgresql://localhost/invader";
    private final String user = "user";
    private final String password = "password";

    // 개인 점수 열람할 계정
    String userId = "user13";

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
    public PersonalScoreScreen(final int width, final int height, final int fps) {
        super(width, height, 60);

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            for(int difficulty = 0; difficulty < 4; difficulty++) {
                String query = "SELECT RANK() OVER (ORDER BY s.score DESC) :: int AS ranking, " +
                        "s.id AS client_id, c.client_name, s.score " +
                        "FROM score s " +
                        "JOIN client c ON s.id = c.id " +
                        "WHERE s.id = ? AND s.difficulty = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, userId);
                    preparedStatement.setInt(2, difficulty);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        List<String> stringList = new ArrayList<>();
                        while (resultSet.next()) {
                            int ranking = resultSet.getInt("ranking");
                            String clientId = resultSet.getString("client_id");
                            String clientName = resultSet.getString("client_name");
                            int score = resultSet.getInt("score");
                            Title = "ID: " + clientId + "  NAME: " + clientName;
                            String resultString = "Ranking: " + ranking + ", Score: " + score;
                            //System.out.println("Ranking: " + ranking + ", Score: " + score);
                            stringList.add(resultString);
                        }
                        if (difficulty == 0){
                            ranking_easy.addAll(stringList);
                        } else if (difficulty == 1) {
                            ranking_normal.addAll(stringList);
                        } else if (difficulty == 2) {
                            ranking_hard.addAll(stringList);
                        } else if (difficulty == 3) {
                            ranking_hardcore.addAll(stringList);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

