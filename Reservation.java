//make reservations according to roomID at some time 
public class Reservation {
    private String reservationID;
    private String roomID;         
    private int startTime;         
    private int endTime;

    public Reservation(String reservationID, String roomID, int startTime, int endTime) {
        if (startTime >= endTime) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        this.reservationID = reservationID;
        this.roomID        = roomID;
        this.startTime     = startTime;
        this.endTime       = endTime;
    }

    // ---------- Getters ----------
    public String getID()     { return reservationID; }
    public String getRoomID() { return roomID; }
    public int    getStart()  { return startTime; }
    public int    getEnd()    { return endTime; }

    public String toString() {
        return "ReservationID: " + reservationID +
               ", RoomID: "       + roomID +
               ", Start: "        + formatTime(startTime) +
               ", End: "          + formatTime(endTime);
    }

    // Converts minute-integer to readable HH:MM string
    private String formatTime(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }
}