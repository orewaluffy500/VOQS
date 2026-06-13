package io.voqs.blocks;

import com.raylib.Raylib;
import io.voqs.helpers.Color;

public class BlockRegistryData {
    public Color fallBackColor;
    public Raylib.Texture texture;

    public BlockRegistryData(Raylib.Texture texture, Color fallBackColor) {
        this.fallBackColor = fallBackColor;
        this.texture = texture;
    }

}
