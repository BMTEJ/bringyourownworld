package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class Movement {
    private TETile[][] world;
    private int[] avatarCoords;
    private Random randSeed;
    private ArrayList<Room> rooms;
    private int currX;
    private int currY;

    public Movement(TETile[][] newworld, ArrayList<Room> roomLst, Random rSeed){
        world = newworld;
        avatarCoords = new int[2];
        randSeed = rSeed;
        rooms = roomLst;
    }

    public void placeAvatar(){
        int randIndex = randSeed.nextInt(rooms.size());
        Room startRoom = rooms.get(randIndex);
        currX = startRoom.getCenter()[0];
        currY = startRoom.getCenter()[1];

        world[currX][currY] = Tileset.AVATAR;

    }

    public void detectMove(String response){
        int x = avatarCoords[0];
        int y = avatarCoords[1];
        if (!moveIntoWall(response)) {
            if (response.equals("W")) {
                world[currX][currY + 1] = Tileset.AVATAR;
                world[currX][currY] = Tileset.FLOOR;
                currY = currY + 1;
            } else if (response.equals("S")) {
                world[currX][currY - 1] = Tileset.AVATAR;
                world[currX][currY] = Tileset.FLOOR;
                currY = currY - 1;
            } else if (response.equals("A")) {
                world[currX - 1][currY] = Tileset.AVATAR;
                world[currX][currY] = Tileset.FLOOR;
                currX = currX - 1;
            } else {
                world[currX + 1][currY] = Tileset.AVATAR;
                world[currX][currY] = Tileset.FLOOR;
                currX = currX + 1;
            }
        }
    }

    private boolean moveIntoWall(String response){
        if (response.equals("W")){
            return world[currX][currY + 1] == Tileset.WALL;
        } else if (response.equals("S")){
            return world[currX][currY - 1] == Tileset.WALL;
        } else if (response.equals("A")) {
            return world[currX - 1][currY] == Tileset.WALL;
        } else {
            return world[currX + 1][currY] == Tileset.WALL;
        }
    }
}
