package io.voqs.plugins.modules;

import io.voqs.globals.EngineModifiers;
import io.voqs.globals.EnginePreferences;
import io.voqs.plugins.PluginAPIBuilder;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import static com.raylib.Raylib.GetFrameTime;

public class ClockModule implements LuaModule {
    @Override
    public LuaTable build(PluginAPIBuilder builder) {
        LuaTable clockTable = new LuaTable();

        clockTable.set("DeltaTime", feature_DeltaTime());

        clockTable.set("GetPulseRate", feature_PulseRate());
        
        clockTable.set("PulseSpeed", valueFeature_PulseSpeed());

        clockTable.set("GetPulseIndex", feature_GetPulseIndex(builder));

        return clockTable;
    }

    private static ZeroArgFunction feature_GetPulseIndex(PluginAPIBuilder builder) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(builder.world.getEngine().getPulseIndex());
            }
        };
    }

    private static OneArgFunction valueFeature_PulseSpeed() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                if (!arg.isnil()) {
                    int m = arg.checkint();

                    EngineModifiers.setPulseSpeed(m);
                }

                return LuaValue.valueOf(EngineModifiers.getPulseSpeed());
            }
        };
    }

    private static OneArgFunction feature_PulseRate() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(EnginePreferences.getPulseRate());
            }
        };
    }

    private static ZeroArgFunction feature_DeltaTime() {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(GetFrameTime());
            }
        };
    }
}
