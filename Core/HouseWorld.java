package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;


public class HouseWorld {

    private final TETile[][] world;
    private final ArrayList<Room> rooms;

    private final long SEED;
    private final int HEIGHT, WIDTH;

    private final Random randSeed;
    private int roomCount;
    private static final int MAXROOMCOUNT = 25;
    private static final int MINIMUMROOMCOUNT = 10;
    private final Hallways hallway;
    private final Movement movement;

    public HouseWorld(TETile[][] world, long seed) {
        SEED = seed;
        this.world = world;
        HEIGHT = world[0].length;
        WIDTH = world.length;
        rooms = new ArrayList<Room>();
        randSeed = new Random(SEED);
        roomCount = randSeed.nextInt(MINIMUMROOMCOUNT, MAXROOMCOUNT);
        hallway = new Hallways(world, rooms);
        movement = new Movement(world, rooms, randSeed);


        startWorld();
    }

    /**
     * Lays down a number of rooms based on RoomCount.
     */
    private void startWorld() {
        int[] newCoords = findNewCoords();
        int x = newCoords[0];
        int y = newCoords[1];
        int xRange = newCoords[2];
        int yRange = newCoords[3];
        while (roomCount != 0) {
            buildRoom(x, y, xRange, yRange);
            rooms.add(new Room(x, y, xRange, yRange, world));
            roomCount -= 1;
            newCoords = findNewCoords();
            x = newCoords[0];
            y = newCoords[1];
            xRange = newCoords[2];
            yRange = newCoords[3];
        }
        buildHallways();
//        cleanUpWorld();
        makeFires();
        makeWater();
        movement.placeAvatar();
    }

    /**
     * Given a set of verified, in bound, x, y and their respective ranges,
     * builds rooms surrounded by wall with the interior being floor.
     */

    private void buildRoom(int x, int y, int xRange, int yRange) {
        for (int i = x; i <= x + xRange; i++) {
            for (int j = y; j <= y + yRange; j++) {
                if (i == x || j == y || i == x + xRange || j == y + yRange) {
                    world[i][j] = Tileset.WALL;
                } else {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    /**
     * Searches for x and y coordinates along with their respective ranges
     * indicating a room can be built in the area they encompass so long as
     * the Tiles within the area are all NOTHING and
     * returns an array of coords x, y, xRange, and yRange when
     */

    private int[] findNewCoords() {
        int x = randSeed.nextInt(WIDTH);
        int y = randSeed.nextInt(HEIGHT);
        int xRange = randSeed.nextInt(4, 12);
        int yRange = randSeed.nextInt(4, 12);
        while (!collisions(x, y, xRange, yRange)) {
            x = randSeed.nextInt(WIDTH);
            y = randSeed.nextInt(HEIGHT);
            xRange = randSeed.nextInt(4, 12);
            yRange = randSeed.nextInt(4, 12);
        }
        return new int[]{x, y, xRange, yRange};
    }

    /**
     * Finds new coordinates where a room can be built randomly using the seed for Random()
     * It will continue to search for a place to put the room.
     */
    private boolean collisions(int x, int y, int xRange, int yRange) {
        for (int i = x; i <= x + xRange; i++) {
            for (int j = y; j <= y + yRange; j++) {
                if (outOfBounds(x, y, xRange, yRange) || world[i][j] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Given an x, y, and their respective ranges, determines whether the area encompassed
     * will be out of bounds of board. Returns true if so, and false if not.
     */

    private boolean outOfBounds(int x, int y, int xRange, int yRange) {
        return x + xRange >= WIDTH || y + yRange >= HEIGHT;
    }


    /**
     * Does a passthrough of closest room for each room and connects them, then does a pass
     * through of the furthest rooms and connects them as well.
     */
    private void buildHallways() {
        hallway.connectRooms();
    }

    private void makeFires(){
        for (Room room: rooms){
            world[room.getCenter()[0]][room.getCenter()[1]] = Tileset.FIRE;
        }
     }

    private void makeWater(){
        for (int i = 0; i < WIDTH; i++){
            for (int j = 0; j < HEIGHT; j++){
                if (world[i][j] == Tileset.NOTHING){
                    world[i][j] = Tileset.WATER;
                }
            }
        }
    }

    public void handleMove(String response){
        movement.detectMove(response);
    }

}
