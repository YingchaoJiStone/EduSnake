package com.edusnake;

import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public interface SnakeGame {

    void start(Stage stage);

    void draw(GraphicsContext gc);

    void initSnake();

    void action();

    void keyEvent();

}
