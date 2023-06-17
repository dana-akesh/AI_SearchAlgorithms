package birzeit.cs.astarpathfinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    static Map<String, Vertex> cities = new HashMap<>();
    private static Graph graph = new Graph();

    public static void main(String[] args) {
        try {
            readVerticesFromFile();
            addEdges();
            addAirDistance();
            calculateHeuristicValues(graph, cities.get("hebron")/* specify the destination vertex */);

            // Example usage of A* algorithm
            /*Vertex start = cities.get("ramallah")*//* specify the start vertex *//*;
            Vertex target = cities.get("hebron") *//* specify the target vertex *//*;
            Vertex result = AStar.CalculateAStar(graph, start, target);*/

           /* if (result != null) {
                System.out.println("Path found:");
                AStar.printPath(result);
            } else {
                System.out.println("Path not found.");
            }*/

            // Call BFS algorithm
            Vertex bfsStart = cities.get("ramallah");  // Replace "startCityName" with the actual start city name
            Vertex bfsTarget = cities.get("hebron");    // Replace "goalCityName" with the actual goal city name

            //List<Vertex> path = BFS.calculateBFS(graph, bfsStart, bfsTarget);

            // Print the path
            /*if (!path.isEmpty()) {
                System.out.println("Path found:");
                for (Vertex vertex : path) {
                    System.out.println(vertex.getCityName());
                }
            } else {
                System.out.println("No path found.");
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // read the vertices from the file and create an object for each vertex and adding vertices to graph
    private static void readVerticesFromFile() throws FileNotFoundException {
        File citiesFile = new File("cities.csv");
        Scanner scanner = new Scanner(citiesFile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length < 3)
                continue;
            String cityName = parts[0];
            double latitude = Double.parseDouble(parts[1]);
            double longitude = Double.parseDouble(parts[2]);
            Vertex vertex = new Vertex(cityName, latitude, longitude);
            cities.put(cityName, vertex);
            graph.addVertex(vertex);
        }
    }

    // add edges and adjacent for each city/vertex and adding edges to graph
    private static void addEdges() throws FileNotFoundException {
        File roadsFile = new File("Roads.csv");
        Scanner scanner = new Scanner(roadsFile);
        int n = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if(parts.length < 3)
                continue;
            String sourceCityName = parts[0];
            String destinationCityName = parts[1];
            double distance = Double.parseDouble(parts[2]);
            cities.get(sourceCityName).addAdjacentVertex(cities.get(destinationCityName));
            graph.addEdge(cities.get(sourceCityName), cities.get(destinationCityName), distance);
        }
    }

    private static void addAirDistance() throws FileNotFoundException {
        File roadsFile = new File("AirDistance.csv");
        Scanner scanner = new Scanner(roadsFile);
        int n = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length < 3)
                continue;
            String sourceCityName = parts[0];
            String destinationCityName = parts[1];
            double airDistance = Double.parseDouble(parts[2]);
            graph.addAirEdge(cities.get(sourceCityName), cities.get(destinationCityName), airDistance);
        }
    }

    // calculate heuristic for specific destination
    private static void calculateHeuristicValues(Graph graph, Vertex destination) throws FileNotFoundException {
        File roadsFile = new File("AirDistance.csv");
        Scanner scanner = new Scanner(roadsFile);
        int counter = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length < 3)
                continue;
            if (parts[1].equalsIgnoreCase(destination.getCityName()) && counter <= 15) {
                counter++;
                String sourceCityName = parts[0];
                String destinationCityName = parts[1];
                double airDistance = Double.parseDouble(parts[2]);
                cities.get(destinationCityName).setHeuristicValue(airDistance);
            } else if (counter > 15) {
                break;
            }
        }
    }
}