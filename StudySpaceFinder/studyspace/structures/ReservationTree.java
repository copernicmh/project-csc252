package studyspace.structures;

// Reservation Scheduling - AVL Reservation Tree
// Stores reservations sorted by startTime (String HH:MM, lexicographic = chronological)

import java.util.ArrayList;
import java.util.List;
import studyspace.model.Reservation;

public class ReservationTree
{
    private class Node
    {
        Reservation reservation;
        Node left, right;
        int height;

        Node(Reservation res)
        {
            this.reservation = res;
            this.height = 1;
        }
    }

    private Node root;

    public ReservationTree() {}

    
    public boolean addReservation(Reservation res)
    {
        if (hasOverlap(root, res.getRoomID(), res.getStartTime(), res.getEndTime()))
        {
            System.out.println("Conflict detected for Room " + res.getRoomID()
                    + "! Reservation " + res.getReservationID() + " cannot be scheduled.");
            return false;
        }

        root = insert(root, res);
        System.out.println("Reservation Confirmed: " + res);
        return true;
    }

    /**
     * Cancel a reservation by its ID.
     */
    public boolean cancelReservation(String reservationID)
    {
        if (!exists(root, reservationID))
        {
            System.out.println("Reservation " + reservationID + " not found.");
            return false;
        }
        root = delete(root, reservationID);
        System.out.println("Reservation " + reservationID + " successfully cancelled.");
        return true;
    }

    /**
     * All reservations for a specific room, in chronological order.
     */
    public List<Reservation> getReservationsForRoom(String roomID)
    {
        List<Reservation> result = new ArrayList<>();
        inOrderByRoom(root, roomID, result);
        return result;
    }

    /**
     * All reservations across all rooms, in chronological order.
     */
    public List<Reservation> getAllReservations()
    {
        List<Reservation> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

  
    public String nextAvailableTime(String roomID, String afterTime, int durationMinutes)
    {
        List<Reservation> roomRes = getReservationsForRoom(roomID);
        String candidateStart = afterTime;

        for (Reservation r : roomRes)
        {
            // If candidate slot overlaps this reservation, push past its end
            if (candidateStart.compareTo(r.getEndTime()) < 0
                    && addMinutes(candidateStart, durationMinutes).compareTo(r.getStartTime()) > 0)
            {
                candidateStart = r.getEndTime();
            }
        }
        return candidateStart;
    }

    /** Display all reservations to console. */
    public void displayAll()
    {
        List<Reservation> all = getAllReservations();
        if (all.isEmpty())
        {
            System.out.println("No reservations scheduled.");
        }
        else
        {
            System.out.println("--- All Reservations ---");
            for (Reservation r : all)
                System.out.println(r);
        }
    }


    private boolean hasOverlap(Node node, String roomID, String newStart, String newEnd)
    {
        if (node == null) return false;

        if (node.reservation.getRoomID().equals(roomID))
        {
            if (node.reservation.getStartTime().compareTo(newEnd) < 0
                    && node.reservation.getEndTime().compareTo(newStart) > 0)
                return true;
        }

        // Must traverse both subtrees – tree is ordered by start time across ALL rooms
        return hasOverlap(node.left, roomID, newStart, newEnd)
                || hasOverlap(node.right, roomID, newStart, newEnd);
    }

    //avl

    private Node insert(Node node, Reservation res)
    {
        if (node == null) return new Node(res);

        if (res.getStartTime().compareTo(node.reservation.getStartTime()) < 0)
            node.left = insert(node.left, res);
        else
            node.right = insert(node.right, res);

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return rebalance(node);
    }

    private Node delete(Node node, String reservationID)
    {
        if (node == null) return null;

        if (node.reservation.getReservationID().equals(reservationID))
        {
            if (node.left == null)  return node.right;
            if (node.right == null) return node.left;

            Node successor = findMin(node.right);
            node.reservation = successor.reservation;
            node.right = delete(node.right, successor.reservation.getReservationID());
        }
        else
        {
            node.left  = delete(node.left,  reservationID);
            node.right = delete(node.right, reservationID);
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return rebalance(node);
    }

    private Node findMin(Node node)
    {
        while (node.left != null) node = node.left;
        return node;
    }

    private int height(Node node)       { return node == null ? 0 : node.height; }
    private int getBalance(Node node)   { return node == null ? 0 : height(node.left) - height(node.right); }

    private Node rebalance(Node node)
    {
        int balance = getBalance(node);

        if (balance > 1  && getBalance(node.left)  >= 0) return rotateRight(node);
        if (balance > 1  && getBalance(node.left)  <  0) { node.left  = rotateLeft(node.left);  return rotateRight(node); }
        if (balance < -1 && getBalance(node.right) <= 0) return rotateLeft(node);
        if (balance < -1 && getBalance(node.right) >  0) { node.right = rotateRight(node.right); return rotateLeft(node); }

        return node;
    }

    private Node rotateRight(Node y)
    {
        Node x  = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left  = T2;
        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return x;
    }

    private Node rotateLeft(Node x)
    {
        Node y  = x.right;
        Node T2 = y.left;
        y.left  = x;
        x.right = T2;
        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));
        return y;
    }



    private void inOrder(Node node, List<Reservation> result)
    {
        if (node == null) return;
        inOrder(node.left, result);
        result.add(node.reservation);
        inOrder(node.right, result);
    }

    private void inOrderByRoom(Node node, String roomID, List<Reservation> result)
    {
        if (node == null) return;
        inOrderByRoom(node.left, roomID, result);
        if (node.reservation.getRoomID().equals(roomID))
            result.add(node.reservation);
        inOrderByRoom(node.right, roomID, result);
    }

    private boolean exists(Node node, String reservationID)
    {
        if (node == null) return false;
        if (node.reservation.getReservationID().equals(reservationID)) return true;
        return exists(node.left, reservationID) || exists(node.right, reservationID);
    }

 
    /** Adds minutes to a "HH:MM" string and returns a "HH:MM" string. */
    private String addMinutes(String time, int minutes)
    {
        String[] parts = time.split(":");
        int total = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]) + minutes;
        int h = (total / 60) % 24;
        int m = total % 60;
        return String.format("%02d:%02d", h, m);
    }
}