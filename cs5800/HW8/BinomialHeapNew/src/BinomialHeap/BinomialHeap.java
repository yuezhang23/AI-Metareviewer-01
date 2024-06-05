package BinomialHeap;

public class BinomialHeap {
  BiTree head;
//  List<BiNode> head;

  public BinomialHeap() {
    head = new BiTree();
  }


  public BinomialHeap(int val) {
    head = new BiTree(val);
  }

  public void insert(int val) {
    if (head.getDegree() == -1) {
      head = new BiTree(val);
    } else {
      BinomialHeap h = new BinomialHeap(val);
      this.union(h);
    }
  }

  public BiTree minimum() {
    if (head.getDegree() == -1) {
      return null;
    } else {
      BiTree temp = this.head;
      BiTree minT = temp;
      while (temp.getDegree() != -1) {
        minT = minT.SmallerRoot(temp);
        temp = temp.next;
      }
      return minT;
    }
  }


  public void extractMinimum() {
    BiTree min = this.minimum();
    BiNode cld = min.root.child;
    min.root.child = null;
    BinomialHeap newH = new BinomialHeap();
    while (cld != null) {
      cld.parent = null;
      BiTree newT = new BiTree(cld);
      newH.head.mergeFront(newT);
      newH.head = newT;
      cld = cld.sibling;
    }

    if (min.prev == null && min.next == null) {
      this.head = newH.head;
    } else if (min.prev == null) {
      this.head = head.next;
      head.prev = null;
      min.next = null;
    } else if (min.next == null) {
      min.prev.next = null;
      min.prev = null;
    } else {
      min.prev.next = min.next;
      min.next.prev = min.prev;
      min.next = null;
      min.prev = null;
    }
    this.union(newH);
  }


  public void union(BinomialHeap other) {
    if (head.getDegree() == -1) {
      this.head = other.head;
    } else {
      this.merge(other);
      BiTree temp = this.head;
      while (temp.getDegree() != -1) {
        while (temp.getDegree() == temp.next.getDegree()) {
          temp.unionNext();
        }
        temp = temp.next;
      }
    }
  }


  public void merge(BinomialHeap other) {
    BiTree temp = this.head;
    BiTree temp1 = other.head;
    while (temp1.getDegree() != -1) {
      while (temp.getDegree() != -1 && temp.getDegree() < temp1.getDegree()) {
        temp = temp.next;
      }
      temp1 = temp1.next;
      if (temp.getDegree() == -1) {
        temp.prev.next = temp1.prev;
      } else {
        temp.mergeFront(temp1.prev);
      }

      if (temp == this.head) {
        head = temp1.prev;
      }
    }
  }

  public void decreaseKey(BiNode t, int k) {
    while (t.parent != null && k < t.parent.key) {
      t.key = t.parent.key;
      t.parent.key = k;
      t = t.parent;
    }
  }


  public void delete(BiNode t) {
    if (t != null) {
      this.decreaseKey(t, -9999);
      this.extractMinimum();
    }
  }


  public BiNode search(int val) {
    BiNode result = head.root;
    BiTree te = head;
    while (te != null) {
      if (te.root.key > val) {
        te = te.next;
      } else {
        if (te.root.key == val) {
          result = te.root;
        } else {
          BiNode child = te.root.child;
          while (child != null) {
            if (child.key == val) {
              result = child;
            } else {
              child = child.sibling;
            }
          }
        }
      }
    }
    return result;
  }


  public void printHeap() {
    BiTree t = head;
    while (t != null) {
      t.printTree();
      System.out.println("next tree:");
      t = t.next;
    }
  }


}
