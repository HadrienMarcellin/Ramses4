package com.example.ramses4.game;

import com.example.ramses4.shapes.Position;

public class Board {
    private final int nbRows;
    private final int nbCols;

    public Board(int nbCols, int nbRows) {
        this.nbCols = nbCols;
        this.nbRows = nbRows;
    }

    public int getNbRows() {
        return nbRows;
    }

    public int getNbCols() {
        return nbCols;
    }

    public int getNbTiles() {
        return nbCols * nbRows;
    }

    public Integer positionToInt(Position position) {
        return position.getRow() * nbCols + position.getColumn();
    }

    public Position intToPosition(Integer position) {
        assert position >= 0;
        assert position <= nbCols * nbRows;

        final int row = position / nbCols;
        final int col = position - row * nbCols;
        return new Position(row, col);
    }
}
