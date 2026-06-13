package io.voqs.world;

import io.voqs.helpers.Color;
import io.voqs.helpers.Flower;
import io.voqs.helpers.Position;
import io.voqs.blocks.Block;
import io.voqs.globals.EnginePreferences;

import java.util.Random;

class Chunk {
    protected Block[] blocks;
    protected Position position;

    public Flower[] flowers = new Flower[4];
    public boolean generated = false;

    public Chunk(int x, int y) {
        blocks = new Block[EnginePreferences.getChunkSize() * EnginePreferences.getChunkSize()];
        position = new Position(x, y);

        Random random = EnginePreferences.getRandom();
        int chunkSize = EnginePreferences.getChunkSize() - 1;
        Color[] flowerColors = EnginePreferences.getFlowerColors();

        flowers[0] = new Flower(Position.rand(1, chunkSize / 2, 1, chunkSize / 2), flowerColors[random.nextInt(0, 4)]);
        flowers[1] = new Flower(Position.rand(chunkSize / 2, chunkSize, 1, chunkSize / 2), flowerColors[random.nextInt(0, 4)]);
        flowers[2] = new Flower(Position.rand(1, chunkSize / 2, chunkSize / 2, chunkSize), flowerColors[random.nextInt(0, 4)]);
        flowers[3] = new Flower(Position.rand(chunkSize / 2, chunkSize, chunkSize / 2, chunkSize), flowerColors[random.nextInt(0, 4)]);
    }

    public int index(int localX, int localY) {
        if (
                localX < 0 || localX >= EnginePreferences.getChunkSize() ||
                        localY < 0 || localY >= EnginePreferences.getChunkSize()
        ) return -1;

        return localX + localY * EnginePreferences.getChunkSize();
    }

    public Block getBlock(int localX, int localY) {
        int idx = index(localX, localY);
        if (idx < 0 || idx >= blocks.length) return null;

        return blocks[idx];
    }

    public void setBlock(int localX, int localY, Block block) {
        int idx = index(localX, localY);
        if (idx < 0 || idx >= blocks.length) return;

        blocks[idx] = block;
    }

    public void setBlockSafe(int localX, int localY, Block block) {
        int idx = index(localX, localY);
        if (idx < 0 || idx >= blocks.length) return;

        if (blocks[idx] != null) return;

        blocks[idx] = block;
    }

    public Position getRelative(int tx, int ty){
        return position.mul(EnginePreferences.getChunkSize()).add(new Position(tx, ty));
    }
}
