package studyspace.services;

import studyspace.model.Reservation;
import studyspace.model.Room;
import java.util.ArrayList;
import java.util.TreeMap;

public class ReservationService
{
    public boolean hasConflict(Room room, String newStart, String newEnd)
    {
        TreeMap<String, Reservation> reservations = room.getReservations();

        for (Reservation current : reservations.values())
        {
            String existingStart = current.getStartTime();
            String existingEnd = current.getEndTime();

            if (newStart.compareTo(existingEnd) < 0 &&
                newEnd.compareTo(existingStart) > 0)
            {
                return true;
            }
        }

        return false;
    }

    public boolean makeReservation(Room room, Reservation reservation)
    {
        if (hasConflict(room, reservation.getStartTime(), reservation.getEndTime()))
        {
            return false;
        }

        room.getReservations().put(reservation.getStartTime(), reservation);
        return true;
    }

    public boolean cancelReservation(Room room, String startTime)
    {
        Reservation removed = room.getReservations().remove(startTime);
        return removed != null;
    }

    public ArrayList<Room> getAvailableRooms(ArrayList<Room> rooms, String startTime, String endTime)
    {
        ArrayList<Room> available = new ArrayList<Room>();

        for (Room room : rooms)
        {
            if (!hasConflict(room, startTime, endTime))
            {
                available.add(room);
            }
        }

        return available;
    }

    public String getNextAvailableTime(Room room)
    {
        String latestEnd = "00:00";

        for (Reservation reservation : room.getReservations().values())
        {
            if (reservation.getEndTime().compareTo(latestEnd) > 0)
            {
                latestEnd = reservation.getEndTime();
            }
        }

        return latestEnd;
    }
}