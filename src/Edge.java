import java.util.Objects;

class Edge {

    private final Node to;
    private final float weight;

    public Edge(Node to, float weight) {
        this.to = to;
        this.weight = weight;
    }

    public Node getTo() {
        return to;
    }

    public float getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return weight == edge.weight && Objects.equals(to, edge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, weight);
    }
}
