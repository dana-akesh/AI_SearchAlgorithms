package birzeit.cs.astarpathfinder;

import java.util.*;

class Graph {
    private Map<Vertex, List<Edges>> edges;
    private Map<Vertex, LinkedList<AirEdges>> airEdges = new java.util.HashMap<>();
    public Graph() {
        this.edges = new HashMap<>();
    }

    public void setEdges(Map<Vertex, List<Edges>> edges) {
        this.edges = edges;
    }

    public Map<Vertex, LinkedList<AirEdges>> getAirEdges() {
        return airEdges;
    }

    public void setAirEdges(Map<Vertex, LinkedList<AirEdges>> airEdges) {
        this.airEdges = airEdges;
    }

    void addVertex(Vertex v) {
        edges.putIfAbsent(v, new ArrayList<>());
    }

    void addEdge(Vertex v1, Vertex v2, double distance) {
        v1.addAdjacentVertex(v2);
        edges.get(v1).add(new Edges(v1, v2, distance));
    }
    void addAirEdge(Vertex v1, Vertex v2, double airDistance) {
        v1.addAdjacentVertex(v2);
        airEdges.putIfAbsent(v1, new LinkedList<>());
        airEdges.get(v1).add(new AirEdges(v1, v2, airDistance));
    }
    public Map<Vertex, List<Edges>> getEdges() {
        return edges;
    }
    public  List<Edges> getEdges(Vertex vertex) {
        return edges.get(vertex);
    }
    @Override
    public String toString() {
        return "Graph{" +
                "edges=" + edges +
                ", airEdges=" + airEdges +
                '}';
    }
}