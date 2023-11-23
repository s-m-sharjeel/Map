public class Dijkstra {

    public static void calculateShortestPath(Vertex source) {

        source.setShortestDistance((float) 0);
        LinkedList<Vertex> settledVertices = new LinkedList<>();
        Queue<Vertex> unsettledVertices = new Queue<>();
        unsettledVertices.enqueue(source);
        while (!unsettledVertices.isEmpty()) {
            Vertex currentVertex = unsettledVertices.dequeue();
            for (int i = 0; i < currentVertex.getAdjacentVertices().size(); i++) {
                if (!settledVertices.contains(currentVertex.getAdjacentVertices().get(i))) {
                    evaluateDistanceAndPath(currentVertex.getAdjacentVertices().get(i), Map.getDistance(currentVertex.getAdjacentVertices().get(i).getCity(), currentVertex.getCity()), currentVertex);
                    // enqueueing the adjacent nodes of the current node
                    unsettledVertices.enqueue(currentVertex.getAdjacentVertices().get(i));
                }
            }

            settledVertices.add(currentVertex);
        }
    }

    /**
     * updates the distance of the vertex if it is less than the one previously set
     * @param sourceVertex is the source vertex
     * @param adjacentVertex is the destination vertex
     * @param edgeWeight is the distance between them
     */
    private static void evaluateDistanceAndPath(Vertex adjacentVertex, Float edgeWeight, Vertex sourceVertex) {
        // calculates the new distance from the absolute source
        Float newDistance = sourceVertex.getShortestDistance() + edgeWeight;
        if (newDistance < adjacentVertex.getShortestDistance()) {
            adjacentVertex.setShortestDistance(newDistance);
            LinkedList<Vertex> linkedList = sourceVertex.getShortestPath();
            LinkedList<Vertex> list = new LinkedList<>();

            // hard copying list
            for (int i = 0; i < linkedList.size(); i++)
                list.add(linkedList.get(i));

            // adding the source vertex to its own shortest path list
            list.add(sourceVertex);
            adjacentVertex.setShortestPath(list);
        }
    }

    private static LinkedList<Vertex> getPath(Vertex destination) {
        LinkedList<Vertex> paths = destination.getShortestPath();
        paths.add(destination);
        return paths;
    }

    public static LinkedList<Vertex> getShortestPath(Vertex from, Vertex to) {

        calculateShortestPath(from);
        return getPath(to);

    }

    public static void reset(Graph graph) {

        for (Vertex vertex : graph.getVertices())
            vertex.resetNode();

    }

}
