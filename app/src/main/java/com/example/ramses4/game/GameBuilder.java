package com.example.ramses4.game;

import com.example.ramses4.R;
import com.example.ramses4.shapes.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class GameBuilder {

    private ArrayList<Target> targets;
    private ArrayList<Tile> treasures;
    private Board board;

    public ArrayList<Target> getTargets() {
        return targets;
    }

    public ArrayList<Tile> getTreasures() {
        return treasures;
    }

    public GameBuilder(Board board) {
        this.board = board;
    }

    public void buid() {
        createTreasures(board);
        createTargets();
    }

    private void createTreasures(Board board) {
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

    private void createTargets() {
        targets = new ArrayList<>();
        for (Tile treasure: treasures) {
            targets.add(new Target(treasure.getId(), 1, treasure.getDrawableId()));
            targets.add(new Target(treasure.getId(), 2, treasure.getDrawableId()));
            targets.add(new Target(treasure.getId(), 4, treasure.getDrawableId()));
        }
        Collections.shuffle(targets);
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
