package com.example.ramses4.game;

public class Target {
    private final Integer treasureId;
    private final Integer score;
    private final Integer drawableId;

    public Target(Integer treasureId, Integer score, Integer drawableId) {
        this.treasureId = treasureId;
        this.score = score;
        this.drawableId = drawableId;
    }

    public Integer getTreasureId() {
        return treasureId;
    }

    public Integer getScore() {
        return score;
    }
    public String getScoreAsString() {
        if (score <= 1)
            return score + " point";
        else
            return score + " points";

    }
    public Integer getDrawableId() {
        return drawableId;
    }
}
