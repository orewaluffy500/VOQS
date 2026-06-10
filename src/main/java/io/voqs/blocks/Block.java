package io.voqs.blocks;

import com.raylib.Helpers;
import com.raylib.Raylib;

public class Block {
    private Raylib.Vector2 position = Helpers.newVector2(0, 0);
    private final String name;
    private final Raylib.Color fallBackColor;

    public Block(Raylib.Vector2 pos, String name){
        this.setPosition(pos);
        this.name = name;
        this.fallBackColor = BlockRegistry.getBlock(name).fallBackColor;
    }

    public Raylib.Vector2 getPosition() {
        return position;
    }

    public void setPosition(Raylib.Vector2 position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public Raylib.Color getFallBackColor() {
        return fallBackColor;
    }
}
