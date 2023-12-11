package screen;

import engine.Cooldown;
import engine.Core;

import java.awt.event.KeyEvent;

public class StageSelectScreen extends Screen {

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;

    /** Stage Selected */
    private int Stage;

    /** Total number of Stages. */
    private int TotalStage;

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
    public StageSelectScreen(final int width, final int height, final int fps, final int Totalstage, final int stage){
        super(width, height, fps);

        // Defaults to Stage 1 (index = 0).
        Stage = stage-1;
        TotalStage = Totalstage;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();

    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        return this.Stage+1; //return selected stage (index + 1)
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
                UpMenuItem(Stage);
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                DownMenuItem(Stage);
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                    || inputManager.isKeyDown(KeyEvent.VK_D)) {
                RightMenuItem(Stage);
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
                    || inputManager.isKeyDown(KeyEvent.VK_A)) {
                LeftMenuItem(Stage);
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                this.isRunning = false;
            }
            if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                this.Stage = -1;
                this.isRunning = false;
            }
        }
    }

    /**
     * Shifts the focus to the right, left, down, and up menu item. Each line has 5 items.
     */
    private void RightMenuItem(int i) {
        if (this.Stage == TotalStage-1)
            this.Stage = 0;
        else
            this.Stage = i+1;
    }
    private void LeftMenuItem(int i) {
        if (this.Stage == 0)
            this.Stage = TotalStage-1;
        else
            this.Stage = i-1;
    }
    private void DownMenuItem(int i) {
        this.Stage = i + 5;
        if (this.Stage >= TotalStage)
            this.Stage = this.Stage % 5;
    }

    private void UpMenuItem(int i) {
        this.Stage = i - 5;
        if (this.Stage < 0) {
            if (TotalStage % 5 > i % 5)
                this.Stage = 5 * (TotalStage / 5) + i % 5;
            else
                this.Stage = 5 * (TotalStage / 5 - 1) + i % 5;
        }
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawStageSelect(this, this.Stage, this.TotalStage);

        drawManager.completeDrawing(this);
    }
}
