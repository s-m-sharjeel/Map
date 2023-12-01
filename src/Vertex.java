public class Vertex implements Comparable<Vertex> {

    private final City city;
    private Float shortestDistance;
    private LinkedList<Vertex> shortestPath;
    private boolean visited;
    private Vertex[] adjacentVertices;
    private int vertexCount;


    public Vertex(City city) {
        this.city = city;
        visited = false;
        shortestDistance = Float.MAX_VALUE;
        shortestPath = new LinkedList<>();
        vertexCount = 0;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setSize(int size) {
        adjacentVertices = new Vertex[size];
    }

    public void addAdjacentVertex(Vertex vertex) {
        adjacentVertices[vertexCount] = vertex;
        vertexCount++;
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

    public Vertex[] getAdjacentVertices() {
        return adjacentVertices;
    }

    public void setShortestPath(LinkedList<Vertex> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public LinkedList<Vertex> getShortestPath() {
        return shortestPath;
    }

    public void resetVertex() {
        visited = false;
        shortestDistance = Float.MAX_VALUE;
        shortestPath = new LinkedList<>();
    }

    @Override
    public String toString() {
        return getCity().getName();
    }
}