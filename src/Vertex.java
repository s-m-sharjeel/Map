public class Vertex implements Comparable<Vertex> {

    private final City city;
    private Float shortestDistance;
    private LinkedList<Vertex> shortestPath;
//    private final Map<Vertex, Float> adjacentVertices;
//    private Vertex[] adjacentVertices;
    private LinkedList<Vertex> adjacentVertices;
    private int vertexCount = 0;


    public Vertex(City city) {
        this.city = city;
        shortestDistance = Float.MAX_VALUE;
        shortestPath = new LinkedList<>();
        adjacentVertices = new LinkedList<>();
    }

    public void addAdjacentVertex(Vertex vertex) {
        adjacentVertices.add(vertex);
    }

    @Override
    public int compareTo(Vertex vertex) {
        return Float.compare(this.shortestDistance, vertex.getShortestDistance());
    }

    public Float getShortestDistance() {
        return shortestDistance;
    }

    public void setShortestDistance(Float shortestDistance) {
        this.shortestDistance = shortestDistance;
    }

    public City getCity() {
        return city;
    }

    public LinkedList<Vertex> getAdjacentVertices() {
        return adjacentVertices;
    }

    public void setShortestPath(LinkedList<Vertex> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public LinkedList<Vertex> getShortestPath() {
        return shortestPath;
    }

    public void resetNode() {
        shortestDistance = Float.MAX_VALUE;
        shortestPath = new LinkedList<>();
    }

    @Override
    public String toString() {
        return getCity().getName();
    }
}