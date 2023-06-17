package birzeit.cs.astarpathfinder;

class Edges {
    private Vertex source;
    private Vertex destination;
    private double distance;

    public Edges(Vertex source, Vertex destination, double distance) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String toString() {
        return "Edges{" +
                "source=" + source +
                ", destination=" + destination +
                ", distance=" + distance +
                '}';
    }
}
