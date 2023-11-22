import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

class Graph{

    int nodeCount;

    private final Node[] nodes;

    public Graph(int size) {
        nodes = new Node[size];
        nodeCount = 0;
    }

    public Node[] getNodes() {
        return nodes;
    }

    public void addNode(Node node){
        if (isFull())
            throw new RuntimeException("Graph is full!");

        nodes[nodeCount++] = node;
    }

    private boolean isFull() {
        return nodeCount == nodes.length;
    }

}