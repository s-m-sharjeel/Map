import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dijkstra {

    public static void calculateShortestPath(Node source) {
        source.setDistance((float) 0);
        Set<Node> settledNodes = new HashSet<>();
        Queue<Node> unsettledNodes = new PriorityQueue<>(Collections.singleton(source));
        while (!unsettledNodes.isEmpty()) {
            Node currentNode = unsettledNodes.poll();
            currentNode.getAdjacentNodes()
                    .entrySet().stream()
                    .filter(entry -> !settledNodes.contains(entry.getKey()))
                    .forEach(entry -> {
                        evaluateDistanceAndPath(entry.getKey(), entry.getValue(), currentNode);
                        unsettledNodes.add(entry.getKey());
                    });
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

    public static void printPaths(List<Node> nodes, Node destination) {
        for (Node node : nodes) {
            if (!node.equals(destination))
                continue;
            StringJoiner joiner = new StringJoiner(" -> ");
            for (Node node1 : node.getShortestPath()) {
                City city = node1.getCity();
                joiner.add(city.getName());
            }
            String path = joiner.toString();
            if (!path.isBlank())
                System.out.printf("%s -> %s : %s%n", path, node.getCity(), node.getDistance());
        }
    }

}
