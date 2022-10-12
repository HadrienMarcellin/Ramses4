package com.example.ramses4.shapes;

public class Position {
    private Integer row;
    private Integer column;

    public Position(Integer row, Integer column) {
        this.row = row;
        this.column = column;
    }

    public Position(Position position) {
        this.row = position.getRow();
        this.column = position.getColumn();
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public boolean equals(Position position) {
        return this.row.equals(position.getRow()) && this.column.equals(position.getColumn());
    }

    public void transpose() {
        Integer col = this.column;
        Integer row = this.row;
        setRow(col);
        setColumn(row);
    }
}
