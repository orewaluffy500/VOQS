package io.voqs.blocks;

import com.raylib.Raylib;
import io.voqs.Position;

public class Block {
    private Position position = Position.ZERO;
    private final String name;
    private final Raylib.Color fallBackColor;

    public Block(Position pos, String name){
        this.setPosition(pos);
        this.name = name;
        this.fallBackColor = BlockRegistry.getBlock(name).fallBackColor;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public Raylib.Color getFallBackColor() {
        return fallBackColor;
    }
}
