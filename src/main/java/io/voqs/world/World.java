package io.voqs.world;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import io.voqs.GameEngine;
import io.voqs.blocks.Block;
import io.voqs.globals.TextureRegistry;
import io.voqs.globals.VGlobals;

import java.util.ArrayList;
import java.util.HashMap;


public class World {
    protected HashMap<Long, Chunk> chunks = new HashMap<>();
    private Player player;
    protected final GameEngine engine;
    private final Chunk[] spawnChunks = new Chunk[4];

    public World(GameEngine engine){
        this.engine = engine;

        setPlayer(new Player(this));

        spawnChunks[0] = chunks.get(chunkKey(1, 0));
        spawnChunks[1] = chunks.get(chunkKey(0, 1));
        spawnChunks[2] = chunks.get(chunkKey(0, 0));
        spawnChunks[3] = chunks.get(chunkKey(1, 1));
    }

    public long chunkKey(int chunkX, int chunkY){
        long key = (((long)chunkX) << 32) | (chunkY & 0xffffffffL);

        chunks.computeIfAbsent(key, k -> new Chunk(chunkX, chunkY));

        return key;
    }

    private Raylib.Vector2 block2Chunk(int x, int y){
        return Helpers.newVector2(Math.floorDiv(x, VGlobals.getChunkSize()), Math.floorDiv(y, VGlobals.getChunkSize()));
    }

    private Raylib.Vector2 block2ChunkLocal(int x, int y){
        return Helpers.newVector2(Math.floorMod(x, VGlobals.getChunkSize()), Math.floorMod(y, VGlobals.getChunkSize()));
    }

    private Block getBlockPrivate(int x, int y){
        Raylib.Vector2 chunkPos = block2Chunk(x, y);
        Raylib.Vector2 chunkLocalPos = block2ChunkLocal(x, y);


        return chunks.get(chunkKey((int) chunkPos.x(), (int) chunkPos.y())).get_block((int) chunkLocalPos.x(), (int) chunkLocalPos.y());
    }

    private void setBlockPrivate(int x, int y, Block block, boolean safe){
        Raylib.Vector2 chunkPos = block2Chunk(x, y);
        Raylib.Vector2 chunkLocalPos = block2ChunkLocal(x, y);


        Chunk chunk = chunks.get(chunkKey((int) chunkPos.x(), (int) chunkPos.y()));

        if (safe){
            chunk.set_block_safe((int) chunkLocalPos.x(), (int) chunkLocalPos.y(), block);
            return;
        }

        chunk.set_block((int) chunkLocalPos.x(), (int) chunkLocalPos.y(), block);
    }

    public void placeBlock(int x, int y, String name){
        if (getBlockPrivate(x, y) != null) return;

        Block block = new Block(Helpers.newVector2((float) x, (float) y), name);

        setBlockPrivate(x, y, block, true);
        handle_place_misc(block);
    }

    public void removeBlock(int x, int y){
        Block block = getBlockPrivate(x, y);

        if (block == null) return;

        if (handle_break_misc(block)) return; // needed so it continues on both no return and true return

        setBlockPrivate(x, y, null, false);
    }



    public void drawAndUpdate(boolean pulsing){
        getPlayer().update();

        Raylib.BeginMode2D(player.getCamera());


        Raylib.Vector2 currentChunkPos = block2Chunk(getPlayer().x, getPlayer().y);

        ArrayList<Chunk> chunksToDraw = getChunksToRender(currentChunkPos);

        for (Chunk chunk : chunksToDraw){
            renderChunkDecor(chunk);

            for (Block block : chunk.blocks){
                if (block == null) continue;

                updateBlock(block, pulsing);
                renderBlock(block);
            }
        }

        for (Chunk chunk : spawnChunks){
            renderChunkDecor(chunk);


            for (Block block : chunk.blocks){
                if (block == null) continue;

                updateBlock(block, pulsing);
                renderBlock(block);
            }
        }

        renderPlayer();

        Raylib.EndMode2D();
    }

    private void renderPlayer() {
        Raylib.DrawRectangle(
                VGlobals.toWorldSpace(getPlayer().cx),
                VGlobals.toWorldSpace(getPlayer().cy),
                VGlobals.getCellSize(),
                VGlobals.getCellSize(),
                Helpers.newColor(120, 120, 255, 100)
        );

        Raylib.DrawRectangle(VGlobals.toWorldSpace(getPlayer().x), VGlobals.toWorldSpace(getPlayer().y), VGlobals.getCellSize(), VGlobals.getCellSize(), Helpers.newColor(80, 255, 80, 255));
    }

    private void updateBlock(Block block, boolean pulse) {
        if (pulse) engine.getPluginEngine().runBlockPulse(block);
    }

    private ArrayList<Chunk> getChunksToRender(Raylib.Vector2 currentChunkPos) {
        ArrayList<Chunk> chunksToDraw = new ArrayList<>();

        for (int x = (int) (currentChunkPos.x() - VGlobals.getChunkDrawDistance()); x <= currentChunkPos.x() + VGlobals.getChunkDrawDistance(); x++){
            for (int y = (int) (currentChunkPos.y() - VGlobals.getChunkDrawDistance()); y <= currentChunkPos.y() + VGlobals.getChunkDrawDistance(); y++){
                if ((x == 0 || x == 1) && (y == 0 || y == 1)) continue;
                chunksToDraw.add(chunks.get(chunkKey(x,  y)));
            }
        }
        return chunksToDraw;
    }

    private static void renderBlock(Block block) {

        Raylib.Color fallBackColor = block.getFallBackColor();

        Raylib.Texture texture = TextureRegistry.getTexture(block.getName());

        Raylib.Vector2 cellSizeV = Helpers.newVector2(VGlobals.getCellSize(), VGlobals.getCellSize());
        Raylib.Vector2 worldPosition = Raylib.Vector2Multiply(block.getPosition(), cellSizeV);

        Raylib.Rectangle sourceRectangle = Helpers.newRectangle(0, 0, VGlobals.getCellSize(), VGlobals.getCellSize());

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

    private static void renderChunkDecor(Chunk chunk) {
        int chunkWorldX =
                (int)(chunk.position.x() * VGlobals.getChunkSize() * VGlobals.getCellSize());

        int chunkWorldY =
                (int)(chunk.position.y() * VGlobals.getChunkSize() * VGlobals.getCellSize());


        Raylib.DrawRectangleLines(
                chunkWorldX, chunkWorldY,
                VGlobals.getChunkSize() * VGlobals.getCellSize(), VGlobals.getChunkSize() * VGlobals.getCellSize(),
                Helpers.newColor(255, 255, 255, 8)
        );

        Raylib.DrawText(Math.round(chunk.position.x()) + ", " + Math.round(chunk.position.y()), chunkWorldX + 2, chunkWorldY + 2, 8, Helpers.newColor(230, 230, 230, 70));
    }


    private boolean handle_break_misc(Block block) {
        return engine.getPluginEngine().runBlockBreakingCallback(block).toboolean();
    }

    private void handle_place_misc(Block block){
        engine.getPluginEngine().runBlockPlacedCallback(block);
    }

    public Block getBlock(int x, int y){
        return getBlockPrivate(x, y);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
