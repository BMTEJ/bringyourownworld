package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;


public class Hallways {
    private final TETile[][] world;

    private final ArrayList<Room> rooms;

    private ArrayList<Building> buildings;

    private Room closestRoom;


    public Hallways(TETile[][] world, ArrayList<Room> rooms) {
        this.world = world;
        this.rooms = rooms;
        buildings = new ArrayList<>();
        closestRoom = null;
    }

    public void connectRooms(){
        for (Room room: rooms){
            if (room.hasBuilding()){
                continue;
            } else {
                findClosestRoom(room);
                connectRooms(room, closestRoom);
            }
        }
        connectBuildings(buildings.get(0));
    }

    /**
     * Takes in a room and connects to its closest room,by finding which direction the connecting
     * hallway should be built.
     */

    public void connectRooms(Room currRoom, Room closestRoom) {
        int currX = currRoom.getCenter()[0];
        int currY = currRoom.getCenter()[1];
        int goalX = closestRoom.getCenter()[0];
        int goalY = closestRoom.getCenter()[1];

        if (goalY > currY) {
            goUp(currX, currY, goalX, goalY);

        } else if (currY > goalY) {
            goDown(currX, currY, goalX, goalY);
        } else if (currY == goalY) {
            if (currX > goalX) {
                goLeft(currX, currY, goalX, goalY);
            } else if (goalX > currX) {
                goRight(currX, currY, goalX, goalY);
            }
        }
    }

    /**
     * Takes in a room and finds the closest room to it while adding to the visited listed,
     * ensuring we only continue to find the closest room to the next room.
     * Kruskal's. After finding closest, calls to connect to the newfound closest room
     * to repeat until all rooms are connected.
     */

    public void findClosestRoom(Room room) {
        Room curr = room;
        int currX;
        int currY;
        int goalX;
        int goalY;
        double distance;
        double newCoords = Double.POSITIVE_INFINITY;


        for (Room closest : rooms) {
            if (closest.equals(room)) {
                continue;
            }
            currX = curr.getCenter()[0];
            currY = curr.getCenter()[1];
            goalX = closest.getCenter()[0];
            goalY = closest.getCenter()[1];
            distance = Math.sqrt(Math.pow((currX - goalX), 2) + Math.pow((currY - goalY), 2));
            if (distance <= newCoords) {
                closestRoom = closest;
                newCoords = distance;
            }
        }
        makeBuilding(curr, closestRoom);
    }

    private void makeBuilding(Room currRoom, Room closestRoom){
        if (!currRoom.hasBuilding() && !closestRoom.hasBuilding()){
            Building newBuild = new Building(currRoom, closestRoom);
            currRoom.setBuilding(newBuild);
            closestRoom.setBuilding(newBuild);
            buildings.add(newBuild);
        } else {
            closestRoom.getBuilding().addRoom(currRoom);
        }
    }

    private void connectBuildings(Building building){
        if (building.getRooms().size() >= rooms.size()){
            return;
        }
        findClosestBuilding(building);
    }

    private void findClosestBuilding(Building currBuilding){
        Room curr = null;
        int currX;
        int currY;
        int goalX;
        int goalY;
        double distance;
        double newCoords = Double.POSITIVE_INFINITY;
        Building closestBuild = null;

        for (Room currRoom: currBuilding.getRooms()){
            currX = currRoom.getCenter()[0];
            currY = currRoom.getCenter()[1];
            for (Building closestBuilding: buildings){
                if (closestBuilding.equals(currBuilding) ||
                        connectedBuilds(currBuilding, closestBuilding)){
                    continue;
                }
                for (Room nextRoom: closestBuilding.getRooms()){
                    goalX = nextRoom.getCenter()[0];
                    goalY = nextRoom.getCenter()[1];
                    distance = Math.sqrt(Math.pow((currX - goalX), 2) + Math.pow((currY - goalY), 2));
                    if (distance <= newCoords) {
                        closestRoom = nextRoom;
                        newCoords = distance;
                        closestBuild = closestBuilding;
                        curr = currRoom;
                    }
                }
            }
        }
        connectBuildings(currBuilding, curr, closestBuild, closestRoom);
    }

    private void connectBuildings(Building currBuild, Room room,
                                  Building closest, Room closeRoom){
        connectRooms(room, closeRoom);
        currBuild.addRooms(closest.getRooms());
        buildings.remove(closest);
        connectBuildings(currBuild);
    }

    private boolean connectedBuilds(Building building1, Building building2){
        Room room1 = building1.getRooms().get(0);
        Room room2 = building2.getRooms().get(0);
        return building1.getRooms().contains(room2)
                || building2.getRooms().contains(room1);
    }

    /**
     * Moves the hallway up by taking current position indicated by currY and currX and
     * adding to currY until hallway is level wtih goalX and goalY.
     */

    private void goUp(int currX, int currY, int goalX, int goalY) {
        int trackY = 0;
        for (int i = currY; i != goalY + 1; i++) {
            if (world[currX][i] != Tileset.FLOOR) {
                world[currX][i] = Tileset.FLOOR;
            }
            if (world[currX - 1][i] == Tileset.NOTHING) {
                world[currX - 1][i] = Tileset.WALL;
            }
            if (world[currX + 1][i] == Tileset.NOTHING) {
                world[currX + 1][i] = Tileset.WALL;
            }
            trackY = i;
        }
        currY = trackY;

        if (currX > goalX) {
            if (world[currX + 1][currY + 1] == Tileset.NOTHING) {
                world[currX + 1][currY + 1] = Tileset.WALL;
            }
            goLeft(currX, currY, goalX, goalY);
        } else if (goalX > currX) {
            if (world[currX - 1][currY + 1] == Tileset.NOTHING) {
                world[currX - 1][currY + 1] = Tileset.WALL;
            }
            goRight(currX, currY, goalX, goalY);
        }
    }

    /**
     * @param currX @param goalY @param goalX @param currY
     *              Takes in above parameters and moves left from currX to goalX
     */
    private void goLeft(int currX, int currY, int goalX, int goalY) {
        int xTracker = 0;
        for (int i = currX; i != goalX - 1; i--) {
            if (world[i][currY] != Tileset.FLOOR) {
                world[i][currY] = Tileset.FLOOR;
            }
            if (world[i][currY - 1] == Tileset.NOTHING) {
                world[i][currY - 1] = Tileset.WALL;
            }
            if (world[i][currY + 1] == Tileset.NOTHING) {
                world[i][currY + 1] = Tileset.WALL;
            }
            xTracker = i;
        }
        currX = xTracker;

        if (currY > goalY) {
            if (world[currX - 1][currY + 1] == Tileset.NOTHING) {
                world[currX - 1][currY + 1] = Tileset.WALL;
            }
            goDown(currX, currY, goalX, goalY);
        } else if (goalY > currY) {
            if (world[currX - 1][currY - 1] == Tileset.NOTHING) {
                world[currX - 1][currY - 1] = Tileset.WALL;
            }
            goUp(currX, currY, goalX, goalY);
        }
    }

    /**
     * @param currX @param goalY @param goalX @param currY
     *              Takes in above parameters and moves right from currX to goalX
     */

    private void goRight(int currX, int currY, int goalX, int goalY) {
        int xTracker = 0;
        for (int i = currX; i != goalX + 1; i++) {
            if (world[i][currY] != Tileset.FLOOR) {
                world[i][currY] = Tileset.FLOOR;
            }
            if (world[i][currY - 1] == Tileset.NOTHING) {
                world[i][currY - 1] = Tileset.WALL;
            }
            if (world[i][currY + 1] == Tileset.NOTHING) {
                world[i][currY + 1] = Tileset.WALL;
            }
            currX = xTracker;
        }
        if (currY > goalY) {
            if (world[currX + 1][currY + 1] == Tileset.NOTHING) {
                world[currX + 1][currY + 1] = Tileset.WALL;
            }
            goDown(currX, currY, goalX, goalY);
        } else if (goalY > currY) {
            if (world[currX + 1][currY - 1] == Tileset.NOTHING) {
                world[currX + 1][currY - 1] = Tileset.WALL;
            }
            goUp(currX, currY, goalX, goalY);
        }
    }

    /**
     * @param currX @param goalY @param goalX @param currY
     *              Takes in above parameters and moves down from currY to goalY
     */

    private void goDown(int currX, int currY, int goalX, int goalY) {
        int yTracker = 0;
        for (int i = currY; i != goalY - 1; i--) {
            if (world[currX][i] != Tileset.FLOOR) {
                world[currX][i] = Tileset.FLOOR;
            }
            if (world[currX - 1][i] == Tileset.NOTHING) {
                world[currX - 1][i] = Tileset.WALL;
            }
            if (world[currX + 1][i] == Tileset.NOTHING) {
                world[currX + 1][i] = Tileset.WALL;
            }
            yTracker = i;
        }
        currY = yTracker;
        if (currX > goalX) {
            if (world[currX + 1][currY - 1] == Tileset.NOTHING) {
                world[currX + 1][currY - 1] = Tileset.WALL;
            }
            goLeft(currX, currY, goalX, goalY);
        } else if (goalX > currX) {
            if (world[currX - 1][currY - 1] == Tileset.NOTHING) {
                world[currX - 1][currY - 1] = Tileset.WALL;
            }
            goRight(currX, currY, goalX, goalY);
        }
    }
}
