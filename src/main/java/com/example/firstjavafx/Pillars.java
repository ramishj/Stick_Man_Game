package com.example.firstjavafx;

public class Pillars extends  GameObjects{
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;

    public Pillars(int x, int y, int width, int height, int speed) {
        super(x, y, width, height, speed);


    }

    public void move() {
        x -= speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
