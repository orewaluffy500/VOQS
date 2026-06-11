package io.voqs.world;

import com.raylib.Helpers;
import com.raylib.Raylib;
import io.voqs.Position;
import io.voqs.blocks.BlockRegistry;
import io.voqs.globals.KeyInput;
import io.voqs.globals.EnginePreferences;
import io.voqs.plugins.Metadata;

import java.util.Arrays;

public class Player {
    public int x, y;
    public int cx, cy;

    private Position velocity = Position.ZERO;

    private Raylib.Camera2D camera;

    public float stepTime;
    public float blockManipulationTime;

    private final World world;
    public int currentBlockIndex = 0;
    public Metadata metadata;


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

        if (currentBlockIndex >= BlockRegistry.getVisibleBlocks().size()){
            currentBlockIndex = 0;
        }

        if (stepTime > 0) stepTime -= Raylib.GetFrameTime();
        if (blockManipulationTime > 0) blockManipulationTime -= Raylib.GetFrameTime();
    }

    private void handleBlocks() {
        boolean canManipulateBlocks = blockManipulationTime <= 0f;

        if (Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_RIGHT) && canManipulateBlocks){
            world.placeBlock(cx, cy, getHolding());
            blockManipulationTime = EnginePreferences.getPlayerBuildDelay();
        }

        if (Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT) && canManipulateBlocks){
            world.removeBlock(cx, cy);
            blockManipulationTime = EnginePreferences.getPlayerBuildDelay();
        }

        if (KeyInput.wasKeyPressed("Z")){
            currentBlockIndex++;
        }
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

        cx = (int)(mouseWorld.x() / EnginePreferences.getCellSize());
        cy = (int)(mouseWorld.y() / EnginePreferences.getCellSize());

        int dx = cx - x;
        int dy = cy - y;

        if (dx >= EnginePreferences.getPlayerReach()) cx     = x +  EnginePreferences.getPlayerReach();
        if (dx <= -EnginePreferences.getPlayerReach()) cx    = x -  EnginePreferences.getPlayerReach();
        if (dy >= EnginePreferences.getPlayerReach()) cy     = y +  EnginePreferences.getPlayerReach();
        if (dy <= -EnginePreferences.getPlayerReach()) cy    = y -  EnginePreferences.getPlayerReach();
    }

    private void handleMovement() {
        boolean isUpEmpty =     world.getBlock(x, y - 1) == null;
        boolean isDownEmpty =   world.getBlock(x, y + 1) == null;
        boolean isLeftEmpty =   world.getBlock(x - 1, y) == null;
        boolean isRightEmpty =  world.getBlock(x + 1, y) == null;

        boolean canMove = stepTime <= 0;

        if (KeyInput.isKeyHeld("W") && canMove && isUpEmpty){
            velocity.set(0, -1);
        }

        if (KeyInput.isKeyHeld("S") && canMove && isDownEmpty){
            velocity.set(0, 1);
        }

        if (KeyInput.isKeyHeld("A") && canMove && isLeftEmpty){
            velocity.set(-1, 0);
        }

        if (KeyInput.isKeyHeld("D") && canMove && isRightEmpty){
            velocity.set(1, 0);
        }

        x += velocity.x();
        y += velocity.y();

        if (!velocity.isZero()){
            stepTime = EnginePreferences.getPlayerStepDelay();
        }

        velocity.set(0, 0);
    }

    private void makeCamera() {
        camera = new Raylib.Camera2D();
        camera.offset(Helpers.newVector2(Raylib.GetScreenWidth() / 2.0f, Raylib.GetScreenHeight() / 2.0f));

        camera.zoom(1.0f);
        camera.rotation(0.0f);
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
