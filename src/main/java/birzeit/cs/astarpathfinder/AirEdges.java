package birzeit.cs.astarpathfinder;

public class AirEdges {
    private Vertex source;
    private Vertex destination;
    private double airDistance;

    public AirEdges(Vertex source, Vertex destination, double airDistance) {
        this.source = source;
        this.destination = destination;
        this.airDistance = airDistance;
    }

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public double getAirDistance() {
        return airDistance;
    }

    public void setAirDistance(double airDistance) {
        this.airDistance = airDistance;
    }
}
