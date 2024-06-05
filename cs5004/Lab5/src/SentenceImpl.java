import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * This class implements the SentenceADT class with a collection of methods.
 * @param <T> a placeholder for the actual data type
 */
public class SentenceImpl<T> implements SentenceADT<T> {

  private List<T> sentence;

  /**
   * Construct a sentence by initializing the value of field sentence. The other
   * field is initialized accordingly.
   * @param sentence the value of field sentence
   */
  public SentenceImpl(List<T> sentence) {
    this.sentence = sentence;
  }

  @Override
  public List<T> getSentence() {
    return sentence;
  }

  @Override
  public int getNumberOfWords() {
    if (sentence.isEmpty()) {
      return 0;
    }
    int temp = 0;
    for (T t : sentence) {
      if (getData(t.toString())) {
        temp ++;
      }
    }
    return temp;
  }

  @Override
  public int countPunctuation(Predicate<? super T> predicate) {
    if (sentence.isEmpty()) {
      return 0;
    }
    Stream<T> punList = sentence.stream().filter(predicate);
    return (int) punList.count();
  }

  @Override
  public int getNumberOfWord(Predicate<? super T> predicate) {
    if (sentence.isEmpty()) {
      return 0;
    }
    Stream<T> wordList = sentence.stream().filter(predicate);
    return (int) wordList.count();
  }

  @Override
  public String longestWord() {
    if (sentence.isEmpty()) {
      return "";
    }
    Optional<T> word;
    word = sentence.stream().max(Comparator.comparingInt(str -> str.toString().length()));
    return word.get().toString();
  }

  @Override
  public String toString() {
    if (sentence.isEmpty()) {
      return "";
    }
    StringBuilder result = new StringBuilder(sentence.get(0).toString());
    for (int i = 1; i < sentence.size() - 1; i ++) {
      String data = sentence.get(i).toString();
      if (getData(data)) {
        result.append(" ").append(data);
      } else {
        result.append(data);
      }
    }
    String last = sentence.get(sentence.size() - 1).toString();
    if (getData(last)) {
      result.append(" ").append(last).append(".");
    } else {
      result.append(last);
    }
    return result.toString();
  }

  private boolean getData(String data) {
    if (data.equals("")) {
      return false;
    }
    return data.length() != 1 || Character.isLetter(data.charAt(0));
  }

  @Override
  public SentenceADT<T> clone() {
    List<T> copyList = new LinkedList<>(sentence);
    return new SentenceImpl<>(copyList);
  }

  @Override
  public SentenceADT<T> merge(SentenceADT<T> other) {
    LinkedList<T> newSentence = new LinkedList<>();
    newSentence.addAll(sentence);

    newSentence.addAll(other.getSentence());
    return new SentenceImpl<>(newSentence);
  }

  @Override
  public <R> SentenceImpl<R> map(Function<T,R> converter) {
    LinkedList<R> temp = new LinkedList<>();
    for (T t : sentence) {
      temp.add(converter.apply(t));
    }
    return new SentenceImpl<>(temp);
  }

  @Override
  public String makePigLatin() {
    List<Integer> vowel = List.of(97, 101, 105, 111, 117, 65, 69, 73, 79, 85);
    List<String> temp = new LinkedList<>();
    for (T word : sentence) {
      char begin = word.toString().charAt(0);
      if (!Character.isLetter(begin)) {
        temp.add(word.toString());
      } else if (vowel.contains((int) begin)) {
        temp.add(word.toString() + "WAY");
      } else {
        temp.add(word.toString().substring(1) + begin + "AY");
      }
    }
    SentenceADT<String> pigSentence = new SentenceImpl<>(temp);
    return pigSentence.toString();
  }
}