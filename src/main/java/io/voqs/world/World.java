package io.voqs.world;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import io.voqs.blocks.Block;
import io.voqs.globals.TextureRegistry;
import io.voqs.globals.VGlobals;

import java.util.ArrayList;
import java.util.HashMap;


public class World {
    protected HashMap<Long, Chunk> chunks = new HashMap<>();
    public Player player;

    public World(){
         player = new Player(this);
    }

    public long chunkKey(int chunkX, int chunkY){
        long key = (((long)chunkX) << 32) | (chunkY & 0xffffffffL);

        chunks.computeIfAbsent(key, k -> new Chunk(chunkX, chunkY));

        return key;
    }

    private Block get_block(int x, int y){
        int chunkX = Math.floorDiv(x, VGlobals.CHUNK_SIZE);
        int chunkY = Math.floorDiv(y, VGlobals.CHUNK_SIZE);

        int localX = Math.floorMod(x, VGlobals.CHUNK_SIZE);
        int localY = Math.floorMod(y, VGlobals.CHUNK_SIZE);

        return chunks.get(chunkKey(chunkX, chunkY)).get_block(localX, localY);
    }

    private void set_block(int x, int y, Block block, boolean safe){
        int chunkX = Math.floorDiv(x, VGlobals.CHUNK_SIZE);
        int chunkY = Math.floorDiv(y, VGlobals.CHUNK_SIZE);

        int localX = Math.floorMod(x, VGlobals.CHUNK_SIZE);
        int localY = Math.floorMod(y, VGlobals.CHUNK_SIZE);

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

        Raylib.DrawRectangle(player.x * VGlobals.CELL_SIZE, player.y * VGlobals.CELL_SIZE, VGlobals.CELL_SIZE, VGlobals.CELL_SIZE, Helpers.newColor(80, 255, 80, 255));
    }

    private ArrayList<Chunk> getChunksToRender(Raylib.Vector2 currentChunkPos) {
        ArrayList<Chunk> chunksToDraw = new ArrayList<>();

        for (int x = (int) (currentChunkPos.x() - VGlobals.CHUNK_DRAW_DISTANCE); x <= currentChunkPos.x() + VGlobals.CHUNK_DRAW_DISTANCE; x++){
            for (int y = (int) (currentChunkPos.y() - VGlobals.CHUNK_DRAW_DISTANCE); y <= currentChunkPos.y() + VGlobals.CHUNK_DRAW_DISTANCE; y++){
                chunksToDraw.add(chunks.get(chunkKey(x,  y)));
            }
        }
        return chunksToDraw;
    }

    private static void renderBlock(Block block) {

        Raylib.Color fallBackColor = block.fallBackColor;

        Raylib.Texture texture = TextureRegistry.getTexture(block.name);

        Raylib.Vector2 cellSizeV = Helpers.newVector2(VGlobals.CELL_SIZE, VGlobals.CELL_SIZE);
        Raylib.Vector2 worldPosition = Raylib.Vector2Multiply(block.position, cellSizeV);

        Raylib.Rectangle sourceRectangle = Helpers.newRectangle(0, 0, VGlobals.CELL_SIZE, VGlobals.CELL_SIZE);

        if (!Raylib.IsTextureValid(texture)){
            Raylib.DrawRectangleV(worldPosition, cellSizeV, fallBackColor);
            return;
        }

        Raylib.DrawTextureRec(
                texture,
                sourceRectangle,
                worldPosition,
                Colors.WHITE
        );
    }

    private static void renderChunkBorders(Chunk chunk) {
        int chunkWorldX =
                (int)(chunk.position.x() * VGlobals.CHUNK_SIZE * VGlobals.CELL_SIZE);

        int chunkWorldY =
                (int)(chunk.position.y() * VGlobals.CHUNK_SIZE * VGlobals.CELL_SIZE);


        Raylib.DrawRectangleLines(
                chunkWorldX, chunkWorldY,
                VGlobals.CHUNK_SIZE * VGlobals.CELL_SIZE, VGlobals.CHUNK_SIZE * VGlobals.CELL_SIZE,
                Helpers.newColor(255, 255, 255, 8)
        );
    }
}
