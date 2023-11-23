class Graph{

    int vertexCount;

    private final Vertex[] vertices;

    public Graph(int size) {
        vertices = new Vertex[size];
        vertexCount = 0;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public void addVertex(Vertex vertex){
        if (isFull())
            throw new RuntimeException("Graph is full!");

        vertices[vertexCount++] = vertex;
    }

    private boolean isFull() {
        return vertexCount == vertices.length;
    }

}