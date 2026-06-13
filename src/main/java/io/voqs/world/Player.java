package io.voqs.world;

import com.raylib.Helpers;
import com.raylib.Raylib;
import io.voqs.helpers.Position;
import io.voqs.blocks.BlockRegistry;
import io.voqs.globals.KeyInput;
import io.voqs.globals.EnginePreferences;
import io.voqs.helpers.Size;
import io.voqs.plugins.Metadata;

import java.util.Arrays;

public class Player {
    public int x, y;
    public Position cursorPos = Position.ZERO();

    private Position velocity = Position.ZERO();

    private Raylib.Camera2D camera;

    public float stepTime;
    public float blockManipulationTime;

    private final World world;
    public int currentBlockIndex = 0;
    public Metadata metadata;

    public Size windowSize;


    public Player(World w){
        x = 5;
        y = 5;
        stepTime = 0.2f;
        world = w;
        metadata = new Metadata();
    }

    public void update(){
        preHandling();

        handleMovement();

        handleMouse();

        handleCamera();

        handleBlocks();
    }

    private void preHandling() {
        if (camera == null){
            makeCamera();
        }

        checkResize();

        if (currentBlockIndex >= BlockRegistry.getVisibleBlocks().size()){
            currentBlockIndex = 0;
        }

        if (stepTime > 0) stepTime -= EnginePreferences.getDeltaTime();
        if (blockManipulationTime > 0) blockManipulationTime -= EnginePreferences.getDeltaTime();
    }

    private void handleBlocks() {
        boolean canManipulateBlocks = blockManipulationTime <= 0f;

        if (Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_RIGHT) && canManipulateBlocks){
            placeBlock();
        }

        if (Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT) && canManipulateBlocks){
            breakBlock();
        }

        if (KeyInput.wasKeyPressed("Z")){
            currentBlockIndex++;
        }
    }

    public void breakBlock() {
        world.removeBlock(cursorPos.x(), cursorPos.y());
        blockManipulationTime = EnginePreferences.getPlayerBuildDelay();
    }

    public void placeBlock() {
        world.placeBlock(cursorPos.x(), cursorPos.y(), getHolding());
        blockManipulationTime = EnginePreferences.getPlayerBuildDelay();
    }


    private void handleCamera() {
        float targetX = EnginePreferences.toWorldSpace(x) + EnginePreferences.getCellSize() / 2.0f;
        float targetY = EnginePreferences.toWorldSpace(y) + EnginePreferences.getCellSize() / 2.0f;

        camera.target(Raylib.Vector2Lerp(camera.target(), Helpers.newVector2(targetX, targetY), .1f));
    }

    private void handleMouse() {
        Raylib.Vector2 mouseWorld =
                Raylib.GetScreenToWorld2D(
                        Helpers.newVector2(
                                Raylib.GetMouseX(),
                                Raylib.GetMouseY()
                        ),
                        camera
                );

        cursorPos.x((int)(mouseWorld.x() / EnginePreferences.getCellSize()));
        cursorPos.y((int)(mouseWorld.y() / EnginePreferences.getCellSize()));

        int dx = cursorPos.x() - x;
        int dy = cursorPos.y() - y;

        if (dx >= EnginePreferences.getPlayerReach()) cursorPos.x(x +  EnginePreferences.getPlayerReach());
        if (dx <= -EnginePreferences.getPlayerReach()) cursorPos.x(x -  EnginePreferences.getPlayerReach());
        if (dy >= EnginePreferences.getPlayerReach()) cursorPos.y(y +  EnginePreferences.getPlayerReach());
        if (dy <= -EnginePreferences.getPlayerReach()) cursorPos.y(y -  EnginePreferences.getPlayerReach());
    }

    private void handleMovement() {
        boolean isUpEmpty =     world.getBlock(x, y - 1) == null;
        boolean isDownEmpty =   world.getBlock(x, y + 1) == null;
        boolean isLeftEmpty =   world.getBlock(x - 1, y) == null;
        boolean isRightEmpty =  world.getBlock(x + 1, y) == null;

        boolean canMove = stepTime <= 0;

        if (KeyInput.isKeyHeld("W") && canMove && isUpEmpty){
            velocity.move(0, -1);
        }

        if (KeyInput.isKeyHeld("S") && canMove && isDownEmpty){
            velocity.move(0, 1);
        }

        if (KeyInput.isKeyHeld("A") && canMove && isLeftEmpty){
            velocity.move(-1, 0);
        }

        if (KeyInput.isKeyHeld("D") && canMove && isRightEmpty){
            velocity.move(1, 0);
        }

        boolean diagonal = velocity.x() != 0 && velocity.y() != 0;

        if (!diagonal) {
            x += velocity.x();
            y += velocity.y();
        } else {
            x += velocity.x();
            y += velocity.y();
            stepTime = EnginePreferences.getPlayerStepDelay();
        }

        if (!velocity.isZero()){
            stepTime = EnginePreferences.getPlayerStepDelay();
        }

        velocity.set(0, 0);
    }

    private void makeCamera() {
        camera = new Raylib.Camera2D();

        updateWindowSize();
        camera.offset(Helpers.newVector2(windowSize.x() / 2.0f, windowSize.y() / 2.0f));

        camera.zoom(1.0f);
        camera.rotation(0.0f);
    }

    private void updateWindowSize() {
        windowSize = new Size(Raylib.GetScreenWidth(), Raylib.GetScreenHeight());
    }

    private void checkResize(){
        if (windowSize.sum() != Raylib.GetScreenWidth() + Raylib.GetScreenHeight()){
            updateWindowSize();
            camera.offset(Helpers.newVector2(windowSize.x() / 2.0f, windowSize.y() / 2.0f));
        }
    }

    public String getHolding(){
        var visibleBlocksArray = BlockRegistry.getVisibleBlocks().keySet().toArray();

        if (currentBlockIndex >= BlockRegistry.getVisibleBlocks().size()) currentBlockIndex = BlockRegistry.getVisibleBlocks().size() - 1;

        return (String) visibleBlocksArray[currentBlockIndex];
    }

    public void setHolding(String name){
        var visibleBlockNames = BlockRegistry.getVisibleBlocks().keySet().toArray();

        var index = Arrays.stream(visibleBlockNames).toList().indexOf(name);
        if (index == -1) return;

        currentBlockIndex = index;
    }

    public Raylib.Camera2D getCamera() {
        return camera;
    }

    public Position getVelocity() {
        return velocity;
    }

    public void setVelocity(Position velocity) {
        this.velocity = velocity;
    }
}
