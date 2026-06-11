package io.voqs.plugins.modules;

import io.voqs.globals.Java2LuaBridge;
import io.voqs.globals.EnginePreferences;
import io.voqs.plugins.PluginAPIBuilder;
import io.voqs.plugins.PluginHelpers;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class PlayerModule implements LuaModule {
    @Override
    public LuaTable build(PluginAPIBuilder builder) {
        LuaTable moduleTable = new LuaTable();

        moduleTable.set("Position", valueFeature_Position(builder));
        moduleTable.set("SelectedBlock", valueFeature_SelectedBlock(builder));

        moduleTable.set("Meta", valueFeature_Meta(builder));

        return moduleTable;
    }

    private static VarArgFunction valueFeature_SelectedBlock(PluginAPIBuilder builder) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                if (args.narg() > 0) {
                    String block = args.checkjstring(1);

                    builder.player.setHolding(block);
                }

                return LuaValue.valueOf(builder.player.getHolding());
            }
        };
    }

    private static VarArgFunction valueFeature_Position(PluginAPIBuilder builder) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                if (args.narg() < 2) {
                    return PluginHelpers.makeVarargs(builder.player.x, builder.player.y);
                }

                int x = args.toint(1);
                int y = args.toint(2);

                int dx = Math.abs(builder.player.x - x);
                int dy = Math.abs(builder.player.y - y);

                int maxTeleportDistance = EnginePreferences.getMaxTeleportDistance();

                if (dx > maxTeleportDistance || dy > maxTeleportDistance) {
                    Java2LuaBridge.Logger.Error("Maximum Distance Threshold", "Cannot teleport player farther than " + maxTeleportDistance + " blocks.");
                    return LuaValue.NONE;
                }

                builder.player.x = x;
                builder.player.y = y;

                return LuaValue.NONE;
            }
        };
    }

    private static OneArgFunction valueFeature_Meta(PluginAPIBuilder builder) {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return builder.player.metadata.metaValueLua(arg.checkjstring());
            }
        };
    }
}
