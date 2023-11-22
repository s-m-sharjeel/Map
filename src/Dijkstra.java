import java.util.*;
import java.util.Map;
import java.util.stream.Stream;

public class Dijkstra {

    private static void calculateShortestPath(Node source) {

        source.setDistance((float) 0);
        LinkedList<Node> settledNodes = new LinkedList<>();
        Queue<Node> unsettledNodes = new PriorityQueue<>();
        unsettledNodes.add(source);
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
            List<Node> list = Stream.concat(sourceNode.getShortestPath().stream(), Stream.of(sourceNode)).toList();
            LinkedList<Node> linkedList = new LinkedList<>();
            for (Node node : list)
                linkedList.add(node);
            adjacentNode.setShortestPath(linkedList);
        }
    }

    private static LinkedList<Node> getPath(Node destination) {
        LinkedList<Node> paths = destination.getShortestPath();
        paths.add(destination);
//        StringJoiner joiner = new StringJoiner(" -> ");
//        for (Node node : destination.getShortestPath()) {
//            City city = node.getCity();
//            paths.add(node);
//            joiner.add(city.getName());
//        }
//        String path = joiner.toString();
//        if (!path.isBlank()) {
////                System.out.printf("%s -> %s : %s%n", path, node.getCity(), node.getDistance());
//            paths.add(destination);
//        }
//        return paths;
        return paths;
    }

    public static LinkedList<Node> getShortestPath(Node from, Node to) {

        calculateShortestPath(from);
        return getPath(to);

    }

    public static void reset(Graph graph) {

        for (Node node : graph.getNodes())
            node.resetNode();

    }

}
