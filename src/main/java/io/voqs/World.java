package io.voqs;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;

import java.util.ArrayList;
import java.util.HashMap;


class Chunk {
    protected Block[] blocks = new Block[Globals.CHUNK_SIZE * Globals.CHUNK_SIZE];
    protected Raylib.Vector2 position;

    public Chunk(int x, int y){
        position = Helpers.newVector2(x, y);
    }

    public int index(int localX, int localY){
        if (
                localX < 0 || localX >= Globals.CHUNK_SIZE ||
                        localY < 0 || localY >= Globals.CHUNK_SIZE
        ) return -1;

        return localX + localY * Globals.CHUNK_SIZE;
    }

    public Block get_block(int localX, int localY){
        int idx = index(localX, localY);
        if (idx < 0 || idx >= blocks.length) return null;

        return blocks[idx];
    }

    public void set_block(int localX, int localY, Block block){
        int idx = index(localX, localY);
        if (idx < 0 || idx >= blocks.length) return;

        blocks[idx] = block;
    }

    public void set_block_safe(int localX, int localY, Block block){
        int idx = index(localX, localY);
        if (idx < 0 || idx >= blocks.length) return;

        if (blocks[idx] != null) return;

        blocks[idx] = block;
    }
}

public class World {
    protected HashMap<Long, Chunk> chunks = new HashMap<>();
    protected Player player;

    public World(){
         player = new Player(this);
    }

    public long chunkKey(int chunkX, int chunkY){
        long key = (((long)chunkX) << 32) | (chunkY & 0xffffffffL);

        chunks.computeIfAbsent(key, k -> new Chunk(chunkX, chunkY));

        return key;
    }

    private Block get_block(int x, int y){
        int chunkX = Math.floorDiv(x, Globals.CHUNK_SIZE);
        int chunkY = Math.floorDiv(y, Globals.CHUNK_SIZE);

        int localX = Math.floorMod(x, Globals.CHUNK_SIZE);
        int localY = Math.floorMod(y, Globals.CHUNK_SIZE);

        return chunks.get(chunkKey(chunkX, chunkY)).get_block(localX, localY);
    }

    private void set_block(int x, int y, Block block, boolean safe){
        int chunkX = Math.floorDiv(x, Globals.CHUNK_SIZE);
        int chunkY = Math.floorDiv(y, Globals.CHUNK_SIZE);

        int localX = Math.floorMod(x, Globals.CHUNK_SIZE);
        int localY = Math.floorMod(y, Globals.CHUNK_SIZE);

        Chunk chunk = chunks.get(chunkKey(chunkX, chunkY));

        if (safe){
            chunk.set_block_safe(localX, localY, block);
            return;
        }

        chunk.set_block(localX, localY, block);
    }




    public void place_block(int x, int y, String name){
        Block block = new Block(Helpers.newVector2((float) x, (float) y), name);

        set_block(x, y, block, true);
    }

    public void remove_block(int x, int y){
        set_block(x, y, null, false);
    }


    public void draw(){
        player.update();

        Raylib.Vector2 currentChunkPos = Helpers.newVector2(0, 0);

        ArrayList<Chunk> chunksToDraw = getChunksToRender(currentChunkPos);

        for (Chunk chunk : chunksToDraw){
            renderChunkBorders(chunk);

            for (Block block : chunk.blocks){
                if (block == null) continue;

                renderBlock(block);
            }
        }

        Raylib.DrawRectangle(player.x * Globals.CELL_SIZE, player.y * Globals.CELL_SIZE, Globals.CELL_SIZE, Globals.CELL_SIZE, Helpers.newColor(80, 255, 80, 255));
    }

    private ArrayList<Chunk> getChunksToRender(Raylib.Vector2 currentChunkPos) {
        ArrayList<Chunk> chunksToDraw = new ArrayList<>();

        for (int x = (int) (currentChunkPos.x() - Globals.CHUNK_DRAW_DISTANCE); x <= currentChunkPos.x() + Globals.CHUNK_DRAW_DISTANCE; x++){
            for (int y = (int) (currentChunkPos.y() - Globals.CHUNK_DRAW_DISTANCE); y <= currentChunkPos.y() + Globals.CHUNK_DRAW_DISTANCE; y++){
                chunksToDraw.add(chunks.get(chunkKey(x,  y)));
            }
        }
        return chunksToDraw;
    }

    private static void renderBlock(Block block) {

        Raylib.Color finalColor = block.fallBackColor;

        Raylib.DrawRectangle((int) block.position.x() * Globals.CELL_SIZE, (int) block.position.y() * Globals.CELL_SIZE, Globals.CELL_SIZE, Globals.CELL_SIZE, finalColor);
    }

    private static void renderChunkBorders(Chunk chunk) {
        int chunkWorldX =
                (int)(chunk.position.x() * Globals.CHUNK_SIZE * Globals.CELL_SIZE);

        int chunkWorldY =
                (int)(chunk.position.y() * Globals.CHUNK_SIZE * Globals.CELL_SIZE);


        Raylib.DrawRectangleLines(
                chunkWorldX, chunkWorldY,
                Globals.CHUNK_SIZE * Globals.CELL_SIZE, Globals.CHUNK_SIZE * Globals.CELL_SIZE,
                Helpers.newColor(255, 255, 255, 8)
        );
    }
}
