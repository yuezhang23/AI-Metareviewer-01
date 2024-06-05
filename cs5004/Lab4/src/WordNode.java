/**
 * This represents a non-empty node of the Sentence. It contains a word,
 * along with the rest of the sentence.
 */
public class WordNode implements Sentence {
  protected String head;
  protected Sentence next;


  public WordNode(String word, Sentence next) {
    this.head = word;
    this.next = next;
  }

  @Override
  public int getNumberOfWords() {
    return this.next.getNumberOfWordsHelper(1);
  }

  @Override
  public int getNumberOfWordsHelper(int count) {
    return this.next.getNumberOfWordsHelper(count + 1);
  }

  @Override
  public String longestWord() {
    String tempLongest = head;
    if (tempLongest.length() >= this.next.longestWord().length()) {
      return tempLongest;
    } else {
      return this.next.longestWord();
    }
  }

  @Override
  public String toString() {
    return this.next.toStringHelper(head, ".");
  }

  @Override
  public String toStringHelper(String before, String end) {
    return this.next.toStringHelper(before + " " + head, ".");
  }

  @Override
  public Sentence clone() {
    return new WordNode(this.head, this.next.clone());
  }

  @Override
  public Sentence merge(Sentence other) {
    return new WordNode(this.head, this.next.merge(other));
  }
}
