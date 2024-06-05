package cs5004.questionnaire;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test all the methods in the Question Interface.
 */
public class TestQuestion {
  private Question que1;
  private Question que2;
  private Question que3;
  private Question que4;
  private Question que5;
  private Question que6;
  private Question que7;

  @Before
  public void setup() {
    que1 = new YesNo("hello, is this right?", true);
    que2 = new YesNo("you, is this wrong?", false);
    que3 = new ShortAnswer("Bob, name your favourite color", true);
    que4 = new ShortAnswer("name pet sounds", false);
    que5 = new Likert("Taylor is great", true);
    que6 = new Likert("folk music is the best", false);
    que7 = new Likert("you will survive", true);
    que1.answer("yes");
    que2.answer("");
    que4.answer("woo~ or hmm");
    que5.answer("Strongly AGREE");
    que6.answer("");
    que7.answer("agree");
  }

  @Test
  public void tesConstructor() {
    // status : true
    assertEquals("hello, is this right?",que1.getPrompt());
    assertTrue(que1.isRequired());

    // status : false
    assertEquals("folk music is the best",que6.getPrompt());
    assertFalse(que6.isRequired());
  }

  @Test (expected = IllegalArgumentException.class)
  public void tesInvalidConstructor() {
    new YesNo("", true);
  }

  @Test (expected = IllegalArgumentException.class)
  public void tesInvalidConstructor1() {
    new ShortAnswer("", false);
  }

  @Test (expected = IllegalArgumentException.class)
  public void tesInvalidConstructor2() {
    new Likert(null, false);
  }

  @Test (expected = IllegalArgumentException.class)
  public void tesInvalidConstructor3() {
    new Likert(null, true);
  }

  @Test
  public void testPrompt() {
    // status = false
    String expected = "name pet sounds";
    assertEquals(expected, que4.getPrompt());

    // status = true
    String expected1 = "Taylor is great";
    assertEquals(expected1, que5.getPrompt());
  }

  @Test
  public void testIsRequired() {
    // status = false
    assertFalse(que4.isRequired());

    // status = true
    assertTrue(que5.isRequired());
  }

  @Test
  public void testGetAnswer() {
    // status : true  answer : non-empty
    assertEquals("yes", que1.getAnswer());

    // status : true  answer : empty
    assertEquals("", que3.getAnswer());

    // status : false  answer : empty
    assertEquals("", que6.getAnswer());

    // status : false  answer : non-empty
    assertEquals("woo~ or hmm", que4.getAnswer());
  }

  @Test
  public void testAnswerYN() {
    // Q: YesNo status : true
    que1.answer("yEs");
    assertEquals("yEs", que1.getAnswer());
    que1.answer("YES");
    assertEquals("YES", que1.getAnswer());
    que1.answer("no");
    assertEquals("no", que1.getAnswer());
    que1.answer("nO");
    assertEquals("nO", que1.getAnswer());

    // Q: YesNo status : false
    que2.answer("yEs");
    assertEquals("yEs", que2.getAnswer());
    que2.answer("NO");
    assertEquals("NO", que2.getAnswer());
    que2.answer("");
    assertEquals("", que2.getAnswer());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAnswerYN() {
    que1.answer(null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAnswerYN1() {
    que1.answer("");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAnswerYN2() {
    que1.answer("yes or no");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAnswerYN3() {
    que2.answer(null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAnswerYN4() {
    que2.answer("do not know");
  }

  @Test
  public void testShortAnswer() {
    String data = "it is hard to tell since I've found many colors unique and"
            + " beautiful. Also, the more I observe the color I love, the more"
            + "I'll get used to its character and this may end up losing passion"
            + "for it. Each color can be different given different context.";
    que3.answer(data);
    assertEquals(data, que3.getAnswer());
    assertTrue(que3.getAnswer().length() <= 280);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidShortAns1() {
    que3.answer(null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidShortAns2() {
    que3.answer("");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidShortAns3() {
    que4.answer(null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidShortAns4() {
    String data = "it is hard to tell since I've found many colors unique and"
            + "beautiful. Also, the more I observe the color I love, the more I'll"
            + "get used to its character and this may end up losing passion for it."
            + "Each color can be different given different context. So it is more"
            + "important to use color in the right way.";
    que3.answer(data);
  }

  @Test
  public void testLikertAnswer() {
    que5.answer("Strongly Disagree");
    assertEquals("Strongly Disagree", que5.getAnswer());

    que6.answer("Disagree");
    assertEquals("Disagree", que6.getAnswer());

    que7.answer("Neither Agree Nor Disagree");
    assertEquals("Neither Agree Nor Disagree", que7.getAnswer());

    que5.answer("Strongly Agree");
    assertEquals("Strongly Agree", que5.getAnswer());

    que7.answer("Agree");
    assertEquals("Agree", que7.getAnswer());

    que6.answer("DISAGREE");
    assertEquals("DISAGREE", que6.getAnswer());

    que5.answer("StrONGly AGrEE");
    assertEquals("StrONGly AGrEE", que5.getAnswer());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidLIKERTAns1() {
    que5.answer(null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidLIKERTAns2() {
    que6.answer(null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidLIKERTAns3() {
    que7.answer("");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidLIKERTAns4() {
    que7.answer("I won't agree more");
  }

  @Test
  public void testCopy1() {
    Question copy1 = que1.copy();
    assertEquals("hello, is this right?", copy1.getPrompt());
    assertTrue(copy1.isRequired());
    assertEquals("yes", copy1.getAnswer());

    // copy and current are independent
    que1.answer("no");
    assertNotEquals(copy1.getAnswer(), que1.getAnswer());
  }

  @Test
  public void testCopy2() {
    Question copy = que4.copy();

    assertEquals("name pet sounds", copy.getPrompt());
    assertFalse(copy.isRequired());
    assertEquals("woo~ or hmm", copy.getAnswer());

    // copy and current are independent
    que4.answer("just woo");
    assertNotEquals(copy.getAnswer(), que4.getAnswer());
  }

  @Test
  public void testCopy3() {
    Question copy = que6.copy();

    assertEquals("folk music is the best", copy.getPrompt());
    assertFalse(copy.isRequired());
    assertEquals("", copy.getAnswer());

    // copy and current are independent
    copy.answer("Strongly agree");
    assertNotEquals(copy.getAnswer(), que6.getAnswer());
  }
}
