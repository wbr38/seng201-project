package seng201.team53.game.map;

import javafx.scene.image.ImageView;
import seng201.team53.items.towers.Tower;

public class Tile {
    private final ImageView imageView;
    private final boolean buildable;
    private final boolean path;
    private final int x;
    private final int y;
    private Tower tower;

    public Tile(ImageView imageView, boolean buildable, boolean path, int x, int y) {
        this.imageView = imageView;
        this.buildable = buildable;
        this.path = path;
        this.x = x;
        this.y = y;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isBuildable() {
        return buildable & !path;
    }

    public boolean isPath() {
        return path;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Tower getTower() {
        return tower;
    }
    public void setTower(Tower tower) {
        if (!buildable)
            throw new IllegalStateException("Tile is marked as a path tile or not buildable");
        this.tower = tower;
    }
}