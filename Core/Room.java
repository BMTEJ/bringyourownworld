package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Room {
    private final int[] center;
    private final int x;
    private final int y;
    private final int xRange;
    private final int yRange;
    private final int WIDTH;
    private final int HEIGHT;
    private final TETile[][] world;
    private Building building;

    public Room(int newX, int newY, int rangeX, int rangeY, TETile[][] world) {
        x = newX;
        y = newY;
        xRange = rangeX;
        yRange = rangeY;
        WIDTH = x + xRange;
        HEIGHT = y + yRange;
        center = new int[]{(x + WIDTH) / 2, (y + HEIGHT) / 2};
        this.world = world;
        world[center[0]][center[1]] = Tileset.AVATAR;
        building = null;

    }

    public int[] getCenter() {
        return center;
    }

    public boolean hasBuilding(){
        return building != null;
    }

    public void setBuilding(Building newBuild){
        building = newBuild;
    }

    public Building getBuilding(){
        return building;
    }

}
