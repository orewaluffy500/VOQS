package io.voqs.helpers;

import com.raylib.Helpers;
import com.raylib.Raylib;

public record Color(int r, int g, int b, int o) {
    public static final Color NONE = new Color(10, 0, 0, 100);

    public Raylib.Color toRaylib(){
        return Helpers.newColor(r, g, b, o);
    }

    public static Color fromRaylib(Raylib.Color color){
        return new Color(color.r(), color.g(), color.b(), color.a());
    }
}
