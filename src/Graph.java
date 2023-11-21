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

}