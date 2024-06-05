package BinaryTree;

public class BST {
  Node root;

  public BST() {
    this.root = null;
  }

  public BST(int[] arr) {
    for (int j : arr) {
      this.insert(j);
    }
  }

  public Node getRoot() {
    return this.root;
  }


  public void insert(int value) {
    Node newN = new Node(value);
    if (root == null) {
      root = newN;
    } else {
      root.insert(newN);
    }
    root.updateHeight();
  }

  public void inOrderWalk() {
    StringBuilder str = new StringBuilder();
    if (root != null) {
      Node min = root.minimum();
      str.append(min.val);
      Node suc = this.successor(min);
      while (suc != null) {
        str.append("->").append(suc.val);
        suc = this.successor(suc);
      }
    }
    System.out.println("print all the node values in the tree in increasing order");
    System.out.println(str);
  }


  public Node minimum() {
    if (root == null) {
      return null;
    } else {
      return root.minimum();
    }
  }

  public Node maximum() {
    if (root == null) {
      return null;
    } else {
      return root.maximum();
    }
  }

  public Node search(int val) {
    if (root == null) {
      throw new NullPointerException("empty tree");
    } else {
      return root.search(val);
    }
  }


  public Node successor(Node t) {
    if (t == null) {
      throw new NullPointerException("value is not in the tree");
    }
    if (t.right != null) {
      return t.right.minimum();
    } else {
      Node p = t.parent;
      while (p != null && p.right == t) {
        t = p;
        p = p.parent;
      }
      return p;
    }
  }

  public Node predecessor(Node t) {
    if (t == null) {
      throw new NullPointerException("value is not in the tree");
    }
    if (t.left != null) {
      return t.left.maximum();
    } else {
      Node p = t.parent;
      while (p != null && p.left == t) {
        t = p;
        p = p.parent;
      }
      return p;
    }
  }

  // doesn't change height
  public void delete(Node z) {
    if (z != null) {
      if (z.left == null) {
        transplant(z, z.right);
      }
      else if (z.right == null) {
        transplant(z, z.left);
      }
      else {
        Node p = z.right.minimum();
        if (p != z.right) {
          transplant(p, p.right);
          p.right = z.right;
          p.right.parent = p;
        }
        transplant(z, p);
        p.left = z.left;
        p.left.parent = p;
      }
      root.updateHeight();
    }
  }

  // u is on the tree
  private void transplant(Node u, Node v) {
    if (u.parent == null) {
      root = v;
    } else if (u == u.parent.left) {
      u.parent.left = v;
    } else {
      u.parent.right = v;
    }
    if (v != null) {
      v.parent = u.parent;
    }
  }


}
