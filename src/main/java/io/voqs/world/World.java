package io.voqs.world;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import io.voqs.GameEngine;
import io.voqs.Position;
import io.voqs.blocks.Block;
import io.voqs.globals.TextureRegistry;
import io.voqs.globals.EnginePreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class World {
    protected HashMap<Long, Chunk> chunks = new HashMap<>();
    protected List<Chunk> queuedForLoad = new ArrayList<>();
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

        Chunk chunk = chunks.get(key);

        if (!chunk.generated) {
            chunk.generated = true;
            queuedForLoad.add(chunk);
        }

        return key;
    }

    private Position block2Chunk(int x, int y){
        int chunkSize = EnginePreferences.getChunkSize();

        return new Position(Math.floorDiv(x, chunkSize), Math.floorDiv(y, chunkSize));
    }

    private Position block2ChunkLocal(int x, int y){
        int chunkSize = EnginePreferences.getChunkSize();
        return new Position(Math.floorMod(x, chunkSize), Math.floorMod(y, chunkSize));
    }

    private Block getBlockPrivate(int x, int y){
        Position chunkPos = block2Chunk(x, y);
        Position chunkLocalPos = block2ChunkLocal(x, y);


        return getChunk(chunkPos).getBlock(chunkLocalPos.x(), chunkLocalPos.y());
    }

    private void setBlockEx(int x, int y, Chunk chunk, Block block, boolean safe){
        Position chunkLocalPos = block2ChunkLocal(x, y);

        if (safe){
            chunk.setBlockSafe(chunkLocalPos.x(), chunkLocalPos.y(), block);
            return;
        }

        chunk.setBlock(chunkLocalPos.x(), chunkLocalPos.y(), block);
    }

    private void setBlockPrivate(int x, int y, Block block, boolean safe){
        Position chunkPos = block2Chunk(x, y);
        Position chunkLocalPos = block2ChunkLocal(x, y);

        Chunk chunk = getChunk(chunkPos);

        if (safe){
            chunk.setBlockSafe(chunkLocalPos.x(), chunkLocalPos.y(), block);
            return;
        }

        chunk.setBlock(chunkLocalPos.x(), chunkLocalPos.y(), block);
    }

    private Chunk getChunk(Position chunkPos) {
        return chunks.get(chunkKey(chunkPos.x(), chunkPos.y()));
    }

    public void placeBlock(int x, int y, String name){
        if (getBlockPrivate(x, y) != null) return;

        Block block = new Block(new Position(x, y), name);

        setBlockPrivate(x, y, block, true);
        handle_place_misc(block);
    }

    public void placeBlockEx(int x, int y, Chunk chunk, String name){
        if (getBlockPrivate(x, y) != null) return;

        Block block = new Block(new Position(x, y), name);

        setBlockEx(x, y, chunk, block, true);
        handle_place_misc(block);
    }

    public void removeBlock(int x, int y){
        Block block = getBlockPrivate(x, y);

        if (block == null) return;

        if (handle_break_misc(block)) return; // needed so it continues on both no return and true return

        setBlockPrivate(x, y, null, false);
    }


    public void drawAndUpdate(boolean pulsing){
        if (!chunks.isEmpty()) {
            for (Chunk c : queuedForLoad) {
                chunkCreated(c);
            }

            queuedForLoad.clear();
        }

        getPlayer().update();

        Raylib.BeginMode2D(player.getCamera());


        Position currentChunkPos = block2Chunk(getPlayer().x, getPlayer().y);

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
                EnginePreferences.toWorldSpace(getPlayer().cx),
                EnginePreferences.toWorldSpace(getPlayer().cy),
                EnginePreferences.getCellSize(),
                EnginePreferences.getCellSize(),
                Helpers.newColor(120, 120, 255, 100)
        );

        Raylib.DrawRectangle(
                EnginePreferences.toWorldSpace(getPlayer().x),
                EnginePreferences.toWorldSpace(getPlayer().y),
                EnginePreferences.getCellSize(),
                EnginePreferences.getCellSize(),
                Helpers.newColor(80, 255, 80, 255)
        );

    }

    private void updateBlock(Block block, boolean pulse) {
        if (pulse) engine.getPluginEngine().runBlockPulse(block);
    }

    private ArrayList<Chunk> getChunksToRender(Position currentChunkPos) {
        ArrayList<Chunk> chunksToDraw = new ArrayList<>();

        for (int x = (currentChunkPos.x() - EnginePreferences.getChunkDrawDistance()); x <= currentChunkPos.x() + EnginePreferences.getChunkDrawDistance(); x++){
            for (int y = (currentChunkPos.y() - EnginePreferences.getChunkDrawDistance()); y <= currentChunkPos.y() + EnginePreferences.getChunkDrawDistance(); y++){
                if ((x == 0 || x == 1) && (y == 0 || y == 1)) continue;
                chunksToDraw.add(chunks.get(chunkKey(x,  y)));
            }
        }
        return chunksToDraw;
    }

    private static void renderBlock(Block block) {

        Raylib.Color fallBackColor = block.getFallBackColor();

        Raylib.Texture texture = TextureRegistry.getTexture(block.getName());

        Position cellSizeV = new Position(EnginePreferences.getCellSize(), EnginePreferences.getCellSize());
        Position worldPosition = block.getPosition().mul(cellSizeV);

        Raylib.Rectangle sourceRectangle = Helpers.newRectangle(0, 0, EnginePreferences.getCellSize(), EnginePreferences.getCellSize());

        if (!Raylib.IsTextureValid(texture)){
            Raylib.DrawRectangleV(worldPosition.toRaylib(), cellSizeV.toRaylib(), fallBackColor);
            return;
        }

        Raylib.DrawTextureRec(
                texture,
                sourceRectangle,
                worldPosition.toRaylib(),
                Colors.WHITE
        );
    }

    private static void renderChunkDecor(Chunk chunk) {
        int chunkWorldX =
                chunk.position.x() * EnginePreferences.getChunkSize() * EnginePreferences.getCellSize();

        int chunkWorldY =
                chunk.position.y() * EnginePreferences.getChunkSize() * EnginePreferences.getCellSize();


        Raylib.DrawRectangleLines(
                chunkWorldX, chunkWorldY,
                EnginePreferences.getChunkSize() * EnginePreferences.getCellSize(), EnginePreferences.getChunkSize() * EnginePreferences.getCellSize(),
                Helpers.newColor(255, 255, 255, 8)
        );

        Raylib.DrawText(chunk.position.x() + ", " + chunk.position.y(), chunkWorldX + 2, chunkWorldY + 2, 8, Helpers.newColor(230, 230, 230, 70));
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

    public GameEngine getEngine() {
        return engine;
    }

    public void fillBlocks(Position start, Position end, String name){
        for (int y = start.y(); y <= end.y(); y++){
            for (int x = start.x(); x <= end.x(); x++){
                placeBlock(x, y, name);
            }
        }
    }

    private void chunkCreated(Chunk chunk){
    }
}
