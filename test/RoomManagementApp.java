//Harmony Barber
//Project 4: StudySpace Finder System
//Room Management System
import java.util.*;
public class RoomManagementApp 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        RoomManager manager = new RoomManager();

        while (true) 
        {
            System.out.println("\n--- Room Management ---");
            System.out.println("1. Add Room");
            System.out.println("2. Search Room");
            System.out.println("3. Remove Room");
            System.out.println("4. Show All Rooms");
            System.out.println("5. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            if (choice == 1) 
            {
                System.out.print("Enter Room ID: ");
                String id = scanner.nextLine();

                System.out.print("Enter Capacity: ");
                int capacity = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter Features: ");
                String features = scanner.nextLine();

                manager.addRoom(id, capacity, features);

            } 
            else if (choice == 2) 
            {
                System.out.print("Enter Room ID: ");
                String id = scanner.nextLine();
                manager.searchRoom(id);

            } 
            else if (choice == 3) 
            {
                System.out.print("Enter Room ID: ");
                String id = scanner.nextLine();
                manager.removeRoom(id);

            } 
            else if (choice == 4) 
            {
                manager.showAllRooms();

            } 
            else if (choice == 5) 
            {
                System.out.println("Exiting...");
                break;

            } 
            else 
            {
                System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}