package io.voqs.plugins;

import io.voqs.globals.KeyInput;
import io.voqs.world.Player;
import io.voqs.world.World;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class PluginAPIBuilder {
    private Globals globals;
    private final World world;
    private final Player player;

    public PluginAPIBuilder(Globals globals, World world, Player player) {
        this.globals = globals;
        this.world = world;
        this.player = player;
    }

    public void initializeAPI(){
        makeLogTable();
        makeKeysTable();
        makePlayerTable();
        makeMetadataTable();
    }

    private void makeLogTable() {
        LuaTable logTable = new LuaTable();

        logTable.set("Notify", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                System.out.println("[NOTIFIC] " + arg.toString());
                return null;
            }
        });

        logTable.set("Error", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue cause, LuaValue arg) {
                System.out.printf("[ERROR] %s :: %s%n", cause.tostring(), arg.tostring());
                return null;
            }
        });

        logTable.set("Warn", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue cause, LuaValue arg) {
                System.out.printf("[WARN] %s :: %s%n", cause.tostring(), arg.tostring());
                return null;
            }
        });

        globals.set("Logger", logTable);
    }

    private void makePlayerTable(){
        LuaTable playerTable = new LuaTable();

        playerTable.set("GetPosition", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                return LuaValue.varargsOf(new LuaValue[] {
                        LuaValue.valueOf(player.x),
                        LuaValue.valueOf(player.y)
                });
            }
        });

        playerTable.set("TeleportTo", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                LuaValue x = args.arg(1);
                LuaValue y = args.arg(2);

                if (!x.isint() || !y.isint()) return LuaValue.NONE;

                player.x = x.toint();
                player.y = y.toint();

                return LuaValue.NONE;
            }
        });

        playerTable.set("GetSelectedBlock", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(player.getHolding());
            }
        });

        playerTable.set("SelectBlock", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                LuaValue name = args.arg(1);
                if (!name.isstring()) return LuaValue.NONE;

                player.setHolding(name.tojstring());

                return LuaValue.NONE;
            }
        });

        globals.set("Player", playerTable);
    }


    private void makeKeysTable(){
        LuaTable keysTable = new LuaTable();

        keysTable.set("Held", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(KeyInput.isKeyHeld(arg.tojstring()));
            }
        });

        keysTable.set("Up", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(KeyInput.isKeyUp(arg.tojstring()));
            }
        });

        globals.set("Keys", keysTable);
    }

    private void makeMetadataTable(){
        LuaTable metadataTable = new LuaTable();

        metadataTable.set("PlayerMeta", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                if (!arg.isstring()) return LuaValue.NONE;

                return player.metadata.metaValueLua(arg.tojstring());
            }
        });

        metadataTable.set("PlayerMetaAdd", metadataTable.get("PlayerMeta"));

        globals.set("Metadata", metadataTable);
    }
}
