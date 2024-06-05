package BinaryTree;
/**
 * This is a sample code
 */

public class BSTDemo {
  public static void main(String[] args) {
//    int[] arr1 = new int[] {7, 2,4,3,5};
    int[] arr1 = new int[] {12, 7, 14, 6, 21, 5, 13, 12, 19, 15, 16, 11,9, 4, 8, 10, 23, 3};
    System.out.println("The original order of an input list ");
    StringBuilder in = new StringBuilder();
    for (int j : arr1) {
      in.append(j).append(" ");
    }
    System.out.println(in);


    BST bst = new BST(arr1);
    bst.inOrderWalk();
    System.out.println("built Tree Height: " + bst.getRoot().getHeight());
    bst.getRoot().printBST();

    bst.delete(bst.search(6));
    System.out.println("Delete 6:");
    bst.inOrderWalk();
    System.out.println("Tree Height: " + bst.getRoot().getHeight());
    bst.getRoot().printBST();
    System.out.println("\n");

    bst.delete(bst.search(14));
    System.out.println("Delete 14:");
    bst.inOrderWalk();
    System.out.println("Tree Height: " + bst.getRoot().getHeight());
    bst.getRoot().printBST();

    bst.delete(bst.search(9));
    System.out.println("Delete 9:");
    bst.inOrderWalk();
    System.out.println("Tree Height: " + bst.getRoot().getHeight());
    bst.getRoot().printBST();
    System.out.println("\n");

    bst.insert(6);
    System.out.println("after inserting 6:");
    bst.inOrderWalk();
    bst.getRoot().printBST();
    System.out.println("Tree Height: " + bst.getRoot().getHeight());
    System.out.println("min: "+ bst.minimum().getVal());
    System.out.println("max: "+ bst.maximum().getVal());
    System.out.println("successor of 5: "+ bst.successor(bst.search(5)).getVal());
    System.out.println("predecessor of 3: "+ bst.predecessor(bst.search(3)).getVal());

    System.out.println("\n");
    System.out.println("\n");
    System.out.println("\n");
    System.out.println("The array presented as a Red Black Tree");
    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    RBtree rbt = new RBtree();
    for (int i : arr1) {
      rbt.insert(i);
      System.out.println("Insert element: "+ i);
      rbt.getRoot().printBST();
      System.out.println("Tree Height: " + rbt.getRoot().getHeight()+"\n");
    }
    rbt.insert(1);
    rbt.insert(6);
    rbt.getRoot().printBST();
  }
}
