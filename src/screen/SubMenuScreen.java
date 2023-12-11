package screen;

import java.awt.event.KeyEvent;

import engine.Cooldown;
import engine.Core;

public class SubMenuScreen extends Screen {
    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;
    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;
    /** For selection moving sound */


    /**
     * Constructor, establishes the properties of the screen.
     *
     *                  Current game state.
     * @param width
     *                  Screen width.
     * @param height
     *                  Screen height.
     * @param fps
     *                  Frames per second, frame rate at which the game is run.
     */
    public SubMenuScreen(int width, int height, int fps) {
        super(width, height, fps);

        // Defaults to play.
        this.returnCode = 2;
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
                previousMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                nextMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)){
                this.isRunning = false;
            }
        }
    }

    /**
     * Shifts the focus to the next menu item.
     */
    private void nextMenuItem() {
        if (this.returnCode == 6)
            this.returnCode = 7;
        else if (this.returnCode == 7)
            this.returnCode = 2;
        else if (this.returnCode == 2)
            this.returnCode = 86;
        else
            this.returnCode = 6;
    }

    /**
     * Shifts the focus to the previous menu item.
     */
    private void previousMenuItem() {
        if (this.returnCode == 86)
            this.returnCode = 2;
        else if (this.returnCode == 2)
            this.returnCode = 7;
        else if (this.returnCode == 7)
            this.returnCode = 6;
        else
            this.returnCode = 86;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        // drawManager.drawTitle(this);
        drawManager.drawSubMenu(this, this.returnCode);

        drawManager.completeDrawing(this);
    }
}
