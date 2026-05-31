package io.voqs;

import com.raylib.Helpers;
import com.raylib.Raylib;

public class Block {
    public Raylib.Vector2 position = Helpers.newVector2(0, 0);
    public String name;
    public Raylib.Color fallBackColor;

    public Block(Raylib.Vector2 pos, String name){
        this.position = pos;
        this.name = name;
        this.fallBackColor = BlockRegistry.getBlock(name).fallBackColor;
    }
}
