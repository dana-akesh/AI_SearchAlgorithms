package birzeit.cs.astarpathfinder;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Driver extends Application {
     static Map<String, Vertex> cities = new HashMap<>();
     final static Map<String , Map<String, Double>> hValues = new HashMap<>();
    private static Graph graph = new Graph();

    public static void main(String[] args) {
        try {
            readVerticesFromFile();
            addEdges();
            addAirDistance();
            calculateHeuristicValues(graph, cities.get("hebron"));
            Application.launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Vertex> getCities() {
        return cities;
    }

    public static void setCities(Map<String, Vertex> cities) {
        Driver.cities = cities;
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

    public static Graph getGraph() {
        return graph;
    }

    // add edges and adjacent for each city/vertex and adding edges to graph
    private static void addEdges() throws FileNotFoundException {
        File roadsFile = new File("Roads.csv");
        Scanner scanner = new Scanner(roadsFile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length < 3)
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
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length < 3)
                continue;
            String sourceCityName = parts[0];
            String destinationCityName = parts[1];
            double airDistance = Double.parseDouble(parts[2]);
            graph.addAirEdge(cities.get(sourceCityName), cities.get(destinationCityName), airDistance);
            Map<String, Double> innerMap = new HashMap<>();
            innerMap.put(destinationCityName,airDistance);
            hValues.put(sourceCityName, innerMap);
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

    @Override
    public void start(Stage stage) throws IOException {
        MainUI mainUI = new MainUI();
        mainUI.getStage().show();

    }

    private static void addXY() throws FileNotFoundException {
        File XYCities = new File("XYCities.csv");
        Scanner scanner = new Scanner(XYCities);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length < 3)
                continue;
            String cityName = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            cities.get(cityName).setXCoordinate(x);
            cities.get(cityName).setYCoordinate(y);
        }
    }
}