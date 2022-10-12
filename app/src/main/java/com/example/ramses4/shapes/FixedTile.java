package com.example.ramses4.shapes;

import android.view.animation.Animation;

public class FixedTile {

    private Position position;
    private Tile tile;

    public FixedTile(Position position, Integer drawableId, Integer backgroundColor, Integer id) {
        this.tile = new Tile(drawableId, backgroundColor, id);
        this.position = position;
    }

    public FixedTile(Position position, Integer drawableId, Integer backgroundColor, Animation animation, Integer id) {
        this.tile = new Tile(drawableId, backgroundColor, animation, id);
        this.position = position;
    }

    public FixedTile(Position position, Tile tile) {
        this.tile = new Tile(tile);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public Tile getTileDescription() {
        return tile;
    }
}
