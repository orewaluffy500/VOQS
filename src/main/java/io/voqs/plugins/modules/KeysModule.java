package io.voqs.plugins.modules;

import io.voqs.globals.KeyInput;
import io.voqs.plugins.PluginAPIBuilder;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class KeysModule implements LuaModule {
    @Override
    public LuaTable build(PluginAPIBuilder builder) {
        LuaTable moduleTable = new LuaTable();

        moduleTable.set("Held", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(KeyInput.isKeyHeld(arg.tojstring()));
            }
        });

        moduleTable.set("Up", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(KeyInput.isKeyUp(arg.tojstring()));
            }
        });

        return moduleTable;
    }
}
