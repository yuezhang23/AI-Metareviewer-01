import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This interface represents all the operations to be supported by a sentence
 * of words and punctuations.
 * @param <T> a placeholder for the actual data type
 */
public interface SentenceADT<T> {

  /**
   * Return the number of words in a sentence.
   * @return the number of words
   */
  int getNumberOfWords();

  /**
   * Return the number of punctuations in a sentence.
   * @param predicate the predicate to filter and find count of punctuation
   * @return the number of punctuations in the sentence
   */
  int countPunctuation(Predicate<? super T> predicate);

  /**
   * Return the number of words, containing letter z, in a sentence.
   * @param predicate the predicate to filter and find count of specific words
   * @return the number of words that contain z.
   */
  int getNumberOfWord(Predicate<? super T> predicate);

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

  /**
   * Return a duplicate of a given sentence with the same words and
   * punctuation in the same sequence.
   * @return a copy of the sentence
   */
  SentenceADT<T> clone();

  /**
   * Return a new sentence after merging the current sentence with the other sentence,
   * with all punctuations preserved.
   * @param other the sentence to merge with current one
   * @return a new sentence after merge
   */
  SentenceADT<T> merge(SentenceADT<T> other);

  /**
   * Convert the type of current data to a new type of data as
   * a type of Sentence by mapping convertor.
   * @param converter the function to make mapping
   * @param <R> the converted type
   * @return a new Sentence
   */
  <R> SentenceADT<R> map(Function<T, R> converter);

  List<T> getSentence();

  /**
   * Return a string sentence using pigLatin language.
   * @return target Sentence instance
   */
  String makePigLatin();
}
