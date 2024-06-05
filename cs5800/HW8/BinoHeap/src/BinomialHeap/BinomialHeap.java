package BinomialHeap;

public class BinomialHeap {
  BiTree head;

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
          if (temp.next.getDegree() != -1 && temp.getDegree() > temp.next.getDegree()) {
            swapNext(temp);
          }
        }
        temp = temp.next;
      }
    }
  }

  public void swapNext(BiTree t) {
    BiTree temp = t.next;
    if (temp != null) {
      if (t.prev == null) {
        head = temp;
      } else {
        t.prev.next = temp;
      }
      if (temp.next != null) {
        temp.next.prev = t;
      }
      temp.prev = t.prev;
      t.next = temp.next;
      temp.next = t;
      t.prev = temp;
    }
  }


  public void merge(BinomialHeap other) {
    BiTree temp = this.head;
    BiTree otherH = other.head;
    while (otherH.getDegree() != -1) {
      while (temp.getDegree() != -1 && temp.getDegree() < otherH.getDegree()) {
        temp = temp.next;
      }
      otherH = otherH.next;
      if (temp.getDegree() == -1) {
        temp.prev.next = otherH.prev;
        otherH.prev.prev = temp.prev;
        temp = otherH.prev;
        temp.next = new BiTree();
        temp.next.prev = temp;
      } else {
        temp.mergeFront(otherH.prev);
      }
      if (temp == this.head) {
        this.head = otherH.prev;
      }
    }
  }

  public void decreaseKey(BiNode t, int k) {
    t.key = k;
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
    if (this.minimum().root.key > val) {
      return null;
    }
    BiNode result = null;
    BiTree te = head;
    while (te.getDegree() != -1) {
      if (te.root.key <= val) {
        result = te.search(val);
        if (result != null) {
          break;
        }
      }
      te = te.next;
    }
    return result;
  }


  public void printHeap() {
    BiTree t = head;
    while (t != null && t.getDegree() >= 0) {
      System.out.println("Tree:");
      t.printTree();
      t = t.next;
    }
  }


}
