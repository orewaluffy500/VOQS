package io.voqs.plugins;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.Arrays;

public class PluginHelpers {
    public static Varargs makeVarargs(Object... obj){
        return LuaValue.varargsOf(
                Arrays.stream(obj)
                        .map(CoerceJavaToLua::coerce)
                        .toArray(LuaValue[]::new)
        );
    }
}
