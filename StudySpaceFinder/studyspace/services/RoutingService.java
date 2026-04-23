package studyspace.services;

import studyspace.structures.CampusGraph;
import studyspace.structures.Edge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class RoutingService
{
    private CampusGraph graph;
    private HashMap<String, String> previous;

    public RoutingService(CampusGraph graph)
    {
        this.graph = graph;
        previous = new HashMap<String, String>();
    }

    public HashMap<String, Double> dijkstraDistances(String start)
    {
        HashMap<String, Double> distances = new HashMap<String, Double>();
        previous.clear();

        PriorityQueue<String> pq = new PriorityQueue<String>(new Comparator<String>()
        {
            public int compare(String a, String b)
            {
                return Double.compare(distances.get(a), distances.get(b));
            }
        });

        for (String node : graph.getAdjList().keySet())
        {
            distances.put(node, Double.MAX_VALUE);
            previous.put(node, null);
        }

        if (!distances.containsKey(start))
        {
            return distances;
        }

        distances.put(start, 0.0);
        pq.add(start);

        while (!pq.isEmpty())
        {
            String current = pq.poll();

            for (Edge edge : graph.getAdjList().get(current))
            {
                String neighbor = edge.getDestination();
                double newDistance = distances.get(current) + edge.getWeight();

                if (newDistance < distances.get(neighbor))
                {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, current);
                    pq.remove(neighbor);
                    pq.add(neighbor);
                }
            }
        }

        return distances;
    }

    public ArrayList<String> getShortestPath(String start, String end)
    {
        dijkstraDistances(start);

        ArrayList<String> path = new ArrayList<String>();
        String current = end;

        while (current != null)
        {
            path.add(current);
            current = previous.get(current);
        }

        Collections.reverse(path);

        if (path.size() == 0 || !path.get(0).equals(start))
        {
            return new ArrayList<String>();
        }

        return path;
    }

    public double getDistance(String start, String end)
    {
        HashMap<String, Double> distances = dijkstraDistances(start);

        if (!distances.containsKey(end))
        {
            return Double.MAX_VALUE;
        }

        return distances.get(end);
    }

    public String formatPath(ArrayList<String> path)
    {
        if (path.size() == 0)
        {
            return "No route found";
        }

        String output = "";

        for (int i = 0; i < path.size(); i++)
        {
            output += path.get(i);

            if (i < path.size() - 1)
            {
                output += " -> ";
            }
        }

        return output;
    }
}