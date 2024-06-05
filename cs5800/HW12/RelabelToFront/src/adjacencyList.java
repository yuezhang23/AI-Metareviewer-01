import java.util.ArrayList;
import java.util.List;

public class adjacencyList {
  private Vertex head;
  private List<Vertex> edgeList;

  public adjacencyList(Vertex v) {
    this.head = v;
    this.edgeList = new ArrayList<>();
  }

  public void addEdge(Vertex w) {
    this.edgeList.add(w);
  }
}
