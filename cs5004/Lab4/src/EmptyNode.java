/**
 * This represents an empty node in the Sentence.
 */
public class EmptyNode implements Sentence {

  @Override
  public int getNumberOfWords() {
    return 0;
  }

  @Override
  public int getNumberOfWordsHelper(int count) {
    return count;
  }

  @Override
  public String longestWord() {
    return "";
  }

  @Override
  public String toString() {
    return this.toStringHelper("", "");
  }

  @Override
  public String toStringHelper(String acc, String end) {
    return acc + end;
  }

  @Override
  public Sentence clone() {
    return new EmptyNode();
  }

  @Override
  public Sentence merge(Sentence other) {
    return other;
  }


}
