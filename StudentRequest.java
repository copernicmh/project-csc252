package studyspace.model;

import java.util.ArrayList;

public class StudentRequest
{
    private String currentBuildingID;
    private int requiredCapacity;
    private ArrayList<String> requiredFeatures;
    private String startTime;
    private String endTime;

    public StudentRequest(String currentBuildingID, int requiredCapacity,
                          ArrayList<String> requiredFeatures,
                          String startTime, String endTime)
    {
        this.currentBuildingID = currentBuildingID;
        this.requiredCapacity = requiredCapacity;
        this.requiredFeatures = requiredFeatures;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getCurrentBuildingID()
    {
        return currentBuildingID;
    }

    public int getRequiredCapacity()
    {
        return requiredCapacity;
    }

    public ArrayList<String> getRequiredFeatures()
    {
        return requiredFeatures;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }
}