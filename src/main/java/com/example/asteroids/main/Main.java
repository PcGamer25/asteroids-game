package com.example.asteroids.main;

import com.example.asteroids.model.Asteroid;
import com.example.asteroids.model.Projectile;
import com.example.asteroids.model.Ship;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main extends Application {

    public static int WIDTH = 600;
    public static int HEIGHT = 400;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) {
        HashMap<KeyCode, Boolean> keyLog = new HashMap<>();
        ArrayList<Asteroid> asteroids = new ArrayList<>();
        ArrayList<Projectile> projectiles = new ArrayList<>();
        HashMap<String, Boolean> checks = new HashMap<>();

        Pane mainPane = new Pane();
        mainPane.setPrefSize(WIDTH, HEIGHT);

        AtomicInteger points = new AtomicInteger();
        Text pointsText = new Text(10, 25, "Points: " + points.get());
        pointsText.setFont(Font.font("Arial", 20));
        mainPane.getChildren().add(pointsText);

        StackPane subPane = new StackPane();
        subPane.setPrefSize(WIDTH, WIDTH);
        subPane.setAlignment(Pos.CENTER);
        Text status = new Text("Press Space to Start");
        status.setFont(Font.font("Arial", 30));
        subPane.getChildren().add(status);
        mainPane.getChildren().add(subPane);

        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);

        mainPane.getChildren().add(ship.getCharacter());
        asteroids.stream().forEach(asteroid -> mainPane.getChildren().add(asteroid.getCharacter()));

        Scene scene = new Scene(mainPane);

        AnimationTimer timer = new AnimationTimer() {
            int level = -1;

            public void handle(long now) {

                if (asteroids.size() == 0) {
                    level++;
                    createAsteroids(level, asteroids, ship);
                    asteroids.stream().forEach(asteroid -> mainPane.getChildren().add(asteroid.getCharacter()));
                }

                if (keyLog.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }
                if (keyLog.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }
                if (keyLog.getOrDefault(KeyCode.UP, false)) {
                    ship.thrust(0.05);
                }

                ship.move();
                ship.decelerate();
                asteroids.stream().forEach(asteroid -> asteroid.move());
                projectiles.stream().forEach(projectile -> projectile.move());

                asteroids.stream().forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        ship.setAlive(false);
                        stop();
                        status.setText("GAME OVER");
                        mainPane.getChildren().add(subPane);
                    }

                    projectiles.stream().forEach(projectile -> {
                        if (projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            mainPane.getChildren().remove(projectile.getCharacter());
                            asteroid.setAlive(false);
                            pointsText.setText("Points: " + points.addAndGet(1000));
                            mainPane.getChildren().remove(asteroid.getCharacter());
                        }
                        if (System.nanoTime() - projectile.getTimeCreated() > 800000000) {
                            projectile.setAlive(false);
                            mainPane.getChildren().remove(projectile.getCharacter());
                        }
                    });
                });

                asteroids.removeAll(asteroids.stream().filter(asteroid -> !asteroid.isAlive()).collect(Collectors.toList()));
                projectiles.removeAll(projectiles.stream().filter(projectile -> !projectile.isAlive()).collect(Collectors.toList()));
            }
        };

        scene.setOnKeyPressed(event -> {
            keyLog.put(event.getCode(), true);

            if (checks.getOrDefault("startScreen", true) && keyLog.getOrDefault(KeyCode.SPACE, false)) {
                timer.start();
                mainPane.getChildren().remove(subPane);
                checks.put("startScreen", false);
                checks.put("lockSpace", true);
            }

            if (keyLog.getOrDefault(KeyCode.SPACE, false) && !checks.getOrDefault("lockSpace", false)) {
                Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                projectile.thrust(7);
                mainPane.getChildren().add(projectile.getCharacter());
                projectiles.add(projectile);
                checks.put("lockSpace", true);
            }
        });

        scene.setOnKeyReleased(event -> {
            keyLog.put(event.getCode(), false);

            if (event.getCode() == KeyCode.SPACE) {
                checks.put("lockSpace", false);
            }
        });

        window.setScene(scene);
        window.setTitle("Asteroids!");
        window.show();
    }

    private void createAsteroids(int level, ArrayList asteroids, Ship ship) {
        for (int i = 0; i < level + 5; i++) {
            Random random = new Random();
            int randomX = random.nextInt(WIDTH);
            int randomY = random.nextInt(HEIGHT);

            if ((randomX > ship.getCharacter().getTranslateX() + 50
                    || randomX < ship.getCharacter().getTranslateX() - 50)
                    && (randomY > ship.getCharacter().getTranslateY() + 50
                    || randomY < ship.getCharacter().getTranslateY() - 50)) {
                Asteroid asteroid = new Asteroid(randomX, randomY);
                asteroids.add(asteroid);
            } else {
                i--;
            }
        }
    }

}
