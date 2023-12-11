package screen;

import java.awt.event.KeyEvent;

import engine.Cooldown;
import engine.Core;

public class SelectScreen extends Screen {

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;
    private int difficulty;
    /** For selection moving sound */

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
    public SelectScreen(final int width, final int height, final int fps, final int diff){
        super(width, height, fps);

        // Defaults to EASY play.
        difficulty = diff;
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

        return this.difficulty;
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
                previousMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                nextMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                this.isRunning = false;
            }
        }
    }

    /**
     * Shifts the focus to the next menu item.
     */
    private void nextMenuItem() {
        if (this.difficulty == 0)
            this.difficulty = 1;
        else if (this.difficulty == 1)
            this.difficulty = 2;
        else if (this.difficulty == 2)
            this.difficulty = 3;
        else if (this.difficulty == 3)
            this.difficulty = 4;
        else
            this.difficulty = 0;
    }

    /**
     * Shifts the focus to the previous menu item.
     */
    private void previousMenuItem() {
        if (this.difficulty == 0)
            this.difficulty = 4;
        else if (this.difficulty == 1)
            this.difficulty = 0;
        else if (this.difficulty == 2)
            this.difficulty = 1;
        else if (this.difficulty == 3)
            this.difficulty = 2;
        else this.difficulty = 3;

    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawSelect(this, this.difficulty);

        drawManager.completeDrawing(this);
    }
}