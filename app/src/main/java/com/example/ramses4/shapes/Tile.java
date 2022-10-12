package com.example.ramses4.shapes;

import android.view.animation.Animation;

public class Tile {
    private Integer id;
    protected Integer drawableId;
    protected Integer backgroundColorId;
    private Animation animation;
    private Integer rotation;

    public Tile(Integer drawableId, Integer backgroundColorId, Integer id) {
        this.drawableId = drawableId;
        this.backgroundColorId = backgroundColorId;
        this.animation = null;
        this.rotation = 0;
        this.id = id;
    }

    public Tile(Integer drawableId, Integer backgroundColorId, Animation animation, Integer id) {
        this.drawableId = drawableId;
        this.backgroundColorId = backgroundColorId;
        this.animation = animation;
        this.rotation = 0;
        this.id = id;
    }

    public Tile(Integer drawableId, Integer backgroundColorId, Integer rotation, Integer id) {
        this.drawableId = drawableId;
        this.backgroundColorId = backgroundColorId;
        this.animation = null;
        this.rotation = rotation;
        this.id = id;
    }

    public Tile(Tile tile) {
        this.drawableId = tile.getDrawableId();
        this.backgroundColorId = tile.getBackgroundColorId();
        this.animation = tile.getAnimation();
        this.rotation = tile.getRotation();
        this.id = tile.getId();
    }

    public Integer getId() {
        return id;
    }

    public Animation getAnimation() {
        return animation;
    }

    public Integer getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(Integer drawableId) {
        this.drawableId = drawableId;
    }

    public Integer getBackgroundColorId() {
        return backgroundColorId;
    }

    public void setBackgroundColorId(Integer backgroundColor) {
        this.backgroundColorId = backgroundColor;
    }

    public Integer getRotation() {
        return rotation;
    }
}
