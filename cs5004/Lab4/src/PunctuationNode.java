/**
 * This represents a non-empty node of the Sentence. It contains a punctuation,
 * along with the rest of the sentence.
 */
public class PunctuationNode implements Sentence {
  protected String head;
  protected Sentence next;

  public PunctuationNode(String punctuation, Sentence next) {
    this.head = punctuation;
    this.next = next;
  }

  @Override
  public int getNumberOfWords() {
    return this.getNumberOfWordsHelper(0);
  }

  @Override
  public int getNumberOfWordsHelper(int count) {
    return this.next.getNumberOfWordsHelper(count);
  }

  @Override
  public String longestWord() {
    return this.next.longestWord();
  }

  @Override
  public String toString() {
    return this.next.toStringHelper(head, "");
  }

  @Override
  public String toStringHelper(String acc, String end) {
    return this.next.toStringHelper(acc + head, "");
  }

  @Override
  public Sentence clone() {
    return new PunctuationNode(this.head, this.next.clone());
  }

  @Override
  public Sentence merge(Sentence other) {
    return new PunctuationNode(this.head, this.next.merge(other));
  }

}
