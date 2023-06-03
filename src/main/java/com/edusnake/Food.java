package com.edusnake;

import java.util.Random;

public class Food {

    private double foodX; //The X position of the food
    private double foodY; //The Y position of the food
    private final Random random;
    private String letter;


    public Food(Snake snake, Food[] foods) {
        random = new Random();
        randomFoodPosition(snake, foods); // Random a food position, but can't coincide with snake
    }

    public Food(Snake snake1, Snake snake2, Food[] foods) { // Random multiple food, but can't coincide with other food and two snake
        random = new Random();
        randomFoodPosition(snake1, snake2, foods);
        letter = String.valueOf((char) new Random().nextInt(65, 90));// The range in which letters appear in A to Z
    }

    public void randomFoodPosition(Snake snake, Food[] foods) { //Random a food position, but can't coincide with snake

        boolean isEqualX;
        boolean isEqualY;
        do {
            isEqualX = false;
            isEqualY = false;
            foodX = SnakeUtils.unitSize * random.nextInt((int) (SnakeUtils.width / SnakeUtils.unitSize - 1)); // The X range in which food appears randomly must be within the game interface, number = width/unitSize-1
            foodY = SnakeUtils.topBarHeight + SnakeUtils.unitSize * random.nextInt((int) (SnakeUtils.height / SnakeUtils.unitSize - 1));// The Y range in which food appears randomly must be within the game interface
            for (double snakeX : snake.getSnakeX()) { //Determine if the food coincides with the snake
                if (foodX == snakeX) {
                    isEqualX = true;
                    break;
                }
            }
            for (double snakeY : snake.getSnakeY()) {
                if (foodY == snakeY) {
                    isEqualY = true;
                    break;
                }
            }

            for (Food food : foods) {
                if (food != null) {
                    if (foodX == food.foodX) {
                        isEqualX = true;
                    }
                    if (foodY == food.foodY) {
                        isEqualY = true;
                    }
                }
            }

        } while (isEqualX && isEqualY);

    }

    public void randomFoodPosition(Snake snake1, Snake snake2, Food[] foods) { //Logic is same as above

        boolean isEqualX;
        boolean isEqualY;
        do {
            isEqualX = false;
            isEqualY = false;
            foodX = SnakeUtils.unitSize * random.nextInt((int) (SnakeUtils.width / SnakeUtils.unitSize - 1)); // The X range in which food appears randomly must be within the game interface, number = width/unitSize-1
            foodY = SnakeUtils.topBarHeight + SnakeUtils.unitSize * random.nextInt((int) (SnakeUtils.height / SnakeUtils.unitSize - 1));// The Y range in which food appears randomly must be within the game interface
            for (double snakeX : snake1.getSnakeX()) {
                if (foodX == snakeX) {
                    isEqualX = true;
                    break;
                }
            }
            for (double snakeY : snake1.getSnakeY()) {
                if (foodY == snakeY) {
                    isEqualY = true;
                    break;
                }
            }
            for (double snakeX : snake2.getSnakeX()) {
                if (foodX == snakeX) {
                    isEqualX = true;
                    break;
                }
            }
            for (double snakeY : snake2.getSnakeY()) {
                if (foodY == snakeY) {
                    isEqualY = true;
                    break;
                }
            }


            for (Food food : foods) {
                if (food != null) {
                    if (foodX == food.foodX) {
                        isEqualX = true;
                    }
                    if (foodY == food.foodY) {
                        isEqualY = true;
                    }
                }
            }
        } while (isEqualX && isEqualY);
    }

    public Food getFoodFromFile() {

        return this;
    }

    public double getFoodX() {
        return foodX;
    }

    public double getFoodY() {
        return foodY;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
