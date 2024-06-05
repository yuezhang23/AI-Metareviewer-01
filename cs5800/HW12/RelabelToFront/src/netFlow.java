import java.util.List;

public class newFlow {
  private Vertex source;
  private Vertex sink;
  private Graph gh;
  private int[][] capacity;
  private int[][] flow;


  public newFlow(int s, int t, Graph gh) {
    this.gh = gh;
    this.source = gh.getVertexOnGraph(s);
    this.sink = gh.getVertexOnGraph(t);
    this.capacity = new int[gh.getSize()][gh.getSize()];
    this.flow = new int[gh.getSize()][gh.getSize()];
  }


  public void addEdge(int u, int v, int cpy) {
    this.gh.addEdge(u,v);
    capacity[u][v]= cpy;
    capacity[v][u]= cpy;
  }



  public void initializePreFlow() {
    source.setHeight(gh.getSize());
    adjacencyList sourceLst = gh.getAdj(source);
    for (Vertex v: sourceLst.getAdjVertex()) {
      flow[source.getVal()][v.getVal()] = capacity[source.getVal()][v.getVal()];
      v.setExcess(capacity[source.getVal()][v.getVal()]);
      source.setExcess(source.getExcess() - v.getExcess());
    }
  }


  public void push(Vertex u, Vertex v) {
    int pushVal = 0;
    if (u.getExcess() > 0
            && flow[u.getVal()][v.getVal()] < capacity[u.getVal()][v.getVal()]
            && u.getHeight() == v.getHeight()+1) {
      pushVal = Math.min(u.getExcess(), capacity[u.getVal()][v.getVal()]-flow[u.getVal()][v.getVal()]);
    }
    if (gh.isEdgeOnGraph(u,v)) {
      flow[u.getVal()][v.getVal()] += pushVal;
    } else {
      flow[v.getVal()][u.getVal()] -= pushVal;
    }
    u.setExcess(u.getExcess() - pushVal);
    v.setExcess(v.getExcess() + pushVal);
  }

  public void discharge(Vertex u) {
    while (u.getExcess() > 0) {
      
    }
  }


  public void relabel(Vertex u) {
    int minH = 2* source.getHeight();
    for (Vertex v : gh.getVertices()) {
      if (u.getExcess() > 0
              && flow[u.getVal()][v.getVal()] < capacity[u.getVal()][v.getVal()]
              && u.getHeight() <= v.getHeight()) {
        minH = Math.min(minH, v.getHeight());
      }
    }
    u.setHeight(minH + 1);
  }



  private List<Vertex> getNeighborList(Vertex u) {
    List<Vertex> result = gh.getAdj(u).getAdjVertex();
    for (Vertex v : gh.getVertices()) {
      if (gh.isEdgeOnGraph(v, u) && !result.contains(v)) {
        result.add(v);
      }
    }
    return result;
  }
}
