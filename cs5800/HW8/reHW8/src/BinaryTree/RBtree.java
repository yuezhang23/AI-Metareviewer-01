package BinaryTree;

public class RBtree {
  Node rbRoot;
  Node nil = new Node();

  public RBtree() {
    rbRoot = nil;
  }

  public Node getRoot() {
    return this.rbRoot;
  }

  public void insert(int val) {
    Node newN = new Node(val);
    if (rbRoot == nil) {
      rbRoot = newN;
      rbRoot.parent = nil;
    } else {
      rbRoot.insert(newN);
    }
    newN.left = nil;
    newN.right = nil;
    newN.color = "R";
    rbRoot.updateHeight();
    insertFixUp(newN);
    rbRoot.updateHeight();
  }


  public void insertFixUp(Node z) {
    while (z.parent.color.equals("R")) {
      if (z.parent == z.parent.parent.left) {
        Node y = z.parent.parent.right;
        if (y.color.equals("R")) {
          z.parent.color = "B";
          y.color = "B";
          z.parent.parent.color = "R";
          z= z.parent.parent;
        }
        else {
          if (z == z.parent.right) {
            z = z.parent;
            leftRotate(z);
          }
          z.parent.color = "B";
          z.parent.parent.color = "R";
          rightRotate(z.parent.parent);
        }
      } else {
        Node y = z.parent.parent.left;
        if (y.color.equals("R")) {
          z.parent.color = "B";
          y.color = "B";
          z.parent.parent.color = "R";
          z = z.parent.parent;
        } else {
          if (z == z.parent.left) {
            z = z.parent;
            rightRotate(z);
          }
          z.parent.color = "B";
          z.parent.parent.color = "R";
          leftRotate(z.parent.parent);
        }
      }
    }
    this.rbRoot.color = "B";
  }


  // rotate to keep balance
//  public void insert(BST tree) {
//    rbRoot = tree.root;
//    // right side is longer
//    while (getBalance(rbRoot) < -2) { // -3 -4 -5
//      this.leftRotate(rbRoot);
//    }
//    Node tempR = rbRoot.right;
//    while (getBalance(tempR) > -2) {  // -1 0
//      tempR = tempR.right;
//    }
//    this.leftRotate(tempR);
//
//    // left side is longer
//    while (getBalance(rbRoot) > 2) {  // 3 4 5
//      this.rightRotate(rbRoot);
//    }
//    Node tempL = rbRoot.left;
//    while (getBalance(tempL) < 2) {  // 1 0
//      tempL = tempL.left;
//    }
//    this.rightRotate(tempL);
//  }
//
//
//  public int getBalance(Node t) {
//    if (t == nil) {
//      return 0;
//    }
//    return t.left.height - t.right.height;
//  }


  public void leftRotate(Node z) {
    Node y = z.right;
    z.right = y.left;
    if (y.left != nil) {
      y.left.parent = z;
    }
    y.left = z;
    changeParent(z, y);
    z.parent = y;
  }


  public void rightRotate(Node z) {
    Node y = z.left;
    z.left = y.right;
    if (y.right != nil) {
      y.right.parent = z;
    }
    y.right = z;
    changeParent(z, y);
    z.parent = y;
  }


  // duplicate code
  private void changeParent(Node z, Node y) {
    y.parent = z.parent;
    if (z.parent != nil) {
      if (z.parent.left == z) {
        z.parent.left = y;
      } else {
        z.parent.right = y;
      }
    } else {
      rbRoot = y;
    }
  }

}
