package studyspace.model;

public class Building
{
    private String buildingID;
    private String name;

    public Building(String buildingID, String name)
    {
        this.buildingID = buildingID;
        this.name = name;
    }

    public String getBuildingID()
    {
        return buildingID;
    }

    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return "Building ID: " + buildingID + ", Name: " + name;
    }
}