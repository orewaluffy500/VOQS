package io.voqs.plugins.modules;

import io.voqs.blocks.BlockRegistry;
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
                String name = args.checkjstring(1);
                LuaValue lColor = args.checktable(2);
                String texName = args.checkjstring(3);

                if (texName.contains("..") || !texName.startsWith("block")) return LuaValue.NONE;

                BlockRegistry.registerBlock(this.name, texName, BlockRegistry.lua2Raylib(lColor));

                return LuaValue.TRUE;
            }
        });


        moduleTable.set("Deregister", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                String name = args.checkjstring(1);

                BlockRegistry.unregisterBlock(name);

                return LuaValue.TRUE;
            }
        });

        moduleTable.set("IsRegistered", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                String name = arg.checkjstring();

                return LuaValue.valueOf(BlockRegistry.isRegistered(name));
            }
        });

        return moduleTable;
    }
}
