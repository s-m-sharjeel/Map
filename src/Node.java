import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node implements Comparable<Node> {

    private final City city;
    private Float distance = Float.MAX_VALUE;
    private List<Node> shortestPath = new LinkedList<>();
    private Map<Node, Float> adjacentNodes = new HashMap<>();

    public Node(City city) {
        this.city = city;
    }

    public void addAdjacentNode(Node node, Float weight) {
        adjacentNodes.put(node, weight);
    }

    @Override
    public int compareTo(Node node) {
        return Float.compare(this.distance, node.getDistance());
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public City getCity() {
        return city;
    }

    public Map<Node, Float> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }
}