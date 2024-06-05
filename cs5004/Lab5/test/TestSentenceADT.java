import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

/**
 * Contains all the unit test for the various types of Sentence.
 */
public class TestSentenceADT {
  private SentenceImpl<String> sentence1;
  private SentenceImpl<String> sentence2;
  private SentenceImpl<String> sentence3;

  @Before
  public void setup() {
    LinkedList<String> test1 = new LinkedList<>();
    LinkedList<String> test2 = new LinkedList<>();
    LinkedList<String> test3 = new LinkedList<>();
    test1.add("what");
    test1.add("a");
    test1.add("day");
    test1.add(",");
    test1.add("same");
    test1.add("as");
    test1.add("usual");
    test1.add(",");
    test1.add("drizzling");
    test1.add("omg");
    test1.add("!");
    test3.add("MAKING");
    test3.add("A");
    test3.add("PIG");
    test3.add("DEAL");
    test3.add("ABOUT");
    test3.add("PIG");
    test3.add("LATIN");
    sentence1 = new SentenceImpl<>(test1);
    sentence2 = new SentenceImpl<>(test2);
    sentence3 = new SentenceImpl<>(test3);
  }

  @Test
  public void testConstructor1() {
    assertEquals("", sentence2.toString());
  }

  @Test
  public void testConstructor2() {
    String expected = "what a day, same as usual, drizzling omg!";
    assertEquals(expected, sentence1.toString());
  }

  @Test
  public void testNumOfWords() {
    assertEquals(8, sentence1.getNumberOfWords());
    assertEquals(0, sentence2.getNumberOfWords());
  }

  @Test
  public void testNumOfPuns() {
    Predicate<String> p = b -> b.length() == 1 && !Character.isLetter(b.charAt(0));
    assertEquals(3, sentence1.countPunctuation(p));
    assertEquals(0, sentence2.countPunctuation(p));
  }

  @Test
  public void testNumOfWordsContain() {
    Predicate<String> p = b -> b.contains("z");
    assertEquals(1, sentence1.getNumberOfWord(p));
    assertEquals(0, sentence2.getNumberOfWord(p));
  }

  @Test
  public void testLongestWord() {
    assertEquals("", sentence2.longestWord());
    assertEquals("drizzling", sentence1.longestWord());
  }

  @Test
  public void testToString() {
    String expected1 = "what a day, same as usual, drizzling omg!";
    String expected2 = "";
    String expected3 = "MAKING A PIG DEAL ABOUT PIG LATIN.";

    assertEquals(expected1, sentence1.toString());
    assertEquals(expected2, sentence2.toString());
    assertEquals(expected3, sentence3.toString());
  }

  @Test
  public void testClone() {
    assertEquals(sentence1.toString(), sentence1.clone().toString());
    assertEquals(sentence2.toString(), sentence2.clone().toString());
  }

  @Test
  public void testMerge() {
    String expected1 = "what a day, same as usual, drizzling omg!"
            + " MAKING A PIG DEAL ABOUT PIG LATIN.";
    String expected2 = "what a day, same as usual, drizzling omg!";
    String expected3 = "MAKING A PIG DEAL ABOUT PIG LATIN.";

    assertEquals(expected1, sentence1.merge(sentence3).toString());
    assertEquals(expected2, sentence1.merge(sentence2).toString());
    assertEquals(expected3, sentence2.merge(sentence3).toString());
  }

  @Test
  public void testPigLatin() {
    String expected = "hatwAY aWAY aydAY, amesAY asWAY usualWAY, rizzlingdAY omgWAY!";
    assertEquals(expected, sentence1.makePigLatin());
  }







}
