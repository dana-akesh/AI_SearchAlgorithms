package birzeit.cs.astarpathfinder;

import java.util.*;

public class AStar {
    private Graph graph;
    private ArrayList<Vertex> verticesUI = new ArrayList<>(); // UI

    public AStar() {
        this.graph = Driver.getGraph();
    }

    public ArrayList<Vertex> getvVrticesUI() {
        return verticesUI;
    }

    //this takes a Vertex object representing the target vertex and prints the path from the target vertex to the start vertex.
    //It also returns the path as a string.
    public String printPath(Vertex target) {
        List<String> cityNames = new ArrayList<>();
        Vertex vertex = target;

        while (vertex != null) {
            cityNames.add(vertex.getCityName()); // Add the city name of the current vertex to the list
            verticesUI.add(vertex); // Add the current vertex to the UI vertex list
            vertex = vertex.getParent(); // Move to the parent vertex
        }

        Collections.reverse(cityNames); // Reverse the list to get the path from start to target vertex

        String path = "";
        for (String cityName : cityNames) {
            System.out.print(cityName + " "); // Print each city name in the path
            path += cityName + " "; // Concatenate the city names to form the path string
        }
        return path; // Return the path string
    }

    //implements the A* algorithm for finding the shortest path from the start vertex to the target vertex.
    public void calculateAStar(Vertex start, Vertex target) {
        PriorityQueue<Vertex> openList = new PriorityQueue<>();
        Set<Vertex> closedList = new HashSet<>();
        double totalWeight = 0;

        start.setG(0); // Set the g-value of the start vertex to 0
        start.setF(start.getG() + start.calculateHeuristic(target)); // Calculate the f-value of the start vertex
        openList.add(start); // Add the start vertex to the open list

        while (!openList.isEmpty()) {
            Vertex current = openList.poll(); // Retrieve the vertex with the lowest f-score from the open list

            if (current == target) {
                return; // Target vertex reached, exit the method
            }

            closedList.add(current); // Mark the current vertex as visited by adding it to the closed list

            for (Edges edge : graph.getEdges().get(current)) {
                Vertex neighbor = edge.getDestination(); // Get the neighboring vertex

                if (closedList.contains(neighbor)) {
                    continue; // Skip the neighbor if it has already been visited
                }

                totalWeight = current.getG() + edge.getDistance(); // Calculate the total weight (g-value) to reach the neighbor

                if (!openList.contains(neighbor)) {
                    neighbor.setParent(current); // Set the current vertex as the parent of the neighbor
                    neighbor.setG(totalWeight); // Update the g-value of the neighbor
                    neighbor.setF(neighbor.getG() + neighbor.calculateHeuristic(target)); // Update the f-value of the neighbor

                    openList.add(neighbor); // Add the neighbor to the open list
                }
            }
        }
    }

//This retrieves the g-value of the target vertex, representing the cost of reaching it from the start vertex.
    public static double calculateTotalCost(Vertex target) {
        return target.getG(); // Return the g-value of the target vertex
    }

}