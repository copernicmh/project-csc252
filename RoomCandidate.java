package studyspace.structures;

import studyspace.model.Room;

public class RoomCandidate implements Comparable<RoomCandidate>
{
    private Room room;
    private double distance;

    public RoomCandidate(Room room, double distance)
    {
        this.room = room;
        this.distance = distance;
    }

    public Room getRoom()
    {
        return room;
    }

    public double getDistance()
    {
        return distance;
    }

    public int compareTo(RoomCandidate other)
    {
        return Double.compare(this.distance, other.distance);
    }
}