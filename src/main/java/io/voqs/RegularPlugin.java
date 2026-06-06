package io.voqs;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class RegularPlugin {
    protected final LuaValue onceCallback;
    protected final LuaValue pulseCallback;
    protected final LuaValue exitCallback;

    public RegularPlugin(LuaTable env) {
        this.onceCallback = env.get("Once");
        this.pulseCallback = env.get("Pulse");
        this.exitCallback = env.get("Exit");
    }

    public void runCallback(LuaValue callback){
        if (!callback.isnil()){
            callback.call();
        }
    }
}
