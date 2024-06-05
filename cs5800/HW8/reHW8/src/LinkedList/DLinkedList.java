package LinkedList;

public class DLinkedList {
  protected ListNode head;

  public DLinkedList() {
    head = new ListNode();
  }

  public ListNode search(String s) {
    ListNode result = head;
    while (result != null && !result.word.equals(s)) {
      result = result.next;
    }
    if (result == null) {
      System.out.printf("no such key %s found\n", s);
      return new ListNode();
    } else {
      return result;
    }
  }

  public void addNode(String item, int count) {
    //case1: empty list
    if (head.word.equals("")) {
      head = new ListNode(item, count);
    }
    //case2:  word not exists
    else if (this.search(item).word.equals("")) {
      ListNode newHead = new ListNode(item, count);
      newHead.next = head;
      head.prev= newHead;
      head = newHead;
    } else {
      // case3: word exists, increase count
      this.search(item).addCount(count);
    }
  }

  public void delete(String word) {
    ListNode pos = search(word);
    if (!pos.getKey().equals("")) {
      if (pos != head) {
        pos.prev.next = pos.next;
        pos.next.prev = pos.prev;
        pos.prev = null;
        pos.next = null;
      } else {
        head = head.next;
      }
      System.out.printf("Deleted: Key %s\n", word);
    }
  }

  public void increase(String word) {
    ListNode pos = search(word);
    if (!pos.getKey().equals("")) {
      pos.addCount(1);
      System.out.printf("Increased, %s: %d \n", word, pos.getValue());
    }
  }

  public int getLength() {
    if (head == null) {
      return 0;
    } else {
      return head.getLength();
    }
  }

  public String print() {
    if (head == null) {
      return "";
    } else {
      return head.toString();
    }
  }

}
