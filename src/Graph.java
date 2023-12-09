public class Graph{

    private int vertexCount;
    private final Vertex[] vertices;    // adjacency list

    public Graph(int size) {
        vertices = new Vertex[size];
        vertexCount = 0;
    }

    /**
     * adds a vertex to the adjacency list | O(1)
     * @param vertex is the vertex to be added
     */
    public void addVertex(Vertex vertex){
        if (isFull())
            throw new RuntimeException("Graph is full!");

        vertices[vertexCount++] = vertex;
    }

    /**
     * adds an edge to the graph | O(V)
     * @param s1 is the name of the source city
     * @param s2 is the name of the destination city
     */
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
     * checks if adjacency list is full | O(1)
     * @return true if full, otherwise return false
     */
    private boolean isFull() {
        return vertexCount == vertices.length;
    }

    /**
     * gets an array of the vertices | O(1)
     * @return an array of all the vertices
     */
    public Vertex[] getVertices() {
        return vertices;
    }

    /**
     * sorts the array using quicksort | O(V*logV)
     */
    public void sort() {
        quickSort(vertices, 0, vertexCount - 1);
    }

    /**
     * quick sorting an array of vertices | O(V*logV)
     * @param arr is the array of vertices
     * @param low is the start index
     * @param high is the last index
     */
    private void quickSort(Vertex[] arr, int low, int high)
    {
        if (low < high) {

            int partitionIndex = partition(arr, low, high);

            quickSort(arr, low, partitionIndex - 1); // sort left sub-array
            quickSort(arr, partitionIndex + 1, high); // sort right sub-array

        }
    }

    /**
     * sorting a partial array with the help of a pivot
     * @param arr is the array of vertices
     * @param low is the start index
     * @param high is the last index
     * @return the new pivot
     */
    private static int partition(Vertex[] arr, int low, int high) {
        Vertex pivot = arr[high]; // choosing the last element as pivot
        int i = (low - 1); // index of smaller element

        for (int j = low; j <= high - 1; j++) {
            if (arr[j].getCity().getName().compareToIgnoreCase(pivot.getCity().getName()) <= 0) {
                i++;
                swap(arr, i, j); // swap elements
            }
        }
        swap(arr, i + 1, high); // swap pivot element
        return (i + 1);
    }

    /**
     * swaps two elements in an array
     * @param arr is the array
     * @param i is the first element
     * @param j is the second element
     */
    private static void swap(Vertex[] arr, int i, int j) {
        Vertex temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < vertexCount; i++) {
            s += vertices[i].toString() + '\n';
        }
        return s;
    }
}