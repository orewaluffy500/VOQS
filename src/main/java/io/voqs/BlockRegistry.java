package io.voqs;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import org.luaj.vm2.LuaTable;

import java.awt.*;
import java.util.HashMap;

public class BlockRegistry {
    protected static HashMap<String, BlockRegistryData> blocKRegister = new HashMap<>();
    protected static HashMap<String, BlockRegistryData> visibleBlocks = blocKRegister;

    static {
        registerBlock("dirt", Colors.BROWN);
        registerBlock("stone", Colors.GRAY);
    }

    public static void registerBlock(String name, Raylib.Color color){
        if (blocKRegister.containsKey(name)) return;

        blocKRegister.put(name, new BlockRegistryData(color));
        updateVisibleBlocks();
    }

    public static void unregisterBlock(String name){
        blocKRegister.remove(name);
        updateVisibleBlocks();
    }

    public static BlockRegistryData getBlock(String name){
        return blocKRegister.getOrDefault(name, null);
    }

    public static BlockRegistryData getBlockSafe(String name){
        return visibleBlocks.getOrDefault(name, null);
    }

    public static Raylib.Color lua2Raylib(LuaTable color){
        if (color.length() < 4) return Colors.BLANK;

        return Helpers.newColor(
            color.get(1).toint(),
            color.get(2).toint(),
            color.get(3).toint(),
            color.get(4).toint()
        );
    }

    public static void updateVisibleBlocks(){
        visibleBlocks = new HashMap<>();

        blocKRegister.forEach((s, blockRegistryData) -> {
            if (!s.startsWith("_")){
                visibleBlocks.put(s, blockRegistryData);
            }
        });
    }
}
