package birzeit.cs.astarpathfinder;

import java.util.*;

public class AStar {
    private Graph graph;

    public AStar() {
        this.graph = Driver.getGraph();
    }

    public String printPath(Vertex target) {
        List<String> cityNames = new ArrayList<>();
        Vertex vertex = target;

        while (vertex != null) {
            cityNames.add(vertex.getCityName());
            vertex = vertex.getParent();
        }

        Collections.reverse(cityNames);

        String path = "";
        for (String cityName : cityNames) {
            System.out.print(cityName + " ");
            path += cityName + " ";
        }
        return path;
    }

    public void calculateAStar(Vertex start, Vertex target) {
        PriorityQueue<Vertex> openList = new PriorityQueue<>();
        Set<Vertex> closedList = new HashSet<>();
        double totalWeight = 0;

        start.setG(0);
        start.setF(start.getG() + start.calculateHeuristic(target));
        openList.add(start);

        while (!openList.isEmpty()) {
            Vertex current = openList.poll();

            if (current == target) {
                return;
            }

            closedList.add(current);

            for (Edges edge : graph.getEdges().get(current)) {
                Vertex neighbor = edge.getDestination();

                if (closedList.contains(neighbor)) {
                    continue;
                }

               totalWeight = current.getG() + edge.getDistance();

                if (!openList.contains(neighbor) || totalWeight < neighbor.getG()) {
                    neighbor.setParent(current);
                    neighbor.setG(totalWeight);
                    neighbor.setF(neighbor.getG() + neighbor.calculateHeuristic(target));

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

    }
    public static double calculateTotalCost(Vertex target) {
        double totalCost = 0;
        Vertex vertex = target;

        while (vertex != null) {
            totalCost += vertex.getG();
            vertex = vertex.getParent();
        }

        return totalCost;
    }

}