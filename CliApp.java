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

import java.util.ArrayList;
import java.util.Scanner;

public class CliApp
{
    public static void main(String[] args)
    {
        Scanner keyboard = new Scanner(System.in);

        StudySpaceService studyService = new StudySpaceService();
        ReservationService reservationService = new ReservationService();
        CampusGraph graph = new CampusGraph();
        RoutingService routingService = new RoutingService(graph);

        loadSampleData(studyService, graph);

        int choice = 0;

        do
        {
            System.out.println("\nStudySpace Finder");
            System.out.println("1. Add Building");
            System.out.println("2. Add Room");
            System.out.println("3. Search Room");
            System.out.println("4. Remove Room");
            System.out.println("5. Make Reservation");
            System.out.println("6. Cancel Reservation");
            System.out.println("7. Find Nearest Available Room");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");

            if (keyboard.hasNextInt())
            {
                choice = keyboard.nextInt();
                keyboard.nextLine();
            }
            else
            {
                System.out.println("Invalid input.");
                keyboard.nextLine();
                continue;
            }

            switch (choice)
            {
                case 1:
                    addBuilding(keyboard, studyService, graph);
                    break;

                case 2:
                    addRoom(keyboard, studyService);
                    break;

                case 3:
                    searchRoom(keyboard, studyService);
                    break;

                case 4:
                    removeRoom(keyboard, studyService);
                    break;

                case 5:
                    makeReservation(keyboard, studyService, reservationService);
                    break;

                case 6:
                    cancelReservation(keyboard, studyService, reservationService);
                    break;

                case 7:
                    findNearestAvailableRoom(keyboard, studyService, reservationService, routingService);
                    break;

                case 8:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 8);

        keyboard.close();
    }

    public static void addBuilding(Scanner keyboard, StudySpaceService studyService, CampusGraph graph)
    {
        System.out.print("Enter building ID: ");
        String buildingID = keyboard.nextLine();

        System.out.print("Enter building name: ");
        String buildingName = keyboard.nextLine();

        studyService.addBuilding(new Building(buildingID, buildingName));
        graph.addBuildingNode(buildingID);

        System.out.println("Building added.");
    }

    public static void addRoom(Scanner keyboard, StudySpaceService studyService)
    {
        System.out.print("Enter room ID: ");
        String roomID = keyboard.nextLine();

        System.out.print("Enter building ID: ");
        String buildingID = keyboard.nextLine();

        System.out.print("Enter capacity: ");
        int capacity = Integer.parseInt(keyboard.nextLine());

        ArrayList<String> features = new ArrayList<String>();
        System.out.print("Enter features separated by commas: ");
        String featureLine = keyboard.nextLine();

        String[] featureArray = featureLine.split(",");

        for (String feature : featureArray)
        {
            if (!feature.trim().equals(""))
            {
                features.add(feature.trim().toLowerCase());
            }
        }

        Room room = new Room(roomID, buildingID, capacity, features);
        studyService.addRoom(room);

        System.out.println("Room added.");
    }

    public static void searchRoom(Scanner keyboard, StudySpaceService studyService)
    {
        System.out.print("Enter room ID: ");
        String roomID = keyboard.nextLine();

        Room room = studyService.searchRoom(roomID);

        if (room != null)
        {
            System.out.println(room);
        }
        else
        {
            System.out.println("Room not found.");
        }
    }

    public static void removeRoom(Scanner keyboard, StudySpaceService studyService)
    {
        System.out.print("Enter room ID to remove: ");
        String roomID = keyboard.nextLine();

        studyService.removeRoom(roomID);
        System.out.println("Room removed.");
    }

    public static void makeReservation(Scanner keyboard, StudySpaceService studyService,
                                       ReservationService reservationService)
    {
        System.out.print("Enter room ID: ");
        String roomID = keyboard.nextLine();

        Room room = studyService.searchRoom(roomID);

        if (room == null)
        {
            System.out.println("Room not found.");
            return;
        }

        System.out.print("Enter reservation ID: ");
        String reservationID = keyboard.nextLine();

        System.out.print("Enter student name: ");
        String studentName = keyboard.nextLine();

        System.out.print("Enter start time (HH:MM): ");
        String startTime = keyboard.nextLine();

        System.out.print("Enter end time (HH:MM): ");
        String endTime = keyboard.nextLine();

        Reservation reservation =
            new Reservation(reservationID, studentName, roomID, startTime, endTime);

        if (reservationService.makeReservation(room, reservation))
        {
            System.out.println("Reservation confirmed.");
        }
        else
        {
            System.out.println("Time conflict. Reservation denied.");
        }
    }

    public static void cancelReservation(Scanner keyboard, StudySpaceService studyService,
                                         ReservationService reservationService)
    {
        System.out.print("Enter room ID: ");
        String roomID = keyboard.nextLine();

        Room room = studyService.searchRoom(roomID);

        if (room == null)
        {
            System.out.println("Room not found.");
            return;
        }

        System.out.print("Enter reservation start time to cancel (HH:MM): ");
        String startTime = keyboard.nextLine();

        if (reservationService.cancelReservation(room, startTime))
        {
            System.out.println("Reservation canceled.");
        }
        else
        {
            System.out.println("Reservation not found.");
        }
    }

    public static void findNearestAvailableRoom(Scanner keyboard, StudySpaceService studyService,
                                                ReservationService reservationService,
                                                RoutingService routingService)
    {
        ArrayList<String> neededFeatures = new ArrayList<String>();

        System.out.print("Enter current building ID: ");
        String currentBuilding = keyboard.nextLine();

        System.out.print("Enter required capacity: ");
        int neededCapacity = Integer.parseInt(keyboard.nextLine());

        System.out.print("Enter needed features separated by commas: ");
        String featuresLine = keyboard.nextLine();

        System.out.print("Enter start time (HH:MM): ");
        String startTime = keyboard.nextLine();

        System.out.print("Enter end time (HH:MM): ");
        String endTime = keyboard.nextLine();

        String[] featuresArray = featuresLine.split(",");

        for (String feature : featuresArray)
        {
            if (!feature.trim().equals(""))
            {
                neededFeatures.add(feature.trim().toLowerCase());
            }
        }

        StudentRequest request =
            new StudentRequest(currentBuilding, neededCapacity, neededFeatures, startTime, endTime);

        RoomCandidate candidate =
            studyService.findNearestAvailableRoom(request, reservationService, routingService);

        if (candidate == null)
        {
            System.out.println("No available room found.");
        }
        else
        {
            Room bestRoom = candidate.getRoom();
            ArrayList<String> path =
                routingService.getShortestPath(currentBuilding, bestRoom.getBuildingID());

            System.out.println("Nearest available room: " + bestRoom.getRoomID());
            System.out.println("Route: " + routingService.formatPath(path));
            System.out.println("Distance: " + candidate.getDistance());

            Reservation autoReservation =
                new Reservation("AUTO-" + bestRoom.getRoomID(), "Student",
                                bestRoom.getRoomID(), startTime, endTime);

            reservationService.makeReservation(bestRoom, autoReservation);
            System.out.println("Reservation Confirmed");
        }
    }

    public static void loadSampleData(StudySpaceService studyService, CampusGraph graph)
    {
        studyService.addBuilding(new Building("LIB", "Library"));
        studyService.addBuilding(new Building("SCI", "Science Center"));
        studyService.addBuilding(new Building("ENG", "Engineering Hall"));
        studyService.addBuilding(new Building("BUS", "Business Hall"));

        graph.addBuildingNode("LIB");
        graph.addBuildingNode("SCI");
        graph.addBuildingNode("ENG");
        graph.addBuildingNode("BUS");

        graph.addEdge("LIB", "SCI", 0.8);
        graph.addEdge("SCI", "ENG", 1.0);
        graph.addEdge("LIB", "BUS", 1.5);
        graph.addEdge("BUS", "ENG", 0.7);

        ArrayList<String> f1 = new ArrayList<String>();
        f1.add("quiet");
        studyService.addRoom(new Room("R101", "LIB", 4, f1));

        ArrayList<String> f2 = new ArrayList<String>();
        f2.add("projector");
        studyService.addRoom(new Room("R203", "SCI", 6, f2));

        ArrayList<String> f3 = new ArrayList<String>();
        f3.add("whiteboard");
        f3.add("quiet");
        studyService.addRoom(new Room("R305", "ENG", 8, f3));

        ArrayList<String> f4 = new ArrayList<String>();
        f4.add("projector");
        f4.add("whiteboard");
        studyService.addRoom(new Room("R110", "BUS", 10, f4));
    }
}