package io.voqs;

import com.raylib.Helpers;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        GameEngine engine = new GameEngine(896, 640, 60, "VOQS Engine");
        engine.setBackgroundColor(Helpers.newColor(4, 4, 20, 255));

        engine.gameloop();
    }
}
