package com.edusnake;

public class Snake {

    private final double[] snakeX;
    private final double[] snakeY;
    private final String[] letters;
    private int length;
    private int score;
    private Direction direction;

    public Snake(double locationX, double locationY, Direction direction){ // Create a snake with parameter
        length = 1; // The initial length of the snake is 1, just snake head
        snakeX = new double[900]; // Create an array to Store all X position of snake.
        snakeY = new double[900]; // Create an array to Store all Y position of snake.
        letters = new String[1500]; // Create an array to Store all letter of snake's body.
        snakeX[0] = locationX; // Set snake head's X position
        snakeY[0] = locationY; // Set snake head's Y position
        this.direction = direction; //Set snake head's direction
    }

    public double[] getSnakeX() {
        return snakeX;
    }

    public double[] getSnakeY() {
        return snakeY;
    }

    public String[] getLetters() {
        return letters;
    }

    public int getLength() {
        return length;
    }

    public int getScore() {
        return score;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setScore(int score) {
        this.score = score;
        if (this.score < 0){
            this.score = 0;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
