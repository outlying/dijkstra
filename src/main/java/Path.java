public class Path {

    private final Main.Graph graph;

    public Path(Main.Graph graph){
        this.graph = graph;
    }

    public float findShortestCost(Integer startNodeIndex, Integer endNodeIndex){

        Main.Graph.Node startNode = graph.getNodeWithIndex(startNodeIndex);
        Main.Graph.Node endNode = graph.getNodeWithIndex(endNodeIndex);



        return 0f;
    }
}
