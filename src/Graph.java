class Graph{

    int vertexCount;
    private final Vertex[] vertices;    // adjacency list

    public Graph(int size) {
        vertices = new Vertex[size];
        vertexCount = 0;
    }

    /**
     * adds a vertex to the adjacency list
     * @param vertex is the vertex to be added
     */
    public void addVertex(Vertex vertex){
        if (isFull())
            throw new RuntimeException("Graph is full!");

        vertices[vertexCount++] = vertex;
    }

    /**
     * checks if adjacency list is full
     * @return true if full, otherwise return false
     */
    private boolean isFull() {
        return vertexCount == vertices.length;
    }

    public Vertex[] getVertices() {
        return vertices;
    }
}