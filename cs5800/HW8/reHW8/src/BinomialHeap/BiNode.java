package BinomialHeap;

public class BiNode {
  BiNode child;
  BiNode sibling;
  BiNode parent;
  int degreeN;
  int key;

  public BiNode(int val) {
    this.key = val;
    this.degreeN = 0;
    this.child = new BiNode();
    this.sibling = new BiNode();
    this.parent = new BiNode();
  }

  public BiNode() {
    this.key = -9999;
    this.degreeN = -1;
    this.child = null;
    this.sibling = null;
    this.parent = null;
  }

  public BiNode merge(BiNode t1, BiNode t2) {
    if (t1.degreeN == t2.degreeN) {
      throw new IllegalArgumentException("two trees can't merge");
    }
    if (t1.key >= t2.key) {
      t2.addChild(t1);
      return t2;
    } else {
      t1.addChild(t2);
      return t1;
    }
  }

  private void addChild(BiNode t) {
    t.parent = this;
    t.sibling = this.child;
    this.child = t;
    this.degreeN++;
  }



  public BiNode minimum() {
    BiNode temp = this;
    BiNode minT = temp;
    while (temp.degreeN != -1) {
      if (temp.key < minT.key) {
        minT = temp;
      }
      temp = temp.sibling;
    }
    return minT;
  }


}
