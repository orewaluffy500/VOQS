package io.voqs.globals;

import com.raylib.Raylib;

import java.util.HashMap;

public class TextureRegistry {
    protected static HashMap<String, Raylib.Texture> textures = new HashMap<>();


    public static void loadTexture(String name, String path){
        if (textures.containsKey(name)) return;
        if (path.contains("..")) return;

        Raylib.Texture texture = Raylib.LoadTexture("./game/" + path);

        textures.put(name, texture);
    }

    public static Raylib.Texture getTexture(String name){
        return textures.getOrDefault(name, null);
    }

    public static void unloadTexture(String name){
        if (textures.containsKey(name)){
            Raylib.UnloadTexture(textures.get(name));
        }
    }

    public static void unloadAll(){
        textures.forEach((s, texture) -> {
            Raylib.UnloadTexture(texture);
        });
    }
}
