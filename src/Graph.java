import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

class Graph{

    private final LinkedList<Node> nodes;

    public Graph() {
        nodes = new LinkedList<>();
    }

    public LinkedList<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node node){
        nodes.add(node);
    }

//
//    public static void calculateShortestPathFromSource(Node source) {
//        source.setDistance((float) 0);
//
//        Set<Node> settledNodes = new HashSet<>();
//        Set<Node> unsettledNodes = new HashSet<>();
//
//        unsettledNodes.add(source);
//
//        while (!unsettledNodes.isEmpty()) {
//            Node currentNode = getLowestDistanceNode(unsettledNodes);
//            unsettledNodes.remove(currentNode);
//            for (Map.Entry< Node, Float> adjacencyPair:
//                    currentNode.getAdjacentNodes().entrySet()) {
//                Node adjacentNode = adjacencyPair.getKey();
//                Float edgeWeight = adjacencyPair.getValue();
//                if (!settledNodes.contains(adjacentNode)) {
//                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
//                    unsettledNodes.add(adjacentNode);
//                }
//            }
//            settledNodes.add(currentNode);
//        }
//    }
//
//    private static Node getLowestDistanceNode(Set < Node > unsettledNodes) {
//        Node lowestDistanceNode = null;
//        float lowestDistance = Float.MAX_VALUE;
//        for (Node node: unsettledNodes) {
//            float nodeDistance = node.getDistance();
//            if (nodeDistance < lowestDistance) {
//                lowestDistance = nodeDistance;
//                lowestDistanceNode = node;
//            }
//        }
//        return lowestDistanceNode;
//    }
//
//    private static void calculateMinimumDistance(Node evaluationNode, Float edgeWeigh, Node sourceNode) {
//        Float sourceDistance = sourceNode.getDistance();
//        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
//            evaluationNode.setDistance(sourceDistance + edgeWeigh);
//            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
//            shortestPath.add(sourceNode);
//            evaluationNode.setShortestPath(shortestPath);
//        }
//    }

}