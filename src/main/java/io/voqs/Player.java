package io.voqs;

import com.raylib.Raylib;

import static io.voqs.BlockRegistry.visibleBlocks;

public class Player {
    public int x, y;
    private float stepTime;
    private World world;
    private int currentBlockIndex = 0;

    public Player(World w){
        x = 5;
        y = 5;
        stepTime = 0.2f;
        world = w;
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
}
