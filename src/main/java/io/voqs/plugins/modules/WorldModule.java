package io.voqs.plugins.modules;

import io.voqs.blocks.Block;
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
                int x = args.checkint(1);
                int y = args.checkint(2);

                String name = args.checkjstring(3);

                builder.world.placeBlock(x, y, name);

                return LuaValue.TRUE;
            }
        });

        worldTable.set("Break", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                int x = args.checkint(1);
                int y = args.checkint(2);

                builder.world.removeBlock(x, y);

                return LuaValue.TRUE;
            }
        });

        worldTable.set("GetAt", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {

                int x = args.checkint(1);
                int y = args.checkint(2);

                Block block = builder.world.getBlock(x, y);

                return LuaValue.valueOf(block.getName());
            }
        });

        return worldTable;
    }
}
