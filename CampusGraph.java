package studyspace.structures;

import java.util.ArrayList;
import java.util.HashMap;

public class CampusGraph
{
    private HashMap<String, ArrayList<Edge>> adjList;

    public CampusGraph()
    {
        adjList = new HashMap<String, ArrayList<Edge>>();
    }

    public void addBuildingNode(String buildingID)
    {
        adjList.putIfAbsent(buildingID, new ArrayList<Edge>());
    }

    public void addEdge(String from, String to, double weight)
    {
        adjList.putIfAbsent(from, new ArrayList<Edge>());
        adjList.putIfAbsent(to, new ArrayList<Edge>());

        adjList.get(from).add(new Edge(to, weight));
        adjList.get(to).add(new Edge(from, weight));
    }

    public HashMap<String, ArrayList<Edge>> getAdjList()
    {
        return adjList;
    }
}