package com.edusnake;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Setting extends Application {

    private String levelItem;
    private String musicItem;
    private String subjectItem;

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox(); //Create VBox for components arranged vertically
        root.setBackground(new Background( //Set color
                new BackgroundFill(
                        new Color(0.2, 0.5, 0.8, 1
                        ), CornerRadii.EMPTY, Insets.EMPTY)));
        root.setAlignment(Pos.CENTER); // Set Centered
        root.setSpacing(80);// Set the spacing of component within the root

        Text titleText = new Text("SETTING");
        titleText.setFont(new Font("times new roman", 80));
        titleText.setFill(Color.WHITE);

        HBox subjectHBox = new HBox();
        subjectHBox.setAlignment(Pos.CENTER);
        subjectHBox.setSpacing(30);
        Text subjectText = new Text("SUBJECT:");
        subjectText.setFont(new Font("times new roman", 50));
        subjectText.setFill(Color.WHITE);
        ComboBox<String> subjectCom = new ComboBox<>(); //Create a level drop-down box
        subjectCom.setPrefSize(300, 50);
        subjectCom.setStyle("-fx-font-family: 'times new roman';-fx-font-size: 20;");
        subjectCom.getItems().addAll("English", "Mathematics");//Add different subject to ComboBox
        subjectCom.getSelectionModel().select(SnakeUtils.readFile(FilePath.SUBJECTFILE.path));
        subjectCom.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                new MusicControl().clickMusic();
                subjectItem = subjectCom.getSelectionModel().getSelectedItem();
            }
        });
        subjectHBox.getChildren().addAll(subjectText, subjectCom);

        HBox levelHBox = new HBox();
        levelHBox.setAlignment(Pos.CENTER);
        levelHBox.setSpacing(30);
        Text levelText = new Text("  LEVEL  :");
        levelText.setFont(new Font("times new roman", 50));
        levelText.setFill(Color.WHITE);
        ComboBox<String> levelCom = new ComboBox<>(); //Create a level drop-down box
        levelCom.setPrefSize(300, 50);
        levelCom.setStyle("-fx-font-family: 'times new roman';-fx-font-size: 20;");
        levelCom.getItems().addAll("Easy", "Normal", "Hard", "Master", "Extreme");//Add different level to ComboBox
        levelCom.getSelectionModel().select(SnakeUtils.readFile(FilePath.LEVELFILE.path)); //Read information from a file as default in the drop-down box
        levelCom.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                new MusicControl().clickMusic();
                levelItem = levelCom.getSelectionModel().getSelectedItem();
            }
        });
        levelHBox.getChildren().addAll(levelText, levelCom);

        HBox musicHBox = new HBox();
        musicHBox.setAlignment(Pos.CENTER);
        musicHBox.setSpacing(30);
        Text musicText = new Text("  MUSIC  :");
        musicText.setFont(new Font("times new roman", 50));
        musicText.setFill(Color.WHITE);
        ComboBox<String> musicCom = new ComboBox<>();//Create a music drop-down box
        musicCom.setPrefSize(300, 50);
        musicCom.setStyle("-fx-font-family: 'times new roman';-fx-font-size: 20;");
        musicCom.getItems().addAll("SuperMario", "GoKarts", "LightAndLively", "Mute");
        musicCom.getSelectionModel().select(SnakeUtils.readFile(FilePath.MUSICFILE.path));
        musicCom.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                new MusicControl().clickMusic();
                musicItem = musicCom.getSelectionModel().getSelectedItem();//Gets the content which item is selected in the drop-down box
            }
        });
        musicHBox.getChildren().addAll(musicText, musicCom);

        HBox buttonHBox = new HBox();
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.setSpacing(100);
        Button okButton = new Button("OK");
        okButton.setPrefSize(200, 60);
        okButton.setStyle("-fx-font-family: 'times new roman';-fx-font-size: 40;");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {//Click the OK button to store the information that select to the file
                new MusicControl().clickMusic();
                if (subjectItem != null){
                    SnakeUtils.writeFile(FilePath.SUBJECTFILE.path, subjectItem);
                }
                if (levelItem != null) {
                    SnakeUtils.writeFile(FilePath.LEVELFILE.path, levelItem);
                }
                if (musicItem != null) {
                    SnakeUtils.writeFile(FilePath.MUSICFILE.path, musicItem);
                }
                try {
                    new Start().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Button backButton = new Button("Back");
        backButton.setPrefSize(200, 60);
        backButton.setStyle("-fx-font-family: 'times new roman';-fx-font-size: 40;");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {// Click the back button, return directly, don't store the information
                try {
                    new MusicControl().clickMusic();
                    new Start().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buttonHBox.getChildren().addAll(okButton, backButton);

        root.getChildren().addAll(titleText, subjectHBox, levelHBox, musicHBox, buttonHBox);
        Scene scene = new Scene(root, SnakeUtils.width, SnakeUtils.height);
        String css = this.getClass().getResource("Setting.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);

    }
}
