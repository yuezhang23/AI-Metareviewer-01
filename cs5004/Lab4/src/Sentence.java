/**
 * This interface represents all the operations to be supported by a sentence
 * of words and punctuations.
 */
public interface Sentence {

  /**
   * Return the number of words contained in a sentence.
   * @return the number of words
   */
  int getNumberOfWords();

  int getNumberOfWordsHelper(int count);

  /**
   * Return the first longest word found in a sentence.
   * @return the longest word
   */
  String longestWord();

  /**
   * Return the sentence with standard format of one space between two words and no
   * space between punctuations. There is no space between the last word and the end
   * of this sentence. If the sentence ends with word, a period will be added as an end.
   * @return the sentence
   */
  String toString();

  String toStringHelper(String accum, String end);

  /**
   * Return a duplicate of a given sentence with the same words and
   * punctuation in the same sequence.
   * @return a copy of the sentence
   */
  Sentence clone();

  /**
   * Return a new sentence after merging the current sentence with the other sentence,
   * with all punctuations preserved.
   * @param other the sentence to merge with current one
   * @return a new sentence after merge
   */
  Sentence merge(Sentence other);

}
