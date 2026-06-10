package io.voqs.plugins.modules;

import io.voqs.blocks.BlockRegistry;
import io.voqs.globals.SystemErrorMan;
import io.voqs.plugins.PluginAPIBuilder;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class RegistryModule implements LuaModule {
    @Override
    public LuaTable build(PluginAPIBuilder builder) {
        LuaTable moduleTable = new LuaTable();

        moduleTable.set("Register", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                LuaValue lName = args.arg(1);
                LuaValue lColor = args.arg(2);
                LuaValue lTexName = args.arg(3);

                if (SystemErrorMan.LuaErrors.checkInvalidArgs(new LuaValue[]{lName, lColor, lTexName}, LuaValue.TSTRING, LuaValue.TTABLE, LuaValue.TSTRING)) return LuaValue.NONE;

                String name = lName.tojstring();
                String texName = lTexName.tojstring();

                if (texName.contains("..") || !texName.startsWith("block")) return LuaValue.NONE;

                BlockRegistry.registerBlock(name, texName, BlockRegistry.lua2Raylib(lColor));

                return LuaValue.TRUE;
            }
        });


        moduleTable.set("Deregister", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                LuaValue lName = args.arg(1);
                if (!lName.isstring()) return LuaValue.NONE;

                String name = lName.tojstring();

                BlockRegistry.unregisterBlock(name);

                return LuaValue.TRUE;
            }
        });

        moduleTable.set("IsRegistered", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                if (!arg.isstring()) return LuaValue.valueOf(false);

                String name = arg.tojstring();

                return LuaValue.valueOf(BlockRegistry.isRegistered(name));
            }
        });

        return moduleTable;
    }
}
