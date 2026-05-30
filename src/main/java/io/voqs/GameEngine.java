package io.voqs;
import com.raylib.*;

public class GameEngine {
    public GameEngine(int width, int height, int fps, String title) {
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.title = title;
        this.world = new World();

        initialize();


        world.place_block(18, 13);
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
        while (!Raylib.WindowShouldClose()){
            update(Raylib.GetFrameTime());
        }

        destroy();
    }

    private void destroy(){
        Raylib.CloseWindow();
    }

    private final int width, height, fps;
    private final String title;
    private final World world;

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
