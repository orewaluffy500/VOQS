package io.voqs;

import com.raylib.Helpers;
import com.raylib.Raylib;

public final class Position {
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

    private int x;
    private int y;
    public static final Position ZERO = new Position(0, 0);

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
