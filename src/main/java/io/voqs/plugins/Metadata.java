package io.voqs.plugins;

import org.luaj.vm2.LuaValue;

import java.util.HashMap;

public class Metadata {
    public HashMap<String, LuaValue> data = new HashMap<>();

    public LuaValue getValue(String name){
        return data.getOrDefault(name, LuaValue.NIL);
    }

    public LuaValue getValueSafe(String name, LuaValue def){
        data.putIfAbsent(name, def);
        return data.get(name);
    }

    public void setValue(String name, LuaValue value){
        data.putIfAbsent(name, LuaValue.NIL);
        data.replace(name, value);
    }

    public void addValue(String name, LuaValue val){
        if (data.containsKey(name)) return;
        data.put(name, val);
    }
}
