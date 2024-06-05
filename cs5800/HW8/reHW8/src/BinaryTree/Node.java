package BinaryTree;

import java.util.ArrayList;
import java.util.List;

public class Node {
  protected int val;
  protected int height;
  protected String color;
  protected Node left;
  protected Node right;
  protected Node parent;


  // For NIL node
  public Node() {
    this.right = null;
    this.left = null;
    this.parent = null;
    this.val = -9999;
    this.height = -1;
    this.color = "B";
  }

  public Node(int val) {
    this.right = null;
    this.left = null;
    this.parent = null;
    this.val = val;
    this.height = 0;
    this.color = "B";
  }



  public int getHeight() {
    return this.height;
  }

  public int getVal() {
    return this.val;
  }

  public Node getLeftChild() {
    return this.left;
  }

  public Node getRightChild() {
    return this.right;
  }

  public Node getParent() {
    return this.parent;
  }


  private boolean isNull(Node t) {
    return t == null || t.val == -9999;
  }

  public void insert(Node t) {
    if (t.val < this.val && isNull(this.left)) {
      this.left = t;
      this.left.parent = this;
    } else if (t.val < this.val) {
      this.left.insert(t);
    } else if (isNull(this.right)) {
      this.right = t;
      this.right.parent = this;
    } else {
      this.right.insert(t);
    }
  }

  public void updateHeight() {
    if (!isNull(this.left) && !isNull(this.right)) {
      this.left.updateHeight();
      this.right.updateHeight();
      this.height = Math.max(this.left.height, this.right.height) + 1;
    }
    else if (isNull(this.left) && !isNull(this.right)) {
      this.right.updateHeight();
      this.height = this.right.height + 1;
    }
    else if (!isNull(this.left)) {
      this.left.updateHeight();
      this.height = this.left.height + 1;
    }
    else {
      this.height = 0;
    }
  }


  public Node minimum() {
    if (isNull(this.left)) {
      return this;
    } else {
      return this.left.minimum();
    }
  }


  public Node maximum() {
    if (isNull(this.right)) {
      return this;
    } else {
      return this.right.maximum();
    }
  }


  public Node search(int t) {
    if (this.val == t) {
      return this;
    } else if (!isNull(this.right) && this.val < t) {
      return this.right.search(t);
    } else if (!isNull(this.left) && this.val > t) {
      return this.left.search(t);
    } else {
      return null;
    }
  }

  public static final String red = "\u001B[31m";
  public static final String reset = "\u001B[0m";

  // helper method for printing
  private List<Node> arrayBST() {
    List<Node> s = new ArrayList<>();
    s.add(this);
    int i = 0; // next empty slot
    int j = i;
    Node t = s.get(j);
    while (j <= Math.pow(2, height + 1) - 2) {
      if (!isNull(t) && t.val != -6666) {
        if (!isNull(t.left) && !isNull(t.right)) {
          s.add(i + 1, t.left);
          s.add(i + 2, t.right);
        } else if (!isNull(t.left) && isNull(t.right)) {
          s.add(i + 1, t.left);
          s.add(i + 2, new Node());
        } else if (isNull(t.left) && !isNull(t.right)) {
          s.add(i + 1, new Node());
          s.add(i + 2, t.right);
        } else {
          s.add(i + 1, new Node());
          s.add(i + 2, new Node());
        }
      } else {
        s.add(i + 1, new Node(-6666));
        s.add(i + 2, new Node(-6666));
      }
      i = i + 2;
      j ++;
      t = s.get(j);
    }
    return s;
  }


  public void printBST() {
    List<Node> s = this.arrayBST();
    int k = 0;
    int h = 0;
    int cnt = (int)Math.pow(2, h);
    int sp = (int) ((Math.pow(2, height + 2))+1);
    while (h <= height +1) {
      StringBuilder str = new StringBuilder();
      int space =(int) ((sp - Math.pow(2, h+1))/ Math.pow(2, h+1));
      if (s.get(k).val == -9999) {
        str.append(" ".repeat(space)).append("NL");
      }
      else if (s.get(k).val == -6666) {
        str.append(" ".repeat(space)).append("  ");
      }
      else {
        if (s.get(k).val < 10) {
          str.append(" ");
        }
        if (s.get(k).color.equals("R")) {
          str.append(" ".repeat(space)).append(red).append(s.get(k).val).append(reset);
        } else {
          str.append(" ".repeat(space)).append(s.get(k).val);
        }
      }
      k++;
      while (cnt > k && k < s.size()) {
        if (s.get(k).val == -9999) {
          str.append(" ".repeat(space*2)).append("NL");
        }
        else if (s.get(k).val == -6666) {
          str.append(" ".repeat(space *2)).append("  ");
        }
        else {
          if (s.get(k).val < 10) {
            str.append(" ");
          }
          if (s.get(k).color.equals("R")) {
            str.append(" ".repeat(space * 2)).append(red).append(s.get(k).val).append(reset);
          } else {
            str.append(" ".repeat(space * 2)).append(s.get(k).val);
          }
        }
        k++;
      }
      System.out.println(str);
      System.out.println("------".repeat((int)(Math.pow(2, height))));
      h ++;
      cnt += (int)Math.pow(2, h);
    }

  }
}
