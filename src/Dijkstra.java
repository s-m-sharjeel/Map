public class Dijkstra {

    /**
     * finds the shortest path from a vertex to all the other vertices in the graph using BFS
     * let V and E be the total number of vertices and edges respectively, then: O(E*logV)
     * @param source is the source vertex
     */
    private static void calculateShortestPath(Vertex source) {

        source.setShortestDistance((float) 0);  // O(1)
        MinHeapTree<Vertex> unsettledVertices = new MinHeapTree<>(270);
        unsettledVertices.insert(source);
        while (!unsettledVertices.isEmpty()) {  // O(V)
            Vertex currentVertex = unsettledVertices.extractMin(); // O(1)
            for (int i = 0; i < currentVertex.getAdjacentVertices().length; i++) {
                Vertex adjacentVertex = currentVertex.getAdjacentVertices()[i]; // O(1) because of the use of array (random access)
                if (!(adjacentVertex.isVisited())) {
                    evaluateDistanceAndPath(adjacentVertex, currentVertex); // O(1)
                    unsettledVertices.insert(adjacentVertex);  // enqueueing the adjacent nodes of the current node in O(logV)
                }
            }
            currentVertex.setVisited(true);
        }
    }

    /**
     * updates the distance of the vertex if it is less than the one previously set | O(1)
     * @param sourceVertex is the source vertex
     * @param adjacentVertex is the destination vertex
     */
    private static void evaluateDistanceAndPath(Vertex adjacentVertex, Vertex sourceVertex) {

        // calculates the new distance from the absolute source
        Float newDistance = sourceVertex.getShortestDistance() + Map.getDistance(adjacentVertex.getCity(), sourceVertex.getCity());

        if (newDistance < adjacentVertex.getShortestDistance()) {

            adjacentVertex.setShortestDistance(newDistance);
            LinkedList<Vertex> sourceVertexShortestPath = sourceVertex.getShortestPath();
            LinkedList<Vertex> list = new LinkedList<>(sourceVertexShortestPath);
            list.addLast(sourceVertex); // adding the source vertex to its own shortest path list in O(1)
            adjacentVertex.setShortestPath(list);

        }
    }

    /**
     * gets the shortest path of the destination vertex from the source vertex | O(1)
     * @param destination is the destination vertex
     * @return the shortest path
     */
    private static LinkedList<Vertex> getPath(Vertex destination) {
        LinkedList<Vertex> paths = destination.getShortestPath();
        paths.addLast(destination);
        return paths;
    }

    /**
     * gets the shortest path from the source to the destination | O(E*logV)
     * @param from is the source vertex
     * @param to is the destination vertex
     * @return the shortest path
     */
    public static LinkedList<Vertex> getShortestPath(Vertex from, Vertex to) {

        calculateShortestPath(from);
        return getPath(to);

    }

}
