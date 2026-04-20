package studyspace.model;

public class Reservation
{
    private String reservationID;
    private String studentName;
    private String roomID;
    private String startTime;
    private String endTime;

    public Reservation(String reservationID, String studentName, String roomID,
                       String startTime, String endTime)
    {
        this.reservationID = reservationID;
        this.studentName = studentName;
        this.roomID = roomID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getReservationID()
    {
        return reservationID;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public String getRoomID()
    {
        return roomID;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public String toString()
    {
        return "Reservation ID: " + reservationID + ", Student: " + studentName +
               ", Room: " + roomID + ", Start: " + startTime + ", End: " + endTime;
    }
}