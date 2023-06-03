package com.edusnake;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MusicControl {

    private Media backgroundMedia;
    private Media eatMedia;
    private Media deathMedia;
    private Media clickMedia;
    private boolean isMute;
    private MediaPlayer mainPlayer;

    public MusicControl(){
        if (SnakeUtils.readFile(FilePath.MUSICFILE.path).equals("Mute")){
            isMute = true;
        }
    }

    public MusicControl(String mainMusic, String coinMusic, String deathMusic){
        this.backgroundMedia = new Media(mainMusic);
        this.eatMedia = new Media(coinMusic);
        this.deathMedia = new Media(deathMusic);
    }

    public void backgroundMusic(){
        if (!isMute){
            mainPlayer = new MediaPlayer(backgroundMedia);
            mainPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mainPlayer.play();
        }
    }

    public void coinMusic(){
        if (!isMute){
            MediaPlayer mediaPlayer = new MediaPlayer(eatMedia);
            mediaPlayer.play();
        }
    }

    public void wrongMusic(){
        if (!isMute){
            Media wrongMedia = new Media(new File(FilePath.WRONGLETTER.path).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(wrongMedia);
            mediaPlayer.play();
        }
    }

    public void deathMusic(){
        if (!isMute){
            MediaPlayer mediaPlayer = new MediaPlayer(deathMedia);
            mediaPlayer.play();
        }
    }

    public void clickMusic(){
        if (!isMute){
            clickMedia = new Media(new File(FilePath.CLICKBUTTON.path).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(clickMedia);
            mediaPlayer.play();
        }
    }

    public void playBackground(){
        if (!isMute){
            mainPlayer.play();
        }

    }

    public void pauseBackground(){
        if (!isMute){
            mainPlayer.pause();
        }

    }

    public void stopBackground(){
        if (!isMute){
            mainPlayer.stop();
        }

    }

}
