package birzeit.cs.astarpathfinder;

import java.util.*;

public class BFS {
    private Graph graph;
    private ArrayList<Vertex> verticesUI = new ArrayList<>(); // UI

    public BFS() {
        this.graph = Driver.getGraph();
    }

    public ArrayList<Vertex> getVerticesUI() {
        return verticesUI;
    }

    // Calculate the path from start to goal
    public List<Vertex> calculateBFS( Vertex start, Vertex goal) {
        // Initialize a queue for BFS
        Queue<Vertex> queue = new LinkedList<>();
        // Initialize a set to keep track of visited vertices
        Set<Vertex> visited = new HashSet<>();
        // Initialize a map to keep track of parent vertices for reconstructing the path
        Map<Vertex, Vertex> parentMap = new HashMap<>();

        // Enqueue the start vertex
        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();

            // Check if the current vertex is the goal
            if (current == goal) {
                // Goal reached, reconstruct the path and return it
                return reconstructPath(parentMap, start, goal);
            }

            // Iterate over edges of the current vertex
            for (Edges edge : graph.getEdges(current)) {
                Vertex neighbor = edge.getDestination();
                if (!visited.contains(neighbor)) {
                    // Enqueue the neighbor vertex
                    queue.offer(neighbor);
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                }
            }
        }

        // No path found
        return Collections.emptyList();
    }

    // Reconstruct the path from start to goal
    private List<Vertex> reconstructPath(Map<Vertex, Vertex> parentMap, Vertex start, Vertex goal) {
        List<Vertex> path = new ArrayList<>();
        Vertex current = goal;
        while (current != start) {
            path.add(0, current);
            current = parentMap.get(current);
        }
        path.add(0, start);
        return path;
    }

    // Calculate the total cost of the path
    public double calculateTotalCost(List<Vertex> path) {
        double totalCost = 0;
        Vertex previous = null;

        for (Vertex current : path) {
            if (previous != null) {
                double distance = graph.getDistance(previous, current);
                //getDistance(previous, current);
                totalCost += distance;
            }
            previous = current;
        }

        return totalCost;
    }
}