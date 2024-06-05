package SkipList;

public class SkipListDemo {
  public static void main(String[] args) {
    SkipList sl = new SkipList();
    int[] arr1 = new int[] {12, 7, 14, 6, 21, 5, 13, 19, 15, 16, 11,9};
//    int[] arr1 = new int[] {12, 7, 14,16, 11};

    for (int j : arr1) {
      System.out.println("insert: "+ j);
      sl.insert(j);
      sl.printSkipList();
    }
  }
}
