package io.voqs.plugins.modules;

import io.voqs.globals.Java2LuaBridge;
import io.voqs.globals.EnginePreferences;
import io.voqs.plugins.PluginAPIBuilder;
import io.voqs.plugins.PluginHelpers;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class PlayerModule implements LuaModule {
    @Override
    public LuaTable build(PluginAPIBuilder builder) {
        LuaTable moduleTable = new LuaTable();

        moduleTable.set("Position", valueFeature_Position(builder));
        moduleTable.set("SelectedBlock", valueFeature_SelectedBlock(builder));

        moduleTable.set("Meta", valueFeature_Meta(builder));
        moduleTable.set("OptMeta", feature_OptMeta(builder));

        moduleTable.set("CursorPosition", valueFeature_CursorPosition(builder));

        moduleTable.set("Place", feature_Place(builder));
        moduleTable.set("BreaK", feature_Break(builder));

        return moduleTable;
    }

    private static ZeroArgFunction feature_Break(PluginAPIBuilder builder) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                builder.player.breakBlock();
                return LuaValue.NONE;
            }
        };
    }

    private static ZeroArgFunction feature_Place(PluginAPIBuilder builder) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                builder.player.placeBlock();
                return LuaValue.NONE;
            }
        };
    }

    private static VarArgFunction valueFeature_CursorPosition(PluginAPIBuilder builder){
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                if (args.narg() > 0) {
                    int x = args.checkint(1);
                    int y = args.checkint(2);

                    builder.player.cursorPos.x(x);
                    builder.player.cursorPos.y(y);
                }

                return PluginHelpers.makeVarargs(builder.player.cursorPos.x(), builder.player.cursorPos.y());
            }
        };
    }

    private static VarArgFunction feature_OptMeta(PluginAPIBuilder builder) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                String name = args.checkjstring(1);
                LuaValue defaul = args.checkvalue(2);

                return builder.player.metadata.getValueSafe(name, defaul);
            }
        };
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

    private static VarArgFunction valueFeature_Meta(PluginAPIBuilder builder) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                if (args.narg() > 1){
                    builder.player.metadata.setValue(args.checkjstring(1), args.checkvalue(2));
                    return LuaValue.NONE;
                }

                return builder.player.metadata.getValue(args.checkjstring(1));
            }
        };
    }
}
