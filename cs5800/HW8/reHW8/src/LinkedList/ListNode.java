package LinkedList;

/**
 * This represents the Linked-list Node class.
 */
public class ListNode {
  protected String word;
  protected int val;
  ListNode next;
  ListNode prev;

  public ListNode() {
    this.word = "";
    this.next = null;
    this.prev = null;
    this.val = 0;
  }

  public ListNode(String word) {
    this.word = word;
    this.next = null;
    this.prev = null;
    this.val = 1;
  }

  public ListNode(String word, int count) {
    this.word = word;
    this.next = null;
    this.prev = null;
    this.val = count;
  }

  public int getValue() {
    return this.val;
  }

  public String getKey() {
    return this.word;
  }

  public void addCount(int incr) {
    this.val = val+incr;
  }

  public int getLength() {
    if (this.next == null) {
      return 1;
    } else {
      return 1 + this.next.getLength();
    }
  }

  @Override
  public String toString(){
    if (this.next == null) {
      return "{"+ this.word + ":" + this.val+"}"+".";
    } else {
      return "{"+ this.word + ":" + this.val+"}"+"," + this.next;
    }
  }

  public void printKey() {
    if (!word.equals("")) {
      System.out.printf("The count of key %s is %d\n", this.word, this.val);
    }
  }
}
