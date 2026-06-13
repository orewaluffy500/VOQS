package io.voqs;
import com.raylib.*;
import io.voqs.globals.Java2LuaBridge;
import io.voqs.globals.TextureRegistry;
import io.voqs.globals.EnginePreferences;
import io.voqs.helpers.Color;
import io.voqs.helpers.Position;
import io.voqs.helpers.Size;
import io.voqs.plugins.PluginEngine;
import io.voqs.world.World;

public class GameEngine {

    private float pulseTimer = 0f;
    private int pulseIndex = 0;

    public GameEngine(int width, int height, int fps, String title) {
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.title = title;
        this.world = new World(this);
        this.pluginEngine = new PluginEngine(world);

        initialize();

        pluginEngine.loadScript("./game/plugins/test.lua");
    }

    private void initialize(){
        Raylib.SetTraceLogLevel(Raylib.LOG_ERROR);
        Raylib.SetWindowState(Raylib.FLAG_WINDOW_RESIZABLE);
        Raylib.InitWindow(width, height, title);
        Raylib.SetTargetFPS(fps);
    }

    private void update(float deltaTime){
        Raylib.ClearBackground(backgroundColor);

        Raylib.BeginDrawing();

        world.drawAndUpdate(getPulseTimer() <= 0f);
        Raylib.DrawText(Integer.toString(Raylib.GetFPS()), 2, 2, 16, Colors.RAYWHITE);
        Raylib.EndDrawing();
    }

    public void drawRect(Position pos, Size size, Color color){
        Raylib.DrawRectangle(
                pos.x(),
                pos.y(),
                size.x(),
                size.y(),
                color.toRaylib()
        );
    }


    public void gameloop(){
        pluginEngine.runOnceCallback();

        while (!Raylib.WindowShouldClose()){
            pulseTimer = getPulseTimer() + Raylib.GetFrameTime();

            if (getPulseTimer() >= (1.0f / EnginePreferences.getPulseRate())){
                pulseTimer = 0;
                pulseIndex++;
                if (pulseIndex >= EnginePreferences.getPulseRate()) pulseIndex = 0;

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

    public PluginEngine getPluginEngine() {
        pluginEngine = new PluginEngine(world);
        return pluginEngine;
    }

    private PluginEngine pluginEngine;

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

    public int getPulseIndex() {
        return pulseIndex;
    }

    public float getPulseTimer() {
        return pulseTimer;
    }
}
