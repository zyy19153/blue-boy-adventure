package tile;

import java.awt.image.BufferedImage;

public class Tile {

    public Tile(String name) {
        this.name = name;
    }

    public String name;
    public BufferedImage image;
    public boolean collision = false;
}
