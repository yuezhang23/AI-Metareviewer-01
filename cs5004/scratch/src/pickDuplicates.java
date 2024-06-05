import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class pickDuplicates {
  private String data;
  private List<String> duplicateList;

  public pickDuplicates(String data) {
    this.data = data;
    this.duplicateList = new ArrayList<>();
  }

  // find number of target data slice that is >= 2.
  public boolean validCount(String dataSlice) {
    String temp = data.replace(".", "");
    boolean isValid = false;

    if (dataSlice == null) {
      throw new IllegalArgumentException("null input");
    }
    if (dataSlice.equals("")
            || temp.length() <= dataSlice.length() || !temp.contains(dataSlice)) {
      return false;
    }

    int start = temp.indexOf(dataSlice);
    for (int i = start + dataSlice.length(); i < temp.length(); i++) {
      if (temp.substring(i).contains(dataSlice)) {
        isValid = true;
        break;
      }
    }
    return isValid;
  }

  public String findLongestRepeated() {
    String temp = data.replace(".", "");
    if (temp.length() < 2) {
      return temp;
    }
    for (int i = 0; i < temp.length(); i ++) {
      for (int j = i + 1; j < temp.length(); j ++) {
        String validEle = temp.substring(i, j);
        if (validCount(validEle)) {
          duplicateList.add(validEle);
        }
      }
    }
    String result = "";
    result += duplicateList.stream().max(Comparator.comparingInt(b -> b.length())).get().toString();
    return result;
  }

}
