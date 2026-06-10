package io.voqs.world;

import com.raylib.Helpers;
import com.raylib.Raylib;
import io.voqs.blocks.BlockRegistry;
import io.voqs.globals.KeyInput;
import io.voqs.globals.VGlobals;
import io.voqs.plugins.Metadata;

import java.util.Arrays;

public class Player {
    public int x, y;
    public int cx, cy;

    private Raylib.Camera2D camera;

    public float stepTime;
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
        if (camera == null){
            makeCamera();
        }

        if (currentBlockIndex >= BlockRegistry.getVisibleBlocks().size()){
            currentBlockIndex = 0;
        }

        if (stepTime > 0) stepTime -= Raylib.GetFrameTime();

        boolean isUpEmpty =     world.getBlock(x, y - 1) == null;
        boolean isDownEmpty =   world.getBlock(x, y + 1) == null;
        boolean isLeftEmpty =   world.getBlock(x - 1, y) == null;
        boolean isRightEmpty =  world.getBlock(x + 1, y) == null;

        boolean canMove = stepTime <= 0;

        if (KeyInput.isKeyHeld("W") && canMove && isUpEmpty){
            y -= 1;
            stepTime = 0.1f;
        }

        if (KeyInput.isKeyHeld("S") && canMove && isDownEmpty){
            y += 1;
            stepTime = 0.1f;
        }

        if (KeyInput.isKeyHeld("A") && canMove && isLeftEmpty){
            x -= 1;
            stepTime = 0.1f;
        }

        if (KeyInput.isKeyHeld("D") && canMove && isRightEmpty){
            x += 1;
            stepTime = 0.1f;
        }


        Raylib.Vector2 mouseWorld =
                Raylib.GetScreenToWorld2D(
                        Helpers.newVector2(
                                Raylib.GetMouseX(),
                                Raylib.GetMouseY()
                        ),
                        camera
                );

        cx = (int)(mouseWorld.x() / VGlobals.getCellSize());
        cy = (int)(mouseWorld.y() / VGlobals.getCellSize());

        camera.target().x(VGlobals.toWorldSpace(x) + VGlobals.getCellSize() / 2.0f);
        camera.target().y(VGlobals.toWorldSpace(y) + VGlobals.getCellSize() / 2.0f);

        int dx = cx - x;
        int dy = cy - y;

        if (dx >= VGlobals.getPlayerReach()) cx     = x +  VGlobals.getPlayerReach();
        if (dx <= -VGlobals.getPlayerReach()) cx    = x -  VGlobals.getPlayerReach();
        if (dy >= VGlobals.getPlayerReach()) cy     = y +  VGlobals.getPlayerReach();
        if (dy <= -VGlobals.getPlayerReach()) cy    = y -  VGlobals.getPlayerReach();


        if (Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_RIGHT)){
            world.placeBlock(cx, cy, getHolding());
        }

        if (Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT)){
            world.removeBlock(cx, cy);
        }

        if (KeyInput.wasKeyPressed("Z")){
            currentBlockIndex++;
        }
    }

    private void makeCamera() {
        camera = new Raylib.Camera2D();
        camera.offset(Helpers.newVector2(Raylib.GetScreenWidth() / 2.0f, Raylib.GetScreenHeight() / 2.0f));
        System.out.println(
                "target=(" + camera.target().x() + ", " + camera.target().y() + ")" +
                        " offset=(" + camera.offset().x() + ", " + camera.offset().y() + ")"
        );

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
}
