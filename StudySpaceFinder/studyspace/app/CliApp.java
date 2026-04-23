package studyspace.app;

import studyspace.model.Building;
import studyspace.model.Reservation;
import studyspace.model.Room;
import studyspace.model.StudentRequest;
import studyspace.services.ReservationService;
import studyspace.services.RoutingService;
import studyspace.services.StudySpaceService;
import studyspace.structures.CampusGraph;
import studyspace.structures.RoomCandidate;
import studyspace.structures.ReservationTree;

import java.util.ArrayList;
import java.util.Scanner;

public class CliApp
{
    private static int autoCounter = 1;

    public static void main(String[] args)
    {
        Scanner keyboard = new Scanner(System.in);

        StudySpaceService studyService = new StudySpaceService();
        ReservationService reservationService = new ReservationService();
        ReservationTree reservationTree = new ReservationTree();
        CampusGraph graph = new CampusGraph();
        RoutingService routingService = new RoutingService(graph);

        loadSampleData(studyService, graph);

        int choice = 0;

        do
        {
            System.out.println("\n=============================");
            System.out.println("     StudySpace Finder");
            System.out.println("=============================");
            System.out.println("1.  Add Building");
            System.out.println("2.  Add Room");
            System.out.println("3.  Search Room");
            System.out.println("4.  Remove Room");
            System.out.println("5.  Show All Rooms");
            System.out.println("6.  Make Reservation");
            System.out.println("7.  Cancel Reservation");
            System.out.println("8.  View Room Schedule");
            System.out.println("9.  Find Nearest Available Room");
            System.out.println("10. Exit");
            System.out.print("Enter choice: ");

            if (keyboard.hasNextInt())
            {
                choice = keyboard.nextInt();
                keyboard.nextLine();
            }
            else
            {
                System.out.println("Invalid input. Please enter a number.");
                keyboard.nextLine();
                continue;
            }

            switch (choice)
            {
                case 1:  addBuilding(keyboard, studyService, graph);                                      break;
                case 2:  addRoom(keyboard, studyService);                                                 break;
                case 3:  searchRoom(keyboard, studyService);                                              break;
                case 4:  removeRoom(keyboard, studyService);                                              break;
                case 5:  showAllRooms(studyService);                                                      break;
                case 6:  makeReservation(keyboard, studyService, reservationService, reservationTree);    break;
                case 7:  cancelReservation(keyboard, studyService, reservationService, reservationTree);  break;
                case 8:  viewRoomSchedule(keyboard, reservationTree);                                     break;
                case 9:  findNearestAvailableRoom(keyboard, studyService, reservationService, routingService); break;
                case 10: System.out.println("Exiting StudySpace Finder. Goodbye!"); break;
                default: System.out.println("Invalid choice. Please enter 1-10.");
            }

        } while (choice != 10);

        keyboard.close();
    }

    public static void addBuilding(Scanner keyboard, StudySpaceService studyService, CampusGraph graph)
    {
        System.out.print("Enter building ID: ");
        String buildingID = keyboard.nextLine().trim();

        System.out.print("Enter building name: ");
        String buildingName = keyboard.nextLine().trim();

        studyService.addBuilding(new Building(buildingID, buildingName));
        graph.addBuildingNode(buildingID);

        System.out.println("Building added: " + buildingID + " - " + buildingName);
    }

    public static void addRoom(Scanner keyboard, StudySpaceService studyService)
    {
        System.out.print("Enter room ID: ");
        String roomID = keyboard.nextLine().trim();

        System.out.print("Enter building ID: ");
        String buildingID = keyboard.nextLine().trim();

        System.out.print("Enter capacity: ");
        int capacity;
        try
        {
            capacity = Integer.parseInt(keyboard.nextLine().trim());
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid capacity. Room not added.");
            return;
        }

        System.out.print("Enter features (comma-separated, e.g. quiet,projector): ");
        String featureLine = keyboard.nextLine().trim();

        ArrayList<String> features = new ArrayList<String>();
        if (!featureLine.isEmpty())
        {
            for (String feature : featureLine.split(","))
            {
                String f = feature.trim().toLowerCase();
                if (!f.isEmpty()) features.add(f);
            }
        }

        studyService.addRoom(new Room(roomID, buildingID, capacity, features));
        System.out.println("Room added: " + roomID + " in " + buildingID
                + " | capacity: " + capacity + " | features: " + features);
    }

    public static void searchRoom(Scanner keyboard, StudySpaceService studyService)
    {
        System.out.print("Enter room ID: ");
        String roomID = keyboard.nextLine().trim();

        Room room = studyService.searchRoom(roomID);
        if (room != null)
            System.out.println(room);
        else
            System.out.println("Room '" + roomID + "' not found.");
    }

    public static void removeRoom(Scanner keyboard, StudySpaceService studyService)
    {
        System.out.print("Enter room ID to remove: ");
        String roomID = keyboard.nextLine().trim();

        if (studyService.searchRoom(roomID) == null)
        {
            System.out.println("Room '" + roomID + "' not found.");
        }
        else
        {
            studyService.removeRoom(roomID);
            System.out.println("Room '" + roomID + "' removed.");
        }
    }

    public static void showAllRooms(StudySpaceService studyService)
    {
        ArrayList<Room> allRooms = studyService.getAllRooms();
        if (allRooms.isEmpty())
        {
            System.out.println("No rooms in the system.");
        }
        else
        {
            System.out.println("\n--- All Rooms (" + allRooms.size() + " total) ---");
            for (Room room : allRooms)
                System.out.println("  " + room);
        }
    }

    public static void makeReservation(Scanner keyboard, StudySpaceService studyService,
                                       ReservationService reservationService,
                                       ReservationTree reservationTree)
    {
        System.out.print("Enter room ID: ");
        String roomID = keyboard.nextLine().trim();

        Room room = studyService.searchRoom(roomID);
        if (room == null)
        {
            System.out.println("Room '" + roomID + "' not found.");
            return;
        }

        System.out.print("Enter reservation ID: ");
        String reservationID = keyboard.nextLine().trim();

        System.out.print("Enter student name: ");
        String studentName = keyboard.nextLine().trim();

        System.out.print("Enter start time (HH:MM): ");
        String startTime = keyboard.nextLine().trim();

        System.out.print("Enter end time (HH:MM): ");
        String endTime = keyboard.nextLine().trim();

        Reservation reservation = new Reservation(reservationID, studentName, roomID, startTime, endTime);

        if (reservationService.makeReservation(room, reservation))
        {
            reservationTree.addReservation(reservation);
            System.out.println("Reservation confirmed: " + studentName
                    + " in room " + roomID + " from " + startTime + " to " + endTime);
        }
        else
        {
            System.out.println("Time conflict detected. Reservation denied.");
            System.out.println("Next available time in this room: "
                    + reservationService.getNextAvailableTime(room));
        }
    }

    public static void cancelReservation(Scanner keyboard, StudySpaceService studyService,
                                         ReservationService reservationService,
                                         ReservationTree reservationTree)
    {
        System.out.print("Enter room ID: ");
        String roomID = keyboard.nextLine().trim();

        Room room = studyService.searchRoom(roomID);
        if (room == null)
        {
            System.out.println("Room '" + roomID + "' not found.");
            return;
        }

        System.out.print("Enter reservation ID to cancel: ");
        String reservationID = keyboard.nextLine().trim();

        // Find the reservation to get its start time for TreeMap removal
        Reservation target = null;
        for (Reservation r : room.getReservations().values())
        {
            if (r.getReservationID().equals(reservationID))
            {
                target = r;
                break;
            }
        }

        if (target == null)
        {
            System.out.println("Reservation '" + reservationID + "' not found in room " + roomID + ".");
            return;
        }

        reservationService.cancelReservation(room, target.getStartTime());
        reservationTree.cancelReservation(reservationID);
        System.out.println("Reservation '" + reservationID + "' cancelled successfully.");
    }

    public static void viewRoomSchedule(Scanner keyboard, ReservationTree reservationTree)
    {
        System.out.print("Enter room ID to view schedule (or press Enter for all reservations): ");
        String roomID = keyboard.nextLine().trim();

        if (roomID.isEmpty())
        {
            reservationTree.displayAll();
        }
        else
        {
            java.util.List<Reservation> schedule = reservationTree.getReservationsForRoom(roomID);
            if (schedule.isEmpty())
            {
                System.out.println("No reservations found for room '" + roomID + "'.");
            }
            else
            {
                System.out.println("--- Schedule for Room " + roomID + " ---");
                for (Reservation r : schedule)
                    System.out.println("  " + r);
            }
        }
    }

    public static void findNearestAvailableRoom(Scanner keyboard, StudySpaceService studyService,
                                                ReservationService reservationService,
                                                RoutingService routingService)
    {
        System.out.print("Enter current building ID: ");
        String currentBuilding = keyboard.nextLine().trim();

        System.out.print("Enter required capacity: ");
        int neededCapacity;
        try
        {
            neededCapacity = Integer.parseInt(keyboard.nextLine().trim());
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid capacity.");
            return;
        }

        System.out.print("Enter needed features (comma-separated, or Enter for none): ");
        String featuresLine = keyboard.nextLine().trim();

        System.out.print("Enter start time (HH:MM): ");
        String startTime = keyboard.nextLine().trim();

        System.out.print("Enter end time (HH:MM): ");
        String endTime = keyboard.nextLine().trim();

        ArrayList<String> neededFeatures = new ArrayList<String>();
        if (!featuresLine.isEmpty())
        {
            for (String feature : featuresLine.split(","))
            {
                String f = feature.trim().toLowerCase();
                if (!f.isEmpty()) neededFeatures.add(f);
            }
        }

        StudentRequest request = new StudentRequest(currentBuilding, neededCapacity,
                                                    neededFeatures, startTime, endTime);

        RoomCandidate candidate =
                studyService.findNearestAvailableRoom(request, reservationService, routingService);

        if (candidate == null)
        {
            System.out.println("No available room found matching your criteria for that time slot.");
        }
        else
        {
            Room bestRoom = candidate.getRoom();
            ArrayList<String> path = routingService.getShortestPath(currentBuilding,
                                                                     bestRoom.getBuildingID());

            System.out.println("\n--- Nearest Available Room ---");
            System.out.println("Room:     " + bestRoom.getRoomID());
            System.out.println("Building: " + bestRoom.getBuildingID());
            System.out.println("Capacity: " + bestRoom.getCapacity());
            System.out.println("Features: " + bestRoom.getFeatures());
            System.out.println("Route:    " + routingService.formatPath(path));
            System.out.printf ("Distance: %.2f miles%n", candidate.getDistance());

            String autoID = "AUTO-" + autoCounter++;
            Reservation autoReservation =
                    new Reservation(autoID, "Student", bestRoom.getRoomID(), startTime, endTime);

            reservationService.makeReservation(bestRoom, autoReservation);
            System.out.println("Reservation Confirmed (ID: " + autoID + ")");
        }
    }

    public static void loadSampleData(StudySpaceService studyService, CampusGraph graph)
    {
        // Buildings
        studyService.addBuilding(new Building("LIB", "Library"));
        studyService.addBuilding(new Building("SCI", "Science Center"));
        studyService.addBuilding(new Building("ENG", "Engineering Hall"));
        studyService.addBuilding(new Building("BUS", "Business Hall"));

        // Graph nodes + edges (distances in miles)
        graph.addBuildingNode("LIB");
        graph.addBuildingNode("SCI");
        graph.addBuildingNode("ENG");
        graph.addBuildingNode("BUS");

        graph.addEdge("LIB", "SCI", 0.8);
        graph.addEdge("SCI", "ENG", 1.0);
        graph.addEdge("LIB", "BUS", 1.5);
        graph.addEdge("BUS", "ENG", 0.7);

        // Rooms
        ArrayList<String> f1 = new ArrayList<>(); f1.add("quiet");
        studyService.addRoom(new Room("R101", "LIB", 4, f1));

        ArrayList<String> f2 = new ArrayList<>(); f2.add("projector");
        studyService.addRoom(new Room("R203", "SCI", 6, f2));

        ArrayList<String> f3 = new ArrayList<>(); f3.add("whiteboard"); f3.add("quiet");
        studyService.addRoom(new Room("R305", "ENG", 8, f3));

        ArrayList<String> f4 = new ArrayList<>(); f4.add("projector"); f4.add("whiteboard");
        studyService.addRoom(new Room("R110", "BUS", 10, f4));

        System.out.println("Sample data loaded:");
        System.out.println("  Buildings : LIB (Library), SCI (Science Center), ENG (Engineering Hall), BUS (Business Hall)");
        System.out.println("  Rooms     : R101/LIB, R203/SCI, R305/ENG, R110/BUS");
        System.out.println("  Graph     : LIB-SCI(0.8mi), SCI-ENG(1.0mi), LIB-BUS(1.5mi), BUS-ENG(0.7mi)");
    }
}