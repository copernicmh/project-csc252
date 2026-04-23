//Harmony Barber
//Project 4: StudySpace Finder System
//Room Management System
import java.util.*;

public class RoomManager
{
   HashMap<String, Room> rooms;

    public RoomManager() 
    {
        rooms = new HashMap<>();
    }

    // Add room
    public void addRoom(String id, int capacity, String features) 
    {
        rooms.put(id, new Room(id, capacity, features));
        System.out.println("Room added.");
    }

    // Search room by ID
    public void searchRoom(String id)
    {
        Room room = rooms.get(id);
        if (room != null) 
        {
            System.out.println(room);
        } 
        else 
        {
            System.out.println("Room not found.");
        }
    }

    // Remove room
    public void removeRoom(String id) 
    {
        if (rooms.remove(id) != null) 
        {
            System.out.println("Room removed.");
        } else 
        {
            System.out.println("Room not found.");
        }
    }

    // Display all rooms
    public void showAllRooms() 
    {
        if (rooms.isEmpty()) 
        {
            System.out.println("No rooms available.");
            return;
        }

        for (Room room : rooms.values()) 
        {
            System.out.println(room);
        }
    }
}