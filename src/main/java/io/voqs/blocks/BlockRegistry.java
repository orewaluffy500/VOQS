package io.voqs.blocks;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import io.voqs.globals.TextureRegistry;
import io.voqs.helpers.Color;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;

public class BlockRegistry {
    private static final HashMap<String, BlockRegistryData> blockRegister = new HashMap<>();
    private static HashMap<String, BlockRegistryData> visibleBlocks = getBlockRegister();

    static {
        registerBlock("dirt", "block/dirt.png", Color.fromRaylib(Colors.BROWN));
        registerBlock("stone", "block/stone.png", Color.fromRaylib(Colors.GRAY));
    }

    public static void registerBlock(String name, String path, Color color){
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

    public static Color lua2Raylib(LuaValue color){
        if (color.length() < 3) return Color.NONE;

        return new Color(
            color.get(1).checkint(),
            color.get(2).checkint(),
            color.get(3).checkint(),
            color.get(4).checkint()
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
