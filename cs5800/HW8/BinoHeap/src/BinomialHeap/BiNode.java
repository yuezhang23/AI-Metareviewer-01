package BinomialHeap;

public class BiNode {
  BiNode child;
  BiNode sibling;
  BiNode parent;
  int degree;
  int key;


  public BiNode(int val) {
    this.key = val;
    this.degree = 0;
    this.child = null;
    this.sibling = null;
    this.parent = null;
  }

  public BiNode() {
    this.key = -9999;
    this.degree = -1;
    this.child = null;
    this.sibling = null;
    this.parent = null;
  }

  public void addChild(BiNode t) {
    t.parent = this;
    t.sibling = this.child;
    this.child = t;
    this.degree++;
  }

}
