package io.voqs.world;

import com.raylib.Helpers;
import com.raylib.Raylib;
import io.voqs.blocks.Block;
import io.voqs.globals.VGlobals;

class Chunk {
    protected Block[] blocks = new Block[VGlobals.getChunkSize() * VGlobals.getChunkSize()];
    protected Raylib.Vector2 position;

    public Chunk(int x, int y) {
        position = Helpers.newVector2(x, y);
    }

    public int index(int localX, int localY) {
        if (
                localX < 0 || localX >= VGlobals.getChunkSize() ||
                        localY < 0 || localY >= VGlobals.getChunkSize()
        ) return -1;

        return localX + localY * VGlobals.getChunkSize();
    }

    public Block get_block(int localX, int localY) {
        int idx = index(localX, localY);
        if (idx < 0 || idx >= blocks.length) return null;

        return blocks[idx];
    }

    public void set_block(int localX, int localY, Block block) {
        int idx = index(localX, localY);
        if (idx < 0 || idx >= blocks.length) return;

        blocks[idx] = block;
    }

    public void set_block_safe(int localX, int localY, Block block) {
        int idx = index(localX, localY);
        if (idx < 0 || idx >= blocks.length) return;

        if (blocks[idx] != null) return;

        blocks[idx] = block;
    }
}
