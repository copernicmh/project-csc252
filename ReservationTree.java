
// Reservation Scheduling - AVL Reservation Tree

import java.util.ArrayList;
import java.util.List;

public class ReservationTree {

   
    private class Node {
        Reservation reservation;
        Node left, right;
        int height;

        Node(Reservation res) {
            this.reservation = res;
            this.height = 1;
        }
    }

  
    private Node root;
    private RoomManager roomManager; // Integration point with teammate's Room Management

  
    public ReservationTree(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

 
// make a reservation

    public boolean addReservation(Reservation res) {
        // 1. Verify the room exists in the Room Management system
        if (!roomManager.rooms.containsKey(res.getRoomID())) {
            System.out.println("Room " + res.getRoomID() + " does not exist. Reservation failed.");
            return false;
        }

        // 2. Check for time conflicts on this specific room
        if (hasOverlap(root, res.getRoomID(), res.getStart(), res.getEnd())) {
            System.out.println("Conflict detected for Room " + res.getRoomID() +
                               "! Reservation " + res.getID() + " cannot be scheduled.");
            return false;
        }

        // 3. Insert and balance
        root = insert(root, res);
        System.out.println("Reservation Confirmed: " + res);
        return true;
    }

    /**
     * Cancel a reservation by ID.
     */
    public boolean cancelReservation(String reservationID) {
        if (!exists(root, reservationID)) {
            System.out.println("Reservation " + reservationID + " not found.");
            return false;
        }
        root = delete(root, reservationID);
        System.out.println("Reservation " + reservationID + " successfully cancelled.");
        return true;
    }

    /**
     * Returns all reservations for a specific room in chronological order.
     * Useful for displaying a room's schedule.
     */
    public List<Reservation> getReservationsForRoom(String roomID) {
        List<Reservation> result = new ArrayList<>();
        inOrderByRoom(root, roomID, result);
        return result;
    }

    /**
     * Returns ALL reservations across all rooms in chronological order.
     */
    public List<Reservation> getAllReservations() {
        List<Reservation> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }
// next available room method 
    public int nextAvailableTime(String roomID, int afterTime, int durationMinutes) {
        List<Reservation> roomRes = getReservationsForRoom(roomID);
        int candidateStart = afterTime;

        for (Reservation r : roomRes) {
            
            if (candidateStart < r.getEnd() && candidateStart + durationMinutes > r.getStart()) {
                candidateStart = r.getEnd();
            }
        }
        return candidateStart;
    }

    /**
     * Display all reservations to console. (might not need after testing)
     */
    public void displayAll() {
        List<Reservation> all = getAllReservations();
        if (all.isEmpty()) {
            System.out.println("No reservations scheduled.");
        } else {
            System.out.println("--- All Reservations ---");
            for (Reservation r : all) {
                System.out.println(r);
            }
        }
    }
    //  CONFLICT DETECTION: (StartA < EndB) AND (EndA > StartB)

    private boolean hasOverlap(Node node, String roomID, int newStart, int newEnd) {
        if (node == null) return false;

        // Only flag a conflict if it's the same room
        if (node.reservation.getRoomID().equals(roomID)) {
            if (node.reservation.getStart() < newEnd &&
                node.reservation.getEnd()   > newStart) {
                return true;
            }
        }

        // Traverse both subtrees — necessary because tree is ordered by
        // start time across ALL rooms, so same-room conflicts can appear anywhere
        return hasOverlap(node.left,  roomID, newStart, newEnd) ||
               hasOverlap(node.right, roomID, newStart, newEnd);
    }
    //avl 
    private Node insert(Node node, Reservation res) {
        // 1. Standard BST insert by start time
        if (node == null) return new Node(res);

        if (res.getStart() < node.reservation.getStart()) {
            node.left = insert(node.left, res);
        } else {
            node.right = insert(node.right, res);
        }

        // 2. Update height
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // 3. AVL rotations
        return rebalance(node);
    }
    private Node delete(Node node, String reservationID) {
        if (node == null) return null;

        if (node.reservation.getID().equals(reservationID)) {
            // Node to delete found
            if (node.left == null)  return node.right;
            if (node.right == null) return node.left;

            // Two children: replace with in-order successor (smallest in right subtree)
            Node successor = findMin(node.right);
            node.reservation = successor.reservation;
            node.right = delete(node.right, successor.reservation.getID());
        } else {
            // Search both subtrees (reservation IDs can be anywhere in the tree)
            node.left  = delete(node.left,  reservationID);
            node.right = delete(node.right, reservationID);
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return rebalance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private Node rebalance(Node node) {
        int balance = getBalance(node);

        // Left-Left case
        if (balance > 1 && getBalance(node.left) >= 0)
            return rotateRight(node);

        // Left-Right case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right-Right case
        if (balance < -1 && getBalance(node.right) <= 0)
            return rotateLeft(node);

        // Right-Left case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node; // already balanced
    }

    private Node rotateRight(Node y) {
        Node x  = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left  = T2;

        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y  = x.right;
        Node T2 = y.left;

        y.left  = x;
        x.right = T2;

        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));

        return y;
    }
    private void inOrder(Node node, List<Reservation> result) {
        if (node == null) return;
        inOrder(node.left, result);
        result.add(node.reservation);
        inOrder(node.right, result);
    }

    private void inOrderByRoom(Node node, String roomID, List<Reservation> result) {
        if (node == null) return;
        inOrderByRoom(node.left, roomID, result);
        if (node.reservation.getRoomID().equals(roomID)) {
            result.add(node.reservation);
        }
        inOrderByRoom(node.right, roomID, result);
    }

    private boolean exists(Node node, String reservationID) {
        if (node == null) return false;
        if (node.reservation.getID().equals(reservationID)) return true;
        return exists(node.left, reservationID) || exists(node.right, reservationID);
    }
}