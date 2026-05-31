package io.voqs;

import com.raylib.Raylib;

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

        if (Raylib.IsKeyDown(Raylib.KEY_W) && canMove){
            y -= 1;
            stepTime = 0.1f;
        }

        if (Raylib.IsKeyDown(Raylib.KEY_S) && canMove){
            y += 1;
            stepTime = 0.1f;
        }

        if (Raylib.IsKeyDown(Raylib.KEY_A) && canMove){
            x -= 1;
            stepTime = 0.1f;
        }

        if (Raylib.IsKeyDown(Raylib.KEY_D) && canMove){
            x += 1;
            stepTime = 0.1f;
        }



        if (Raylib.IsKeyDown(Raylib.KEY_E)){
            world.place_block(x, y, (String) visibleBlocks.keySet().toArray()[currentBlockIndex]);
        }

        else if (Raylib.IsKeyDown(Raylib.KEY_Q)){
            world.remove_block(x, y);
        }

        if (Raylib.IsKeyPressed(Raylib.KEY_Z)){
            currentBlockIndex++;
        }
    }
}
