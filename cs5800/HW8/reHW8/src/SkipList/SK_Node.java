package SkipList;

public class SK_Node {
  protected int val;
  protected SK_Node down;
  protected SK_Node right;
  protected SK_Node left;
  protected SK_Node up;

  // For NIL node
  public SK_Node() {
    this.right = null;
    this.left = null;
    this.down = null;
    this.up = null;
    this.val = -9999;
  }

  public SK_Node(int val) {
    this.right = null;
    this.left = null;
    this.down = null;
    this.up = null;
    this.val = val;
  }


  public SK_Node getUp() {
    return this.up;
  }
  public SK_Node getDown() {
    return this.down;
  }

  public SK_Node getRight() {
    return this.right;
  }

  public SK_Node getLeft() {
    return this.left;
  }

  public double getValue() {
    return this.val;
  }


}
