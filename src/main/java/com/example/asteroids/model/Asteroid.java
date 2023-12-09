package com.example.asteroids.model;

import com.example.asteroids.util.PolygonCreater;

import java.util.Random;

public class Asteroid extends Character {

    private double rotationAmount;

    public Asteroid(int x, int y) {
        super(new PolygonCreater().createPolygon(), x, y);

        Random random = new Random();

        this.getCharacter().setRotate(random.nextInt(360));

        int randomAcc = 1 + random.nextInt(10);
        for (int i = 0; i < randomAcc; i++) {
            this.thrust(0.30);
        }

        this.rotationAmount = 50 - random.nextInt(100);
    }

    public void move() {
        super.move();
        this.getCharacter().setRotate(this.getCharacter().getRotate() + this.rotationAmount);
    }
}
