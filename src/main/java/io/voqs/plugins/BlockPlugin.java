package io.voqs.plugins;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class BlockPlugin extends RegularPlugin {
    protected final LuaValue placedCallback;
    protected final LuaValue blockPulseCallback;
    protected final LuaValue breakingCallback;
    protected final String name;

    public BlockPlugin(LuaTable env) {
        super(env);
        placedCallback = env.get("Placed");
        blockPulseCallback = env.get("BlockPulse");
        breakingCallback = env.get("Breaking");
        this.name = env.get("BlockId").tojstring();
    }
}
