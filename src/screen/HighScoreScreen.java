package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import engine.Cooldown;
import engine.Core;
import engine.DatabaseConnect;
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

/**
 * Implements the high scores screen, it shows player records.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class HighScoreScreen extends Screen {

	/** List of past high scores. */
	private List<Score> highScores_EASY;
	private List<Score> highScores_NORMAL;
	private List<Score> highScores_HARD;
	private List<Score> highScores_HARDCORE;
	private int difficulty;
	private Cooldown SelectCooldown;
	private Connection conn;

	private ArrayList<String> ranking_easy = new ArrayList<>();
	private ArrayList<String> ranking_normal = new ArrayList<>();
	private ArrayList<String> ranking_hard = new ArrayList<>();
	private ArrayList<String> ranking_hardcore = new ArrayList<>();
	private DatabaseConnect dbconnect;
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
	public HighScoreScreen(final int width, final int height, final int fps) {
		super(width, height, 60);
		dbconnect = new DatabaseConnect();
		conn = dbconnect.connect();

		for(int difficulty = 0; difficulty < 4; difficulty++) {
			String query = "SELECT * FROM ranking(?)";
				try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
					preparedStatement.setInt(1, difficulty);

					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						List<String> stringList = new ArrayList<>();
						while (resultSet.next()) {
							int rank = resultSet.getInt("ranking");
							String id = resultSet.getString("id");
							String clientName = resultSet.getString("client_name");
							int score = resultSet.getInt("score");
							String resultString = "Rank: " + rank + ", ID: " + id + ", Name: " + clientName + ", Score: " + score;
							System.out.println("Rank: " + rank + ", ID: " + id + ", Name: " + clientName + ", Score: " + score);
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
				catch (SQLException e){
					e.printStackTrace();
				}

				// 호출할 함수와 매개변수를 포함한 쿼리


		}

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
		drawManager.drawHighScoreMenu(this);
		drawManager.drawDiffScore(this, this.difficulty);
		if (this.difficulty == 0)
			drawManager.drawHighScores(this, this.highScores_EASY);
		else if (this.difficulty == 1)
			drawManager.drawHighScores(this, this.highScores_NORMAL);
		else if (this.difficulty == 2)
			drawManager.drawHighScores(this, this.highScores_HARD);
		else
			drawManager.drawHighScores(this, this.highScores_HARDCORE);

		drawManager.completeDrawing(this);
	}
}
