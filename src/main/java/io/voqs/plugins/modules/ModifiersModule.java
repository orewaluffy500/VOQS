package io.voqs.plugins.modules;

import io.voqs.globals.EngineModifiers;
import io.voqs.plugins.PluginAPIBuilder;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class ModifiersModule implements LuaModule {
    @Override
    public LuaTable build(PluginAPIBuilder builder) {
        LuaTable modModule = new LuaTable();

        modModule.set("WalkSpeed", valueFeature_WalkSpeed());

        modModule.set("PulseSpeed", valueFeature_PulseSpeed());

        modModule.set("BuildSpeed", valueFeature_BuildSpeed());


        return modModule;
    }

    private static OneArgFunction valueFeature_BuildSpeed() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                if (!arg.isnil()) {
                    EngineModifiers.setBuildSpeed(arg.checknumber().tofloat());
                }
                return LuaValue.valueOf(EngineModifiers.getBuildSpeed());
            }
        };
    }

    private static OneArgFunction valueFeature_PulseSpeed() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                if (!arg.isnil()) {
                    EngineModifiers.setPulseSpeed(arg.checknumber().tofloat());
                }
                return LuaValue.valueOf(EngineModifiers.getPulseSpeed());
            }
        };
    }

    private static OneArgFunction valueFeature_WalkSpeed() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                if (!arg.isnil()) {
                    EngineModifiers.setWalkSpeed(arg.checknumber().tofloat());
                }
                return LuaValue.valueOf(EngineModifiers.getWalkSpeed());
            }
        };
    }
}
