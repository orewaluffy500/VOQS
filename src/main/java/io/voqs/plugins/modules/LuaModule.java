package io.voqs.plugins.modules;

import io.voqs.plugins.PluginAPIBuilder;
import org.luaj.vm2.LuaTable;

interface LuaModule {
    LuaTable build(PluginAPIBuilder builder);
}