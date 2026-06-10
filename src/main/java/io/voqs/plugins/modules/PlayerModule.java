package io.voqs.plugins.modules;

import io.voqs.globals.SystemErrorMan;
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
                LuaValue x = args.arg(1);
                LuaValue y = args.arg(2);

                if (SystemErrorMan.LuaErrors.checkInvalidArgs(new LuaValue[]{x, y}, LuaValue.TINT, LuaValue.TINT)) return LuaValue.NONE;

                builder.player.x = x.toint();
                builder.player.y = y.toint();

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
                LuaValue name = args.arg(1);
                if (!name.isstring()) return LuaValue.NONE;

                builder.player.setHolding(name.tojstring());

                return LuaValue.TRUE;
            }
        });


        moduleTable.set("Meta", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                if (!arg.isstring()) return LuaValue.NONE;

                return builder.player.metadata.metaValueLua(arg.tojstring());
            }
        });

        return moduleTable;
    }
}
