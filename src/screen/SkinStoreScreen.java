package screen;

import java.awt.Color;
import java.awt.event.KeyEvent;

import engine.Cooldown;
import engine.Core;
import engine.EnhanceManager;
import engine.GameState;
import engine.SkinBuyManager;
import entity.Coin;

public class SkinStoreScreen extends Screen {
     /** Milliseconds between changes in user selection. */
     private static final int SELECTION_TIME = 200;
  
     /** Time between changes in user selection. */
     private Cooldown selectionCooldown;

    /** For selection moving sound */

    private Coin coin;

    private EnhanceManager enhanceManager;
    private GameState gameState;
    private SkinBuyManager skinBuyManager;

    private int skinPrice = 200;

    private Color skinColor1 = Color.YELLOW;
    private Color skinColor2 = Color.BLUE;
    private Color skinColor3 = Color.RED;
    private Color skinColor4 = Color.CYAN;
 
     /**
      * Constructor, establishes the properties of the screen.
      *
      * @param width
      *               Screen width.
      * @param height
      *               Screen height.
      * @param fps
      *               Frames per second, frame rate at which the game is run.
      */
     public SkinStoreScreen(final int width, final int height, final int fps, final GameState gameState, final EnhanceManager enhanceManager) {
         super(width, height, fps);
 
         // Defaults to play.
         this.returnCode = 86;
         this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
         this.selectionCooldown.reset();
         this.coin = gameState.getCoin();
         this.gameState = gameState;
         this.enhanceManager = enhanceManager;
         this.skinBuyManager = new SkinBuyManager(gameState);
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
                UpMenuSkin();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                DownMenuSkin();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                    || inputManager.isKeyDown(KeyEvent.VK_D)) {
                rightMenuSkin();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
                    || inputManager.isKeyDown(KeyEvent.VK_A)) {
                leftMenuSkin();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                if (returnCode == 86){
                    if(skinBuyManager.isSkinOwned(Color.YELLOW)){
                        if(skinBuyManager.isSkinEquipped(Color.YELLOW)){
                            skinBuyManager.unequipSkin(Color.YELLOW);
                        }
                        else{
                            skinBuyManager.equipSkin(Color.YELLOW);
                        }
                    } else if (!(skinBuyManager.isSkinOwned(Color.YELLOW))){
                        if (this.coin.getCoin() >= skinPrice) {
                            skinBuyManager.purchaseSkin(skinColor1, skinPrice);
                            skinBuyManager.equipSkin(skinColor1);
                            System.out.println("plese do");
                        }
                        else{
                        
                        }
                    }
                    else{
                        
                    }
                }
                if (returnCode == 88 && gameState != null){
                    if(skinBuyManager.isSkinOwned(skinColor2)){
                        if(skinBuyManager.isSkinEquipped(skinColor2)){
                            skinBuyManager.unequipSkin(skinColor2);
                        }
                        else{
                            skinBuyManager.equipSkin(skinColor2);
                        }
                    }
                    else{
                        if (this.coin.getCoin() >= skinPrice) {
                            skinBuyManager.purchaseSkin(skinColor2, skinPrice);
                            skinBuyManager.equipSkin(skinColor2);
                            System.out.println("plese do");
                        }
                        else{
                        
                        }
                    }
                }
                if (returnCode == 87 && gameState != null){
                    if(skinBuyManager.isSkinOwned(skinColor3)){
                        if(skinBuyManager.isSkinEquipped(skinColor3)){
                            skinBuyManager.unequipSkin(skinColor3);
                        }
                        else{
                            skinBuyManager.equipSkin(skinColor3);
                        }
                    }
                    else{
                        if (this.coin.getCoin() >= skinPrice) {
                            skinBuyManager.purchaseSkin(skinColor3, skinPrice);
                            skinBuyManager.equipSkin(skinColor3);
                            System.out.println("plese do");
                        }
                        else{
                        
                        }
                    }
                }
                if (returnCode == 89 && gameState != null){
                    if(skinBuyManager.isSkinOwned(skinColor4)){
                        if(skinBuyManager.isSkinEquipped(skinColor4)){
                            skinBuyManager.unequipSkin(skinColor4);
                        }
                        else{
                            skinBuyManager.equipSkin(skinColor4);
                        }
                    }
                    else{
                        if (this.coin.getCoin() >= skinPrice) {
                            skinBuyManager.purchaseSkin(skinColor4, skinPrice);
                            skinBuyManager.equipSkin(skinColor4);
                            System.out.println("plese do");
                        }
                        else{
                        
                        }
                    }
                }
                this.isRunning = false;
            }
        }
     }
 
     /**
      * Shifts the focus to the next menu item.
      */
      private void UpMenuSkin() {
        if (this.returnCode == 86)
            this.returnCode = 8;
        else if (this.returnCode == 8)
            this.returnCode = 87;
        else if (this.returnCode == 87)
            this.returnCode = 86;
        else if (this.returnCode == 88)
            this.returnCode = 35;
        else if (this.returnCode == 35)
            this.returnCode = 89;
        else if (this.returnCode == 89)
            this.returnCode = 88;
    }
 
    private void DownMenuSkin() {
        if (this.returnCode == 86)
            this.returnCode = 87;
        else if (this.returnCode == 87)
            this.returnCode = 8;
        else if (this.returnCode == 8)
            this.returnCode = 86;
        else if (this.returnCode == 88)
            this.returnCode = 89;
        else if (this.returnCode == 89)
            this.returnCode = 35;
        else if (this.returnCode == 35)
            this.returnCode = 88;
    }
    private void rightMenuSkin() {
        if (this.returnCode == 86)
            this.returnCode = 88;
        else if (this.returnCode == 88)
            this.returnCode = 86;
        else if (this.returnCode == 87)
            this.returnCode = 89;   
        else if (this.returnCode == 89)
            this.returnCode = 87;
        else if (this.returnCode == 8)
            this.returnCode = 2;
        else if (this.returnCode == 2)
            this.returnCode = 35;
        else if (this.returnCode == 35)
            this.returnCode = 8;                                                     
    }

    private void leftMenuSkin() {
        if (this.returnCode == 86)
            this.returnCode = 88;
        else if (this.returnCode == 88)
            this.returnCode = 86;
        else if (this.returnCode == 87)
            this.returnCode = 89;   
        else if (this.returnCode == 89)
            this.returnCode = 87;
        else if (this.returnCode == 8)
            this.returnCode = 35;  
        else if (this.returnCode == 35)
            this.returnCode = 2;
        else if (this.returnCode == 2)
            this.returnCode = 8;            
    }
 
     /**
      * Draws the elements associated with the screen.
      */
      private void draw() {
        drawManager.initDrawing(this);
        drawManager.drawCoin(this, this.coin, 2);
        drawManager.drawSkinStore(this.gameState, this, this.returnCode);
        drawManager.completeDrawing(this);
    }
    
    /**
	 * Returns a DrawManager object representing the status of the game.
	 *
	 * @return Current game state.
	 */
    public EnhanceManager getEnhanceManager() {
		return this.enhanceManager;
	}

    public GameState getGameState() {
        return this.gameState;
    }
}
