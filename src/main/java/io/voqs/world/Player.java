package io.voqs.world;

import com.raylib.Raylib;
import io.voqs.blocks.BlockRegistry;
import io.voqs.globals.KeyInput;
import io.voqs.plugins.Metadata;

import java.util.Arrays;

import static io.voqs.blocks.BlockRegistry.visibleBlocks;

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
        var visibleBlocks = BlockRegistry.visibleBlocks;

        if (currentBlockIndex >= visibleBlocks.size()){
            currentBlockIndex = 0;
        }

        if (stepTime > 0) stepTime -= Raylib.GetFrameTime();

        boolean canMove = stepTime <= 0;

        if (KeyInput.isKeyHeld("W") && canMove){
            y -= 1;
            stepTime = 0.1f;
        }

        if (KeyInput.isKeyHeld("S") && canMove){
            y += 1;
            stepTime = 0.1f;
        }

        if (KeyInput.isKeyHeld("A") && canMove){
            x -= 1;
            stepTime = 0.1f;
        }

        if (KeyInput.isKeyHeld("D") && canMove){
            x += 1;
            stepTime = 0.1f;
        }



        if (KeyInput.isKeyHeld("E")){
            world.place_block(x, y, getHolding());
        }

        else if (KeyInput.isKeyHeld("Q")){
            world.remove_block(x, y);
        }

        if (KeyInput.wasKeyPressed("Z")){
            currentBlockIndex++;
        }
    }

    public String getHolding(){
        var visibleBlocksArray = visibleBlocks.keySet().toArray();

        if (currentBlockIndex >= visibleBlocks.size()) currentBlockIndex = visibleBlocks.size() - 1;

        return (String) visibleBlocksArray[currentBlockIndex];
    }

    public void setHolding(String name){
        var visibleBlockNames = visibleBlocks.keySet().toArray();

        var index = Arrays.stream(visibleBlockNames).toList().indexOf(name);

        currentBlockIndex = index;
    }
}
