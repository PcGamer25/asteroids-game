package com.example.asteroids.model;

import com.example.asteroids.main.Main;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Character {

    private Polygon character;
    private Point2D acceleration;
    private boolean alive;

    public Character(Polygon polygon, int x, int y) {
        this.character = polygon;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);
        this.acceleration = new Point2D(0, 0);
        this.alive = true;
    }

    public Polygon getCharacter() {
        return this.character;
    }

    public void move() {
        this.character.setTranslateX(this.character.getTranslateX() + this.acceleration.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.acceleration.getY());

        if (this.character.getTranslateX() < 0) {
            this.character.setTranslateX(Main.WIDTH);
        }

        if (this.character.getTranslateX() > Main.WIDTH) {
            this.character.setTranslateX(0);
        }

        if (this.character.getTranslateY() < 0) {
            this.character.setTranslateY(Main.HEIGHT);
        }

        if (this.character.getTranslateY() > Main.HEIGHT) {
            this.character.setTranslateY(0);
        }
    }

    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - 5);
    }

    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 5);
    }

    public void thrust(double speed) {
        double deltaX = Math.cos(Math.toRadians(this.character.getRotate()));
        double deltaY = Math.sin(Math.toRadians(this.character.getRotate()));

        deltaX *= speed;
        deltaY *= speed;

        this.acceleration = this.acceleration.add(deltaX, deltaY);
    }

    public void decelerate() {
        this.acceleration = this.acceleration.add(-this.acceleration.getX() * 0.01, -this.acceleration.getY() * 0.01);
    }

    public boolean collide(Character character) {
        Shape collision = Shape.intersect(this.character, character.getCharacter());
        return collision.getBoundsInLocal().getWidth() != -1;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean status) {
        this.alive = status;
    }

}
