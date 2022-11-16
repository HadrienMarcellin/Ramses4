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

    public Referee(ArrayList<Player> players, Board board) {
        createTreasures(board);
        createTargets();
        this.players = players;
        currentTarget = targets.get(0);
        currentPlayer = players.get(0);
        incrementTarget();
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

    public boolean isCurrentTarget(Tile tile) {
        return tile.getId().equals(currentTarget.getTreasureId());
    }

    public boolean isTreasure(Tile tile) {
        return treasures.stream().anyMatch(t -> t.getId().equals(tile.getId()));
    }

    public void incrementTarget() {
        int currentTargetIndex = targets.indexOf(currentTarget);
        int nextTargetIndex = (!(currentTargetIndex == targets.size()-1)) ? currentTargetIndex + 1 : 0;
        currentTarget = targets.get(nextTargetIndex);
    }

    public void incrementPlayer() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int nextPlayerIndex = (!(currentPlayerIndex == players.size()-1)) ? currentPlayerIndex + 1 : 0;
        currentPlayer = players.get(nextPlayerIndex);
    }

    public void checkTile(Tile tile, VibratorHandler vibrator) {
        if(isCurrentTarget(tile)) {
            currentPlayer.addPointsToScore(currentTarget.getScore());
            incrementTarget();
            vibrator.vibrateForSuccess();
            return;
        }
        if (isTreasure(tile)) {
            vibrator.vibrateForFailure();
            incrementPlayer();
        }
    }

    public void createTreasures(Board board) {
        treasures = new ArrayList<>();
        ArrayList<Integer> treasureIds = listTreasureIds(board.getNbTiles());
        int id = 1;
        for (int col = 0; col < board.getNbCols() - 1; col = col + 2) {
            for (int row = 0; row < board.getNbRows() - 1; row = row + 2) {
                treasures.add(new Tile(treasureIds.get(id),
                        R.color.ramses_deep_green,
                        id++));
            }
        }
        Collections.shuffle(treasures);
    }

    public void createTargets() {
        targets = new ArrayList<>();
        for (Tile treasure: treasures) {
            targets.add(new Target(treasure.getId(), 1, treasure.getDrawableId()));
            targets.add(new Target(treasure.getId(), 2, treasure.getDrawableId()));
            targets.add(new Target(treasure.getId(), 4, treasure.getDrawableId()));
        }
        Collections.shuffle(targets);
    }

    private Integer getNextPayer(Integer currentPlayerId) {
        return (!currentPlayerId.equals(players.size())) ? currentPlayerId + 1 : 0;
    }

    private Integer getNextTarget(Integer currentTargetId) {
        return (!currentTargetId.equals(targets.size())) ? currentTargetId + 1 : 0;
    }

    private static ArrayList<Integer> listTreasureIds(int size) {
        ArrayList<Integer> treasureIds = new ArrayList<>();
        treasureIds.add(R.mipmap.bague);
        treasureIds.add(R.mipmap.casquette);
        treasureIds.add(R.mipmap.confiture);
        treasureIds.add(R.mipmap.diamant);
        treasureIds.add(R.mipmap.souris);
        treasureIds.add(R.mipmap.oeil);
        treasureIds.add(R.mipmap.toutankamon);
        treasureIds.add(R.mipmap.toile);
        treasureIds.add(R.mipmap.stele);
        treasureIds.add(R.mipmap.dentier);
        treasureIds.add(R.mipmap.cles);
        treasureIds.add(R.mipmap.radio);
        treasureIds.add(R.mipmap.rouleau);
        treasureIds.add(R.mipmap.sandales);
        treasureIds.add(R.mipmap.stabilos);

        for (int i = treasureIds.size(); i < size; i++)
            treasureIds.add(R.mipmap.toutankamon);

        return treasureIds.stream().limit(size).collect(Collectors.toCollection(ArrayList::new));
    }
}
