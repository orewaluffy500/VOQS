package io.voqs.world;

import com.raylib.Raylib;
import io.voqs.blocks.BlockRegistry;
import io.voqs.globals.KeyInput;
import io.voqs.plugins.Metadata;

import java.util.Arrays;

public class Player {
    public int x, y;
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
        var visibleBlocks = BlockRegistry.getVisibleBlocks();

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



        if (KeyInput.isKeyHeld("E")){
            world.placeBlock(x, y, getHolding());
        }

        else if (KeyInput.isKeyHeld("Q")){
            world.removeBlock(x, y);
        }

        if (KeyInput.wasKeyPressed("Z")){
            currentBlockIndex++;
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
}
