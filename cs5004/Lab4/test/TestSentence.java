import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Contains all the unit test for the various types of Sentence.
 */
public class TestSentence {
  Sentence sentence1;
  Sentence sentence2;
  Sentence sentence3;
  Sentence sentence4;
  Sentence sentence5;
  Sentence sentence6;

  @Before
  public void setup() {
    sentence1 = new EmptyNode();

    sentence2 = new WordNode("This", new WordNode("is",
            new WordNode("the", new WordNode("greatest", new EmptyNode()))));

    sentence3 = new WordNode("This", new WordNode("is", new WordNode("dull",
            new PunctuationNode("!", new EmptyNode()))));

    sentence4 = new PunctuationNode("'", new WordNode("talk", new WordNode("to",
            new WordNode("me", new PunctuationNode("'", new PunctuationNode(",",
                    new WordNode("said", new WordNode("she",
                            new PunctuationNode(".", new EmptyNode())))))))));
    sentence5 = new WordNode("death", new EmptyNode());
    sentence6 = new PunctuationNode("!", new EmptyNode());
  }

  @Test
  public void testGetNumberOfWords() {
    assertEquals(0, sentence1.getNumberOfWords());
    assertEquals(4, sentence2.getNumberOfWords());
    assertEquals(3, sentence3.getNumberOfWords());
    assertEquals(5, sentence4.getNumberOfWords());
    assertEquals(1, sentence5.getNumberOfWords());
    assertEquals(0, sentence6.getNumberOfWords());
  }

  @Test
  public void testLongestWord() {
    assertEquals("", sentence1.longestWord());
    assertEquals("greatest", sentence2.longestWord());
    assertEquals("This", sentence3.longestWord());
    assertEquals("talk", sentence4.longestWord());
    assertEquals("death", sentence5.longestWord());
    assertEquals("", sentence6.longestWord());
  }

  @Test
  public void testToString() {
    String expected1 = "";
    String expected2 = "This is the greatest.";
    String expected3 = "This is dull!";
    String expected4 = "' talk to me', said she.";
    String expected5 = "death.";
    String expected6 = "!";

    assertEquals(expected1, sentence1.toString());
    assertEquals(expected2, sentence2.toString());
    assertEquals(expected3, sentence3.toString());
    assertEquals(expected4, sentence4.toString());
    assertEquals(expected5, sentence5.toString());
    assertEquals(expected6, sentence6.toString());
  }

  @Test
  public void testClone() {
    assertEquals(sentence1.toString(), sentence1.clone().toString());
    assertEquals(sentence2.toString(), sentence2.clone().toString());
    assertEquals(sentence3.toString(), sentence3.clone().toString());
    assertEquals(sentence4.toString(), sentence4.clone().toString());
    assertEquals(sentence5.toString(), sentence5.clone().toString());
    assertEquals(sentence6.toString(), sentence6.clone().toString());
  }

  @Test
  public void testMerge() {
    Sentence expected1 = new WordNode("This", new WordNode("is", new WordNode("the",
            new WordNode("greatest", new WordNode("This", new WordNode("is",
                    new WordNode("dull", new PunctuationNode("!", new EmptyNode()))))))));
    Sentence expected2 = new WordNode("death", new PunctuationNode("!", new EmptyNode()));
    Sentence expected3 = new WordNode("death", new PunctuationNode("'",
            new WordNode("talk", new WordNode("to", new WordNode("me",
                    new PunctuationNode("'", new PunctuationNode(",", new WordNode("said",
                            new WordNode("she", new PunctuationNode(".", new EmptyNode()))))))))));

    assertEquals(sentence1.toString(), sentence1.merge(sentence1).toString());
    assertEquals(sentence2.toString(), sentence1.merge(sentence2).toString());
    assertEquals(sentence2.toString(), sentence2.merge(sentence1).toString());
    assertEquals(expected1.toString(), sentence2.merge(sentence3).toString());
    assertEquals(expected2.toString(), sentence5.merge(sentence6).toString());
    assertEquals(expected3.toString(), sentence5.merge(sentence4).toString());

  }
}
