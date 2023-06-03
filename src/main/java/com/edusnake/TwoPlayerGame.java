package com.edusnake;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TwoPlayerGame extends Application implements SnakeGame {
    private final double width = SnakeUtils.width;
    private final double height = SnakeUtils.height;
    private final int unitSize = SnakeUtils.unitSize;
    private Snake snake1;
    private Snake snake2;
    private Food[] foods;
    private String letter;
    private boolean isStarted = true;
    private boolean snake1Failed = false;
    private boolean snake2Failed = false;
    private boolean snake1IsTurned = false;
    private boolean snake2IsTurned = false;
    private GraphicsContext gc;
    private GraphicsContext gcBuffer;
    private Timer timer;
    private Text play1FailedText;
    private Text play2FailedText;
    private HBox pauseHbox;
    private HBox hBox;
    private KeyCode keyCode;
    private MusicControl music;


    public TwoPlayerGame() {
        letter = String.valueOf((char) new Random().nextInt(65, 90));
        initSnake();
        music = SnakeUtils.getMusic();
        music.backgroundMusic();
    }

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        Canvas canvasBuffer = new Canvas(width, height);
        gcBuffer = canvasBuffer.getGraphicsContext2D();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw(gcBuffer);
                gc.clearRect(0, 0, width, height);
                action();
                draw(gc);
                gcBuffer.clearRect(0, 0, width, height);
                snake1IsTurned = false;//Restore the snake1IsTurned value
                snake2IsTurned = false;//Restore the snake2IsTurned value
            }
        }, 0, SnakeUtils.getLevel());

        pauseHbox = new HBox();
        Button continueButton = new Button("Continue");
        continueButton.setPrefSize(200, 60);
        continueButton.setStyle("-fx-font-family: 'times new roman';-fx-font-size: 40;");
        Button quitButton = new Button("Quit");
        quitButton.setPrefSize(200, 60);
        quitButton.setStyle("-fx-font-family: 'times new roman';-fx-font-size: 40;");
        pauseHbox.getChildren().addAll(continueButton, quitButton);
        pauseHbox.setSpacing(60);
        pauseHbox.setVisible(false);
        pauseHbox.setAlignment(Pos.CENTER);
        continueButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                music.clickMusic();
                music.playBackground();
                pauseHbox.setVisible(isStarted);
                isStarted = !isStarted;
            }
        });
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    music.clickMusic();
                    music.stopBackground();
                    new Start().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //Showing after game over
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.setLayoutX((width - 850) / 2);
        vBox.setLayoutY(height / 4);
        play1FailedText = new Text("Snake 1 Failed");
        play1FailedText.setFill(Color.RED);
        play1FailedText.setVisible(false);
        play1FailedText.setFont(new Font("times new roman", 60));
        play2FailedText = new Text("Snake 2 Failed");
        play2FailedText.setFill(Color.RED);
        play2FailedText.setVisible(false);
        play2FailedText.setFont(new Font("times new roman", 60));
        hBox = new HBox();
        hBox.setVisible(false);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(60);
        Button restartButton = new Button("Restart");
        Button backButton = new Button("Back");
        restartButton.setPrefSize(200, 60);
        restartButton.setStyle("-fx-font-family: 'times new roman';-fx-font-size: 40;");
        backButton.setPrefSize(200, 60);
        backButton.setStyle("-fx-font-family: 'times new roman';-fx-font-size: 40;");
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    music.clickMusic();
                    music.playBackground();
                    snake1Failed = false;
                    snake2Failed = false;
                    initSnake();
                    keyCode = null;
                    start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    music.clickMusic();
                    music.stopBackground();
                    new Start().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        hBox.getChildren().addAll(restartButton, backButton);
        vBox.getChildren().addAll(play1FailedText, play2FailedText, pauseHbox, hBox);
        root.getChildren().addAll(canvas, canvasBuffer, vBox);
        Scene scene = new Scene(root, width, height);
        String css = this.getClass().getResource("TwoPlayer.css").toExternalForm();
        scene.getStylesheets().add(css);
        scene.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                keyCode = event.getCode();
                //In order to avoid the behavior of the snake turning around in place caused by multiple keystrokes
                if (!snake1IsTurned){//snake1 can only accept one key value at a time of timer
                    if (keyCode == KeyCode.A || keyCode == KeyCode.D || keyCode == KeyCode.W || keyCode == KeyCode.S){
                        keyEvent();//If is keystroke for snake1, execute keyEvent() to change the snake1's direction
                        snake1IsTurned = true;//Set the value of snake1IsTurned to true, so that this if statement will not execute in a timer.
                    }
                }
                if (!snake2IsTurned){//snake2 can only accept one key value at a time of timer
                    if (keyCode == KeyCode.LEFT || keyCode == KeyCode.RIGHT || keyCode == KeyCode.UP || keyCode == KeyCode.DOWN){
                        keyEvent();//If is keystroke for snake2, execute keyEvent() to change the snake2's direction
                        snake2IsTurned = true;//Set the value of snake2IsTurned to true, so that this if statement will not execute in a timer.
                    }
                }
                if (keyCode == KeyCode.SPACE) {
                    music.pauseBackground();
                    pauseHbox.setVisible(isStarted);
                    isStarted = !isStarted;
                }
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.GREY);
        gc.fillRect(0, 0, width, SnakeUtils.topBarHeight);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("times new roman", FontWeight.BOLD, unitSize));
        gc.fillText("Snake 1:  Length: " + snake1.getLength(), 10, unitSize, 180);
        gc.fillText("Score: " + snake1.getScore(), 220, unitSize, 80);
        gc.fillText("Record: " + SnakeUtils.readFile(FilePath.TWOPLAYERFILE.path), width / 2 - 50, unitSize, 100);
        gc.fillText("Snake 2:  Length: " + snake2.getLength(), width - 310, unitSize, 180);
        gc.fillText("Score: " + snake2.getScore(), width - 100, unitSize, 80);

        gc.drawImage(new Image(snake1.getDirection().path), snake1.getSnakeX()[0], snake1.getSnakeY()[0], unitSize, unitSize);
        for (int i = 1; i < snake1.getLength(); i++) {
            gc.drawImage(new Image(new File("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar +
                            "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "snake1body.png").toURI().toString()),
                    snake1.getSnakeX()[i], snake1.getSnakeY()[i], unitSize, unitSize);
        }
        gc.drawImage(new Image(snake2.getDirection().path), snake2.getSnakeX()[0], snake2.getSnakeY()[0], unitSize, unitSize);
        for (int i = 1; i < snake2.getLength(); i++) {
            gc.drawImage(new Image(new File("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar +
                            "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "snake2body.png").toURI().toString()),
                    snake2.getSnakeX()[i], snake2.getSnakeY()[i], unitSize, unitSize);
        }
        for (Food food : foods) {
            gc.drawImage(new Image(new File("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar +
                            "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "food.png").toURI().toString()),
                    food.getFoodX(), food.getFoodY(), unitSize, unitSize);
        }
        if (snake1Failed || snake2Failed) {
            music.stopBackground();
            music.deathMusic();
            if (snake1Failed) {
                play1FailedText.setVisible(true);
            }
            if (snake2Failed) {
                play2FailedText.setVisible(true);
            }
            hBox.setVisible(true);
            timer.cancel();
            if (snake1.getScore() >= snake2.getScore()) {
                if (snake1.getScore() > Integer.parseInt(SnakeUtils.readFile(FilePath.TWOPLAYERFILE.path))) {
                    SnakeUtils.writeFile(FilePath.SINGLEGAMEFILE.path, String.valueOf(snake1.getScore()));
                }
            } else if (snake2.getScore() >= snake1.getScore()) {
                if (snake2.getScore() > Integer.parseInt(SnakeUtils.readFile(FilePath.TWOPLAYERFILE.path))) {
                    SnakeUtils.writeFile(FilePath.SINGLEGAMEFILE.path, String.valueOf(snake2.getScore()));
                }
            }
        }


    }

    public void initSnake() { //Initialize the snake and food
        snake1 = new Snake(unitSize * 4, SnakeUtils.topBarHeight + unitSize * 3, Direction.RIGHT);
        snake2 = new Snake(width - unitSize * 4, SnakeUtils.topBarHeight + unitSize * 4, Direction.LEFT2);
        foods = new Food[15];
        for (int i = 0; i < foods.length; i++) {
            foods[i] = new Food(snake1, snake2, foods);
        }
    }

    public void action() {//Controlling snake movement.By changing the coordinates of the snake
        if (isStarted) {
            SnakeUtils.moveOneStep(snake1);
            switch (snake1.getDirection()) {
                case LEFT -> {
                    snake1.getSnakeX()[0] -= unitSize;
                    if (snake1.getSnakeX()[0] < 0) {
                        snake1Failed = true;
                    }
                    isHitSnake();
                    if (snake1Failed) {
                        SnakeUtils.snakeRollBack(snake1);
                    }
                }
                case RIGHT -> {
                    snake1.getSnakeX()[0] += unitSize;
                    if (snake1.getSnakeX()[0] > width - unitSize) {
                        snake1Failed = true;
                    }
                    isHitSnake();
                    if (snake1Failed) {
                        SnakeUtils.snakeRollBack(snake1);
                    }
                }
                case UP -> {
                    snake1.getSnakeY()[0] -= unitSize;
                    if (snake1.getSnakeY()[0] < SnakeUtils.topBarHeight) {
                        snake1Failed = true;
                    }
                    isHitSnake();
                    if (snake1Failed) {
                        SnakeUtils.snakeRollBack(snake1);
                    }
                }
                case DOWN -> {
                    snake1.getSnakeY()[0] += unitSize;
                    if (snake1.getSnakeY()[0] > height - unitSize) {
                        snake1Failed = true;
                    }
                    isHitSnake();
                    if (snake1Failed) {
                        SnakeUtils.snakeRollBack(snake1);
                    }
                }
            }
            SnakeUtils.moveOneStep(snake2);
            switch (snake2.getDirection()) {
                case LEFT2 -> {
                    snake2.getSnakeX()[0] -= unitSize;
                    if (snake2.getSnakeX()[0] < 0) {
                        snake2Failed = true;
                    }
                    isHitSnake();
                    if (snake2Failed) {
                        SnakeUtils.snakeRollBack(snake2);
                    }
                }
                case RIGHT2 -> {
                    snake2.getSnakeX()[0] += unitSize;
                    if (snake2.getSnakeX()[0] > width - unitSize) {
                        snake2Failed = true;
                    }
                    isHitSnake();
                    if (snake2Failed) {
                        SnakeUtils.snakeRollBack(snake2);
                    }
                }
                case UP2 -> {
                    snake2.getSnakeY()[0] -= unitSize;
                    if (snake2.getSnakeY()[0] < SnakeUtils.topBarHeight) {
                        snake2Failed = true;
                    }
                    isHitSnake();
                    if (snake2Failed) {
                        SnakeUtils.snakeRollBack(snake2);
                    }
                }
                case DOWN2 -> {
                    snake2.getSnakeY()[0] += unitSize;
                    if (snake2.getSnakeY()[0] > height - unitSize) {
                        snake2Failed = true;
                    }
                    isHitSnake();
                    if (snake2Failed) {
                        SnakeUtils.snakeRollBack(snake2);
                    }
                }
            }
            if (!snake1Failed && !snake2Failed) {
                //Determine if the snake has eaten food
                for (int i = 0; i < foods.length; i++) {
                    if (snake1.getSnakeX()[0] == foods[i].getFoodX() && snake1.getSnakeY()[0] == foods[i].getFoodY()) {
                        music.coinMusic();
                        //snake1.getLetters()[snake1.getLength()] = foods[i].getLetter();
                        snake1.setLength(snake1.getLength() + 1);
                        snake1.setScore(snake1.getScore() + 10);
                        foods[i] = new Food(snake1, snake2, foods);
                    }
                    if (snake2.getSnakeX()[0] == foods[i].getFoodX() && snake2.getSnakeY()[0] == foods[i].getFoodY()) {
                        music.coinMusic();
                        //snake2.getLetters()[snake2.getLength()] = foods[i].getLetter();
                        snake2.setLength(snake2.getLength() + 1);
                        snake2.setScore(snake2.getScore() + 10);
                        foods[i] = new Food(snake1, snake2, foods);
                    }
                }

            }

            if (snake1.getScore() >= snake2.getScore()) {
                if (snake1.getScore() > Integer.parseInt(SnakeUtils.readFile(FilePath.TWOPLAYERFILE.path))) {
                    SnakeUtils.writeFile(FilePath.TWOPLAYERFILE.path, String.valueOf(snake1.getScore()));
                }
            } else if (snake2.getScore() >= snake1.getScore()) {
                if (snake2.getScore() > Integer.parseInt(SnakeUtils.readFile(FilePath.TWOPLAYERFILE.path))) {
                    SnakeUtils.writeFile(FilePath.TWOPLAYERFILE.path, String.valueOf(snake2.getScore()));
                }
            }
        }
    }

    public void isHitSnake() {
        for (int i = 1; i < snake1.getLength(); i++) {
            if (snake1.getSnakeX()[0] == snake1.getSnakeX()[i] && snake1.getSnakeY()[0] == snake1.getSnakeY()[i]) {
                snake1Failed = true;
                break;
            }
            if (snake2.getSnakeX()[0] == snake1.getSnakeX()[i] && snake2.getSnakeY()[0] == snake1.getSnakeY()[i]) {
                snake2Failed = true;
                break;
            }
        }
        for (int i = 1; i < snake2.getLength(); i++) {
            if (snake2.getSnakeX()[0] == snake2.getSnakeX()[i] && snake2.getSnakeY()[0] == snake2.getSnakeY()[i]) {
                snake2Failed = true;
                break;
            }
            if (snake1.getSnakeX()[0] == snake2.getSnakeX()[i] && snake1.getSnakeY()[0] == snake2.getSnakeY()[i]) {
                snake1Failed = true;
                break;
            }
        }
    }

    public void keyEvent() {
        switch (keyCode) {
            case A -> {
                if (snake1.getDirection() != Direction.RIGHT) {
                    snake1.setDirection(Direction.LEFT);
                }
            }
            case D -> {
                if (snake1.getDirection() != Direction.LEFT) {
                    snake1.setDirection(Direction.RIGHT);
                }
            }
            case W -> {
                if (snake1.getDirection() != Direction.DOWN) {
                    snake1.setDirection(Direction.UP);
                }
            }
            case S -> {
                if (snake1.getDirection() != Direction.UP) {
                    snake1.setDirection(Direction.DOWN);
                }
            }
            case LEFT -> {
                if (snake2.getDirection() != Direction.RIGHT2) {
                    snake2.setDirection(Direction.LEFT2);
                }
            }
            case RIGHT -> {
                if (snake2.getDirection() != Direction.LEFT2) {
                    snake2.setDirection(Direction.RIGHT2);
                }
            }
            case UP -> {
                if (snake2.getDirection() != Direction.DOWN2) {
                    snake2.setDirection(Direction.UP2);
                }
            }
            case DOWN -> {
                if (snake2.getDirection() != Direction.UP2) {
                    snake2.setDirection(Direction.DOWN2);
                }
            }
        }
    }

}
