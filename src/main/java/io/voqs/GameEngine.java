package io.voqs;
import com.raylib.*;
import io.voqs.globals.TextureRegistry;
import io.voqs.globals.VGlobals;
import io.voqs.plugins.PluginEngine;
import io.voqs.world.World;

public class GameEngine {
    public GameEngine(int width, int height, int fps, String title) {
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.title = title;
        this.world = new World();
        this.pluginEngine = new PluginEngine(world);

        initialize();

        pluginEngine.loadScript("./metadata/plugins/test.lua");
    }

    private void initialize(){
        Raylib.InitWindow(width, height, title);
        Raylib.SetTargetFPS(fps);
    }

    private void update(float deltaTime){
        Raylib.ClearBackground(backgroundColor);

        Raylib.BeginDrawing();

        world.draw();
        Raylib.DrawText(Integer.toString(Raylib.GetFPS()), 2, 2, 16, Colors.RAYWHITE);
        Raylib.EndDrawing();
    }

    public void gameloop(){
        pluginEngine.runOnceCallback();

        float timer = 0f;

        while (!Raylib.WindowShouldClose()){
            timer += Raylib.GetFrameTime();

            if (timer >= (1.0f / VGlobals.PULSE_RATE)){
                timer = 0;
                pluginEngine.runPulseCallback();
            }

            update(Raylib.GetFrameTime());
        }

        pluginEngine.runExitCallback();

        destroy();
    }

    private void destroy(){

        TextureRegistry.unloadAll();
        Raylib.CloseWindow();
    }

    private final int width, height, fps;
    private final String title;
    private final World world;
    private final PluginEngine pluginEngine;

    private Raylib.Color backgroundColor = Colors.RAYWHITE;

    public void setBackgroundColor(Raylib.Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFps() {
        return fps;
    }

    public String getTitle() {
        return title;
    }

    public Raylib.Color getBackgroundColor() {
        return backgroundColor;
    }
}
