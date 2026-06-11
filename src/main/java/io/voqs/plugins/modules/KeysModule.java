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

        moduleTable.set("Held", feature_Held());

        moduleTable.set("Up", feature_Up());

        return moduleTable;
    }

    private static OneArgFunction feature_Up() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(KeyInput.isKeyUp(arg.checkjstring()));
            }
        };
    }

    private static OneArgFunction feature_Held() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(KeyInput.isKeyHeld(arg.checkjstring()));
            }
        };
    }
}
