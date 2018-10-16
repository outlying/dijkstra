import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Path {

    private final Graph graph;

    public Path(Set<Main.Triple<Integer, Integer, Float>> triples) {
        this.graph = new Graph(triples);
    }

    public Float findShortestCost(Integer startNodeIndex, Integer endNodeIndex) {

        Graph.Node startNode = graph.getNodeWithIndex(startNodeIndex);
        Graph.Node endNode = graph.getNodeWithIndex(endNodeIndex);

        // Star node has 0 arrival cost, always
        startNode.arrivalCost = 0f;

        calculateNeighborCosts(startNode);

        return 0.2f;
    }

    private void calculateNeighborCosts(Graph.Node origin) {

    }

    /**
     * Full graph of [Node]s with [connection]s
     */
    public final static class Graph {

        private Set<Node> nodes = new HashSet<>();

        private Graph(Set<Main.Triple<Integer, Integer, Float>> triples) {

            for (Main.Triple<Integer, Integer, Float> item : triples) {
                Node leftNode = new Node(item.first);
                Node rightNode = new Node(item.second);
                Float capacity = item.third;

                Connection toRight = new Connection(rightNode, capacity);
                Connection toLeft = new Connection(leftNode, capacity);

                leftNode.addConnection(toRight);
                rightNode.addConnection(toLeft);

                nodes.add(leftNode);
                nodes.add(rightNode);
            }
        }

        public Set<Node> getNodes() {
            return nodes;
        }

        Node getNodeWithIndex(Integer index) throws IllegalStateException {
            for (Node node : nodes) {
                if (node.index.equals(index)) {
                    return node;
                }
            }
            throw new IllegalStateException("Node with index [" + index + "] does not exist");
        }

        /**
         * Single node in [Graph]
         */
        public final static class Node {

            private Integer index;
            private Set<Connection> connections = new HashSet<>();

            private Float arrivalCost = Float.MAX_VALUE;

            private Node(Integer index) {
                this.index = index;
            }

            private boolean addConnection(Connection connection) {
                return connections.add(connection);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Node node = (Node) o;
                return Objects.equals(index, node.index);
            }

            @Override
            public int hashCode() {
                return Objects.hash(index);
            }
        }

        /**
         * Connection
         */
        public final static class Connection {

            private Node targetNode;
            private Float capacity;

            private Connection(Node targetNode, Float capacity) {
                this.targetNode = targetNode;
                this.capacity = capacity;
            }

            public Node getTargetNode() {
                return targetNode;
            }

            public Float getCapacity() {
                return capacity;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Connection that = (Connection) o;
                return Float.compare(that.capacity, capacity) == 0 &&
                        Objects.equals(targetNode, that.targetNode);
            }

            @Override
            public int hashCode() {
                return Objects.hash(targetNode, capacity);
            }
        }
    }
}
