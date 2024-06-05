package HashTable;
import LinkedList.DLinkedList;
import LinkedList.ListNode;


public class HashTable {
  DLinkedList[] hash_list;
  int size;


  public HashTable(int m) {
    hash_list = new DLinkedList[m];
    for (int i=0;i<m;i++) {
      hash_list[i] = new DLinkedList();
    }
    this.size = m;
  }


  public int Compute_Hash(String s) {
    String key = s.toUpperCase();
    int hash_val = 0;
    int p = 33;
    for (int i = 0; i < key.length(); i++) {
      if (Character.isLetter(key.charAt(i))) {
        hash_val = (hash_val * p + (key.charAt(i) - 'A')) % size;
      }
    }
    return hash_val;
  }


  public void insert(String key, int count) {
    String nKey = key.toUpperCase();
    int index = this.Compute_Hash(nKey);
    hash_list[index].addNode(nKey,count);
  }

 public void delete(String key) {
   String nKey = key.toUpperCase();
   int index = this.Compute_Hash(nKey);
   hash_list[index].delete(nKey);
 }

 public void increase(String key) {
   String nKey = key.toUpperCase();
   int index = this.Compute_Hash(nKey);
   hash_list[index].increase(nKey);
 }

 public ListNode findKey(String key) {
   String nKey = key.toUpperCase();
   int index = this.Compute_Hash(nKey);
   System.out.printf("hash value for %s is %d \n", key, index);
   return hash_list[index].search(nKey);
 }

 public void listAllKeys() {
    StringBuilder str_keys = new StringBuilder();
    for (int i=0; i<size;i++) {
      str_keys.append("hash value: ").append(i).append(", list_length: ").append(hash_list[i].getLength()).append("\n")
              .append(hash_list[i].print()).append(';').append('\n');
    }
    System.out.println(str_keys);
  }


  public int getLength(int i) {
    return hash_list[i].getLength();
  }


}

