package io.voqs.globals;

import com.raylib.Raylib;

import java.util.HashMap;

public class KeyInput {
    public static final HashMap<String, Integer> keys = new HashMap<>();

    static {
        keys.put("A", Raylib.KEY_A);
        keys.put("B", Raylib.KEY_B);
        keys.put("C", Raylib.KEY_C);
        keys.put("D", Raylib.KEY_D);
        keys.put("E", Raylib.KEY_E);
        keys.put("F", Raylib.KEY_F);
        keys.put("G", Raylib.KEY_G);
        keys.put("H", Raylib.KEY_H);
        keys.put("J", Raylib.KEY_J);
        keys.put("K", Raylib.KEY_K);
        keys.put("L", Raylib.KEY_L);
        keys.put("M", Raylib.KEY_M);
        keys.put("N", Raylib.KEY_N);
        keys.put("O", Raylib.KEY_O);
        keys.put("P", Raylib.KEY_P);
        keys.put("Q", Raylib.KEY_Q);
        keys.put("R", Raylib.KEY_R);
        keys.put("S", Raylib.KEY_S);
        keys.put("T", Raylib.KEY_T);
        keys.put("U", Raylib.KEY_U);
        keys.put("V", Raylib.KEY_V);
        keys.put("W", Raylib.KEY_W);
        keys.put("X", Raylib.KEY_X);
        keys.put("Y", Raylib.KEY_Y);
        keys.put("Z", Raylib.KEY_Z);
        keys.put("Return", Raylib.KEY_ENTER);
        keys.put("LShift", Raylib.KEY_LEFT_SHIFT);
        keys.put("RShift", Raylib.KEY_RIGHT_SHIFT);
    }

    public static boolean isKeyHeld(String k){
        return keys.containsKey(k) && Raylib.IsKeyDown(keys.get(k));
    }

    public static boolean wasKeyPressed(String k){
        return keys.containsKey(k) && Raylib.IsKeyPressed(keys.get(k));
    }

    public static boolean isKeyUp(String k){
        return keys.containsKey(k) && Raylib.IsKeyUp(keys.get(k));
    }
}
