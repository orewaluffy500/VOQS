package io.voqs.plugins;

import io.voqs.plugins.modules.*;
import io.voqs.world.Player;
import io.voqs.world.World;
import org.luaj.vm2.Globals;

public class PluginAPIBuilder {
    private Globals globals;
    public final World world;
    public final Player player;

    public PluginAPIBuilder(Globals globals, World world, Player player) {
        this.globals = globals;
        this.world = world;
        this.player = player;
    }

    public void initializeAPI() {
        globals.set("Logger",   new LoggerModule().build(this));
        globals.set("Player",   new PlayerModule().build(this));
        globals.set("Keys",     new KeysModule().build(this));
        globals.set("Registry", new RegistryModule().build(this));
        globals.set("World",    new WorldModule().build(this));
    }

}
