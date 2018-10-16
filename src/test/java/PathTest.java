import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Nie wysyłać do końcowego projektu !!!
 * <p>
 * Testy zawierają grafy testowe do weryfikacji algorytmu, każdy test zawiera inny graf z zestawem danych testowych
 * i wynikami
 * <p>
 * Forma zapisu grafu w komentarzu
 * <p>
 * [1]--(2)--[2]
 * <p>
 * gdzie:
 * [x] oznacza wierzchołek o indeksie x
 * (y) oznacza przepustowość krawędzi, wartość przepustowości y
 */
public class PathTest {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() {
        System.out.println("Running test `" + name.getMethodName() + "`\n");
    }

    @After
    public void tearDown() {
        System.out.println("----------------------------");
    }

    /**
     * Graf:
     * <p>
     * [1]--(5)--[2]
     */
    @Test
    public void simple() {
        Set<Main.Triple<Integer, Integer, Float>> graph = graph(
                triple(1, 2, 5f));

        Collection<Result> correctResults = correctResults(
                indexResult(1, 0f), // Trivial, if start and end  node is the same the we should get 0
                indexResult(2, 1 / 5f));

        verify(graph, correctResults);
    }

    /**
     * Graf:
     * <p>
     * [1]--(5)--[2]--(2)--[3]
     */
    @Test
    public void lessSimple() {
        Set<Main.Triple<Integer, Integer, Float>> graph = graph(
                triple(1, 2, 5f),
                triple(2, 3, 2f));

        Collection<Result> correctResults = correctResults(
                indexResult(2, 0.2f),
                indexResult(3, 3.7f));

        verify(graph, correctResults);
    }

    /**
     * Graf:
     * <p>
     *
     * <code>
     * [1]--(2)--[2]
     * |       / |
     * |      /  |
     * (5) (2)   (10)
     * |  /      |
     * | /       |
     * [3]--(2)--[4]
     * </code>
     */
    @Test
    public void moreComplex() {
        Set<Main.Triple<Integer, Integer, Float>> graph = graph(
                triple(1, 2, 2f),
                triple(1, 3, 5f),
                triple(2, 3, 2f),
                triple(3, 4, 2f),
                triple(2, 4, 10f));

        Collection<Result> correctResults = correctResults(
                indexResult(2, 0.5f),
                indexResult(3, 0.2f),
                indexResult(4, 1.5f));

        verify(graph, correctResults);
    }

    /**
     * Weryfikuje graf
     */
    private void verify(Set<Main.Triple<Integer, Integer, Float>> graph, Collection<Result> results) {
        Path path = factoryPath(graph);

        for (Result result : results) {
            System.out.println("Testing for index " + result.endPointIndex);
            Float calculatedCost = path.findShortestCost(1, result.endPointIndex);
            Float correctCost = result.cost;
            assertEquals(correctCost, calculatedCost);
            System.out.println("Index " + result.endPointIndex + " calculation valid\n");
        }
    }

    /**
     * Budowanie klasy liczącej
     */
    private Path factoryPath(Set<Main.Triple<Integer, Integer, Float>> graph) {
        return new Path(graph);
    }

    /**
     * Pomocnicza dla łatwiejszego wyrażenia rezultatów
     */
    private static Collection<Result> correctResults(Result... result) {
        Set<Result> resultsList = new HashSet<>();
        Collections.addAll(resultsList, result);
        return resultsList;
    }

    /**
     * Pomocnicza aby łatwiej wyrazić pojedynczy rezultat
     */
    private static Result indexResult(Integer endPointIndex, Float cost) {
        return new Result(endPointIndex, cost);
    }

    /**
     * Pomocnicza, ułatwia tworzenie grafu
     */
    @SafeVarargs
    private static Set<Main.Triple<Integer, Integer, Float>> graph(Main.Triple<Integer, Integer, Float>... triples) {
        Set<Main.Triple<Integer, Integer, Float>> triplesList = new HashSet<>();
        Collections.addAll(triplesList, triples);
        return triplesList;
    }

    /**
     * Pomocnicza, ułatwia tworzenie tripletu
     */
    private static Main.Triple<Integer, Integer, Float> triple(Integer x, Integer y, Float p) {
        return new Main.Triple<>(x, y, p);
    }

    /**
     * Pomocnicza klasa
     */
    private static class Result {

        private final Integer endPointIndex;
        private final Float cost;

        public Result(Integer endPointIndex, Float cost) {
            this.endPointIndex = endPointIndex;
            this.cost = cost;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Result result = (Result) o;
            return Objects.equals(endPointIndex, result.endPointIndex) &&
                    Objects.equals(cost, result.cost);
        }

        @Override
        public int hashCode() {
            return Objects.hash(endPointIndex, cost);
        }
    }
}
