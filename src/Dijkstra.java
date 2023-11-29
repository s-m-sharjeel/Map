public class Dijkstra {

    /**
     * finds the shortest path from a vertex to all the other vertices in the graph using BFS
     * @param source is the source vertex
     */
    private static void calculateShortestPath(Vertex source) {

        source.setShortestDistance((float) 0);
        LinkedList<Vertex> settledVertices = new LinkedList<>();
        PriorityQueue<Vertex> unsettledVertices = new PriorityQueue<>();
        unsettledVertices.enqueue(source);  // O(n), because it is sorted at the time of enqueuing in a priority list
        while (!unsettledVertices.isEmpty()) {
            Vertex currentVertex = unsettledVertices.dequeue(); // O(1)
            for (int i = 0; i < currentVertex.getAdjacentVertices().length; i++) {  // O(n)
                Vertex adjacentVertex = currentVertex.getAdjacentVertices()[i]; // O(1) because of the use of array (random access)
                if (!settledVertices.contains(adjacentVertex)) {
                    evaluateDistanceAndPath(adjacentVertex, Map.getDistance(adjacentVertex.getCity(), currentVertex.getCity()), currentVertex);
                    unsettledVertices.enqueue(adjacentVertex);  // enqueueing the adjacent nodes of the current node in constant time
                }
            }
            // order doesn't matter, so insertion in constant time is preferred
            settledVertices.addLast(currentVertex);
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
            LinkedList<Vertex> list = new LinkedList<>(linkedList);

//            // deep copying elements from list
//            for (int i = 0; i < linkedList.size(); i++)
//                list.add(linkedList.get(i));

            // adding the source vertex to its own shortest path list
            list.addLast(sourceVertex);
            adjacentVertex.setShortestPath(list);
        }
    }

    /**
     * gets the shortest path of the destination vertex from the source vertex
     * @param destination is the destination vertex
     * @return the shortest path
     */
    private static LinkedList<Vertex> getPath(Vertex destination) {
        LinkedList<Vertex> paths = destination.getShortestPath();
        paths.addLast(destination);
        return paths;
    }

    /**
     * gets the shortest path from the source to the destination
     * @param from is the source vertex
     * @param to is the destination vertex
     * @return the shortest path
     */
    public static LinkedList<Vertex> getShortestPath(Vertex from, Vertex to) {

        calculateShortestPath(from);
        return getPath(to);

    }

}
