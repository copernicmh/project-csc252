package studyspace.model;

import java.util.ArrayList;
import java.util.TreeMap;

public class Room
{
    private String roomID;
    private String buildingID;
    private int capacity;
    private ArrayList<String> features;
    private TreeMap<String, Reservation> reservations;

    public Room(String roomID, String buildingID, int capacity, ArrayList<String> features)
    {
        this.roomID = roomID;
        this.buildingID = buildingID;
        this.capacity = capacity;
        this.features = features;
        this.reservations = new TreeMap<String, Reservation>();
    }

    public String getRoomID()
    {
        return roomID;
    }

    public String getBuildingID()
    {
        return buildingID;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public ArrayList<String> getFeatures()
    {
        return features;
    }

    public TreeMap<String, Reservation> getReservations()
    {
        return reservations;
    }

    public void setReservations(TreeMap<String, Reservation> reservations)
    {
        this.reservations = reservations;
    }

    public String toString()
    {
        return "Room ID: " + roomID + ", Building: " + buildingID +
               ", Capacity: " + capacity + ", Features: " + features;
    }
}