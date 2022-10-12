package com.example.ramses4.game;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Settings {

    private static final String HAS_ARROW_CONTROLS_KEY = "HAS_ARROW_CONTROLS_KEY";
    private static final String PLAYERS_NAME_KEY = "PLAYERS_NAME_KEY";
    private static final String DIFFICULTY_KEY = "DIFFICULTY_KEY";

    private boolean hasArrowControls;
    private ArrayList<Player> players;
    private int difficulty;

    public Settings() {
        players = new ArrayList<>();
        difficulty = 1;
        hasArrowControls = false;
    }

    public Settings(Intent intent) {
        hasArrowControls = intent.getBooleanExtra(HAS_ARROW_CONTROLS_KEY, false);
        difficulty = intent.getIntExtra(DIFFICULTY_KEY, 0);

        if (intent.getStringArrayListExtra(PLAYERS_NAME_KEY) == null)
            players = new ArrayList<>();
        else
            players = intent.getStringArrayListExtra(PLAYERS_NAME_KEY).stream().map(n -> new Player(n, 0)).collect(Collectors.toCollection(ArrayList::new));
    }

    public Settings(Bundle bundle) {
        hasArrowControls = bundle.getBoolean(HAS_ARROW_CONTROLS_KEY, false);
        difficulty = bundle.getInt(DIFFICULTY_KEY, 0);

        if (bundle.getCharSequenceArrayList(PLAYERS_NAME_KEY) == null)
            players = new ArrayList<>();
        else
            players = bundle.getCharSequenceArrayList(PLAYERS_NAME_KEY).stream().map(n -> new Player((String) n, 0)).collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean hasArrowControls() {
        return hasArrowControls;
    }

    public void setHasArrowControls(boolean hasArrowControls) {
        this.hasArrowControls = hasArrowControls;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Intent addToIntent(Intent intent) {
        intent.putExtra(HAS_ARROW_CONTROLS_KEY, hasArrowControls);
        intent.putExtra(DIFFICULTY_KEY, difficulty);
        intent.putStringArrayListExtra(PLAYERS_NAME_KEY, players.stream().map(p -> p.getName()).collect(Collectors.toCollection(ArrayList::new)));
        return intent;
    }

    public Bundle addToBundle(Bundle bundle) {
        bundle.putBoolean(HAS_ARROW_CONTROLS_KEY, hasArrowControls);
        bundle.putInt(DIFFICULTY_KEY, difficulty);
        bundle.putCharSequenceArrayList(PLAYERS_NAME_KEY, players.stream().map(p -> p.getName()).collect(Collectors.toCollection(ArrayList::new)));
        return bundle;
    }


}
