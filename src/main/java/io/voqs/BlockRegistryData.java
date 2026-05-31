package io.voqs;

import com.raylib.Raylib;

public class BlockRegistryData {
    public Raylib.Color fallBackColor;
    public Raylib.Texture texture;

    public BlockRegistryData(Raylib.Texture texture, Raylib.Color fallBackColor) {
        this.fallBackColor = fallBackColor;
        this.texture = texture;
    }

}
