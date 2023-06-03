package com.edusnake;

import java.io.FileInputStream;

import javafx.application.Application;
//import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Start extends Application {// Application is the entrance of javaFX, so we need to extend the Application and rewrite the start method.
    /*
     * In the Start class, we implement four button functions.
     * They are SinglePlayer, TwoPlayers, Setting and exit.
     * Click the different buttons to go to different pages, click exit to exit the game
     * */
    public static void main(String[] args) {
        launch();
    }// The entrance of programmer, invoke the launch() to execute start(Stage stage)

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("EduSnake");// Set title for frame
        VBox vbox = new VBox();//Create VBox for components arranged vertically
        vbox.setAlignment(Pos.CENTER);// Set centered
        final ImageView selectedImage = new ImageView();// Create a ImageView
        Image image2 = new Image(new FileInputStream(FilePath.TITLE.path));// Create a Image
        selectedImage.setImage(image2);// Set Image to ImageView to Show the title image
        Button sinButton = new Button("Single Player");// Create Single Player buttons
        Button twoButton = new Button("Two Players");// Create Two Players buttons
        Button settingButton = new Button("Settings");// Create Settings buttons
        Button exitButton = new Button("Exit");// Create Exit buttons
        sinButton.setOnAction(new EventHandler<ActionEvent>() { //The event that is executed by clicking the button
            @Override
            public void handle(ActionEvent event) {
                try {
                    new MusicControl().clickMusic();//Play click button sound
                    String subject = SnakeUtils.readFile(FilePath.SUBJECTFILE.path);
                    if (subject.equals("English")){
                        new SingleGame_English().start(stage); // Execute the SingleGame
                    } else if (subject.equals("Mathematics")) {
                        new SingleGame_Math().start(stage);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        twoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new MusicControl().clickMusic();//Play click button sound
                    new TwoPlayerGame().start(stage); // Execute the TwoPlayerGame
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        settingButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new MusicControl().clickMusic();//Play click button sound
                    new Setting().start(stage); // Execute the Setting
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new MusicControl().clickMusic();
                System.exit(0);
            } // Close the game
        });
        vbox.getChildren().addAll(selectedImage, sinButton, twoButton, settingButton, exitButton); // Put image and buttons into vbox container
        Scene scene = new Scene(vbox, SnakeUtils.width, SnakeUtils.height); // Create and set the scene
        String css = this.getClass().getResource("MainMenu.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene); //Set the scene to the stage
        stage.show();
    }
}
