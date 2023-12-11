package engine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage; // monster animation on a loading box
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import entity.Coin;
import entity.Entity;
import screen.GameScreen;
import screen.Screen;


/**
 * Manages screen drawing.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public final class DrawManager {

	/** Singleton instance of the class. */
	private static DrawManager instance;
	/** Current frame. */
	private static Frame frame;
	/** FileManager instance. */
	private static FileManager fileManager;
	/** Application logger. */
	private static Logger logger;
	/** Graphics context. */
	private static Graphics graphics;
	/** Buffer Graphics. */
	private static Graphics backBufferGraphics;
	/** Buffer image. */
	private static BufferedImage backBuffer;
	/** Normal sized font. */
	private static Font fontSmall;
	/** Normal sized font properties. */
	private static FontMetrics fontSmallMetrics;
	/** Normal sized font. */
	private static Font fontRegular;
	/** Normal sized font properties. */
	private static FontMetrics fontRegularMetrics;
	/** Big sized font. */
	private static Font fontBig;
	/** Big sized font properties. */
	private static FontMetrics fontBigMetrics;

	private  static Font fontVeryBig;
	public Cooldown endTimer = new Cooldown(3000);
	public long ghostTImer;
	public int ghostPostionX;
	public int ghostPostionY;

	public Color ghostColor = new Color(1, 1, 1);
	/** Cooldown timer for background animation. */
	private Cooldown bgTimer = new Cooldown(100);  // Draw bg interval
	private int brightness = 0;  // Used as RGB values for changing colors
	private int lighter = 1;  // For color to increase then decrease
	private Cooldown bgTimer_init = new Cooldown(3000);  // For white fade in at game start
	private Cooldown bgTimer_lines = new Cooldown(100);  // For bg line animation
	private int lineConstant = 0;  // For bg line animation
	private Coin coin;
	/** Sprite types mapped to their images. */
	private static Map<SpriteType, boolean[][]> spriteMap;
	private boolean initialSound = true;
	public boolean initialSound2 = true;
	private boolean isAfterLoading = false;



	private CountUpTimer timer;
	public int timercount = 0;
	public String rewardTypeString;
	public GameScreen gamescreen;

	//BufferedImage img1, img2, img3, img4;


	public int vector_x= 200, vector_y= 200, directionX = new Random().nextBoolean() ? 1 : -1,
			directionY = new Random().nextBoolean() ? 1 : -1;
	public Cooldown pump = new Cooldown(1000);

	boolean isFirst = true;

	int bigger = 36, direction = 1;
	public String getRandomCoin;

	/** Sprite types. */
	public static enum SpriteType {
		/** Player ship. */
		ShipA,
		ShipB,
		ShipC,
		ShipD,
		ShipE,
		ShipF,
		ShipG,
		/** Destroyed player ship. */
		ShipADestroyed,
		ShipBDestroyed,
		ShipCDestroyed,
		ShipDDestroyed,
		ShipEDestroyed,
		ShipFDestroyed,
		ShipGDestroyed,
		/** Player bullet. */
		Bullet,
		/** Player bulletY. */
		BulletY,
		/** Enemy bullet. */
		EnemyBullet,
		/** Enemy bullet goes left diag. */
		EnemyBulletLeft,
		/** Enemy bullet goes right diag. */
		EnemyBulletRight,
		/** First enemy ship normal - first form. */
		ESnA_1,
		/** First enemy ship normal - second form. */
		ESnA_2,
		/** Second enemy ship normal - first form. */
		ESnB_1,
		/** Second enemy ship normal - second form. */
		ESnB_2,
		/** Third enemy ship normal - first form. */
		ESnC_1,
		/** Third enemy ship normal - second form. */
		ESnC_2,
		/** Enemy ship mod1 damaged - first form. */
		ESm1_1D,
		/** Enemy ship mod1 damaged - second form. */
		ESm1_2D,
		/** Enemy ship mod1 - first form. */
		ESm1_1,
		/** Enemy ship mod1 - second form. */
		ESm1_2,
		/** First enemy ship mod2 - first form. */
		ESm2A_1,
		/** First enemy ship mod2 - second form. */
		ESm2A_2,
		/** First enemy ship mod2 (hit 1) - third form. */
		ESm2A_1D1,
		/** First enemy ship mod2 (hit 1) - forth form. */
		ESm2A_2D1,
		/** First enemy ship mod2 (hit 2) - fifth form. */
		ESm2A_1D2,
		/** First enemy ship mod2 (hit 2)- sixth form. */
		ESm2A_2D2,
		/** Second enemy ship mod2 - first form. */
		ESm2B_1,
		/** Second enemy ship mod2 - second form. */
		ESm2B_2,
		/** Second enemy ship mod2 (hit 1) - third form. */
		ESm2B_1D1,
		/** Second enemy ship mod2 (hit 1) - forth form. */
		ESm2B_2D1,
		/** Second enemy ship mod2 (hit 2) - fifth form. */
		ESm2B_1D2,
		/** Second enemy ship mod2 (hit 2)- sixth form. */
		ESm2B_2D2,
		/** Bonus ship1. */
		EnemyShipSpecial1,
		/** Bonus ship2. */
		EnemyShipSpecial2,
		/** Bonus ship3. */
		EnemyShipSpecial3,
		/** Bonus ship4. */
		EnemyShipSpecial4,
		/** Boss ship - first form. */
		BossA1,
		/** Boss ship - second form. */
		BossA2,
		/** Destroyed enemy ship. */
		Explosion,

		/** random sprit**/
		Trash1,
		Trash2,
		Trash3,
		Trash4,

		BulletLine,
		/** Destroyed enemy ship2. */
		Explosion2,
		/** Destroyed enemy ship3. */
		Explosion3,
		/** Destroyed enemy ship4. */
		Explosion4,
		/** Buff_item dummy sprite*/
		Buff_Item,
		/** Debuff_item dummy sprite */
		Debuff_Item,
		/** Laser */
		Laser,
		/** Laserline */
		LaserLine,
		Coin,
		BlueEnhanceStone,
		PerpleEnhanceStone,
		ShipAShileded,
		ShipBShileded,
		ShipCShileded,
		EnhanceStone,
		//ShipCShileded,
		gravestone,
		Ghost;
	};


	/**
	 * Private constructor.
	 */
	private DrawManager() {
		fileManager = Core.getFileManager();
		logger = Core.getLogger();
		logger.info("Started loading resources.");

		try {
			Random random = new Random();
			int Trash_enemyA = random.nextInt(3);
			spriteMap = new LinkedHashMap<SpriteType, boolean[][]>();
			spriteMap.put(SpriteType.ShipA, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipB, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipC, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipD, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipE, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipF, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipG, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipADestroyed, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipBDestroyed, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipCDestroyed, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipDDestroyed, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipEDestroyed, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipFDestroyed, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipGDestroyed, new boolean[13][8]);
			spriteMap.put(SpriteType.Bullet, new boolean[3][5]);
			spriteMap.put(SpriteType.BulletY, new boolean[5][7]);
			spriteMap.put(SpriteType.EnemyBullet, new boolean[3][5]);
			spriteMap.put(SpriteType.EnemyBulletLeft, new boolean[3][5]);
			spriteMap.put(SpriteType.EnemyBulletRight, new boolean[3][5]);
			if (Trash_enemyA == 0){
				spriteMap.put(SpriteType.ESnA_1, new boolean[12][8]);
				spriteMap.put(SpriteType.ESnA_2, new boolean[12][8]);
				spriteMap.put(SpriteType.Trash1, new boolean[12][8]);
				spriteMap.put(SpriteType.Trash2, new boolean[12][8]);
				spriteMap.put(SpriteType.Trash3, new boolean[12][8]);
				spriteMap.put(SpriteType.Trash4, new boolean[12][8]);
			}
			else if (Trash_enemyA == 1){
				spriteMap.put(SpriteType.Trash1, new boolean[12][8]);
				spriteMap.put(SpriteType.Trash2, new boolean[12][8]);
				spriteMap.put(SpriteType.ESnA_1, new boolean[12][8]);
				spriteMap.put(SpriteType.ESnA_2, new boolean[12][8]);
				spriteMap.put(SpriteType.Trash3, new boolean[12][8]);
				spriteMap.put(SpriteType.Trash4, new boolean[12][8]);
			}
			else{
				spriteMap.put(SpriteType.Trash1, new boolean[12][8]);
				spriteMap.put(SpriteType.Trash2, new boolean[12][8]);
				spriteMap.put(SpriteType.Trash3, new boolean[12][8]);
				spriteMap.put(SpriteType.Trash4, new boolean[12][8]);
				spriteMap.put(SpriteType.ESnA_1, new boolean[12][8]);
				spriteMap.put(SpriteType.ESnA_2, new boolean[12][8]);
			}
			spriteMap.put(SpriteType.ESnB_1, new boolean[12][8]);
			spriteMap.put(SpriteType.ESnB_2, new boolean[12][8]);
			spriteMap.put(SpriteType.ESnC_1, new boolean[12][8]);
			spriteMap.put(SpriteType.ESnC_2, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm1_1D, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm1_2D, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm1_1, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm1_2, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm2A_1, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm2A_2, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm2A_1D1, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm2A_2D1, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm2A_1D2, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm2A_2D2, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm2B_1, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm2B_2, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm2B_1D1, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm2B_2D1, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm2B_1D2, new boolean[12][8]);
			spriteMap.put(SpriteType.ESm2B_2D2, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipSpecial1, new boolean[16][7]);
			spriteMap.put(SpriteType.EnemyShipSpecial2, new boolean[16][7]);
			spriteMap.put(SpriteType.EnemyShipSpecial3, new boolean[16][7]);
			spriteMap.put(SpriteType.EnemyShipSpecial4, new boolean[16][7]);
			spriteMap.put(SpriteType.Explosion, new boolean[13][7]);
			spriteMap.put(SpriteType.BulletLine, new boolean[1][160]);
			spriteMap.put(SpriteType.Explosion2, new boolean[13][7]);
			spriteMap.put(SpriteType.Explosion3, new boolean[12][8]);
			spriteMap.put(SpriteType.Buff_Item, new boolean[9][9]);
			spriteMap.put(SpriteType.Debuff_Item, new boolean[9][9]);
			spriteMap.put(SpriteType.BlueEnhanceStone, new boolean[8][8]);
			spriteMap.put(SpriteType.PerpleEnhanceStone, new boolean[8][8]);
			spriteMap.put(SpriteType.BossA1, new boolean[22][13]);
			spriteMap.put(SpriteType.BossA2, new boolean[22][13]);
			spriteMap.put(SpriteType.Laser, new boolean[2][240]);
			spriteMap.put(SpriteType.LaserLine, new boolean[1][240]);
			spriteMap.put(SpriteType.Coin, new boolean[7][7]);
			spriteMap.put(SpriteType.ShipAShileded, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipBShileded, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipCShileded, new boolean[13][8]);
			spriteMap.put(SpriteType.Explosion4, new boolean[10][10]);
			spriteMap.put(SpriteType.gravestone, new boolean[13][9]);
			spriteMap.put(SpriteType.Ghost, new boolean[9][11]);
			fileManager.loadSprite(spriteMap);
			logger.info("Finished loading the sprites.");

			// Font loading
			fontSmall = fileManager.loadFont(12f);
			fontRegular = fileManager.loadFont(14f);
			fontBig = fileManager.loadFont(24f);
			fontVeryBig = fileManager.loadFont(40f);
			logger.info("Finished loading the fonts.");

		} catch (IOException e) {
			logger.warning("Loading failed.");
		} catch (FontFormatException e) {
			logger.warning("Font formating failed.");
		}
	}

	public void stopTimer(CountUpTimer timer) {
		timer.stop();
	}



	/**
	 * Returns shared instance of DrawManager.
	 *
	 * @return Shared instance of DrawManager.
	 */
	protected static DrawManager getInstance() {
		if (instance == null)
			instance = new DrawManager();
		return instance;
	}

	/**
	 * Sets the frame to draw the image on.
	 *
	 * @param currentFrame
	 *                     Frame to draw on.
	 */
	public void setFrame(final Frame currentFrame) {
		frame = currentFrame;
	}

	/**
	 * First part of the drawing process. Initialices buffers, draws the
	 * background and prepares the images.
	 *
	 * @param screen
	 *               Screen to draw in.
	 */
	public void initDrawing(final Screen screen) {
		backBuffer = new BufferedImage(screen.getWidth(), screen.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		graphics = frame.getGraphics();
		backBufferGraphics = backBuffer.getGraphics();

		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics
				.fillRect(0, 0, screen.getWidth(), screen.getHeight());

		fontSmallMetrics = backBufferGraphics.getFontMetrics(fontSmall);
		fontRegularMetrics = backBufferGraphics.getFontMetrics(fontRegular);
		fontBigMetrics = backBufferGraphics.getFontMetrics(fontBig);

		// drawBorders(screen);
		// drawGrid(screen);
	}

	/**
	 * Draws the completed drawing on screen.
	 *
	 * @param screen
	 *               Screen to draw on.
	 */
	public void completeDrawing(final Screen screen) {
		graphics.drawImage(backBuffer, frame.getInsets().left,
				frame.getInsets().top, frame);
	}

	/**
	 * Draws an entity, using the apropiate image.
	 *
	 * @param entity
	 *                  Entity to be drawn.
	 * @param positionX
	 *                  Coordinates for the left side of the image.
	 * @param positionY
	 *                  Coordinates for the upper side of the image.
	 */
	public void drawEntity(final Entity entity, final int positionX,
						   final int positionY) {
		boolean[][] image = spriteMap.get(entity.getSpriteType());

		backBufferGraphics.setColor(entity.getColor());
		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < image[i].length; j++)
				if (image[i][j])
					backBufferGraphics.drawRect(positionX + i * 2, positionY
							+ j * 2, 1, 1);
	}

	/**
	 * Entity can be drawn more precise size.
	 *
	 * [Clean Code Team] This method was created by dodo_kdy.
	 *
	 *
	 * @param SpriteType
	 * @param positionX
	 * @param positionY
	 * @param width
	 * @param height
	 */
	public void drawEntity(final SpriteType SpriteType, final int positionX,
						   final int positionY, final double width, final double height,
						   final Color color) {
		boolean[][] image = spriteMap.get(SpriteType);
		Graphics2D g2 = (Graphics2D) backBufferGraphics;
		g2.setColor(color);

		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < image[i].length; j++)
				if (image[i][j])
					g2.fill(new Rectangle2D.Double(positionX + i * width, positionY + j * height, width, height));
	}

	/**
	 * For debugging purpouses, draws the canvas borders.
	 *
	 * @param screen
	 *               Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private void drawBorders(final Screen screen) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, 0, screen.getWidth() - 1, 0);
		backBufferGraphics.drawLine(0, 0, 0, screen.getHeight() - 1);
		backBufferGraphics.drawLine(screen.getWidth() - 1, 0,
				screen.getWidth() - 1, screen.getHeight() - 1);
		backBufferGraphics.drawLine(0, screen.getHeight() - 1,
				screen.getWidth() - 1, screen.getHeight() - 1);
	}

	/**
	 * For debugging purpouses, draws a grid over the canvas.
	 *
	 * @param screen
	 *               Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private void drawGrid(final Screen screen) {
		backBufferGraphics.setColor(Color.DARK_GRAY);
		for (int i = 0; i < screen.getHeight() - 1; i += 2)
			backBufferGraphics.drawLine(0, i, screen.getWidth() - 1, i);
		for (int j = 0; j < screen.getWidth() - 1; j += 2)
			backBufferGraphics.drawLine(j, 0, j, screen.getHeight() - 1);
	}

	/**
	 * The color changes slightly depending on the score.
	 * [Clean Code Team] This method was created by highlees.
	 *
	 * @param score
	 */
	private Color scoreColor(final int score) {
		if (score < 800)
			return Color.WHITE;
		if (score >= 800 && score < 1600)
			return new Color(206, 255, 210);
		if (score >= 1600 && score < 2400)
			return new Color(151, 255, 158);
		if (score >= 2400 && score < 3200)
			return new Color(88, 255, 99);
		if (score >= 3200 && score < 4000)
			return new Color(50, 255, 64);
		if (score >= 4000 && score < 4800)
			return new Color(0, 255, 17);
		else
			return blinkingColor("HIGH_SCORES");
	}

	private Color scoreColor_1p(final int score) {
		if (score < 800)
			return new Color(238, 241, 255);
		if (score >= 800 && score < 1600)
			return new Color(210, 218, 255);
		if (score >= 1600 && score < 2400)
			return new Color(170, 196, 255);
		if (score >= 2400 && score < 3200)
			return new Color(142, 143, 250);
		if (score >= 3200 && score < 4000)
			return new Color(119, 82, 254);
		if (score >= 4000 && score < 4800)
			return new Color(25, 4, 130);
		else
			return blinkingColor("HIGH_SCORES");
	}

	private Color levelColor(final int level) {
		if (level == 1)
			return Color.WHITE;
		if (level == 2)
			return new Color(206, 255, 210);
		if (level == 3)
			return new Color(151, 255, 158);
		if (level == 4)
			return new Color(88, 255, 99);
		if (level == 5)
			return new Color(50, 255, 64);
		if (level == 6)
			return new Color(0, 255, 17);
		if (level == 7)
			return new Color(0,250,13);
		else
			return new Color(0,250,10);
	}

	/**
	 * The emoji changes slightly depending on the score.
	 * [Clean Code Team] This method was created by highlees.
	 *
	 * @param screen

	 *
	 */


	public void username(final Screen screen, final String user) {
		backBufferGraphics.setFont(fontRegular);
		if(user == null){
			backBufferGraphics.drawString("NO_NAME", screen.getWidth()-240, 25);
		}
		else {
			backBufferGraphics.drawString("Name : " + user, screen.getWidth() - 240, 25);
		}
	}

	public void drawLevel(final Screen screen, final int level){
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.setColor(levelColor(level));
		backBufferGraphics.drawString(Integer.toString(level), 150, 28);
	}
	public void drawSoundButton1(GameScreen gamescreen){
		backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.fillOval(375,425,55,45);
	}


	public void drawSoundStatus1(GameScreen gamescreen, boolean keyboard) {
		String statusText = keyboard ? "ON" : "OFF";
		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics.drawString(statusText, 379, 455);
	}



	/**
	 * Draws current score on screen.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param score
	 *               Current score.
	 */
	public void drawScore(final Screen screen, final int score) {
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.setColor(scoreColor(score));
		String scoreString = String.format("%04d", score);
		backBufferGraphics.drawString(scoreString, screen.getWidth() - 80, 28);
	}


	public void drawTimer(final Screen screen, final long elapsedTime) {
		backBufferGraphics.setFont(fontSmall);
		backBufferGraphics.setColor(Color.WHITE);
		String timeString = formatTime(elapsedTime);
		backBufferGraphics.drawString(timeString, 30, 450);
	}

	private String formatTime(long elapsedTime) {
		long totalSeconds = elapsedTime / 1000;
		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60;

		return String.format("%02d:%02d", minutes, seconds);
	}

	/**
	 * Draws current score on screen.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param coin
	 *               Current score.
	 */
	public void drawCoin(final Screen screen, final Coin coin, final int drawCoinOption) {
		if (drawCoinOption == 0) {
			this.drawEntity(SpriteType.Coin, 15, 55, 1.5, 1.5, Color.YELLOW);
			backBufferGraphics.setFont(fontRegular);
			backBufferGraphics.setColor(Color.WHITE);
			String coinString = String.format("%03d", coin.getCoin());
			backBufferGraphics.drawString(coinString, 30, 65);
		}
		else if (drawCoinOption == 1) {
			this.drawEntity(SpriteType.Coin, 20, 13, 2, 2, Color.YELLOW);
			backBufferGraphics.setFont(fontBig);
			backBufferGraphics.setColor(Color.WHITE);
			String coinString = String.format("%03d", coin.getCoin());
			backBufferGraphics.drawString(coinString, 40, 28);
		}
		else if (drawCoinOption == 2) {
			this.drawEntity(SpriteType.Coin, screen.getWidth()* 8/9 - 8, 43, 2, 2, Color.YELLOW);
			backBufferGraphics.setFont(fontRegular);
			backBufferGraphics.setColor(Color.WHITE);
			String coinString = String.format("%03d", coin.getCoin());
			backBufferGraphics.drawString(coinString, screen.getWidth() * 8/ 9 + 10, 55);
		}
	}

	public void BulletsCount(final Screen screen, final int BulletsCount) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
		String text = "Remaining Bullets: " + String.format("%02d", BulletsCount);
		backBufferGraphics.drawString(text, screen.getWidth() - 180, 60);
	}


	/**
	 * Draws number of remaining lives on screen.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param lives
	 *               Current lives.
	 */

	public void drawLivesbar(final Screen screen, final double lives) {
		// Calculate the fill ratio based on the number of lives (assuming a maximum of 3 lives).
		double fillRatio = lives / 3.0;

		// Determine the width of the filled portion of the rectangle.
		int filledWidth = (int) (120 * fillRatio);

		// Create a gradient paint that transitions from green to yellow.
		GradientPaint gradient = new GradientPaint(8, 8, Color.GREEN, 8 + filledWidth, 8, Color.YELLOW);

		// Cast Graphics to Graphics2D for gradient painting.
		Graphics2D g2d = (Graphics2D) backBufferGraphics;

		// Draw the outline of the rectangle.
		g2d.setColor(Color.WHITE);
		g2d.drawRect(8, 8, 120, 20);

		// Set the paint to the gradient and fill the left portion of the rectangle.
		g2d.setPaint(gradient);
		g2d.fillRect(8, 8, filledWidth, 20);

		// Set the new font size and type
		Font newFont = g2d.getFont().deriveFont(Font.BOLD, 19); // Adjust the font size as needed

		// Set the new font in the Graphics2D context
		g2d.setFont(newFont);

		// Set color for the "lives" text.
		g2d.setColor(Color.WHITE);

		// Calculate the position to center the "lives" text.
		int textX = (120 - g2d.getFontMetrics().stringWidth("Lives")) / 2 + 8; // Center horizontally
		int textY = 7 + 20 / 2 + g2d.getFontMetrics().getAscent() / 2; // Center vertically

		// Draw the "lives" text in the center of the rectangle.
		g2d.drawString("Lives", textX, textY);
	}


	public void drawitemcircle(final Screen screen, final int itemcount1, final int itemcount2) {
		Graphics2D g2d = (Graphics2D) backBufferGraphics;
		// this.drawEntity(SpriteType.Bullet,350,450,5,5); <<-- 이런식으로 아이콘 추가
		float strokeWidth = 3.0f; // 원의 선굵기
		BasicStroke stroke = new BasicStroke(strokeWidth); // 원의 선굵기
		g2d.setStroke(stroke); // 원의 선굵기
		g2d.setColor(Color.white); // 원의 선색깔
		g2d.fillOval(375, 310, 55, 45); // 원 위치
		g2d.fillOval(375, 365, 55, 45); // 원 위치
		g2d.setColor(Color.black); // 원의 선색깔
		g2d.drawString(Integer.toString(itemcount1), 395, 340); // 글자 추가
		g2d.drawString(Integer.toString(itemcount2), 395, 395); // 글자 추가
	}

	public void drawBossLivesbar(final Screen screen, int boss_lives) {
		double fillRatio = boss_lives / 50.0;

		int x = 15;
		int y = 85;

		// Determine the width of the filled portion of the rectangle.
		int filledWidth = (int) (398 * fillRatio);

		// Cast Graphics to Graphics2D for gradient painting.
		Graphics2D g2d = (Graphics2D) backBufferGraphics;

		// Create a RoundRectangle2D for the filled portion with rounded edges.
		RoundRectangle2D filledRect = new RoundRectangle2D.Double(x, y, filledWidth, 10, 10, 10);

		// Create a RoundRectangle2D for the outline with rounded edges.
		RoundRectangle2D outlineRect = new RoundRectangle2D.Double(x, y, 398, 10, 10, 10);

		// Create a gradient paint that transitions from green to yellow.
		GradientPaint gradient = new GradientPaint(x, y, Color.YELLOW, x + filledWidth, y, Color.RED);

		// Draw the outline of the rounded rectangle.
		g2d.setColor(Color.BLACK);
		g2d.draw(outlineRect);

		// Set the paint to the gradient and fill the left portion of the rounded rectangle.
		g2d.setPaint(gradient);
		g2d.fill(filledRect);
	}

	/**
	 * Draws a thick line from side to side of the screen.
	 *
	 * @param screen
	 *                  Screen to draw on.
	 * @param positionY
	 *                  Y coordinate of the line.
	 */
	public void drawHorizontalLine(final Screen screen, final int positionY) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, positionY, screen.getWidth(), positionY);
		backBufferGraphics.drawLine(0, positionY + 1, screen.getWidth(),
				positionY + 1);
	}

	/**
	 * Draws a circle line.
	 *
	 * @param screen
	 *                  Screen to draw on.
	 * @param positionX
	 *                  X coordinate of the line.
	 * @param positionY
	 *                  Y coordinate of the line.
	 * @param width
	 *                  Y coordinate of the line.
	 * @param height
	 *                  Y coordinate of the line.
	 * @param graphicOption
	 *                  if option 0, use backBufferGraphics Object. Otherwise use graphics Object.
	 */
	public void drawCircleLine(final Screen screen, final int positionX, final int positionY, final int width, final int height, final int graphicOption) {
		backBufferGraphics.setColor(Color.GREEN);
		((Graphics2D) backBufferGraphics).setStroke(new BasicStroke(2));
		if (graphicOption == 0){
			backBufferGraphics.drawOval(positionX, positionY, width, height);
		}
		else {
			graphics.drawOval(positionX, positionY, width, height);
		}
	}
	/**
	 * Draws a circle filled.
	 *
	 * @param screen
	 *                  Screen to draw on.
	 * @param positionX
	 *                  X coordinate of the line.
	 * @param positionY
	 *                  Y coordinate of the line.
	 * @param width
	 *                  Y coordinate of the line.
	 * @param height
	 *                  Y coordinate of the line.
	 */
	public void drawCircleFill(final Screen screen, final int positionX, final int positionY, final int width, final int height) {
		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics.fillOval(positionX, positionY, width, height);
	}

	/**
	 * Creates blinking colors like an arcade screen.
	 * [Clean Code Team] This method was created by highlees.
	 *
	 *
	 */

	private Color blinkingColor(String color) {
		if (color == "HIGH_SCORES") {
			int R = (int) (Math.pow(Math.random() * (15 - 0), 2));
			int G = (int) (Math.random() * (255 - 0));
			int B = (int) 3.3 * LocalTime.now().getSecond();
			Color title = new Color(R, G, B);
			return title;
		}
		if (color == "GREEN") {
			Color green = new Color(0, (int) (Math.random() * (255 - 155) + 155), 0);
			return green;
		}
		if (color == "WHITE") {
			int RGB = (int) (Math.random() * (255 - 155) + 155);
			Color white = new Color(RGB, RGB, RGB);
			return white;
		}
		if (color == "GRAY") {
			int RGB = (int) (Math.random() * (160 - 100) + 100);
			Color gray = new Color(RGB, RGB, RGB);
			return gray;
		}
		return Color.WHITE;
	}

	/**
	 * Create slowly changing colors.
	 * Can be applied to multiple screens in the game.
	 * [Clean Code Team] This method was created by highlees.
	 *
	 *
	 */

	private Color slowlyChangingColors(String color) {
		String sec = Integer.toString(LocalTime.now().getSecond());
		char c = sec.charAt(sec.length() - 1);
		if (color == "GREEN") {
			if (c == '0') return new Color(0, 75, 0);
			if (c == '1') return new Color(0, 100, 0);
			if (c == '2') return new Color(0, 125, 0);
			if (c == '3') return new Color(0, 150, 0);
			if (c == '4') return new Color(0, 175, 0);
			if (c == '5') return new Color(0, 205, 0);
			if (c == '6') return new Color(0, 225, 0);
			if (c == '7') return new Color(0, 254, 0);
			if (c == '8') return new Color(0, 55, 0);
			if (c == '9') return new Color(0, 65, 0);
		}
		if (color == "GRAY") {
			if (c == '0') return new Color(75, 75, 75);
			if (c == '1') return new Color(85, 85, 85);
			if (c == '2') return new Color(105, 105, 105);
			if (c == '3') return new Color(130, 130, 130);
			if (c == '4') return new Color(155, 155, 155);
			if (c == '5') return new Color(180, 180, 180);
			if (c == '6') return new Color(205, 205, 205);
			if (c == '7') return new Color(225, 225, 225);
			if (c == '8') return new Color(55, 55, 55);
			if (c == '9') return new Color(65, 65, 65);
		}
		if (color == "RAINBOW") {
			if (c == '0') return new Color(254, 254, 0);
			if (c == '1') return new Color(135, 254, 0);
			if (c == '2') return new Color(0, 254, 0);
			if (c == '3') return new Color(0, 254, 254);
			if (c == '4') return new Color(0, 135, 254);
			if (c == '5') return new Color(0, 0, 254);
			if (c == '6') return new Color(135, 0, 205);
			if (c == '7') return new Color(254, 0, 224);
			if (c == '8') return new Color(254, 0, 135);
			if (c == '9') return new Color(220, 200, 254);
		}
		return Color.WHITE;
	}

	/**
	 * Draws game title.
	 *
	 * @param screen
	 *               Screen to draw on.
	 */
	public void drawTitle(final Screen screen) {
		String titleString = "I N V A D E R S";
		String instructionsString = "Select with W + S, confirm with SPACE.";
		String aboutLogString = "Select with W + S, Sign in, Sign Out, confirm with L";
		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 2 - (fontRegularMetrics.getHeight() * 2));

		backBufferGraphics.setColor(blinkingColor("GREEN"));
		drawCenteredBigString(screen, titleString, screen.getHeight() / 3);

		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, aboutLogString, screen.getHeight() / 2 - fontRegularMetrics.getHeight());
	}

	/**
	 * Draws main menu.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param option
	 *               Option selected.
	 */
	public void drawMenu(final Screen screen, final int option) {
		String playString = "P L A Y";
		String rankingString = "R A N K I N G";
		String exitString = "E X I T";
		String login = "S I G N  I N";
		String logout = "S I G N  O U T";
		String friend = "F I R E N D";
		if (option == 2)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, playString,
				screen.getHeight() / 3 * 2 - (fontRegularMetrics.getHeight() * 3));
		if (option == 3)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, rankingString, screen.getHeight()
				/ 3 * 2 - fontRegularMetrics.getHeight());
		if(option == 40)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, login, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight());
		if(option == 41)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, logout, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 3);
		if(option == 42)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, friend, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 5);
		if (option == 0)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, exitString, screen.getHeight() / 3
				* 2 + fontRegularMetrics.getHeight() * 7);
	}


	public void drawRandomBox(final Screen screen, final int option) {
		String introduceString1 = "SELECT ONE OF THE THREE BOXES";
		String introduceString2 = "FOR A RANDOM REWARD";
		String oneString = "1";
		String twoString = "2";
		String threeString = "3";
		try {
			BufferedImage image1 = ImageIO.read(new File("res/giftbox1.png"));
			BufferedImage greenImage1 = image1;
			if (option == 20) {
				RescaleOp greenFilter = new RescaleOp(new float[]{0f, 1f, 0f, 1f}, new float[]{0f, 0f, 0f, 0f}, null);
				greenImage1 = greenFilter.filter(image1, null);
			}
			backBufferGraphics.drawImage(greenImage1, screen.getWidth() / 4 - 27, screen.getHeight() / 2 + 20, 60, 60, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedImage image2 = ImageIO.read(new File("res/giftbox1.png"));
			BufferedImage greenImage2 = image2;
			if (option == 21) {
				RescaleOp greenFilter = new RescaleOp(new float[]{0f, 1f, 0f, 1f}, new float[]{0f, 0f, 0f, 0f}, null);
				greenImage2 = greenFilter.filter(image2, null);
			}
			backBufferGraphics.drawImage(greenImage2, screen.getWidth() * 2 / 4 - 25, screen.getHeight() / 2 + 20, 60, 60, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedImage image3 = ImageIO.read(new File("res/giftbox1.png"));
			BufferedImage greenImage3 = image3;
			if (option == 22) {
				RescaleOp greenFilter = new RescaleOp(new float[]{0f, 1f, 0f, 1f}, new float[]{0f, 0f, 0f, 0f}, null);
				greenImage3 = greenFilter.filter(image3, null);
			}
			backBufferGraphics.drawImage(greenImage3, screen.getWidth() * 3 / 4 - 25, screen.getHeight() / 2 + 20, 60, 60, null);
		} catch (IOException e) {
			e.printStackTrace();
		}


		// backBufferGraphics.setColor(slowlyChangingColors("RAINBOW"));
		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, introduceString1, screen.getHeight() / 8);
		drawCenteredRegularString(screen, introduceString2, screen.getHeight() / 6);
		if (option == 20)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(oneString, screen.getWidth() / 4, screen.getHeight() * 3 / 4);

		if (option == 21)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(twoString, screen.getWidth() * 2 / 4, screen.getHeight() * 3 / 4);

		if (option == 22)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(threeString, screen.getWidth() * 3 / 4, screen.getHeight() * 3 / 4);
	}

	private BufferedImage image1;
	public void drawRandomReward(final Screen screen, final int option, final String randomTypeString, final int randomRes) {
		String introduceString = "RANDOM REWARD";
		String nextString = "N E X T";

		getRandomCoin = Integer.toString(randomRes);
		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, introduceString, screen.getHeight() / 8);
		drawCenteredRegularString(screen, randomTypeString, screen.getHeight() * 3 / 4 - 20);
		drawCenteredRegularString(screen, getRandomCoin, screen.getHeight() * 3 / 4);
		backBufferGraphics.setColor(blinkingColor("GREEN"));
		backBufferGraphics.drawString(nextString, (screen.getWidth() - fontRegularMetrics.stringWidth(nextString)) / 2, screen.getHeight() * 7 /8 - 20);
		try {
			image1 = ImageIO.read(new File("res/giftbox3.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		backBufferGraphics.drawImage(image1, screen.getWidth() * 2 / 4 - 25, screen.getHeight() / 2 - 20, 60, 60, null);
	}

	/**
	 * Draws sub menu.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param option
	 *               Option selected.
	 */
	public void drawSubMenu(final Screen screen, final int option) {
		String SelectString = "Select difficulty with W + S, confirm with SPACE.";
		String itemStoreString = "I T E M S T O R E";
		String ehanceString = "E N H A N C E M E N T";
		String playString = "C O N T I N U E";
		String skinStoreString = "S K I N S T O R E";

		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, SelectString, screen.getHeight() / 8);
		if (option == 6)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, itemStoreString,
				screen.getHeight() / 3 * 2);
		if (option == 7)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, ehanceString,
				screen.getHeight() / 3 * 2 + fontRegularMetrics.getHeight() * 2);
		if (option == 2)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, playString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 4);
		if (option == 86)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, skinStoreString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 6);
	}

	/**
	 * Draws sub menu.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param option
	 *               Option selected.
	 */


	/**
	 * Draws score menu.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param option
	 *               Option selected.
	 */
	public void drawScoreMenu(final Screen screen, final int option) {
		String SelectString = "Select Mode with W + S, confirm with SPACE.";
		String PersonalString = "P E R S O N A L R A N K I N G";
		String CountryString = "C O U N T R Y R A N K I N G";
		String GlobalString = "G L O B A L R A N K I N G";
		String MainMenuString = "M a i n  M e n u";

		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, SelectString, screen.getHeight() / 8);
		if (option == 31)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, PersonalString,
				screen.getHeight() / 3 * 2);
		if(option == 32)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, CountryString, screen.getHeight() / 3 * 2
		+fontRegularMetrics.getHeight() * 2);
		if(option == 33)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, GlobalString, screen.getHeight() / 3 * 2
		+ fontRegularMetrics.getHeight() * 4);
		if (option == 1)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, MainMenuString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 6);

	}

	/**
	 * Draws Recovery select menu.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param option
	 *               Option selected.
	 */

	public void drawRecoveryMenu(final Screen screen, final int option) {
		String SelectString = "Select state with W + S, confirm with SPACE.";
		String recoveryString = " R E C O V E R Y ";
		String recovdefaultString = "D E F A U L T   S T A T E";
		String exitString = "E X I T";

		backBufferGraphics.setColor(Color.green);
		drawCenteredBigString(screen, recoveryString, screen.getHeight() / 5);
		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, SelectString, screen.getHeight() / 3);

		if (option == 30)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, recovdefaultString,
				screen.getHeight() / 3 * 2);
		if (option == 31)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, exitString,
				screen.getHeight() / 3 * 2 + fontRegularMetrics.getHeight() * 2);

	}

	/**
	 * Draws Recovery Payment page.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param option
	 *               Option selected.
	 */

	public void drawRecoveryConfirmPage(GameState gameState,final Screen screen, final int option) {
		String paymentMessage = "Please pay 150 amount to recover:";
		backBufferGraphics.setColor(Color.white);
		drawCenteredRegularString(screen,paymentMessage, screen.getHeight() / 3 + fontRegularMetrics.getHeight() * 4);

		GameState recoveryGameState = gameState;
		Coin recoveryCoin = recoveryGameState.getCoin();

		String coinString = " C O I N : " + recoveryCoin.getCoin();

		if(recoveryCoin.getCoin() >= 30){

			backBufferGraphics.setColor(Color.YELLOW);
			drawCenteredBigString(screen, coinString, (screen.getHeight() / 5) + 10);

			String successMessage = "Your coin is enough";
			backBufferGraphics.setColor(Color.PINK);
			drawCenteredRegularString(screen, successMessage, screen.getHeight() / 3 + fontRegularMetrics.getHeight() * 6);
		} else {
			backBufferGraphics.setColor(Color.red);
			drawCenteredBigString(screen, coinString, (screen.getHeight() / 5) + 10);

			String successMessage = "You need more coin to continue";
			backBufferGraphics.setColor(Color.orange);
			drawCenteredRegularString(screen, successMessage, screen.getHeight() / 3 + fontRegularMetrics.getHeight() * 6);
		}


		String dorecoveryString = " Y E S ";
		String notrecoveryString = " N O ";

		if (option == 51){

			if(recoveryCoin.getCoin() >= 30){
				backBufferGraphics.setColor(blinkingColor("GREEN"));
			} else {backBufferGraphics.setColor(Color.red);}

		}
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, dorecoveryString,
				screen.getHeight() / 3 * 2);
		if (option == 52)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, notrecoveryString,
				screen.getHeight() / 3 * 2 + fontRegularMetrics.getHeight() * 2);



	}


	/**
	 * @param screen
	 *               Screen to draw on.
	 * @param option
	 *               Option selected.
	 */
	public void drawSelect(final Screen screen, final int option) {
		String SelectString = "Select difficulty with W + S, confirm with SPACE.";
		String EasyString = "E A S Y";
		String NormalString = "N O R M A L";
		String HardString = "H A R D";
		String HardCoreString = "H A R D C O R E";
		String MainString = "M A I N";

		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, SelectString, screen.getHeight() / 8);

		if (option == 0)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, EasyString,
				screen.getHeight() / 6 * 2);
		if (option == 1)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, NormalString,
				screen.getHeight() / 6 * 2 + fontRegularMetrics.getHeight() * 2);
		if (option == 2)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, HardString, screen.getHeight()
				/ 6 * 2 + fontRegularMetrics.getHeight() * 4);
		if (option == 3)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, HardCoreString, screen.getHeight() / 6 * 2
				+ fontRegularMetrics.getHeight() * 6);
		if (option == 4)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, MainString, screen.getHeight() / 6 * 2
				+ fontRegularMetrics.getHeight() * 8);
	}

	/**
	 * Draws Select menu.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param option
	 *            Stage(level) selected.
	 * If the number of Levels is changed, this page is also automatically changed the same as it.
	 */
	public void drawStageSelect(final Screen screen, final int option, final int stages) {
		String SelectString = "Select Level with WASD, confirm with Space,";
		String SelectString_2 = "cancel with ESC.";
		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, SelectString,screen.getHeight() / 8);
		drawCenteredRegularString(screen, SelectString_2,screen.getHeight() / 8 + screen.getHeight() / 16);
		String[] Stage = new String[stages];
		backBufferGraphics.setFont(fontBig);
		for (int i = 0; i < stages; i++) {
			Stage[i] = String.valueOf(i+1);
			if (option == i)
				backBufferGraphics.setColor(blinkingColor("GREEN"));
			else
				backBufferGraphics.setColor(blinkingColor("WHITE"));
			backBufferGraphics.drawString(Stage[i], screen.getWidth() / 2
							- (screen.getWidth()/10) * (2-(i%5)),
					screen.getHeight() / 5 * 2 + fontRegularMetrics.getHeight() * (2*((i/5)-1)));
		}
	}

	/**
	 * Draws game results.
	 *
	 * @param screen
	 *                       Screen to draw on.
	 * @param score
	 *                       Score obtained.
	 * @param livesRemaining
	 *                       Lives remaining when finished.
	 * @param shipsDestroyed
	 *                       Total ships destroyed.
	 * @param accuracy
	 *                       Total accuracy.
	 * @param isNewRecord
	 *                       If the score is a new high score.
	 */
	public void drawResults(final Screen screen, final int score,
							final double livesRemaining, final int shipsDestroyed, final int difficulty,
							final float accuracy, final boolean isNewRecord) {
		String scoreString = String.format("score %04d", score);
		String difficultyString = "Difficulty ";
		String livesRemainingString = "lives remaining " + livesRemaining;
		String shipsDestroyedString = "enemies destroyed " + shipsDestroyed;
		String accuracyString = String
				.format("Accuracy %.2f%%", accuracy * 100);

		int height = isNewRecord ? 4 : 2;

		if (difficulty == 0)
			difficultyString = difficultyString + "EASY";
		else if (difficulty == 1)
			difficultyString = difficultyString + "NORMAL";
		else if (difficulty == 2)
			difficultyString = difficultyString + "HARD";
		else if (difficulty == 3)
			difficultyString = difficultyString + "HARDCORE";

		backBufferGraphics.setColor(slowlyChangingColors("GRAY"));
		drawCenteredRegularString(screen, scoreString, screen.getHeight()
				/ height);
		drawCenteredRegularString(screen, difficultyString,
				screen.getHeight() / height + fontRegularMetrics.getHeight()
						* 2);
		drawCenteredRegularString(screen, livesRemainingString,
				screen.getHeight() / height + fontRegularMetrics.getHeight()
						* 4);
		drawCenteredRegularString(screen, shipsDestroyedString,
				screen.getHeight() / height + fontRegularMetrics.getHeight()
						* 6);
		drawCenteredRegularString(screen, accuracyString, screen.getHeight()
				/ height + fontRegularMetrics.getHeight() * 8);
	}

	/**
	 * Draws interactive characters for name input.
	 *
	 * @param screen
	 *                         Screen to draw on.
	 * @param name
	 *                         Current name selected.
	 * @param nameCharSelected
	 *                         Current character selected for modification.
	 */
	public void drawNameInput(final Screen screen, final char[] name,
							  final int nameCharSelected) {
		String newRecordString = "New Record!";
		String introduceNameString = "Introduce name:";

		backBufferGraphics.setColor(slowlyChangingColors("GREEN"));
		drawCenteredRegularString(screen, newRecordString, screen.getHeight()
				/ 4 + fontRegularMetrics.getHeight() * 10);
		backBufferGraphics.setColor(slowlyChangingColors("GRAY"));
		drawCenteredRegularString(screen, introduceNameString,
				screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 12);

		// 3 letters name.
		int positionX = screen.getWidth()
				/ 2
				- (fontRegularMetrics.getWidths()[name[0]]
				+ fontRegularMetrics.getWidths()[name[1]]
				+ fontRegularMetrics.getWidths()[name[2]]
				+ fontRegularMetrics.getWidths()[' ']) / 2;

		for (int i = 0; i < 3; i++) {
			if (i == nameCharSelected)
				backBufferGraphics.setColor(slowlyChangingColors("GREEN"));
			else
				backBufferGraphics.setColor(slowlyChangingColors("GRAY"));

			positionX += fontRegularMetrics.getWidths()[name[i]] / 2;
			positionX = i == 0 ? positionX
					: positionX
					+ (fontRegularMetrics.getWidths()[name[i - 1]]
					+ fontRegularMetrics.getWidths()[' ']) / 2;

			backBufferGraphics.drawString(Character.toString(name[i]),
					positionX,
					screen.getHeight() / 4 + fontRegularMetrics.getHeight()
							* 14);
		}
	}

	/**
	 * Draws basic content of game over screen.
	 *
	 * @param screen
	 *                     Screen to draw on.
	 * @param acceptsInput
	 *                     If the screen accepts input.
	 * @param isNewRecord
	 *                     If the score is a new high score.
	 */
	public void drawGameOver(final Screen screen, final boolean acceptsInput,
							 final boolean isNewRecord) {
		String gameOverString = "Game Over";
		String continueOrExitString = "Press SPACE to play again, ESC to exit";

		int height = isNewRecord ? 4 : 2;

		backBufferGraphics.setColor(slowlyChangingColors("GREEN"));
		drawCenteredBigString(screen, gameOverString, screen.getHeight()
				/ height - fontBigMetrics.getHeight() * 2);

		if (acceptsInput)
			backBufferGraphics.setColor(slowlyChangingColors("GREEN"));
		else
			backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, continueOrExitString,
				screen.getHeight() / 2 + fontRegularMetrics.getHeight() * 10);
	}

	/**
	 * Draws Pause notification during game
	 *
	 * @param screen
	 *            Screen to draw on.
	 */
	public void drawPaused(final Screen screen) {
		String Paused = "Press ENTER to continue.";
		String Quit = "Press BackSpace to quit.";
		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, Paused, screen.getHeight() * 3 / 4);
		drawCenteredBigString(screen, Quit, screen.getHeight() * 5 / 6);
	}


	public void drawDiffScore(final Screen screen, final int difficulty) {
		String EasyString = "EASY";
		String NormalString = "NORMAL";
		String HardString = "HARD";
		String HardCoreString = "HARDCORE";

		backBufferGraphics.setFont(fontRegular);
		if (difficulty == 0)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("GRAY"));
		backBufferGraphics.drawString(EasyString, screen.getWidth() / 8
				- fontRegularMetrics.stringWidth(EasyString) / 2, screen.getHeight() * 2/7);
		if (difficulty == 1)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("GRAY"));
		backBufferGraphics.drawString(NormalString, screen.getWidth() * 3 / 8
				- fontRegularMetrics.stringWidth(NormalString) / 2, screen.getHeight() * 2/7);
		if (difficulty == 2)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("GRAY"));
		backBufferGraphics.drawString(HardString, screen.getWidth() * 5 / 8
				- fontRegularMetrics.stringWidth(HardString) / 2, screen.getHeight() * 2/7);
		if (difficulty == 3)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("GRAY"));
		backBufferGraphics.drawString(HardCoreString, screen.getWidth() * 7 / 8
				- fontRegularMetrics.stringWidth(HardCoreString) / 2, screen.getHeight() * 2/7);
	}
	/**
	 * Draws high score screen title and instructions.
	 *
	 * @param screen
	 *               Screen to draw on.
	 */
	public void drawHighScoreMenu(final Screen screen) {
		String highScoreString = "Personal RANKING";
		String instructionsString = "Press SPACE to return";

		backBufferGraphics.setColor(blinkingColor("HIGH_SCORES"));
		drawCenteredBigString(screen, highScoreString, screen.getHeight() / 8);

		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 5);
	}
	public void drawTitle(final Screen screen, String Title){
		String TitlescoreString = Title;
		String instructionsString = "Press SPACE to return";

		if("".equals(Title)){
			backBufferGraphics.setColor(blinkingColor("GREEN"));
			drawCenteredBigString(screen, "Sign in Please", screen.getHeight() / 8);
		}
		else{
			backBufferGraphics.setColor(blinkingColor("GREEN"));
			drawCenteredBigString(screen, TitlescoreString, screen.getHeight() / 8);
		}

		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 5);
	}

	/**
	 * Draws high scores.
	 *
	 * @param screen
	 *                   Screen to draw on.
	 * @param highScores
	 *                   List of high scores.
	 */
	public void drawHighScores(final Screen screen,
							   final List<Score> highScores) {
		backBufferGraphics.setColor(blinkingColor("WHITE"));
		int i = 0;
		String scoreString = "";
		String rank[] = {"1st", "2nd", "3th", "4th", "5th"};

		for (Score score : highScores) {
			scoreString = String.format("%s        %s        %04d", rank[i], score.getName(),
					score.getScore());
			drawCenteredRegularString(screen, scoreString, screen.getHeight()
					/ 3 + fontRegularMetrics.getHeight() * (i + 1) * 2);
			i++;
			if (i >= 5)
				break;
		}
	}

	/**
	 * Draws a centered string on regular font.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param string
	 *               String to draw.
	 * @param height
	 *               Height of the drawing.
	 */
	public void drawCenteredRegularString(final Screen screen,
										  final String string, final int height) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontRegularMetrics.stringWidth(string) / 2, height);
	}

	/**
	 * Draws a centered string on big font.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param string
	 *               String to draw.
	 * @param height
	 *               Height of the drawing.
	 */
	public void drawCenteredBigString(final Screen screen, final String string,
									  final int height) {
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontBigMetrics.stringWidth(string) / 2, height);
	}

	/**
	 * Countdown to game start.
	 *
	 * @param screen
	 *                  Screen to draw on.
	 * @param level
	 *                  Game difficulty level.
	 * @param number
	 *                  Countdown number.
	 * @param bonusLife
	 *                  Checks if a bonus life is received.
	 */
	public void drawCountDown(final Screen screen, final int level,
							  final int number, final boolean bonusLife) {
		backBufferGraphics.setColor(Color.GREEN);

		if (number >= 4)
			if (!bonusLife) {
				pumpingLevel(screen, "Level " + level,screen.getHeight() / 2
						+ fontBigMetrics.getHeight() / 3);
			} else {
				drawCenteredBigString(screen, "Level " + level
								+ " - Bonus life!",
						screen.getHeight() / 2
								+ fontBigMetrics.getHeight() / 3);
			}
		else if (number != 0) {
			if (isFirst){
				drawLoading(screen.getHeight() / 6, screen.getHeight() / 3, screen);
				if (initialSound) {
					initialSound = false;
				}
			}
			else {
				drawLoadingNeon(screen, "Loading...",
						screen.getHeight() / 2
								+ fontBigMetrics.getHeight() / 3, number);
				isAfterLoading = true;
				initialSound2 = true;
				timercount++;
			}
		} else {
			drawGo(screen, "GO!", screen.getHeight() / 2 + fontBigMetrics.getHeight() / 3);
			if (isAfterLoading && initialSound2) {
				initialSound2 = false;
			}
			isFirst = false;
			timercount = 0;
		}
	}




	public void pumpingLevel(Screen screen,String string,int height){
		Font font = fontBig;
		try {
			font = fileManager.loadFont(bigger);
			if (bigger >= 40 || bigger <= 25 ) direction *= -1;
		} catch (IOException e) {
			logger.warning("Loading failed.");
		} catch (FontFormatException e) {
			logger.warning("Font formating failed.");
		}

		Graphics2D g2 = (Graphics2D)backBufferGraphics;
		g2.setColor(pumpColor());
		g2.setFont(font);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawString(string,screen.getWidth() / 2 - g2.getFontMetrics().stringWidth(string) / 2, height);

		bigger+=direction;
	}

	public Color pumpColor(){
		int r = new Random().nextInt(5);
		if(r == 1) return new Color(147, 227, 83, 234);
		else if (r==2) return new Color(26, 255, 0, 255);
		else if (r==3) return new Color(45, 255, 167, 245);
		else if (r==4) return new Color(0, 255, 0, 77);
		else return new Color(27, 215, 136, 245);

	}


	public void drawGo(final Screen screen, final String string, final int height){
		Font font = fontBig;
		try {
			font = fileManager.loadFont(30);
		} catch (IOException e) {
			logger.warning("Loading failed.");
		} catch (FontFormatException e) {
			logger.warning("Font formating failed.");
		}

		Graphics2D g2 = (Graphics2D)backBufferGraphics;
		g2.setFont(font);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		float[] fractions = new float[30];
		Color[] colors = new Color[30];
		for (int i = 0; i < colors.length; i++) {
			fractions[i] = ((float)i) / 30;
			float hue = fractions[i];
			colors[i] = Color.getHSBColor(hue, 1f, 1f);
		}
		//Paint p = new LinearGradientPaint(0, 0, 80, 0, fractions, colors);
		//g2.setPaint(p);

		GlyphVector gv = font.createGlyphVector(g2.getFontRenderContext(),string);
		Shape shape = gv.getOutline();
		g2.setStroke(new BasicStroke(1.6f));
		g2.translate(screen.getWidth() / 2 - fontBigMetrics.stringWidth(string) / 2 - 5, height);
		g2.draw(shape);

	}

	/**
	 * Draw a Loading String like neon sign.
	 * [Clean-Code Team] This method was created by dodo_kdy.
	 *
	 * @param screen
	 * @param string
	 * @param height
	 * @param number
	 */
	public void drawLoadingNeon(final Screen screen, final String string, final int height, int number) {
		Font font1 = fontBig;
		try {
			font1 = fileManager.loadFont(33f);
		} catch (IOException e) {
			logger.warning("Loading failed.");
		} catch (FontFormatException e) {
			logger.warning("Font formating failed.");
		}

		Graphics2D g2d = (Graphics2D)backBufferGraphics;
		g2d.setFont(font1);
		g2d.setColor(new Color(26, 255, 0));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if ((25 + 20 * (3 - number) < timercount && timercount < 40 + 20 * (3 - number)))
			g2d.setColor(new Color(0, 0, 0,0));


		GlyphVector gv = font1.createGlyphVector(g2d.getFontRenderContext(),string);
		Shape shape = gv.getOutline();
		g2d.setStroke(new BasicStroke(1.6f));
		g2d.translate(screen.getWidth() / 2 - fontBigMetrics.stringWidth(string) / 2 - 5, height);
		g2d.draw(shape);
	}


	public void drawItemthings(final int width, final int height, final int size, final Color color, final int option, String string){
		String shield = "S H I E L D";
		String bomb = "B O M B";
		String BST = "B S T O N E";
		String PST = "P S T O N E";
		backBufferGraphics.drawRect(width, height, size, size);
		if (option == 1){
			backBufferGraphics.setColor(Color.white);
			backBufferGraphics.setFont(fontSmall);
			backBufferGraphics.drawString(shield, width + 18, height + size - 10);
			drawEntity(SpriteType.ShipAShileded, width + 12, height +23, size/15, size/15, color);
			backBufferGraphics.setColor(Color.white);
			backBufferGraphics.setFont(fontBig);
			backBufferGraphics.drawString(string, width+ 90, height+ 105);
		}
		else if (option == 2){
			backBufferGraphics.setColor(Color.white);
			backBufferGraphics.setFont(fontSmall);
			backBufferGraphics.drawString(bomb, width + 25, height + size - 10);
			drawEntity(SpriteType.Explosion, width + 12, height + 25, size/15, size/15, color);
			backBufferGraphics.setColor(Color.white);
			backBufferGraphics.setFont(fontBig);
			backBufferGraphics.drawString(string, width + 90, height + 105);
		}
		else if (option == 3){

			backBufferGraphics.setColor(Color.white);
			backBufferGraphics.setFont(fontSmall);
			backBufferGraphics.drawString(BST, width + 18, height + size - 10);
			drawEntity(SpriteType.BlueEnhanceStone, width + 25, height + 25, size/15, size/15, color);
			backBufferGraphics.setColor(Color.white);
			backBufferGraphics.setColor(Color.white);
			backBufferGraphics.setFont(fontBig);
			backBufferGraphics.drawString(string, width + 90, height + 105);
		}
		else if (option == 4){
			backBufferGraphics.setColor(Color.white);
			backBufferGraphics.setFont(fontSmall);
			backBufferGraphics.drawString(PST, width + 18, height + size - 10);
			drawEntity(SpriteType.PerpleEnhanceStone, width + 25, height + 25, size/15, size/15, color);
			backBufferGraphics.setColor(Color.white);
			backBufferGraphics.setFont(fontBig);
			backBufferGraphics.drawString(string, width+ 90 , height+105);
		}


		backBufferGraphics.setColor(Color.GREEN);
	}
	public void drawItemStore(final Screen screen, final int option, final int PST, final int BST, final ItemManager itemManager) {
		String itemStoretxt = " * I T E M S T O R E * ";
		String continueString = " > C O N T I N U E";
		String EnhanceString = " > E N H A N C E";
		String skinStoreString = " > S K I N S T O R E";
		String BuyString = "B U Y";
		String PrizeString = "1 5 0";
		String PrizeString1 = "1 5 0";
		String PrizeString2 = "5 0";
		String PrizeString3 = "5 0";
		String ShieldString = "" + itemManager.getShieldCount();
		String BombString = "" + itemManager.getBombCount();
		String BSTString = "" + BST;
		String PSTStiring = "" + PST;

		int rectWidth = screen.getWidth();
		int rectHeight = screen.getHeight() / 6;
		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics.fillRect(0, screen.getHeight() / 2 - rectHeight / 2,
				rectWidth, rectHeight);
		backBufferGraphics.setColor(Color.green);
		drawCenteredBigString(screen, itemStoretxt,	screen.getHeight()/4 - 97);
		drawHorizontalLine(screen, screen.getHeight()/14);
		drawItemthings(screen.getWidth()/7, screen.getHeight()/6, 100, Color.GRAY,1, ShieldString);
		drawItemthings(screen.getWidth() *5/8, screen.getHeight()/6, 100, Color.RED,2, BombString);
		drawItemthings(screen.getWidth()/7, screen.getHeight()*4/7 - 30, 100,Color.BLUE,3, BSTString);
		drawItemthings(screen.getWidth()*5/8, screen.getHeight()*4/7 - 30, 100, Color.magenta,4, PSTStiring);

		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.yellow);
		backBufferGraphics.drawString(PrizeString, screen.getWidth()/7 + 33 , screen.getHeight()/2 - 35);
		backBufferGraphics.drawString(PrizeString1, screen.getWidth()*5/8 + 33, screen.getHeight()/2 - 35);
		backBufferGraphics.drawString(PrizeString2, screen.getWidth()/7 + 40 , screen.getHeight() - 115);
		backBufferGraphics.drawString(PrizeString3, screen.getWidth()*5/8 + 40 , screen.getHeight() - 115);
		if (option == 14)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(EnhanceString, screen.getWidth()/15 - 20, screen.getHeight() - 30);
		if (option == 2)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(continueString, screen.getWidth()/3, screen.getHeight() - 30);
		if (option == 15)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(skinStoreString, screen.getWidth() - 140, screen.getHeight() - 30);
		if (option == 35)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(BuyString, screen.getWidth()/7 + 33 , screen.getHeight()/2 - 15);
		if (option == 36)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(BuyString, screen.getWidth() *5/8 + 33, screen.getHeight()/2 - 15);
		if (option == 37)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(BuyString, screen.getWidth()/7+33, screen.getHeight() - 95);
		if (option == 38)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(BuyString, screen.getWidth()*5/8 + 33, screen.getHeight() - 95);
	}

	/**
	 * Draws  skin store.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param option
	 *               Option selected.
	 */

	public void drawSkinStore(final GameState gameState, final Screen screen, final int option) {

		String skinStoretxt = " * S K I N S T O R E * ";
		String continueString = " > C O N T I N U E";
		String EnhanceString = " > E N H A N C E";
		String itemStoreString = " > I T E M S T O R E";
		SkinBuyManager skinBuyManager = new SkinBuyManager(gameState);
		String BuyString = "B U Y";
		String ApplyString = "A P P L Y";
		String ApplyingString = "U N A P P L Y";
		String PrizeString = "2 0 0";
		int x1 = screen.getWidth()/7+20;
		int x2 = screen.getWidth() *5/8+20;
		int y1 = screen.getHeight()/6;
		int y2 = screen.getHeight()*4/7 - 30;


		int rectWidth = screen.getWidth();
		int rectHeight = screen.getHeight() / 6;
		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics.fillRect(0, screen.getHeight() / 2 - rectHeight / 2,
				rectWidth, rectHeight);
		backBufferGraphics.setColor(Color.green);
		drawCenteredBigString(screen, skinStoretxt,	screen.getHeight()/4 - 97);
		drawHorizontalLine(screen, screen.getHeight()/14);
		drawEntity(SpriteType.ShipA, x1, y1, 5, 5, Color.YELLOW);
		drawEntity(SpriteType.ShipA, x2, y1, 5, 5, Color.BLUE);
		drawEntity(SpriteType.ShipA, x1, y2, 5, 5, Color.RED);
		drawEntity(SpriteType.ShipA, x2, y2, 5, 5, Color.CYAN);

		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.yellow);
		backBufferGraphics.drawString(PrizeString, screen.getWidth()/7 + 33 , screen.getHeight()/2 - 35);
		backBufferGraphics.drawString(PrizeString, screen.getWidth()*5/8 + 33, screen.getHeight()/2 - 35);
		backBufferGraphics.drawString(PrizeString, screen.getWidth()/7 + 33 , screen.getHeight() - 115);
		backBufferGraphics.drawString(PrizeString, screen.getWidth()*5/8 + 33 , screen.getHeight() - 115);

		if (option == 8)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(EnhanceString, screen.getWidth()/15 - 20, screen.getHeight() - 30);
		if (option == 2)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(continueString, screen.getWidth()/3, screen.getHeight() - 30);
		if (option == 35)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		backBufferGraphics.drawString(itemStoreString, screen.getWidth() - 140, screen.getHeight() - 30);
		if (option == 86)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		if (skinBuyManager.isSkinOwned(Color.YELLOW)){
			if(skinBuyManager.isSkinEquipped(Color.YELLOW)){
				gameState.setNowSkinString(ApplyingString);
			}
			else {
				gameState.setNowSkinString(ApplyString);
			}
		} else {
			gameState.setNowSkinString(BuyString);
		}
		backBufferGraphics.drawString(gameState.getNowSkinString(), screen.getWidth()/7 + 33 , screen.getHeight()/2 - 15);
		if (option == 88)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		if (skinBuyManager.isSkinOwned(Color.BLUE)){
			if(skinBuyManager.isSkinEquipped(Color.BLUE)){
				gameState.setNowSkinString(ApplyingString);
			} else {
				gameState.setNowSkinString(ApplyString);
			}
		} else {
			gameState.setNowSkinString(BuyString);
		}
		backBufferGraphics.drawString(gameState.getNowSkinString(), screen.getWidth() *5/8 + 33, screen.getHeight()/2 - 15);
		if (option == 87)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		if (skinBuyManager.isSkinOwned(Color.RED)){
			if(skinBuyManager.isSkinEquipped(Color.RED)){
				gameState.setNowSkinString(ApplyingString);
			}
			else {
				gameState.setNowSkinString(ApplyString);
			}
		} else {
			gameState.setNowSkinString(BuyString);
		}
		backBufferGraphics.drawString(gameState.getNowSkinString(), screen.getWidth()/7+33, screen.getHeight() - 95);
		if (option == 89)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		if (skinBuyManager.isSkinOwned(Color.CYAN)){
			if(skinBuyManager.isSkinEquipped(Color.CYAN)){
				gameState.setNowSkinString(ApplyingString);
			}
			else {
				gameState.setNowSkinString(ApplyString);
			}
		} else {
			gameState.setNowSkinString(BuyString);
		}
		backBufferGraphics.drawString(gameState.getNowSkinString(), screen.getWidth()*5/8 + 33, screen.getHeight() - 95);

	}

	/**
	 * Draws String on Enhance screen.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param enhanceString
	 *               String to draw on.
	 * @param positionX
	 *               X coordinate of the line.
	 * @param positionY
	 *               Y coordinate of the line.
	 * @param color
	 *               Color of the String on Enhance Screen.
	 * @param fontSizeOption
	 *               Option of font size.
	 */
	public void drawEnhanceStoneString(final Screen screen, final String enhanceString,
									   final int positionX, final int positionY,
									   final Color color, int fontSizeOption) {

		if (fontSizeOption == 0)
			backBufferGraphics.setFont(fontSmall);
		else if (fontSizeOption == 1)
			backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(color);

		int textWidth = backBufferGraphics.getFontMetrics().stringWidth(enhanceString);
		int centerX = positionX - textWidth / 2;

		backBufferGraphics.drawString(enhanceString, centerX, positionY);
	}

	/**
	 * Draws Sprites on Enhance screen.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param centeredCircleX
	 *               X coordinate of the centered Circle.
	 * @param centeredCircleY
	 *               Y coordinate of the centered Circle.
	 * @param centeredCircleWidth
	 *               Width of the centered Circle.
	 * @param centeredCircleHeight
	 *               Height of the centered Circle.
	 * @param leftCircleX
	 *               X coordinate of the left Circle.
	 * @param rightCircleX
	 *               X coordinate of the right Circle.
	 * @param sideCircleY
	 *               Y coordinate of the left and right Circle.
	 * @param sideCircleWidth
	 *               Width of the left and right Circle.
	 * @param sideCircleHeight
	 *               Height of the left and right Circle.
	 */
	public void drawEnhanceSprite(final Screen screen,
								  final int centeredCircleX, final int centeredCircleY,
								  final int centeredCircleWidth, final int centeredCircleHeight,
								  final int leftCircleX, final int rightCircleX, final int sideCircleY,
								  final int sideCircleWidth, final int sideCircleHeight) {
		SpriteType CurrentShip = SpriteType.ShipA;
		SpriteType BlueEnhanceAreaStone = SpriteType.BlueEnhanceStone;
		SpriteType PerpleEnhanceAttackStone = SpriteType.PerpleEnhanceStone;

		this.drawEntity(CurrentShip, centeredCircleX + centeredCircleWidth / 3 + 9,
				centeredCircleY + centeredCircleHeight / 4, 3, 3, Color.white);
		this.drawEntity(BlueEnhanceAreaStone, leftCircleX + sideCircleWidth / 4 - 2,
				sideCircleY + sideCircleHeight / 4 - 2, 5, 5, Color.BLUE);
		this.drawEntity(PerpleEnhanceAttackStone, rightCircleX + sideCircleWidth / 4 - 2,
				sideCircleY + sideCircleHeight / 4 - 2, 5, 5, Color.magenta);
	}

	/**
	 * Draws String on Enhance Menu.
	 *
	 * @param screen
	 *               Screen to draw on.
	 * @param option
	 *               Option selected.

	 * @param lvEnhanceArea
	 *               Current Level of Enhanced Area Range.
	 * @param lvEnhanceDamage
	 *               Current Level of Enhanced Damage.
	 * @param attackDamage
	 *               Current Damage about attackDamage.
	 * @param addedValAttackDamage
	 *               Value to be added of Attack Damage.
	 * @param requiredNumEnhanceAttackStone
	 *               Required Number of Enhance Attack Stone.
	 */
	public void drawEnhanceMenu(final Screen screen, final int option,
								final int numEnhanceArea, final int numEnhanceDamage,
								final int lvEnhanceArea, final int lvEnhanceDamage,
								final int attackDamage, final int addedValAttackDamage,
								final int requiredNumEnhanceAttackStone) {
		String subMenuString = "S U B M E N U";
		String itemStoreString = "I T E M S T O R E";
		String playString = "C O N T I N U E";
		String lvEnhanceAreaString = "Area Lv" + Integer.toString(lvEnhanceArea) + " > "
				+ Integer.toString(lvEnhanceArea + 1);
		String lvEnhanceDamageString = "Attack Lv" + Integer.toString(lvEnhanceDamage) + " > "
				+ Integer.toString(lvEnhanceDamage + 1);
		String valEnhanceAreaString =  Integer.toString(numEnhanceArea) + "/1";
		String valEnhanceDamageString = Integer.toString(numEnhanceDamage) + "/" + Integer.toString(requiredNumEnhanceAttackStone);
		String changedAttackDamageString = Integer.toString(attackDamage) + ">" + Integer.toString(attackDamage + addedValAttackDamage);
		String changedAreaString = "";

		if (lvEnhanceArea == 0) {
			changedAreaString = ">|";
		} else if (lvEnhanceArea == 1) {
			changedAreaString = "|>|x2";
		} else if (lvEnhanceArea == 2) {
			changedAreaString = "|x2>|x3";
		} else {
			changedAreaString = "|x3>|x3";
		}

		if (lvEnhanceArea >= 3) {
			lvEnhanceAreaString = "Area Lv" + Integer.toString(lvEnhanceArea) + " > "
					+ Integer.toString(lvEnhanceArea);
			valEnhanceAreaString =  Integer.toString(numEnhanceArea) + "/0";
		}
		if (lvEnhanceDamage >= 6) {
			lvEnhanceDamageString = "Attack Lv" + Integer.toString(lvEnhanceDamage) + " > "
					+ Integer.toString(lvEnhanceDamage);
			valEnhanceDamageString = Integer.toString(numEnhanceDamage) + "/0";
			changedAttackDamageString = Integer.toString(attackDamage) + ">" + Integer.toString(attackDamage);
		}

		/** Height of the interface separation line. */
		int SEPARATION_LINE_HEIGHT = 40;

		int screenWidth = screen.getWidth();
		int centeredCircleWidth = 170;
		int centeredCircleHeight = 170;
		int centeredCircleX = (screenWidth - 170) / 2;
		int centeredCircleY = SEPARATION_LINE_HEIGHT * 2;
		int sideCircleWidth = 70;
		int sideCircleHeight = 70;
		int leftCircleX = (screenWidth - 220) / 2;
		int rightCircleX = screenWidth - (screenWidth - 220) / 2 - 70;
		int sideCircleY = SEPARATION_LINE_HEIGHT * 5;

		backBufferGraphics.setColor(Color.GREEN);



		if (option == 8){
			drawEnhanceStoneString(screen, valEnhanceAreaString,
					leftCircleX + sideCircleWidth / 2, sideCircleY + sideCircleHeight + 60,
					Color.GREEN, 1);
			drawEnhanceStoneString(screen, lvEnhanceAreaString,
					centeredCircleX + centeredCircleWidth / 2, centeredCircleY + centeredCircleHeight * 4 / 5 - 30,
					Color.lightGray, 0);
			drawEnhanceStoneString(screen, "Direction",
					centeredCircleX + centeredCircleWidth / 2, centeredCircleY + centeredCircleHeight * 4 / 5 - 60,
					Color.cyan, 0);
			drawEnhanceStoneString(screen, changedAreaString,
					centeredCircleX + centeredCircleWidth / 2, centeredCircleY + centeredCircleHeight * 4 / 5 - 50,
					Color.cyan, 0);
		}
		else{
			drawEnhanceStoneString(screen, valEnhanceAreaString,
					leftCircleX + sideCircleWidth / 2, sideCircleY + sideCircleHeight + 60,
					Color.WHITE, 1);
			drawEnhanceStoneString(screen, lvEnhanceAreaString,
					centeredCircleX + centeredCircleWidth / 2, centeredCircleY + centeredCircleHeight * 4 / 5 - 30,
					Color.GRAY, 0);
		}
		if (option == 9){
			drawEnhanceStoneString(screen, valEnhanceDamageString,
					rightCircleX + sideCircleWidth / 2, sideCircleY + sideCircleHeight + 60,
					Color.GREEN, 1);
			drawEnhanceStoneString(screen, lvEnhanceDamageString,
					centeredCircleX + centeredCircleWidth / 2, centeredCircleY + centeredCircleHeight * 4 / 5 - 10,
					Color.lightGray, 0);
			drawEnhanceStoneString(screen, "Damage",
					centeredCircleX + centeredCircleWidth / 2, centeredCircleY + centeredCircleHeight * 4 / 5 - 60,
					Color.magenta, 0);
			drawEnhanceStoneString(screen, changedAttackDamageString,
					centeredCircleX + centeredCircleWidth / 2, centeredCircleY + centeredCircleHeight * 4 / 5 - 50,
					Color.magenta, 0);
		}
		else{
			drawEnhanceStoneString(screen, valEnhanceDamageString,
					rightCircleX + sideCircleWidth / 2, sideCircleY + sideCircleHeight + 60,
					Color.WHITE, 1);
			drawEnhanceStoneString(screen, lvEnhanceDamageString,
					centeredCircleX + centeredCircleWidth / 2, centeredCircleY + centeredCircleHeight * 4 / 5 - 10,
					Color.GRAY, 0);
		}
		if (option == 5)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, subMenuString,
				screen.getHeight() / 3 * 2 + 45);
		if (option == 6)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, itemStoreString,
				screen.getHeight() / 3 * 2 + fontRegularMetrics.getHeight() * 2 + 45);
		if (option == 2)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, playString,
				screen.getHeight() / 3 * 2 + fontRegularMetrics.getHeight() * 4 + 45);
	}

	/**
	 * Creates a loading string with blink effect on the loading box.
	 *
	 * [Clean Code Team] This method was created by dodo_kdy.
	 *
	 *
	 */
	public void drawLoadingString(int x, int y, String string) {
		backBufferGraphics.setColor(Color.white);
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(string, x, y);

		if (timercount % 25 == 0) backBufferGraphics.setColor(new Color(253, 253, 253));
		else backBufferGraphics.setColor(new Color(255, 255, 255, 55));

		backBufferGraphics.drawString("...", x + fontBigMetrics.stringWidth("LOADING"), y);
	}



	/**
	 * Creates a loading progress bar/
	 *
	 * [Clean Code Team] This method was created by dodo_kdy.
	 *
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param g2
	 */
	public void loadingProgress(float startX, float startY, float endX, float endY, Graphics2D g2) {
		Color endColor = Color.green;
		Color startColor = Color.yellow;

		GradientPaint gradient = new GradientPaint(startX, startY, startColor,  endX, endY , endColor);
		g2.setPaint(gradient);
		g2.fill(new Rectangle2D.Double(startX, startY, endX - startX, endY - startY));

		g2.setColor(Color.black);
		g2.fill(new Rectangle2D.Double(startX, startY, endX - startX , endY - startY  - timercount));
	}

	/**
	 * Creates a loading box for 3 seconds.
	 *
	 * [Clean Code Team] This method was created by dodo_kdy.
	 *
	 * @param x
	 * @param y
	 * @param screen
	 */

	public void drawLoading(int x, int y, Screen screen) {
		float box1_W = screen.getWidth() / 2, box1_H = box1_W / 2;
		Graphics2D g2 = (Graphics2D) backBufferGraphics;

		/* Background Box */
		g2.setColor(new Color(0, 255, 0, 230));
		g2.fill(new Rectangle2D.Double(x, y, box1_W, box1_H));
		drawLoadingString((int) (x + box1_W / 5), (int) (y + box1_H * 0.85), "LOADING");

		/* Loading Box */
		float box2_x = x + box1_W + screen.getWidth() / 30, box2_W = box1_W / 5;
		g2.setColor(new Color(0, 255, 0, 222));
		g2.fill(new Rectangle2D.Double(box2_x, y, box2_W, box1_H));

		float dx = box2_W / 7;
		g2.setColor(Color.black);
		g2.fill(new Rectangle2D.Double(box2_x + dx, y + dx, box2_W - 2 * dx, box1_H - 2 * dx));

		/* Loading progress bar */
		float startX = box2_x + dx + dx/2 , startY = y + dx + dx/2,
				endX = startX + box2_W - 3*dx, endY = startY + box1_H - 3 * dx;
		loadingProgress(startX, startY, endX, endY, g2);

		/* Animation box*/
		g2.setColor(Color.black);
		g2.fill(new Rectangle2D.Double(x + box1_W * 0.075, y + box1_W * 0.075, box1_W * 0.85, box1_H * 0.45));
		animateLoading((int) (x + (box1_W * 3) / 44), (int) (y + (box1_W * 3) / 44));

		/* Box border */
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.white);
		g2.draw(new Rectangle2D.Double(x - 1, y - 1, box1_W + 2, box1_H + 2));
		g2.setColor(new Color(255, 255, 255, 222));

		g2.draw(new Rectangle2D.Double(box2_x - 1, y - 1, box2_W + 1, box1_H + 1));

		timercount++;
	}

	public void gameOver(final Screen screen, boolean levelFinished, double lives,int bullets, CountUpTimer timer, Coin coin, String clearcoin){
		if(levelFinished){
			if(lives <= 0 || bullets<=0){
				backBufferGraphics.setColor(animateColor(new Color(0, 0, 0, 0), Color.black, 3000, endTimer));
				backBufferGraphics.fillRect(0, 0, screen.getWidth(), screen.getHeight());

				backBufferGraphics.setFont(fontBig);
				backBufferGraphics.setColor(Color.red);
				backBufferGraphics.drawString("Game Over", screen.getWidth() / 2 - fontBigMetrics.stringWidth("Game Over") / 2, screen.getHeight() / 2);
			}
			else {
				String getClearTime = "" + (int)(timer.getElapsedTime() / 1000) + "." +  (timer.getElapsedTime() % 1000);
				backBufferGraphics.setFont(fontBig);
				backBufferGraphics.setColor(Color.white);
				backBufferGraphics.drawString("Stage Clear", screen.getWidth() / 2 - fontBigMetrics.stringWidth("Stage Clear") / 2, screen.getHeight() / 2);
				backBufferGraphics.drawString(getClearTime, screen.getWidth() / 2 - fontBigMetrics.stringWidth(getClearTime) / 2, screen.getHeight() / 2 + 20);
				if ((int)(timer.getElapsedTime() / 1000) > 0 && (int)(timer.getElapsedTime() / 1000) < 30) {
					backBufferGraphics.drawString("COIN : 20", screen.getWidth() / 2 - fontBigMetrics.stringWidth("COIN : 20") / 2, screen.getHeight() / 2 + 40);

				}
				else if ((int)(timer.getElapsedTime() / 1000) >= 30 && (int)(timer.getElapsedTime() / 1000) < 40) {
					backBufferGraphics.drawString("COIN : 15", screen.getWidth() / 2 - fontBigMetrics.stringWidth("COIN : 15") / 2, screen.getHeight() / 2 + 40);
				}
				else if ((int)(timer.getElapsedTime() / 1000) >= 40 && (int)(timer.getElapsedTime() / 1000) < 50) {
					backBufferGraphics.drawString("COIN : 10", screen.getWidth() / 2 - fontBigMetrics.stringWidth("COIN : 10") / 2, screen.getHeight() / 2 + 40);
				}
				else{
					backBufferGraphics.drawString("COIN : 5", screen.getWidth() / 2 - fontBigMetrics.stringWidth("COIN : 5") / 2, screen.getHeight() / 2 + 40);
				}
			}
		}
	}

	public void changeGhostColor(boolean levelFinished, double lives){
		if(levelFinished && lives == 0) {
			int ghostColorValue;
			if (225 < (255 - ((int)(System.currentTimeMillis() - ghostTImer) / 10)))
				ghostColorValue = 225;
			else if (0 < (255 - ((int)(System.currentTimeMillis() - ghostTImer) / 10)))
				ghostColorValue = (255 - ((int)(System.currentTimeMillis() - ghostTImer) / 10));
			else
				ghostColorValue = 0;
			ghostColor = new Color(ghostColorValue, ghostColorValue, ghostColorValue, 0);
			//backBufferGraphics.setColor(ghostColor);
		}
	}


	public void drawGhost(boolean levelFinished, double lives){
		if(levelFinished && lives == 0) {
			boolean timer = (System.currentTimeMillis() - ghostTImer) % 2 == 0;
			System.out.println(ghostColor);
			if(timer){
				if(System.currentTimeMillis() - ghostTImer < 1000)
					this.drawEntity(SpriteType.Ghost, ghostPostionX--, ghostPostionY--, 2, 2, Color.white);
				else if (System.currentTimeMillis() - ghostTImer < 2000)
					this.drawEntity(SpriteType.Ghost, ghostPostionX++, ghostPostionY--, 2, 2, Color.white);
				else
					this.drawEntity(SpriteType.Ghost, ghostPostionX--, ghostPostionY--, 2, 2, Color.white);
			}
			else {
				if(System.currentTimeMillis() - ghostTImer < 1000)
					this.drawEntity(SpriteType.Ghost, ghostPostionX, ghostPostionY, 2, 2, Color.white);
				else if (System.currentTimeMillis() - ghostTImer < 2000)
					this.drawEntity(SpriteType.Ghost, ghostPostionX, ghostPostionY, 2, 2, Color.white);
				else
					this.drawEntity(SpriteType.Ghost, ghostPostionX, ghostPostionY, 2, 2, Color.white);
			}
		}
	}


	/**
	 * Creates an animation of monster.
	 * [Clean Code Team] This method was created by dodo_kdy.
	 *
	 * @param x
	 * @param y
	 */
	public void animateLoading(int x, int y){
		int y1 = y+7, x1 = x;
		if ( (30 <timercount && timercount<50) || (110 <timercount && timercount<130) ) y1 -=5;
		else if (70<timercount && timercount <90) x1+=5;

		this.drawEntity(SpriteType.ESnA_1,x1+15,y1+10,2.3,2.3, Color.white);
		this.drawEntity(SpriteType.ESnB_1,x1+60,y1+10,2.4,2.4, Color.white);
		this.drawEntity(SpriteType.ESm1_1,x1+100,y1+10,3,2.4, Color.white);
		this.drawEntity(SpriteType.ESm2B_1,x1+145,y1+13,2,2, Color.white);
	}


	/**
	 * Draws basic gradient background that animates between colors.
	 * [Clean Code Team] This method was created by alicek0.
	 * @param screen
	 * @param separationLineHeight
	 * @param lives
	 */
	public void drawBackground(final Screen screen, int separationLineHeight, int lives){
		int height = screen.getHeight();
		int width = screen.getWidth();

		if (bgTimer.checkFinished()){
			brightness+= lighter;
			if (brightness >= 70) lighter *= -1;
			else if (brightness <= 0) lighter *= -1;
			bgTimer.reset();
		}

		Graphics2D g2 = (Graphics2D)backBufferGraphics;
		GradientPaint gp = new GradientPaint(0, separationLineHeight, new Color(31, 0, 0, 216), 0, height, new Color(brightness,brightness/2,100+brightness/2,230));
		g2.setPaint(gp);
		g2.fill(new Rectangle(0, separationLineHeight, width, height));

		if (lives <= 3) {
			backBufferGraphics.setColor(new Color(10, 0,  0, 200 - (lives * 50)));
			backBufferGraphics.fillRect(0, separationLineHeight, width, height);
		}
	}

	/**
	 * Draws background that fades from white to black at game start.
	 * [Clean Code Team] This method was created by alicek0.
	 * @param screen
	 * 			Screen to draw on. Used for dimensions.
	 * @param separationLineHeight
	 * 			To determine where the background should start.
	 */
	public void drawBackgroundStart(final Screen screen, int separationLineHeight){
		int height = screen.getHeight();
		int width = screen.getWidth();
		backBufferGraphics.setColor(animateColor(Color.white, new Color(0, 0, 0, 0), 3000, bgTimer_init));
		backBufferGraphics.fillRect(0, separationLineHeight, width, height);
	}

	/**
	 * Draws transparent red background that increases in opacity when Special Enemy appears.
	 * [Clean Code Team] This method was created by alicek0.
	 * @param screen
	 * @param separationLineHeight
	 */
	public void drawBackgroundSpecialEnemy(final Screen screen, int separationLineHeight){
		int height = screen.getHeight();
		int width = screen.getWidth();
		backBufferGraphics.setColor(new Color(50, 50, 0, brightness));
		backBufferGraphics.fillRect(0, separationLineHeight, width, height);
	}



	/**
	 *	Returns color between two colors over duration. Used to animate color.
	 * [Clean Code Team] This method was created by alicek0.
	 * @param color1
	 * @param color2
	 * @param duration
	 * 			How long it should take to go from c1 to c2 in milliseconds.
	 * @return
	 */
	public Color animateColor(Color color1, Color color2, int duration, Cooldown timer){
		int red, green, blue, alpha;
		float ratio = (float)timer.timePassed()/duration;
		if (ratio >=1) ratio = 1;
		red = (int)((float)color1.getRed() * (1-ratio) + (float)color2.getRed() * ratio);
		green = (int)((float)color1.getGreen() * (1-ratio) + (float)color2.getGreen() * ratio);
		blue = (int)((float)color1.getBlue() * (1-ratio) + (float)color2.getBlue() * ratio);
		alpha = (int)((float)color1.getAlpha() * (1-ratio) + (float)color2.getAlpha() * ratio);
		return new Color(red, green, blue, alpha);
	}

	/**
	 * Draws green glow behind player sprite.
	 * [Clean Code Team] This method was created by alicek0.
	 * @param screen
	 * @param separationLineHeight
	 * @param playerX
	 * @param playerY
	 * @param playerWidth
	 * @param playerHeight
	 */
	public void drawBackgroundPlayer(final Screen screen, int separationLineHeight, int playerX, int playerY, int playerWidth, int playerHeight){
		Point2D center = new Point2D.Float(playerX + playerWidth/2, playerY + playerHeight/2);
		float radius = 90;
		float[] dist = {0.0f, 0.2f, 1.0f};
		Color[] colors = {new Color(178,245,149,brightness), new Color(178,245,149,brightness+20), new Color(0,0,0,0)};
		RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
		Graphics2D g2 = (Graphics2D) backBufferGraphics;
		g2.setPaint(p);
		g2.fillRect(0, separationLineHeight, screen.getWidth(), screen.getHeight());
	}

	/**
	 * Draws background lines.
	 * [Clean Code Team] This method was created by alicek0.
	 * @param screen
	 * @param separationLineHeight
	 */
	public void drawBackgroundLines(final Screen screen, int separationLineHeight){

		int xPaddingTop = 140;
		int lineCount = 30;
		int xPaddingBottom = -500;

		// Vertical Lines
		backBufferGraphics.setColor(new Color(255,255,255,70));
		Graphics2D gr2 = (Graphics2D)backBufferGraphics;
		Stroke defaultStroke=gr2.getStroke();
		gr2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,  BasicStroke.JOIN_MITER, 10.0f, new float[] {10.0f,9.0f,3.0f,9.0f},10.0f));
		for (int i = 0; i<lineCount; i++){
			gr2.drawLine(xPaddingTop + (screen.getWidth()-xPaddingTop-xPaddingTop)/(lineCount-1) * i , separationLineHeight, xPaddingBottom + (screen.getWidth()-xPaddingBottom-xPaddingBottom)/(lineCount-1) * i, screen.getHeight());
		}

		// Gradient to fade top part of vertical lines.
		GradientPaint gp = new GradientPaint(0, separationLineHeight, new Color(10, 0, 0, 255), 0, screen.getHeight(), new Color(255,255,255,0));
		gr2.setPaint(gp);
		gr2.fillRect(0, separationLineHeight, screen.getWidth(), screen.getHeight());

		// Horizontal Lines
		gr2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,  BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f, 5.0f, 5.0f, 5.0f},10.0f));

		backBufferGraphics.setColor(new Color(255,255,255,70));
		for (int i = 0; i<lineCount; i++){
			int max_opacity = 130;
			int opacity = (i*10<=max_opacity?i*10:max_opacity);
			backBufferGraphics.setColor(new Color(255,255,255,opacity));
			backBufferGraphics.drawLine(0, separationLineHeight+lineConstant+i*30, screen.getWidth(), separationLineHeight+lineConstant+i*30);
		}

		((Graphics2D) backBufferGraphics).setStroke(defaultStroke);

		if (bgTimer_lines.checkFinished()) {
			lineConstant++;
			bgTimer_lines.reset();
		}
		if (lineConstant >= 30) lineConstant = 0;
	}

	/**
	 * Resets background timer.
	 * [Clean Code Team] This method was created by alicek0.
	 */
	public void initBackgroundTimer(final Screen screen, int separationLineHeight){
		bgTimer_init.reset();
		bgTimer_lines.reset();
	}

	public void ComboCount(final Screen screen, final int ComboCount) {
		if (ComboCount !=0) {
			backBufferGraphics.setFont(fontRegular);
			backBufferGraphics.setColor(Color.WHITE);
			String text = String.format("%d", ComboCount) + " Combo";
			backBufferGraphics.drawString(text, screen.getWidth() - 90, 80);
		}
		else{
			backBufferGraphics.setFont(fontRegular);
			backBufferGraphics.setColor(Color.red);
			String text = "Miss";
			backBufferGraphics.drawString(text, screen.getWidth() - 90, 80);
		}
	}
	public void drawRanking(final Screen screen,
							final ArrayList<String> highScores) {
		backBufferGraphics.setColor(blinkingColor("WHITE"));
		int i = 0;
		String scoreString = "";
		String rank[] = {"1st", "2nd", "3th", "4th", "5th"};

		for (String str : highScores) {
			drawCenteredRegularString(screen, str, screen.getHeight()
					/ 3 + fontRegularMetrics.getHeight() * (i + 1) * 2);
			i++;
			if (i >= 5)
				break;
		}
	}
	public void drawFriend(final Screen screen,
							final ArrayList<String> highScores) {
		backBufferGraphics.setColor(blinkingColor("WHITE"));
		int i = 0;
		String scoreString = "";
		String rank[] = {"1st", "2nd", "3th", "4th", "5th"};

		for (String str : highScores) {
			drawCenteredRegularString(screen, str, screen.getHeight()
					/ 3 + fontRegularMetrics.getHeight() * (i + 1) * 2);
			i++;
			if (i >= 10)
				break;
		}
	}
	public void drawFriendMenu(final Screen screen, final int option){
		String SelectString = "Select Mode with W + S, confirm with SPACE.";
		String AddDeleteString = "Select Add or Delete, confirm with L";
		String FriendListString = "F R I E N D L I S T";
		String AddFriendString = "A D D F R I E N D";
		String DeleteFriendString = "D E L E T E F R I E N D";
		String MainMenuString = "M a i n  M e n u";

		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, SelectString, screen.getHeight() / 8);
		backBufferGraphics.setColor(blinkingColor("GRAY"));
		drawCenteredRegularString(screen, AddDeleteString, screen.getHeight() - fontRegularMetrics.getHeight());
		if (option == 43)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, FriendListString,
				screen.getHeight() / 3 * 2);
		if(option == 44)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, AddFriendString, screen.getHeight() / 3 * 2
				+fontRegularMetrics.getHeight() * 2);
		if(option == 45)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, DeleteFriendString, screen.getHeight() / 3 * 2
				+ fontRegularMetrics.getHeight() * 4);
		if (option == 1)
			backBufferGraphics.setColor(blinkingColor("GREEN"));
		else
			backBufferGraphics.setColor(blinkingColor("WHITE"));
		drawCenteredRegularString(screen, MainMenuString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 6);

	}
}