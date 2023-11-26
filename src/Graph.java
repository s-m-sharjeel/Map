public class Graph{

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

    public void addEdge(String s1, String s2) {

        Vertex from = null;
        Vertex to = null;

        for (int i = 0; i < vertexCount; i++) {

            if (vertices[i] == null)
                continue;

            if (vertices[i].getCity().getName().equals(s1))
                from = vertices[i];
            else if (vertices[i].getCity().getName().equals(s2))
                to = vertices[i];

        }

        if (from == null || to == null) {
            if (from == null)
                System.out.println(s1 + " not found!");
            if (to == null)
                System.out.println(s2 + " not found!");
            return;
        }

        from.addAdjacentVertex(to);

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