package io.voqs.plugins.modules;

import io.voqs.plugins.PluginAPIBuilder;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class PlayerModule implements LuaModule {
    @Override
    public LuaTable build(PluginAPIBuilder builder) {
        LuaTable moduleTable = new LuaTable();

        moduleTable.set("GetPosition", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                return LuaValue.varargsOf(new LuaValue[] {
                        LuaValue.valueOf(builder.player.x),
                        LuaValue.valueOf(builder.player.y)
                });
            }
        });

        moduleTable.set("TeleportTo", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                int x = args.checkint(1);
                int y = args.checkint(2);

                builder.player.x = x;
                builder.player.y = y;

                return LuaValue.TRUE;
            }
        });

        moduleTable.set("GetSelectedBlock", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(builder.player.getHolding());
            }
        });

        moduleTable.set("SelectBlock", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {

                builder.player.setHolding(args.checkjstring(1));

                return LuaValue.TRUE;
            }
        });


        moduleTable.set("Meta", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {

                return builder.player.metadata.metaValueLua(arg.checkjstring());
            }
        });

        return moduleTable;
    }
}
