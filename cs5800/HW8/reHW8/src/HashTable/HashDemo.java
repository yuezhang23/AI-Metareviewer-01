package HashTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HashDemo {
  public static void main(String[] args) throws FileNotFoundException {
    File file = new File("res/test.txt");
    Scanner sc = new Scanner(file);
    List<String> string_ls = filter_text(sc);

    // build a hash table for the text
    int m = 500;
    HashTable table = new HashTable(m);
    for (String str : string_ls) {
      table.insert(str, 1);
    }

    table.listAllKeys();
//    table.findKey("butter").printKey();
//    table.findKey("bbbbu").printKey();
//
//    table.increase("butter");
//    table.findKey("butter").printKey();
//    System.out.println(table.getLength(456));
//
//    table.delete("butter");
//    table.findKey("butter").printKey();
//    System.out.println(table.getLength(456));
//    table.delete("bbbbu");
//    table.increase("bbbbu");

    int[] arr = new int[m];
    for (int j =0;j < m; j++) {
      arr[j] = table.getLength(j);
    }

    double variance = computeVariance(arr);
    System.out.println("hash table size: "+ m);
    System.out.printf("sample variance: %.2f \n",variance);
    Histogram hist = new Histogram(arr, tenPercentValue(arr));
    hist.drawHistogram();

  }


  public static int tenPercentValue(int[] arr) {
    int[] lst = new int[arr.length];
    System.arraycopy(arr, 0, lst, 0, arr.length);
    int[] ls = Arrays.stream(lst).sorted().toArray();
    int acc = 0;
    for (int k = arr.length * 9 / 10; k < arr.length; k++) {
      acc += ls[k];
    }
    System.out.println("total lengths of lists: " + Arrays.stream(arr).sum());
    System.out.println("total lengths of 10% longest lists: " +acc);
    return ls[arr.length * 9 / 10 - 1];
  }


  public static double computeVariance(int[] arr) {
    double sum = Arrays.stream(arr).sum();
    double mean = sum/arr.length;
    double variance = 0.00;
    for (int i : arr) {
      variance = variance + Math.pow((i - mean), 2);
    }
    variance = variance / (arr.length -1);
    return variance;
  }


  public static List<String> filter_text(Scanner sc) {
    List<String> string_list = new ArrayList<>();
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (!s.isBlank()) {
        if (s.contains("--")) {
          s = s.replace("--", " ");
        }
        String[] s_lis = s.split(" ");
        for (int i=0;i<s_lis.length;i++) {
          while (s_lis[i].length() != 0 && (!Character.isLetter(s_lis[i].charAt(0))
                  || !Character.isLetter(s_lis[i].charAt(s_lis[i].length() - 1)))) {
            if (!Character.isLetter(s_lis[i].charAt(0))) {
              s_lis[i] = s_lis[i].substring(1);
            }
            if (s_lis[i].length() > 1 && !Character.isLetter(s_lis[i].charAt(s_lis[i].length() - 1))) {
              s_lis[i] = s_lis[i].substring(0, s_lis[i].length() - 1);
            }
          }
          if (!s_lis[i].isBlank() && !s_lis[i].contains("www.")
                  && !s_lis[i].contains("@")) {
            string_list.add(s_lis[i]);
          }
        }
      }
    }
    return string_list;
  }
}
