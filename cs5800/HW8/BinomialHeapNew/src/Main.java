package BinomialHeap;

public class Main {
  public static void main(String[] args) {
    BinomialHeap h = new BinomialHeap();
    h.insert(6);
    h.insert(29);
    h.insert(12);
    h.insert(8);
    h.insert(7);
    h.insert(31);
    h.insert(25);
    h.insert(10);
    h.printHeap();
    BiNode t = h.search(12);
    h.printHeap();

//    System.out.println("after decreasing:" + t.key);
//    h.decreaseKey(t, -9999);
//    h.delete(t);
//    h.printHeap();


//    System.out.println("extract 1th time:");
//    h.extractMinimum();
//    h.printHeap();
//    System.out.println("extract 2th time:");
//    h.extractMinimum();
//    h.printHeap();
//    System.out.println("extract 3th time:");
//    h.extractMinimum();
//    h.printHeap();
//    System.out.println("extract 4th time:");
//    h.extractMinimum();
//    h.printHeap();


  }
}