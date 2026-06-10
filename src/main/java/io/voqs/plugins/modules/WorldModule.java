package io.voqs.plugins.modules;

import io.voqs.globals.SystemErrorMan;
import io.voqs.plugins.PluginAPIBuilder;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class WorldModule implements LuaModule {
    @Override
    public LuaTable build(PluginAPIBuilder builder) {
        LuaTable worldTable = new LuaTable();


        worldTable.set("Place", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                if (args.narg() < 3) {
                    return LuaValue.NONE;
                }

                LuaValue x = args.arg(1);
                LuaValue y = args.arg(2);
                LuaValue name = args.arg(3);

                if (SystemErrorMan.LuaErrors.checkInvalidArgs(new LuaValue[]{x, y, name}, LuaValue.TINT, LuaValue.TINT, LuaValue.TSTRING)) return LuaValue.NONE;

                builder.world.placeBlock(x.toint(), y.toint(), name.tojstring());

                return LuaValue.TRUE;
            }
        });

        worldTable.set("Break", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                if (args.narg() < 2) {
                    return LuaValue.NONE;
                }

                LuaValue x = args.arg(1);
                LuaValue y = args.arg(2);

                if (SystemErrorMan.LuaErrors.checkInvalidArgs(new LuaValue[]{x, y}, LuaValue.TINT, LuaValue.TINT)) return LuaValue.NONE;

                builder.world.removeBlock(x.toint(), y.toint());

                return LuaValue.TRUE;
            }
        });

        return worldTable;
    }
}
