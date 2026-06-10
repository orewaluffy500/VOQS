package io.voqs.blocks;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import io.voqs.globals.TextureRegistry;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;

public class BlockRegistry {
    private static final HashMap<String, BlockRegistryData> blockRegister = new HashMap<>();
    private static HashMap<String, BlockRegistryData> visibleBlocks = getBlockRegister();

    static {
        registerBlock("dirt", "block/dirt.png", Colors.BROWN);
        registerBlock("stone", "block/stone.png", Colors.GRAY);
    }

    public static void registerBlock(String name, String path, Raylib.Color color){
        if (getBlockRegister().containsKey(name)) return;

        TextureRegistry.loadTexture(name, path);

        getBlockRegister().put(name, new BlockRegistryData(TextureRegistry.getTexture(name), color));
        updateVisibleBlocks();
    }

    public static void unregisterBlock(String name){
        if (getVisibleBlocks().size() < 2) return;

        getBlockRegister().remove(name);
        updateVisibleBlocks();
    }

    public static boolean isRegistered(String name){
        return getBlockRegister().containsKey(name);
    }

    public static BlockRegistryData getBlock(String name){
        return getBlockRegister().getOrDefault(name, null);
    }

    public static BlockRegistryData getBlockSafe(String name){
        return getVisibleBlocks().getOrDefault(name, null);
    }

    public static Raylib.Color lua2Raylib(LuaValue color){
        if (color.length() < 3) return Colors.BLANK;

        LuaValue opacity = color.get(4);
        if (opacity.isnil()){
            opacity = LuaValue.valueOf(100);
        }

        return Helpers.newColor(
            color.get(1).toint(),
            color.get(2).toint(),
            color.get(3).toint(),
            (int) ((opacity.tofloat() / 100) * 255)
        );
    }

    public static void updateVisibleBlocks(){
        visibleBlocks = new HashMap<>();

        getBlockRegister().forEach((s, blockRegistryData) -> {
            if (!s.startsWith("_")){
                getVisibleBlocks().put(s, blockRegistryData);
            }
        });
    }

    public static HashMap<String, BlockRegistryData> getBlockRegister() {
        return blockRegister;
    }

    public static HashMap<String, BlockRegistryData> getVisibleBlocks() {
        return visibleBlocks;
    }
}
