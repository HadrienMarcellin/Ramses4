package com.example.ramses4.game;

import com.example.ramses4.shapes.Position;

public class PositionManager {

    private final Integer nbRows;
    private final Integer nbCols;
    private Position oldPosition;
    private Position currentPosition;

    public Position getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(Position oldPosition) {
        this.oldPosition = oldPosition;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.oldPosition = this.currentPosition;
        this.currentPosition = currentPosition;
    }

    public PositionManager(Board board, Position position){

        assert position.getColumn() >= 0;
        assert position.getColumn() < board.getNbCols();
        assert position.getRow() >= 0;
        assert position.getRow() < board.getNbRows();

        this.nbCols = board.getNbCols();
        this.nbRows = board.getNbRows();
        this.oldPosition = position;
        this.currentPosition = position;
    }

    public Position goUp() {
        oldPosition = new Position(currentPosition);
        int row = Math.max(0, oldPosition.getRow() - 1);
        currentPosition.setRow(row);
        return currentPosition;
    }

    public Position goDown() {
        oldPosition = new Position(currentPosition);
        int row = Math.min(nbRows - 1, oldPosition.getRow() + 1);
        currentPosition.setRow(row);
        return currentPosition;
    }

    public Position goRight() {
        oldPosition = new Position(currentPosition);
        int column = Math.min(nbCols - 1, oldPosition.getColumn() + 1);
        currentPosition.setColumn(column);
        return currentPosition;
    }

    public Position goLeft() {
        oldPosition = new Position(currentPosition);
        int column = Math.max(0, oldPosition.getColumn() - 1);
        currentPosition.setColumn(column);
        return currentPosition;
    }


    public boolean isPositionAllowed(Position newPosition) {
        int diffCol = Math.abs(newPosition.getColumn() - this.currentPosition.getColumn());
        int diffRow = Math.abs(newPosition.getRow() - this.currentPosition.getRow());
        return  diffCol <= 1 && diffRow <= 1 && (diffCol == 1 ^ diffRow == 1);
    }

    public boolean isHorizontalMovement(Position newPosition) {
        return newPosition.getRow().equals(currentPosition.getRow());
    }





}
