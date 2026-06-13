package io.voqs.helpers;

import com.raylib.Helpers;
import com.raylib.Raylib;
import io.voqs.globals.EnginePreferences;

public class Position {
    public Position add(Position b) {
        return new Position(x + b.x, y + b.y);
    }

    public Position add(int b) {
        return new Position(x + b, y + b);
    }

    public Position mul(Position b) {
        return new Position(x * b.x, y * b.y);
    }

    public Position mul(int b) {
        return new Position(x * b, y * b);
    }

    public Position div(Position b) {
        return new Position(x / b.x, y / b.y);
    }

    public Position div(int b) {
        return new Position(x / b, y / b);
    }

    public Position sub(Position b) {
        return new Position(x - b.x, y - b.y);
    }

    public Position sub(int b) {
        return new Position(x - b, y - b);
    }

    public static Position rand(int x1, int x2, int y1, int y2){
        return new Position(
                EnginePreferences.getRandom().nextInt(x1, x2),
                EnginePreferences.getRandom().nextInt(y1, y2)
        );
    }

    public Raylib.Vector2 toRaylib() {
        return Helpers.newVector2(x, y);
    }

    public void move(int dx, int dy){
        x += dx;
        y += dy;
    }

    public void move(Position other){
        move(other.x(), other.y());
    }

    public void set(Position n){
        x = n.x;
        y = n.y;
    }

    public void set(int nx, int ny){
        x = nx;
        y = ny;
    }

    public boolean isZero(){
        return x + y == 0;
    }

    public int sum(){
        return x + y;
    }

    public int x() {
        return x;
    }

    public void x(int x) {
        this.x = x;
    }

    public int y() {
        return y;
    }

    public void y(int y) {
        this.y = y;
    }
    public static Position ZERO(){
        return new Position(0, 0);
    }
    public static Position from(int _x, int _y){ return new Position(_x, _y); }

    private int x;
    private int y;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
