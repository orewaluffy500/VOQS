package io.voqs.plugins;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;

public class Metadata {
    public HashMap<String, LuaValue> data = new HashMap<>();

    public LuaValue metaValueLua(String name){
        data.putIfAbsent(name, makeLuaValue(LuaValue.NONE));
        return data.getOrDefault(name, LuaValue.NIL);
    }

    public void addValue(String name, LuaValue val){
        if (data.containsKey(name)) return;
        LuaValue dat = makeLuaValue(val);

        data.put(name, dat);
    }

    private static LuaValue makeLuaValue(LuaValue val) {
        LuaValue dat = new LuaTable();
        dat.set("Value", val);
        return dat;
    }

    public LuaValue metaValueNative(String name){
        data.putIfAbsent(name, makeLuaValue(LuaValue.NONE));

        return data.get(name).get("Value");
    }
}
