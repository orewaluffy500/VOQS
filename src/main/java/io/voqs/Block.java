package io.voqs;

import com.raylib.Helpers;
import com.raylib.Raylib;

public class Block {
    public Raylib.Vector2 position = Helpers.newVector2(0, 0);

    public Block(Raylib.Vector2 pos){
        this.position = pos;
    }
}
