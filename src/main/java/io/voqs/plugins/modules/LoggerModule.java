package io.voqs.plugins.modules;

import io.voqs.plugins.PluginAPIBuilder;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class LoggerModule implements LuaModule {
    @Override
    public LuaTable build(PluginAPIBuilder builder) {
        LuaTable moduleTable = new LuaTable();

        moduleTable.set("Notify", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                System.out.println("[NOTIFIC] " + arg.checkjstring());
                return null;
            }
        });

        moduleTable.set("Error", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue cause, LuaValue arg) {
                System.out.printf("[ERROR] %s :: %s%n", cause.checkjstring(), arg.checkjstring());
                return null;
            }
        });

        moduleTable.set("Warn", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue cause, LuaValue arg) {
                System.out.printf("[WARN] %s :: %s%n", cause.checkjstring(), arg.checkjstring());
                return null;
            }
        });

        return moduleTable;
    }
}
