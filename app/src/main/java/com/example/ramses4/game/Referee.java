package com.example.ramses4.game;

import com.example.ramses4.R;
import com.example.ramses4.shapes.Tile;
import com.example.ramses4.utils.VibratorHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Referee {

    private ArrayList<Target> targets;
    private Target currentTarget;

    private ArrayList<Tile> treasures;

    private final ArrayList<Player> players;
    private Player currentPlayer;

    private Integer maxScore;

    public Referee(Board board,
                   ArrayList<Player> players,
                   ArrayList<Target> targets,
                   ArrayList<Tile> treasures,
                   Integer maxScore) {
        this.players = players;
        this.treasures = treasures;
        this.targets = targets;
        this.maxScore = maxScore;
        initGame();
    }

    public void checkPlayerMove(Tile newTile, VibratorHandler vibrator) {
        if(isCurrentTarget(newTile)) {
            currentPlayer.addPointsToScore(currentTarget.getScore());
            incrementTarget();
            vibrator.vibrateForSuccess();
            return;
        }
        if (isTreasure(newTile)) {
            vibrator.vibrateForFailure();
            incrementPlayer();
        }
    }

    public Player getWinner() {
        return players.stream().filter(p -> p.getScore() > maxScore).findFirst().orElse(null);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<Tile> getTreasures() {
        return treasures;
    }

    public Target getCurrentTarget() {
        return currentTarget;
    }

    private boolean isCurrentTarget(Tile tile) {
        return tile.getId().equals(currentTarget.getTreasureId());
    }

    private boolean isTreasure(Tile tile) {
        return treasures.stream().anyMatch(t -> t.getId().equals(tile.getId()));
    }

    private void incrementTarget() {
        int currentTargetIndex = targets.indexOf(currentTarget);
        int nextTargetIndex = (!(currentTargetIndex == targets.size()-1)) ? currentTargetIndex + 1 : 0;
        currentTarget = targets.get(nextTargetIndex);
    }

    private void incrementPlayer() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int nextPlayerIndex = (!(currentPlayerIndex == players.size()-1)) ? currentPlayerIndex + 1 : 0;
        currentPlayer = players.get(nextPlayerIndex);
    }

    private void initGame() {
        this.currentPlayer = players.get(0);
        this.currentTarget = targets.get(0);
    }
}
