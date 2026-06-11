package io.voqs.plugins.modules;

import io.voqs.blocks.Block;
import io.voqs.plugins.PluginAPIBuilder;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class TerrainModule implements LuaModule {
    @Override
    public LuaTable build(PluginAPIBuilder builder) {
        LuaTable terrainTable = new LuaTable();


        terrainTable.set("PlaceBlock", feature_Place(builder));

        terrainTable.set("BreakBlock", feature_Break(builder));

        terrainTable.set("GetBlock", feature_GetAt(builder));

        return terrainTable;
    }

    private static VarArgFunction feature_GetAt(PluginAPIBuilder builder) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {

                int x = args.checkint(1);
                int y = args.checkint(2);

                Block block = builder.world.getBlock(x, y);

                return LuaValue.valueOf(block.getName());
            }
        };
    }

    private static VarArgFunction feature_Break(PluginAPIBuilder builder) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                int x = args.checkint(1);
                int y = args.checkint(2);

                builder.world.removeBlock(x, y);

                return LuaValue.TRUE;
            }
        };
    }

    private static VarArgFunction feature_Place(PluginAPIBuilder builder) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                int x = args.checkint(1);
                int y = args.checkint(2);

                String name = args.checkjstring(3);

                builder.world.placeBlock(x, y, name);

                return LuaValue.TRUE;
            }
        };
    }
}
