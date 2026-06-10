package io.voqs.globals;

public class VGlobals {
    private static final int CHUNK_SIZE = 16;
    private static final int CELL_SIZE = 16;
    private static final int CHUNK_DRAW_DISTANCE = 2;
    private static final int PULSE_RATE = 16;
    private static final int PLAYER_REACH = 5;

    public static int getChunkSize() {
        return CHUNK_SIZE;
    }

    public static int getCellSize() {
        return CELL_SIZE;
    }

    public static int getChunkDrawDistance() {
        return CHUNK_DRAW_DISTANCE;
    }

    public static int getPulseRate() {
        return PULSE_RATE;
    }

    public static int getPlayerReach() {
        return PLAYER_REACH;
    }

    public static int toWorldSpace(int v){
        return v * CELL_SIZE;
    }
}
