package com.example.asteroids.model;

import javafx.scene.shape.Polygon;

public class Projectile extends Character {

    private int x;
    private int y;
    private long timeCreated;

    public Projectile(int x, int y) {
        super(new Polygon(4, -1.5, 4, 1.5, -4, 1.5, -4, -1.5), x, y);
        this.x = x;
        this.y = y;
        this.timeCreated = System.nanoTime();
    }

    public long getTimeCreated() {
        return this.timeCreated;
    }
}
