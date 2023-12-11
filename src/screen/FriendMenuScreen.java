package screen;

import java.awt.event.KeyEvent;

import engine.Cooldown;
import engine.Core;

public class FriendMenuScreen extends Screen{
    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;
    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;
    /** For selection moving sound */


    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width
     *                  Screen width.
     * @param height
     *                  Screen height.
     * @param fps
     *                  Frames per second, frame rate at which the game is run.
     */
    public FriendMenuScreen(int width, int height, int fps) {
        super(width, height, fps);

        // Defaults to play.
        this.returnCode = 43;
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
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE) && (!(this.returnCode == 45) && !(this.returnCode == 44))) {

                this.isRunning = false;
            }
            if(inputManager.isKeyPressedOnce(KeyEvent.VK_L)){
                this.isRunning = false;
            }
        }
    }

    /**
     * Shifts the focus to the next menu item.
     */
    private void nextMenuItem() {
        if (this.returnCode == 43)
            this.returnCode = 44;
        else if(this.returnCode == 44)
            this.returnCode = 45;
        else if(this.returnCode == 45)
            this.returnCode = 1;
        else if(this.returnCode == 1)
            this.returnCode = 43;
    }

    /**
     * Shifts the focus to the previous menu item.
     */
    private void previousMenuItem() {
        if(this.returnCode == 43)
            this.returnCode = 1;
        else if(this.returnCode == 44)
            this.returnCode = 43;
        else if (this.returnCode == 45)
            this.returnCode = 44;
        else if(this.returnCode == 1)
            this.returnCode = 45;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawFriendMenu(this, this.returnCode);

        drawManager.completeDrawing(this);
    }
}

