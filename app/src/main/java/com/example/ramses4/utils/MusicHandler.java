package com.example.ramses4.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.ramses4.R;

public class MusicHandler {

    private boolean isSilentMode;
    private final MediaPlayer musicPlayer;

    public MusicHandler(Context context) {
        musicPlayer = MediaPlayer.create(context, R.raw.musique);
    }

    public boolean isSilentMode() {
        return isSilentMode;
    }

    public void startMusic() {
        musicPlayer.start();
    }


    public void stopMusic() {
        musicPlayer.stop();
    }

    public void mute() {
        this.musicPlayer.setVolume(0, 0);
        this.isSilentMode = true;
    }

    public void unmute(){
        this.musicPlayer.setVolume(1, 1);
        this.isSilentMode = false;
    }

}
