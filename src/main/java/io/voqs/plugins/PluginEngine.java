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
import java.util.HashMap;

public class PluginEngine {
    private final Globals globals = JsePlatform.standardGlobals();
    private final HashMap<String, RegularPlugin> plugins = new HashMap<>();
    private final World world;
    private final Player player;

    public PluginEngine(World world1){
        world = world1;
        player = world.player;

        new PluginAPIBuilder(globals, world, player).initializeAPI();
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
}
