package byow.Core;

import java.util.ArrayList;

public class Building {
    private ArrayList<Room> rooms;

    public Building(Room currRoom, Room nextRoom){
        rooms = new ArrayList<>();
        rooms.add(currRoom);
        rooms.add(nextRoom);
    }

    public Building(){
        rooms = new ArrayList<>();
    }

    public ArrayList<Room> getRooms(){
        return rooms;
    }

    public void addRooms(ArrayList<Room> building){
        for (Room room: building){
            rooms.add(room);
        }
    }

    public void addRoom(Room room){
        rooms.add(room);
    }
}
