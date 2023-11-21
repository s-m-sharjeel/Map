import java.util.*;
import java.util.Map;
import java.util.stream.Stream;

public class Dijkstra {

    public static void calculateShortestPath(Node source) {
        source.setDistance((float) 0);
        LinkedList<Node> settledNodes = new LinkedList<>();
        Queue<Node> unsettledNodes = new PriorityQueue<>(Collections.singleton(source));
        while (!unsettledNodes.isEmpty()) {
            Node currentNode = unsettledNodes.poll();
            for (Map.Entry<Node, Float> entry : currentNode.getAdjacentNodes().entrySet()) {
                if (!settledNodes.contains(entry.getKey())) {
                    evaluateDistanceAndPath(entry.getKey(), entry.getValue(), currentNode);
                    unsettledNodes.add(entry.getKey());
                }
            }
            settledNodes.add(currentNode);
        }
    }

    private static void evaluateDistanceAndPath(Node adjacentNode, Float edgeWeight, Node sourceNode) {
        Float newDistance = sourceNode.getDistance() + edgeWeight;
        if (newDistance < adjacentNode.getDistance()) {
            adjacentNode.setDistance(newDistance);
            adjacentNode.setShortestPath(Stream.concat(sourceNode.getShortestPath().stream(), Stream.of(sourceNode)).toList());
        }
    }

    public static LinkedList<Node> getPath(List<Node> nodes, Node destination) {
        LinkedList<Node> paths = new LinkedList<>();
        for (Node node : nodes) {
            if (!node.equals(destination))
                continue;
            StringJoiner joiner = new StringJoiner(" -> ");
            for (Node node1 : node.getShortestPath()) {
                City city = node1.getCity();
                paths.add(node1);
                joiner.add(city.getName());
            }
            String path = joiner.toString();
            if (!path.isBlank()) {
//                System.out.printf("%s -> %s : %s%n", path, node.getCity(), node.getDistance());
                paths.add(destination);
            }
        }
        return paths;
    }

}
