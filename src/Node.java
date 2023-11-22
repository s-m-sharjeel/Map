import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node implements Comparable<Node> {

    private final City city;
    private Float distance;
    private LinkedList<Node> shortestPath;
    private final Map<Node, Float> adjacentNodes;

    public Node(City city) {
        this.city = city;
        distance = Float.MAX_VALUE;
        shortestPath = new LinkedList<>();
        adjacentNodes = new HashMap<>();
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

    public void setShortestPath(LinkedList<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public LinkedList<Node> getShortestPath() {
        return shortestPath;
    }

    public void resetNode() {
        distance = Float.MAX_VALUE;
        shortestPath = new LinkedList<>();
    }

    @Override
    public String toString() {
        return getCity().getName();
    }
}