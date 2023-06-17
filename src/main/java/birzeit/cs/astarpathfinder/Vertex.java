package birzeit.cs.astarpathfinder;

import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Vertex implements Comparable<Vertex> {
    private final String cityName;
    private final double latitude;
    private final double longitude;
    private double xCoordinate;
    private double yCoordinate;
    private double heuristicValue;
    private List<Vertex> adjacentVertices;
    private Boolean visited;
    private Vertex parent;
    private Circle circle;
    private double g;
    private double f;

    public Vertex(String cityName, double latitude, double longitude) {
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.heuristicValue = 0;
        this.adjacentVertices = new ArrayList<>();
        this.visited = false;
        this.parent = null;
        this.circle = circle;
        this.g = 0;
        this.f = 0;
    }

    public String getCityName() {
        return this.cityName;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public double getHeuristicValue() {
        return heuristicValue;
    }

    public void setHeuristicValue(double heuristicValue) {
        this.heuristicValue = heuristicValue;
    }

    public List<Vertex> getAdjacentVertices() {
        return adjacentVertices;
    }

    public void addAdjacentVertex(Vertex vertex) {
        adjacentVertices.add(vertex);
    }

    public Boolean isVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    public Vertex getParent() {
        return parent;
    }

    public void setParent(Vertex parent) {
        this.parent = parent;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double calculateHeuristic(Vertex target) {
        // Implement your heuristic calculation logic here
        // Use latitude, longitude, xCoordinate, yCoordinate, etc.
        return 0.0;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    @Override
    public String toString() {
        return cityName;
    }

    @Override
    public int compareTo(Vertex o) {
        return Double.compare(this.f, o.f);
    }
}