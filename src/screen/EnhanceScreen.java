package screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;

import engine.Cooldown;
import engine.Core;
import engine.EnhanceManager;
import engine.GameSettings;
import engine.GameState;
import entity.Coin;

/**
 * Implements the Enhance screen, where clicking 'Enhancement' on SubMenu Screen.
 */
public class EnhanceScreen extends Screen {
    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;
    /** Height of the interface separation line. */
    private static final int SEPARATION_LINE_HEIGHT = 40;
    /** Current score. */
    private int score;
    /** Current coin. */
    private Coin coin;
    /** Player lives left. */
	private double lives;
    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;
    /** Settings of Centered Circle Frame */
    private int centeredCircleWidth = 170;
    private int centeredCircleHeight = 170;
    private int centeredCircleX = (this.width - 170) / 2;
    private int centeredCircleY = SEPARATION_LINE_HEIGHT * 2;
    /** Settings of Both Side Circle Frame */
    private int sideCircleWidth = 70;
    private int sideCircleHeight = 70;
    private int leftCircleX = (this.width - 220) / 2;
    private int rightCircleX = this.width - (this.width - 220) / 2 - 70;
    private int sideCircleY = SEPARATION_LINE_HEIGHT * 5;
    /** EnhanceManager taken from the Core.java */
    private EnhanceManager enhanceManager;
    /** Game settings taken from the Core.java */
    private List<GameSettings> gameSettings;
    /** For selection moving sound */

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param enhanceManager
     *               Screen width.
     * @param gameSettings
     *               Screen width.
     * @param gameState
     *               Screen width.
     * @param width
     *               Screen width.
     * @param height
     *               Screen height.
     * @param fps
     *               Frames per second, frame rate at which the game is run.
     */
    public EnhanceScreen(final EnhanceManager enhanceManager, final List<GameSettings> gameSettings,
                         final GameState gameState, final int width, final int height, final int fps) {
        super(width, height, fps);
        this.enhanceManager = enhanceManager;
        this.gameSettings = gameSettings;
        
        // Defaults to play.
        this.returnCode = 8;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
        this.score = gameState.getScore();
        this.coin = gameState.getCoin();
        this.lives = gameState.getLivesRemaining();

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
        if (this.selectionCooldown.checkFinished()
                && this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W)) {
                previousVerticalMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                nextVerticalMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
                    || inputManager.isKeyDown(KeyEvent.VK_A)) {
                previousHorizontalMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                    || inputManager.isKeyDown(KeyEvent.VK_D)) {
                nextHorizontalMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)){
                if (this.returnCode == 8) {
                    if (this.enhanceManager.getlvEnhanceArea() <= 2)
                        this.enhanceManager.enhanceAreaDamage();
                    for (GameSettings gameSetting : this.gameSettings) {
                        gameSetting.setAreaDamage(this.enhanceManager.getlvEnhanceArea());
                    }
                }
                if (this.returnCode == 9) {
                    if (this.enhanceManager.getlvEnhanceAttack() <= 5)
                        this.enhanceManager.enhanceAttackDamage();
                    for (GameSettings gameSetting : this.gameSettings) {
                        gameSetting.setAttackDamage(this.enhanceManager.getAttackDamage());
                    }
                }
                this.isRunning = false;
            }
        }
    }

    /**
     * Shifts the focus to the next menu item. (Horizontal Ver.)
     */
    private void nextHorizontalMenuItem() { 
        if (this.returnCode == 8)
            this.returnCode = 9;
        else
            this.returnCode = 8;
    }

    /**
     * Shifts the focus to the next menu item. (Vertical Ver.)
     */
    private void nextVerticalMenuItem() {
        if (this.returnCode == 8 || this.returnCode == 9)
            this.returnCode = 5;
        else if (this.returnCode == 5)
            this.returnCode = 6;
        else if (this.returnCode == 6)
            this.returnCode = 2;
        else
            this.returnCode = 5;
    }

    /**
     * Shifts the focus to the previous menu item. (Horizontal Ver.)
     */
    private void previousHorizontalMenuItem() {
        if (this.returnCode == 8)
            this.returnCode = 9;
        else
            this.returnCode = 8;
    }

    /**
     * Shifts the focus to the previous menu item. (Vertical Ver.)
     */
    private void previousVerticalMenuItem() {
        if (this.returnCode == 8)
            this.returnCode = 8;
        else if (this.returnCode == 9)
            this.returnCode = 9;
        else if (this.returnCode == 2)
            this.returnCode = 6;
        else if (this.returnCode == 6)
            this.returnCode = 5;
        else
            this.returnCode = 8;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);
        drawManager.drawScore(this, this.score);
        drawManager.drawCoin(this, this.coin, 1);
        drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);

        drawManager.drawCircleLine(this, centeredCircleX, centeredCircleY, centeredCircleWidth, centeredCircleHeight, 0);
        drawManager.drawCircleFill(this, leftCircleX, sideCircleY, sideCircleWidth, sideCircleHeight);
        drawManager.drawCircleFill(this, rightCircleX, sideCircleY, sideCircleWidth, sideCircleHeight);
        drawManager.drawCircleLine(this, leftCircleX, sideCircleY, sideCircleWidth, sideCircleHeight, 0);
        drawManager.drawCircleLine(this, rightCircleX, sideCircleY, sideCircleWidth, sideCircleHeight, 0);
        drawManager.drawEnhanceSprite(this, centeredCircleX, centeredCircleY, centeredCircleWidth, centeredCircleHeight, 
                                            leftCircleX, rightCircleX, sideCircleY, sideCircleWidth, sideCircleHeight);

        String AreaString = "Area";
		String DamageString = "Damage";
		String EnhanceString = "Enhance";
		String StoneString = "Stone";
        int fontSizeOption = 0;

        drawManager.drawEnhanceStoneString(this, AreaString, 
                                            leftCircleX + sideCircleWidth / 2, sideCircleY + sideCircleHeight + 20, 
                                            Color.GRAY, fontSizeOption);
        drawManager.drawEnhanceStoneString(this, EnhanceString + " " + StoneString, 
                                            leftCircleX + sideCircleWidth / 2, sideCircleY + sideCircleHeight + 40, 
                                            Color.GRAY, fontSizeOption);
        drawManager.drawEnhanceStoneString(this, DamageString, 
                                            rightCircleX + sideCircleWidth / 2, sideCircleY + sideCircleHeight + 20, 
                                            Color.GRAY, fontSizeOption);
        drawManager.drawEnhanceStoneString(this, EnhanceString + " " + StoneString, 
                                            rightCircleX + sideCircleWidth / 2, sideCircleY + sideCircleHeight + 40, 
                                            Color.GRAY, fontSizeOption);

        drawManager.drawEnhanceMenu(this, this.returnCode, 
                                    this.enhanceManager.getNumEnhanceStoneArea(), this.enhanceManager.getNumEnhanceStoneAttack(), 
                                    this.enhanceManager.getlvEnhanceArea(), this.enhanceManager.getlvEnhanceAttack(),
                                    this.enhanceManager.getAttackDamage(), this.enhanceManager.getValEnhanceAttack(),
                                    this.enhanceManager.getRequiredNumEnhanceStoneAttack());

        drawManager.completeDrawing(this);
    }

    /**
	 * Returns a List of GameSettings object representing the status of the game.
	 *
	 * @return Current game settings.
	 */
    public List<GameSettings> getGameSettings() {
		return this.gameSettings;
	}

    /**
	 * Returns a DrawManager object representing the status of the game.
	 *
	 * @return Current game state.
	 */
    public EnhanceManager getEnhanceManager() {
		return this.enhanceManager;
	}
}
