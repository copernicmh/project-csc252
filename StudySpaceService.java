package studyspace.services;

import studyspace.model.Building;
import studyspace.model.Room;
import studyspace.model.StudentRequest;
import studyspace.structures.RoomCandidate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class StudySpaceService
{
    private HashMap<String, Room> roomMap;
    private HashMap<String, Building> buildingMap;

    public StudySpaceService()
    {
        roomMap = new HashMap<String, Room>();
        buildingMap = new HashMap<String, Building>();
    }

    public void addBuilding(Building building)
    {
        buildingMap.put(building.getBuildingID(), building);
    }

    public Building searchBuilding(String buildingID)
    {
        return buildingMap.get(buildingID);
    }

    public void addRoom(Room room)
    {
        roomMap.put(room.getRoomID(), room);
    }

    public Room searchRoom(String roomID)
    {
        return roomMap.get(roomID);
    }

    public void removeRoom(String roomID)
    {
        roomMap.remove(roomID);
    }

    public HashMap<String, Room> getRoomMap()
    {
        return roomMap;
    }

    public HashMap<String, Building> getBuildingMap()
    {
        return buildingMap;
    }

    public ArrayList<Room> getAllRooms()
    {
        return new ArrayList<Room>(roomMap.values());
    }

    public ArrayList<Room> findMatchingRooms(int neededCapacity, ArrayList<String> neededFeatures)
    {
        ArrayList<Room> matches = new ArrayList<Room>();

        for (Room room : roomMap.values())
        {
            if (room.getCapacity() >= neededCapacity &&
                room.getFeatures().containsAll(neededFeatures))
            {
                matches.add(room);
            }
        }

        return matches;
    }

    public RoomCandidate findNearestAvailableRoom(StudentRequest request,
                                                  ReservationService reservationService,
                                                  RoutingService routingService)
    {
        ArrayList<Room> matchingRooms = findMatchingRooms(request.getRequiredCapacity(),
                                                          request.getRequiredFeatures());

        ArrayList<Room> availableRooms =
            reservationService.getAvailableRooms(matchingRooms,
                                                 request.getStartTime(),
                                                 request.getEndTime());

        PriorityQueue<RoomCandidate> pq = new PriorityQueue<RoomCandidate>();

        for (Room room : availableRooms)
        {
            double distance = routingService.getDistance(request.getCurrentBuildingID(),
                                                         room.getBuildingID());

            if (distance != Double.MAX_VALUE)
            {
                pq.add(new RoomCandidate(room, distance));
            }
        }

        if (pq.isEmpty())
        {
            return null;
        }

        return pq.poll();
    }
}