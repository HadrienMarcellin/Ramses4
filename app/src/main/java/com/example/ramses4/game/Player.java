package com.example.ramses4.game;

public class Player {

    private final String name;
    private Integer score;

    public Player(String name, Integer score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Integer getScore() {
        return score;
    }

    public String getScoreAsString() {
        return "Score: " + score;
    }

    public void addPointsToScore(Integer points) {
        score = score + points;
    }

    public void removePointsFromScore(Integer points) {
        score = score - points;
    }
}
