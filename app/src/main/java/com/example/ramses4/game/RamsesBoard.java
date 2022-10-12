package com.example.ramses4.game;

import android.view.animation.RotateAnimation;

import com.example.ramses4.R;
import com.example.ramses4.shapes.FixedTile;
import com.example.ramses4.shapes.Position;
import com.example.ramses4.shapes.Tile;

import java.util.ArrayList;
import java.util.Random;

public class RamsesBoard {

    private static final Random RAND = new Random();
    private final Board board;
    private ArrayList<FixedTile> treasures;
    private ArrayList<Tile> tiles;

    enum PyramidColor {
        RED,
        BLUE,
        GOLD
    }

    public int getNbRows() {
        return board.getNbRows();
    }

    public int getNbCols() {
        return board.getNbCols();
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public RamsesBoard(Board board, ArrayList<Tile> treasures) {
        this.board = board;
        this.tiles = new ArrayList<>();
        this.dispatchTreasures(treasures);
        this.computeBoard();
    }

    private ArrayList<PyramidColor> shuffleColors(int length) {
        PyramidColor[] colors = PyramidColor.values();
        ArrayList<PyramidColor> shuffle = new ArrayList<>();
        for(int i=0; i<length; i++) {
            shuffle.add(colors[RAND.nextInt(colors.length)]);
        }
        return shuffle;
    }

    private void computeBoard() {
        Integer backgroundColor = R.color.ramses_deep_red;
        ArrayList<PyramidColor> pyramidColors = shuffleColors(getNbCols() * getNbRows());
        tiles = new ArrayList<>();

        for (PyramidColor color : pyramidColors) {
            Integer orientation = 90 * RAND.nextInt(4);
            switch (color) {
                case BLUE:
                    tiles.add(new Tile(R.mipmap.pyramid_blue, backgroundColor, orientation, 0));
                    break;
                case RED:
                    tiles.add(new Tile(R.mipmap.pyramid_red, backgroundColor, orientation, 0));
                    break;
                default:
                    tiles.add(new Tile(R.mipmap.pyramid_gold, backgroundColor, orientation, 0));
            }
        }
    }

    public void updateBoard(PositionManager positionManager) {
        int oldPosition = board.positionToInt(positionManager.getOldPosition());
        int newPosition = board.positionToInt(positionManager.getCurrentPosition());

        tiles.set(oldPosition, tiles.get(newPosition));
        tiles.set(newPosition, getHiddenTileDescription(newPosition));
    }

    public Tile getHiddenTileDescription(int currentPosition) {
        Position position = board.intToPosition(currentPosition);
        for (FixedTile tile : treasures) {
            if (tile.getPosition().equals(position)) {
                return tile.getTileDescription();
            }
        }
        return new Tile(0, R.color.ramses_deep_red, new RotateAnimation(0, 0), -1);
    }

    private void dispatchTreasures(ArrayList<Tile> treasures) {
        this.treasures = new ArrayList<>();
        Random random = new Random();
        int min = 0;
        int max = 1;
        int i = 0;
        for (int col = 0; col < getNbCols() - 1; col = col + 2) {
            for (int row = 0; row < getNbRows() - 1; row = row + 2) {
                int randomLocalCol = random.nextInt(max - min + 1) + min;
                int randomLocalRow = random.nextInt(max - min + 1) + min;
                this.treasures.add(new FixedTile(
                        new Position(row + randomLocalRow, col + randomLocalCol),
                        treasures.get(i++)));
            }
        }
    }


}
