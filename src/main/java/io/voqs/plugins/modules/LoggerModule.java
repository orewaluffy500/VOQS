package io.voqs.plugins.modules;

import io.voqs.globals.Java2LuaBridge;
import io.voqs.plugins.PluginAPIBuilder;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class LoggerModule implements LuaModule {
    @Override
    public LuaTable build(PluginAPIBuilder builder) {
        LuaTable moduleTable = new LuaTable();

        moduleTable.set("Notify", feature_Notify());

        moduleTable.set("Error", feature_Error());

        moduleTable.set("Warn", feature_Warn());

        return moduleTable;
    }

    private static TwoArgFunction feature_Warn() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue cause, LuaValue arg) {
                Java2LuaBridge.Logger.Warn(cause.checkjstring(), arg.checkjstring());
                return null;
            }
        };
    }

    private static TwoArgFunction feature_Error() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue cause, LuaValue arg) {
                Java2LuaBridge.Logger.Error(cause.checkjstring(), arg.checkjstring());
                return null;
            }
        };
    }

    private static OneArgFunction feature_Notify() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                Java2LuaBridge.Logger.Notify(arg.checkjstring());
                return null;
            }
        };
    }
}
