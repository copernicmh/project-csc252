import java.util.*;

//Chase Jones
//252 StudySpace Finder System
/*
  CampusGraph which represents the university campus as a weighted and undirected graph.
  
  Nodes = buildings
  Edges = walking paths between buildings
  Weight = distance in metres
  
  Data Structure: Adjacency List
  HashMap<String, List<Edge>>
  Each building maps to a list of its neighbours and distances.
 */
public class CampusGraph {

    // ── Inner class that represents a connection to a neighbour ────────────
    static class Edge {
        String neighbour;
        int distance;

        Edge(String neighbour, int distance) {
            this.neighbour = neighbour;
            this.distance = distance;
        }
    }

    // ── Adjacency List: building name → list of edges ───────────────────
    private HashMap<String, List<Edge>> adjList = new HashMap<>();

    // ── Add a building (node) ────────────────────────────────────────────
    public void addBuilding(String building) {
        adjList.putIfAbsent(building, new ArrayList<>());
    }

    // ── Add a walking path (undirected edge) between two buildings ───────
    public void addPath(String from, String to, int distance) {
        adjList.get(from).add(new Edge(to, distance));
        adjList.get(to).add(new Edge(from, distance));
    }

    // ── Print the adjacency list ─────────────────────────────────────────
    public void printGraph() {
        System.out.println("Campus Map (Adjacency List):");
        for (String building : adjList.keySet()) {
            System.out.print("  " + building + " -> ");
            for (Edge e : adjList.get(building)) {
                System.out.print("[" + e.neighbour + " | " + e.distance + "m]  ");
            }
            System.out.println();
        }
    }

    // Main
    public static void main(String[] args) {

        CampusGraph campus = new CampusGraph();

        // Add buildings
        campus.addBuilding("Engineering");
        campus.addBuilding("Library");
        campus.addBuilding("Science");
        campus.addBuilding("StudentHub");

        // Add walking paths (distance in metres)
        campus.addPath("Engineering", "Library", 200);
        campus.addPath("Library", "Science", 150);
        campus.addPath("Engineering", "StudentHub", 300);
        campus.addPath("Science", "StudentHub", 100);

        // Display the graph
        campus.printGraph();
    }
}
