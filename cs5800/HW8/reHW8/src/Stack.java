package SkipList;

import java.util.ArrayList;
import java.util.List;

public class Stack {
  List<Integer> arr;
  int ptr;

  public Stack () {
    this.arr = new ArrayList<>();
    this.ptr = -1;
  }

  public int pop() {
    if (ptr == -1) {
      throw new NullPointerException("no element in the stack");
    }
    ptr = ptr - 1;
    return arr.get(ptr +1);
  }

  public void push(int val) {
    ptr = ptr +1;
    arr.add(ptr, val);
  }












}
