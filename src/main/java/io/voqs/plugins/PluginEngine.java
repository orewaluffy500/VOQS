package io.voqs.plugins;

import io.voqs.world.Player;
import io.voqs.world.World;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class PluginEngine {
    private final Globals globals = JsePlatform.standardGlobals();
    private final HashMap<String, RegularPlugin> plugins = new HashMap<>();
    private final HashMap<String, ArrayList<BlockPlugin>> blockPlugins = new HashMap<>();

    public PluginEngine(World world1){
        Player player = world1.getPlayer();

        new PluginAPIBuilder(globals, world1, player).initializeAPI();
    }

    public void loadScript(String path) {
        String code;
        Path pathInst = Path.of(path);
        try {
            code = Files.readString(pathInst);
        } catch (IOException e) {
            return;
        }

        LuaTable mt = new LuaTable();
        mt.set("__index", globals);

        LuaTable env = new LuaTable();
        env.setmetatable(mt);

        LuaValue chunk = globals.load(code, "script", env);
        chunk.call();

        // Check if its a block script or regular script
        LuaValue blockId = env.get("BlockId");

        if (!blockId.isnil() && blockId.isstring()){
            BlockPlugin plug = new BlockPlugin(env);

            String id = blockId.tojstring();

            blockPlugins.putIfAbsent(id, new ArrayList<>());
            blockPlugins.get(id).add(plug);

            plugins.put(String.valueOf(pathInst.getFileName()), plug);
        }

        plugins.put(String.valueOf(pathInst.getFileName()), new RegularPlugin(env));
    }

    /* REGULAR SCRIPT CALLBACKS */

    public void runOnceCallback(){
        plugins.forEach((s, regularPlugin) -> regularPlugin.runCallback(regularPlugin.onceCallback));
    }

    public void runPulseCallback(){
        plugins.forEach((s, regularPlugin) -> regularPlugin.runCallback(regularPlugin.pulseCallback));
    }

    public void runExitCallback(){
        plugins.forEach((s, regularPlugin) -> regularPlugin.runCallback(regularPlugin.exitCallback));
    }

    public void runBlockPlacedCallback(String name, int x, int y){
        if (!blockPlugins.containsKey(name)) return;

        for (BlockPlugin plug : blockPlugins.get(name)){
            plug.runCallback(plug.placedCallback, LuaValue.valueOf(x), LuaValue.valueOf(y));
        }
    }

    public LuaValue runBlockBreakingCallback(String name, int x, int y){
        if (!blockPlugins.containsKey(name)) return LuaValue.NONE;

        for (BlockPlugin plug : blockPlugins.get(name)){
            return plug.runCallback(plug.breakingCallback, LuaValue.valueOf(x), LuaValue.valueOf(y));
        }
        return LuaValue.NONE;
    }

    public void runBlockPulse(String name, int x, int y){
        if (!blockPlugins.containsKey(name)) return;

        for (BlockPlugin plug : blockPlugins.get(name)){
            plug.runCallback(plug.blockPulseCallback, LuaValue.valueOf(x), LuaValue.valueOf(y));
        }
    }
}
