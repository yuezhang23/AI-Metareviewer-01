package SkipList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkipList {
  List<SK_Node> headLst;
  List<SK_Node> tailLst;
  int size;

  public SkipList() {
    headLst = new ArrayList<>();
    tailLst = new ArrayList<>();
    headLst.add(new SK_Node());
    tailLst.add(new SK_Node(9999));
    headLst.get(0).right = tailLst.get(0);
    tailLst.get(0).left = headLst.get(0);
    size = 1;
  }

  public void newHeadTail() {
    headLst.add(new SK_Node());
    tailLst.add(new SK_Node(9999));
    size += 1;
    headLst.get(size-1).right = tailLst.get(size-1);
    tailLst.get(size-1).left = headLst.get(size-1);
    headLst.get(size-1).down = headLst.get(size-2);
    tailLst.get(size-1).down = tailLst.get(size-2);
    headLst.get(size-2).up = headLst.get(size-1);
    tailLst.get(size-2).up = tailLst.get(size-1);
  }

  public void insert(int val) {
    SK_Node head = headLst.get(0);
    while (true) {
      if (head.val == val) {
        System.out.println("node already exists");
        break;
      } else {
        while (val > head.val) {
          head = head.right;
        }
        head = head.left;
        if (head.down == null) {
          SK_Node n = new SK_Node(val);
          insertAfter(head, n);
          promotion(head, n);
          break;
        } else {
          head = head.down;
        }
      }
    }
  }


  // return the node on the base level
  public SK_Node lookUp(SK_Node t, int val) {
    if (t == null || tailLst.contains(t)) {
      return null;
    }
    if (t.val == val && t.down == null) {
      return t;
    }
    while (val > t.val) {
      t = t.right;
    }
    return lookUp(t.left.down, val);
  }


  public void delete(SK_Node t) {
    if (t != null) {
      t.left.right = t.right;
      t.right.left = t.left;
      t.right = null;
      t.left = null;
    }
    this.delete(t.up);
    this.delete(t.down);
  }


  public void promotion(SK_Node leftND, SK_Node n) {
    while (coin_flip()) {
      if (leftND.up != null) {
        SK_Node nn = new SK_Node(n.val);
        insertAfter(leftND.up, nn);
        nn.down = n;
        n.up = nn;
        n = nn;
        leftND = leftND.up;
      } else if (leftND != headLst.get(size-1)) {
        leftND = leftND.left;
      } else {
        newHeadTail();
        SK_Node nn = new SK_Node(n.val);
        insertAfter(headLst.get(size-1), nn);
        nn.down = n;
        n.up = nn;
        n = nn;
        leftND = headLst.get(size-1);
      }
    }
  }


  public void insertAfter(SK_Node t, SK_Node u) {
    SK_Node t1 = t.right;
    t.right = u;
    u.right = t1;
    u.left = t;
    t1.left = u;
  }

  public boolean coin_flip() {
    return new Random().nextBoolean();
  }

  public void printSkipList() {
    StringBuilder str = new StringBuilder();
//    str.append("-∞");
//    while (start != tailLst.get(0)) {
//      str.append("-->").append(start.val);
//      start = start.right;
//    }
//    str.append("-->").append("∞").append("\n");

    for (int i = 0; i < size; i++) {
      str.append("-∞");
      SK_Node start = headLst.get(i).right;
      while (start != tailLst.get(i)) {
//        if (start == null) {
//          str.append("----");
//        } else {
          str.append("-->").append(start.val);
//        }
        start = start.right;
      }
      str.append("-->").append("∞").append("\n");
    }
    System.out.println(str);
  }

}

