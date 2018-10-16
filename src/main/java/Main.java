import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws SQLException {

        String connectionString = args[0];
        int endNodeIndex = Integer.parseInt(args[1]);

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

        System.out.print(new Path(rows).findShortestCost(1, endNodeIndex));
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

}