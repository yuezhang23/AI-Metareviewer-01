package BinomialHeap;

import java.util.ArrayList;
import java.util.List;

public class BiTree {
  BiNode root;
  BiTree prev;
  BiTree next;
  String red = "\u001B[33m";
  String reset = "\u001B[0m";

  public BiTree() {
    this.root = null;
    this.prev = null;
    this.next = null;
  }

  public BiTree(int val) {
    this.root = new BiNode(val);
    this.prev = null;
    this.next = new BiTree();
    this.next.prev = this;
  }

  public BiTree(BiNode t) {
    this.root = t;
    this.prev = null;
    this.next = new BiTree();
    this.next.prev = this;
  }


  public int getDegree() {
    if (this.root == null) {
      return -1;
    } else {
      return this.root.degree;
    }
  }


  public BiTree SmallerRoot(BiTree tree) {
    if (root.key < tree.root.key) {
      return this;
    } else {
      return tree;
    }
  }


  public void unionNext() {
    BiTree temp = this.next;
    if (temp.root != null && this.root.degree == temp.root.degree) {
      if (this.root.key >= temp.root.key) {
        temp.root.addChild(this.root);
        this.root = temp.root;
        this.next = temp.next;
        temp.next.prev = this;
        temp.next = null;
      } else {
        this.root.addChild(temp.root);
        if (temp.next != null) {
          temp.next.prev = this;
          this.next = temp.next;
          temp.next = null;
        } else {
          this.next = new BiTree();
        }
      }
      temp.prev = null;
    }
  }


  public void mergeFront(BiTree tree) {
    if (this.getDegree() == -1) {
      this.root = tree.root;
    }
    else if (this.getDegree() >= tree.getDegree()) {
      if (this.prev == null) {
        tree.prev = null; // single tree insert
      } else {
        this.prev.next = tree;
        tree.prev = this.prev;
      }
      this.prev = tree;
      tree.next = this;
    } else {
      throw new IllegalArgumentException("can insert bigger degree before");
    }
  }


  public BiNode search(int val) {
    BiNode result = null;
    List<BiNode> s = this.arrayBiTree();
    for (BiNode biNode : s) {
      if (biNode.key == val) {
        result = biNode;
        break;
      }
    }
    return result;
  }



  private List<BiNode> arrayBiTree() {
    List<BiNode> s = new ArrayList<>();
    s.add(root);
    s.add(new BiNode());
    int i = 0; // next empty slot
    int j = i + 1;
    BiNode c = s.get(0);
    while (i < j && c != null) {
      if (c.child != null) {
        s.add(c.child);
        j++;
        BiNode sib = c.child.sibling;
        while (sib != null) {
          s.add(sib);
          j++;
          sib = sib.sibling;
        }
        s.add(new BiNode());
        j ++;
      }
      i ++;
      c = s.get(i);
    }
    return s;
  }

  public void printTree() {
    StringBuilder str = new StringBuilder();
    List<BiNode> s = this.arrayBiTree();
    int j = 0;
    for (int k = 0; k < s.size() - 1; k++) {
      if (s.get(k).degree == -1 && s.get(k + 1) == s.get(j).child) {
        str.append("\n");
        j = k + 1;
      } else if (s.get(k).degree == -1) {
        str.append("  ");
      }
      else {
        str.append("  ".repeat(s.get(k).degree)).append(red).append(s.get(k).key).append(reset).append("  ".repeat(s.get(k).degree));
      }
    }
    System.out.println(str);
  }



}
