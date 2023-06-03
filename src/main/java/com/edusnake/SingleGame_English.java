package com.edusnake;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SingleGame_English extends Application implements SnakeGame{
    /*
     * SingleGame class implements complete singleGame function.These functions include:
     * 1.Draw the snake, two foods and data that needs to be displayed like length, score and record.
     * 2.Accept different key data and update the snake data and repaint it to make the snake move.
     * 3.When the X and Y position of the snake's head and the right food overlap,
     *   add the letters of the food to the snake body.If you eat the wrong food, mark it once.
     *   The letters of the food are derived from the words read from the wordsFile.
     *   The data of record, level and music all are derived from the files, we also need to update it if it should be changed.
     * 4.When the position of snake head out of frame or position of snake head and snake body is overlap, the game will fail.
     *   And display Text and buttons after the game fails.
     * 5.When user clicks the space key, the game is paused. And display the buttons after the game pauses.
     * 6.Set what every button should do.
     * 7.To add music to the game, we set background music,
     *   musics for eating the right and wrong food, and music for the game failed.
     * */
    private final double width = SnakeUtils.width;// The width of the game frame
    private final double height = SnakeUtils.height;// The height of the game frame
    private final int unitSize = SnakeUtils.unitSize;// The size of the every unit that The distance the snake moves each time,
    private Snake snake;// The snake object contains information of the snake, such as position, direction, length, scores and letters
    private Food[] foods;// Array contain multiple foods, every food contains information of food, such as position and letter.
    private int letterIndex;// The index of word array. Each index corresponds to one letter
    private final String[] wordArray;// The array contain words
    private String word;// The word that read from wordFile
    private String letter;// The letter that can be food's letter or snake body's letter
    private int errorAmount;// The number of eating error food
    private boolean isStarted = true;// Determine if the game has started
    private boolean isFailed = false;// Determine if the game has failed
    private GraphicsContext gc;// The GraphicsContext as a paintbrush to draw
    private GraphicsContext gcBuffer;// The another GraphicsContext as a paintbrush to draw
    private Timer timer;// Control and update the time
    private HBox pauseHbox;// HBox lays out its children in a single horizontal row. Contains continueButton and quiteButton
    private Text gameOverText;// "Game Over" text
    private HBox hBox;// HBox lays out its children in a single horizontal row. Contains restartButton and backButton
    private KeyCode keyCode;// keyboard data that is received
    private final MusicControl musicControl;// To control music

    public SingleGame_English() {
        wordArray = SnakeUtils.dictionary;// Read the word
        initSnake();
        musicControl = SnakeUtils.getMusic();// Get the music by SnakeUtils method
        musicControl.backgroundMusic(); //Play background music when the game start
    }

    public void start(Stage stage) {

        Pane root = new Pane();
        Canvas canvas = new Canvas(width, height);//Create canvas to draw on canvas
        gc = canvas.getGraphicsContext2D();//Get graphics from canvas
        Canvas canvasBuffer = new Canvas(width, height);//Create another canvas to solve the image flicker problem.
        gcBuffer = canvasBuffer.getGraphicsContext2D();//Get second graphics from canvas

        timer = new Timer();//The timer can update the game in a while
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//Using two graphics to fix flicker issue.
                draw(gcBuffer);//The second graphics update the game screen
                gc.clearRect(0, 0, width, height);//The first graphics clear the page
                if (keyCode != null) {
                    keyEvent(); //Member method, Controls what each key does
                }
                action(); //Update game data
                draw(gc); //The first graphics update the game screen
                gcBuffer.clearRect(0, 0, width, height);//The second graphics clear the page
            }
        }, 0, SnakeUtils.getLevel()); //SnakeUtils.gameLevel() Control different game speeds with different levels

        //The pause section consists of two parts, continueButton and quitButtion,
        // click the space keyboard to pause and display the two buttons, then make a selection.
        pauseHbox = new HBox();//Containers that arrange components horizontally
        Button continueButton = new Button("Continue");//Create a button
        continueButton.setPrefSize(200, 60);//Set the button size
        continueButton.setStyle("-fx-font-family: 'times new roman';-fx-font-size: 40;");//Set the font style
        Button quitButton = new Button("Quit");
        quitButton.setPrefSize(200, 60);
        quitButton.setStyle("-fx-font-family: 'times new roman';-fx-font-size: 40;");
        pauseHbox.getChildren().addAll(continueButton, quitButton);// Add continueButton and quitButton to pauseHbox
        pauseHbox.setSpacing(60); //Set the spacing of buttons within the pauseHbox
        pauseHbox.setVisible(false); //Set pauseHbox are not visible, because it only appears when paused
        pauseHbox.setAlignment(Pos.CENTER); //Set Centered
        continueButton.setOnAction(new EventHandler<ActionEvent>() { //The event that is executed by clicking the button
            @Override
            public void handle(ActionEvent actionEvent) { // isStarted = false when paused, hide the button and continue the game
                musicControl.clickMusic();//Play click button sound
                musicControl.playBackground();//Continue background music
                pauseHbox.setVisible(isStarted);
                isStarted = !isStarted;
            }
        });
        quitButton.setOnAction(new EventHandler<ActionEvent>() { //The event that is executed by clicking the button
            @Override
            public void handle(ActionEvent actionEvent) { // return to Start class (main interface)
                try {
                    musicControl.clickMusic();//Play click button sound
                    musicControl.stopBackground();//Stop background music
                    new Start().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //Showing after game over. This part contains two buttons, restartButton and backButton
        VBox vBox = new VBox(); //Containers that arrange components vertically
        vBox.setAlignment(Pos.CENTER); // These settings are similar to those above
        vBox.setSpacing(10);

        vBox.setLayoutX((width - 850) / 2);
        vBox.setLayoutY(height / 4);
        gameOverText = new Text("Game Over");
        gameOverText.setVisible(false);
        gameOverText.setFont(new Font("times new roman", 60));
        gameOverText.setFill(Color.RED);
        hBox = new HBox();
        hBox.setVisible(false);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(60);
        Button restartButton = new Button("Restart");
        Button backButton = new Button("Back");
        restartButton.setPrefSize(200, 60);
        backButton.setPrefSize(200, 60);
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {// Restart the game, Restore some settings
                try {
                    musicControl.clickMusic();//Play click button sound
                    musicControl.playBackground();//Play background music
                    isFailed = false;
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
                    musicControl.clickMusic();//Play click button sound
                    musicControl.stopBackground();//Stop background music
                    new Start().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        hBox.getChildren().addAll(restartButton, backButton); // Put buttons to container
        vBox.getChildren().addAll(gameOverText, pauseHbox, hBox); // Put text and containers to larger containers
        root.getChildren().addAll(canvas, canvasBuffer, vBox);  // Put containers to larger containers
        Scene scene = new Scene(root, width, height);// Here are multiple layers of containers, which were eventually added to scene
        String css = this.getClass().getResource("SinglePlayer.css").toExternalForm();
        scene.getStylesheets().add(css);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() { //Monitor keyboards
            @Override
            public void handle(KeyEvent event) {
                keyCode = event.getCode(); //Get key press information
                if (keyCode == KeyCode.SPACE) { //If the key is a space, pause game and display the pause message
                    musicControl.pauseBackground();
                    pauseHbox.setVisible(isStarted);
                    isStarted = !isStarted;
                }
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public void draw(GraphicsContext gc) { // Draw all the game screens
        gc.setFill(Color.GREY); // Set the color
        gc.fillRect(0, 0, width, SnakeUtils.topBarHeight); // Set the topBar, location and size
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("times new roman", FontWeight.BOLD, unitSize + 5)); // Set the font style
        gc.fillText("[" + word + "]", width / 2 - 50, unitSize, 200); // The word that appears in the center of the topBar
        gc.fillText("Length: " + snake.getLength(), 10, unitSize, 90); //Snake length
        gc.fillText("Score: " + snake.getScore(), 120, unitSize, 90); // Snake score
        gc.fillText("Error: " + errorAmount, width - 210, unitSize, 80); //Current number of errors
        gc.fillText("Record: " + SnakeUtils.readFile(FilePath.SINGLEGAMEFILE.path), width - 110, unitSize, 100); //All-time high
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("times new roman", FontWeight.BOLD, unitSize));
        //Draw the snake head
        gc.drawImage(new Image(snake.getDirection().path), snake.getSnakeX()[0], snake.getSnakeY()[0], unitSize, unitSize);
        for (int i = 1; i < snake.getLength(); i++) { //Drsaw the snake body by for loop
            gc.drawImage(new Image(new File("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar +
                            "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "body" + snake.getLetters()[i].toUpperCase() + ".png").toURI().toString()),
                    snake.getSnakeX()[i], snake.getSnakeY()[i], unitSize, unitSize);
        }
        gc.setFill(Color.ORANGE);
        gc.setFont(Font.font("times new roman", FontWeight.BOLD, 40));
        // Set the food location, The numbers added at the end are to adjust the position of the letters, which is to look more reasonable
        for (Food food : foods) {
            gc.drawImage(new Image(new File("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar +
                            "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "food" + food.getLetter().toUpperCase() + ".png").toURI().toString()),
                    food.getFoodX(), food.getFoodY(), unitSize, unitSize);
        }
        //gc.fillText(letter, food.getFoodX() + 3, food.getFoodY() + 22);
        if (isFailed) { // When game failed
            musicControl.stopBackground();//Stop background music when the game failed
            musicControl.deathMusic();// Play death music
            gameOverText.setVisible(true); // Show the gameOverText
            hBox.setVisible(true); // Show the buttons
            timer.cancel(); // Stop timer update
        }


    }

    public void initSnake() { //Initialize the snake and food
        word = wordArray[new Random().nextInt(wordArray.length - 1)]; //Select a random selection of words read from the file
        letterIndex = 0;
        letter = String.valueOf(word.toCharArray()[letterIndex]); //Choose the first letter of the word, at this time letterIndex = 0
        snake = new Snake(unitSize * 4, SnakeUtils.topBarHeight + unitSize * 3, Direction.RIGHT); //Create a new snake and set location and direction
        //Create a new foods
        initFood();
    }

    public void initFood() {
        foods = new Food[2];
        for (int i = 0; i < foods.length; i++) {
            foods[i] = new Food(snake, foods);
            if (i == 0) {
                foods[i].setLetter(letter);
            } else {
                foods[i].setLetter(String.valueOf((char) new Random().nextInt(97, 123)));
                while (foods[i].getLetter().equals(foods[0].getLetter())) {
                    foods[i].setLetter(String.valueOf((char) new Random().nextInt(97, 123)));
                }
            }
        }
    }

    public void action() {//Controlling snake movement.By changing the coordinates of the snake
        if (isStarted && !isFailed) {
            SnakeUtils.moveOneStep(snake);//Invoke the method to move the data for each index in the snake array forward
            switch (snake.getDirection()) {//Data changes in the snake array in different directions
                case LEFT -> {
                    snake.getSnakeX()[0] -= unitSize;//When the direction is to the left, the X coordinate is subtracted by 25 each time
                    if (snake.getSnakeX()[0] < 0) {// Less than 0 means that the snake hit the wall
                        isFailed = true;
                    }
                    isHitSelf();//Call the method to check if the snake has hit itself
                    if (isFailed) {
                        SnakeUtils.snakeRollBack(snake);//After the game fails, invoke the method to take the snake back a step, otherwise the snake will go out of bounds
                    }
                }
                case RIGHT -> { // The logic is the same as above, for four different directions
                    snake.getSnakeX()[0] += unitSize;
                    if (snake.getSnakeX()[0] > width - unitSize) {
                        isFailed = true;
                    }
                    isHitSelf();
                    if (isFailed) {
                        SnakeUtils.snakeRollBack(snake);
                    }
                }
                case UP -> {
                    snake.getSnakeY()[0] -= unitSize;
                    if (snake.getSnakeY()[0] < SnakeUtils.topBarHeight) {
                        isFailed = true;
                    }
                    isHitSelf();
                    if (isFailed) {
                        SnakeUtils.snakeRollBack(snake);
                    }
                }
                case DOWN -> {
                    snake.getSnakeY()[0] += unitSize;
                    if (snake.getSnakeY()[0] > height - unitSize) {
                        isFailed = true;
                    }
                    isHitSelf();
                    if (isFailed) {
                        SnakeUtils.snakeRollBack(snake);
                    }
                }
            }
            if (!isFailed) {
                //Determine if the snake has eaten food, When the position of the snake's head and the position of the food overlap
                for (Food food : foods) {
                    if (snake.getSnakeX()[0] == food.getFoodX() && snake.getSnakeY()[0] == food.getFoodY()) {
                        if (food.getLetter().equals(foods[0].getLetter())) {
                            musicControl.coinMusic();
                            snake.getLetters()[snake.getLength()] = letter; //Store the letters in snake letters array
                            snake.setLength(snake.getLength() + 1); // snake's length + 1
                            snake.setScore(snake.getScore() + 10); //Score + 10
                            letterIndex++; //Letter's index + 1
                        } else {
                            musicControl.wrongMusic();
                            errorAmount++;
                            snake.setScore(snake.getScore() - 10);
                        }
                        if (letterIndex > word.toCharArray().length - 1) { //If the letterIndex is greater than the word length -1, Reset word and letterIndex
                            letterIndex = 0;
                            word = wordArray[new Random().nextInt(wordArray.length - 1)];
                        }
                        letter = String.valueOf(word.toCharArray()[letterIndex]);
                        initFood();

                    }
                }

            }
            // If snake score greater than the record, then write the new record to the singleGameFile
            if (snake.getScore() > Integer.parseInt(SnakeUtils.readFile(FilePath.SINGLEGAMEFILE.path))) {
                SnakeUtils.writeFile(FilePath.SINGLEGAMEFILE.path, String.valueOf(snake.getScore()));
            }
        }
    }

    public void isHitSelf() { // Determine if the snake has hit itself
        for (int i = 1; i < snake.getLength(); i++) {
            if (snake.getSnakeX()[0] == snake.getSnakeX()[i] && snake.getSnakeY()[0] == snake.getSnakeY()[i]) {
                isFailed = true;
                break;
            }
        }
    }

    public void keyEvent() { //Controls what each key does
        switch (keyCode) {
            case LEFT, A -> {
                if (snake.getDirection() != Direction.RIGHT) { // If the direction is right, can't change to left directly
                    snake.setDirection(Direction.LEFT);// Set snake direction to left when the keyCode is LEFT or A
                }
            }
            case RIGHT, D -> { //The logic is the same as above
                if (snake.getDirection() != Direction.LEFT) {
                    snake.setDirection(Direction.RIGHT);
                }
            }
            case UP, W -> {
                if (snake.getDirection() != Direction.DOWN) {
                    snake.setDirection(Direction.UP);
                }
            }
            case DOWN, S -> {
                if (snake.getDirection() != Direction.UP) {
                    snake.setDirection(Direction.DOWN);
                }
            }
        }
    }


}
