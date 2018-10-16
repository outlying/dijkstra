import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws SQLException {

        String connectionString = args[0];
        int index = Integer.parseInt(args[1]);

        Connection connection = DriverManager.getConnection(connectionString);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Gtable");

        Set<Triple<Integer, Integer, Float>> rows = new HashSet<>();

        while (resultSet.next()) {
            int x = resultSet.getInt("x");
            int y = resultSet.getInt("y");
            float p = resultSet.getFloat("p");
            rows.add(new Triple<>(x, y, p));
        }

        Graph graph = new Graph(rows);
    }

    /**
     * Simple triple implementations
     */
    public static class Triple<T, R, U> {

        public final T first;
        public final R second;
        public final U third;

        public Triple(T first, R second, U third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
            return Objects.equals(first, triple.first) &&
                    Objects.equals(second, triple.second) &&
                    Objects.equals(third, triple.third);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second, third);
        }
    }

    /**
     * Full graph of [Node]s with [connection]s
     */
    public final static class Graph {

        private Set<Node> nodes = new HashSet<>();

        public Graph(Set<Triple<Integer, Integer, Float>> triples) {

            for (Triple<Integer, Integer, Float> item : triples) {
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

            private Node(Integer index) {
                this.index = index;
            }

            public Set<Connection> getConnections() {
                return connections;
            }

            private boolean addConnection(Connection connection) {
                return connections.add(connection);
            }

            public Integer getIndex() {
                return index;
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