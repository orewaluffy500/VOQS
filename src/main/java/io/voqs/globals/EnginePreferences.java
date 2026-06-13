package io.voqs.globals;

import com.raylib.Raylib;
import io.voqs.helpers.Color;

import java.util.Random;

public class EnginePreferences {
    private static final int CHUNK_SIZE = 16;
    private static final int CELL_SIZE = 16;
    private static final int CHUNK_DRAW_DISTANCE = 4;
    private static final int PLAYER_REACH = 5;
    private static final int MAX_TELEPORT_DISTANCE = 192;

    private static final float PULSE_RATE = 16;
    private static final float PLAYER_STEP_DELAY = 0.15f;
    private static final float PLAYER_BUILD_DELAY = 0.05f;

    private static final Random random = new Random();
    private static final Color[] flowerColors = new Color[]{
        new Color(255, 100, 100, 90),
        new Color(100, 255, 100, 55),
        new Color(100, 100, 255, 35),
        new Color(245, 197, 66, 75)
    };

    public static int getChunkSize() {
        return CHUNK_SIZE;
    }

    public static int getCellSize() {
        return CELL_SIZE;
    }

    public static int getChunkDrawDistance() {
        return CHUNK_DRAW_DISTANCE;
    }

    public static float getPulseRate() {
        return PULSE_RATE * EngineModifiers.getPulseSpeed();
    }

    public static int getPlayerReach() {
        return PLAYER_REACH;
    }

    public static int toWorldSpace(int v){
        return v * CELL_SIZE;
    }

    public static int getMaxTeleportDistance() {
        return MAX_TELEPORT_DISTANCE;
    }

    public static float getPlayerStepDelay() {
        return PLAYER_STEP_DELAY / EngineModifiers.getWalkSpeed();
    }

    public static float getPlayerBuildDelay() {
        return PLAYER_BUILD_DELAY / EngineModifiers.getBuildSpeed();
    }

    public static float getDeltaTime(){
        return Raylib.GetFrameTime();
    }


    public static Random getRandom() {
        return random;
    }

    public static Color[] getFlowerColors() {
        return flowerColors;
    }
}
