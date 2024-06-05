package cs5004.questionnaire;

import org.junit.Before;
import org.junit.Test;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test all the methods in the Questionnaire Interface.
 */
public class TestQuestionnaire {
  private Questionnaire test1;
  private Questionnaire test2;
  private Questionnaire test3;
  private Question que1;
  private Question que2;
  private Question que3;
  private Question que4;
  private Question que5;
  private Question que6;
  private Question que7;

  @Before
  public void setUp() {
    test1 = new QuestionnaireImpl();
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
    test1.addQuestion("Q1", que1);
    test1.addQuestion("Q2", que2);
    test1.addQuestion("Q3", que3);
    test1.addQuestion("Q4", que4);
    test1.addQuestion("Q5", que5);
    test1.addQuestion("Q6", que6);
    test3 = new QuestionnaireImpl();
    test3.addQuestion("Q1", que1);
    test3.addQuestion("Q5", que5);
    test3.addQuestion("Q7", que7);
    test2 = new QuestionnaireImpl();
  }

  @Test
  public void testImpl() {
    String expected = "";
    assertEquals(expected, test2.toString());
  }

  @Test
  public void testAddQue() {
    String expected0 = "Question: hello, is this right?\n\nAnswer: yes\n\n"
            + "Question: you, is this wrong?\n\nAnswer: \n\nQuestion: Bob, name "
            + "your favourite color\n\nAnswer: \n\n"
            + "Question: name pet sounds\n\nAnswer: woo~ or hmm\n\n"
            + "Question: Taylor is great\n\nAnswer: Strongly AGREE\n\n"
            + "Question: folk music is the best\n\nAnswer: ";
    assertEquals(expected0, test1.toString());

    // add to non-empty questionnaire
    test1.addQuestion("Q7", que7);
    String expected1 = "Question: hello, is this right?\n\nAnswer: yes\n\n"
            + "Question: you, is this wrong?\n\nAnswer: \n\nQuestion: Bob, name your "
            + "favourite color\n\nAnswer: \n\n"
            + "Question: name pet sounds\n\nAnswer: woo~ or hmm\n\n"
            + "Question: Taylor is great\n\nAnswer: Strongly AGREE\n\n"
            + "Question: folk music is the best\n\nAnswer: \n\n"
            + "Question: you will survive\n\nAnswer: agree";
    assertEquals(expected1, test1.toString());

    // add to empty questionnaire
    String expected2 = "Question: hello, is this right?\n\nAnswer: yes";
    test2.addQuestion("Q1", que1);
    assertEquals(expected2, test2.toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAddQue1() {
    test1.addQuestion("", null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAddQue2() {
    test1.addQuestion(null, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAddQue3() {
    test1.addQuestion(null, que1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAddQue4() {
    test1.addQuestion("", que1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAddQue5() {
    test1.addQuestion("Q1", null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAddQue6() {
    test1.addQuestion("Q1", que7);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAddQue7() {
    test1.addQuestion("Q8", que6);
  }

  @Test
  public void testRemove() {
    // remove first element
    test1.removeQuestion("Q1");
    String expected0 = "Question: you, is this wrong?\n\nAnswer: \n\n"
            + "Question: Bob, name your favourite color\n\nAnswer: \n\n"
            + "Question: name pet sounds\n\nAnswer: woo~ or hmm\n\n"
            + "Question: Taylor is great\n\nAnswer: Strongly AGREE\n\n"
            + "Question: folk music is the best\n\nAnswer: ";
    assertEquals(expected0, test1.toString());

    // remove element in the middle
    test1.removeQuestion("Q3");
    String expected1 = "Question: you, is this wrong?\n\nAnswer: \n\n"
            + "Question: name pet sounds\n\nAnswer: woo~ or hmm\n\n"
            + "Question: Taylor is great\n\nAnswer: Strongly AGREE\n\n"
            + "Question: folk music is the best\n\nAnswer: ";
    assertEquals(expected1, test1.toString());

    // remove last element
    test1.removeQuestion("Q6");
    String expected2 = "Question: you, is this wrong?\n\nAnswer: \n\n"
            + "Question: name pet sounds\n\nAnswer: woo~ or hmm\n\n"
            + "Question: Taylor is great\n\nAnswer: Strongly AGREE";
    assertEquals(expected2, test1.toString());
  }

  @Test (expected = NoSuchElementException.class)
  public void testInvalidRemove1() {
    test1.removeQuestion(null);
  }

  @Test (expected = NoSuchElementException.class)
  public void testInvalidRemove2() {
    test1.removeQuestion("");
  }

  @Test (expected = NoSuchElementException.class)
  public void testInvalidRemove3() {
    test1.removeQuestion("Q7");
  }

  @Test (expected = NoSuchElementException.class)
  public void testInvalidRemove4() {
    test2.removeQuestion("Q1");
  }

  @Test
  public void testGetQue() {
    // get by count
    assertEquals(que1, test1.getQuestion(1));
    assertEquals(que4, test1.getQuestion(4));
    assertEquals(que6, test1.getQuestion(6));

    // get by id
    assertEquals(que6, test1.getQuestion("Q6"));
    assertEquals(que4, test1.getQuestion("Q4"));
    assertEquals(que1, test1.getQuestion("Q1"));
  }

  @Test (expected = IndexOutOfBoundsException.class)
  public void testInvalidGet1() {
    test1.getQuestion(0);
  }

  @Test (expected = IndexOutOfBoundsException.class)
  public void testInvalidGet2() {
    test1.getQuestion(7);
  }

  @Test (expected = IndexOutOfBoundsException.class)
  public void testInvalidGet3() {
    test2.getQuestion(0);
  }

  @Test (expected = NoSuchElementException.class)
  public void testInvalidGet4() {
    test2.getQuestion("Q0");
  }

  @Test (expected = NoSuchElementException.class)
  public void testInvalidGet5() {
    test2.getQuestion("");
  }

  @Test (expected = NoSuchElementException.class)
  public void testInvalidGet6() {
    test2.getQuestion(null);
  }

  @Test
  public void testGetRequired() {
    // mix of true status and false status
    List<Question> expected1 = List.of(que1, que3, que5);
    assertEquals(expected1, test1.getRequiredQuestions());
    assertEquals(que1, test1.getQuestion(1));
    assertEquals(que3, test1.getQuestion(3));
    assertEquals(que5, test1.getQuestion(5));

    // all false status
    test2.addQuestion("Q4", que4);
    test2.addQuestion("Q6", que6);
    test2.addQuestion("Q2", que2);
    assertTrue(test2.getRequiredQuestions().isEmpty());

    // all true status
    List<Question> expected2 = List.of(que1, que5, que7);
    assertEquals(expected2, test3.getRequiredQuestions());
    assertEquals(que1, test3.getQuestion(1));
    assertEquals(que5, test3.getQuestion(2));
    assertEquals(que7, test3.getQuestion(3));

    // empty questionnaire
    assertTrue(test2.getRequiredQuestions().isEmpty());
  }

  @Test
  public void testGetOptional() {
    // mix of true status and false status
    List<Question> expected1 = List.of(que2, que4, que6);
    assertEquals(expected1, test1.getOptionalQuestions());
    assertEquals(que2, test1.getQuestion(2));
    assertEquals(que4, test1.getQuestion(4));
    assertEquals(que6, test1.getQuestion(6));

    // all false status
    test2.addQuestion("Q2", que2);
    test2.addQuestion("Q4", que4);
    test2.addQuestion("Q6", que6);
    List<Question> expected2 = List.of(que2, que4, que6);
    assertEquals(expected2, test2.getOptionalQuestions());
    assertEquals(que2, test2.getQuestion(1));
    assertEquals(que4, test2.getQuestion(2));
    assertEquals(que6, test2.getQuestion(3));

    // all true status
    assertTrue(test3.getOptionalQuestions().isEmpty());

    // empty questionnaire
    assertTrue(test2.getRequiredQuestions().isEmpty());
  }

  @Test
  public void testIsComplete() {
    // mix of true and false, some not answered
    assertFalse(test1.isComplete());

    // empty Q
    assertFalse(test2.isComplete());

    // all true answered
    assertTrue(test3.isComplete());

    // all false, some answered
    test2.addQuestion("Q2", que2);
    test2.addQuestion("Q4", que4);
    test2.addQuestion("Q6", que6);
    assertTrue(test2.isComplete());
  }

  @Test
  public void testResponse() {
    // some empty answer
    List<String> expected0 = List.of("yes", "", "", "woo~ or hmm", "Strongly AGREE", "");
    assertEquals(expected0, test1.getResponses());

    // empty questionnaire
    assertTrue(test2.getResponses().isEmpty());

    // empty answer
    List<String> expected1 = List.of("");
    test2.addQuestion("Q2", que2);
    assertEquals(expected1, test2.getResponses());

    // all non-empty answer
    List<String> expected2 = List.of("yes", "Strongly AGREE", "agree");
    assertEquals(expected2, test3.getResponses());
  }

  @Test
  public void testFilter() {
    // use filter to implement getResponse method
    Predicate<Question> getResponse = b -> !b.getAnswer().equals("");
    Questionnaire newQ = new QuestionnaireImpl();
    newQ.addQuestion("Q1", que1.copy());
    newQ.addQuestion("Q4", que4.copy());
    newQ.addQuestion("Q5", que5.copy());

    assertEquals(newQ.getQuestion(1).getPrompt(),
            test1.filter(getResponse).getQuestion(1).getPrompt());
    assertEquals(newQ.getQuestion(2).getPrompt(),
            test1.filter(getResponse).getQuestion(2).getPrompt());
    assertEquals(newQ.getQuestion(3).getPrompt(),
            test1.filter(getResponse).getQuestion(3).getPrompt());
    assertEquals(que1, test1.getQuestion(1));
    assertEquals(que2, test1.getQuestion(2));
    assertEquals(que3, test1.getQuestion(3));

    // changes in current questionnaire won't affect new questionnaire
    test1.removeQuestion("Q1");
    assertEquals(que2.getPrompt(), test1.getQuestion(1).getPrompt());
    assertEquals(que1.getPrompt(), newQ.getQuestion(1).getPrompt());

    // filter by prompt length
    Predicate<Question> longPrompt = b -> b.getPrompt().length() > 20;
    Questionnaire newQ1 = new QuestionnaireImpl();
    newQ1.addQuestion("Q1", que1.copy());
    assertEquals(newQ1.getQuestion(1).getPrompt(),
            test3.filter(longPrompt).getQuestion(1).getPrompt());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidFilter() {
    test1.filter(null);
  }

  @Test
  public void testSort() {
    // sort by prompt lexicographically
    test1.sort(Comparator.comparing(Question :: getPrompt));
    String expected1;
    expected1 = "Question: Bob, name your favourite color\n\nAnswer: \n\n"
            + "Question: Taylor is great\n\nAnswer: Strongly AGREE\n\n"
            + "Question: folk music is the best\n\nAnswer: \n\n"
            + "Question: hello, is this right?\n\nAnswer: yes\n\n"
            + "Question: name pet sounds\n\nAnswer: woo~ or hmm\n\n"
            + "Question: you, is this wrong?\n\nAnswer: ";
    assertEquals(expected1, test1.toString());

    // sort by prompt length
    test1.sort(Comparator.comparingInt(b -> b.getPrompt().length()));
    String expected2 = "Question: Taylor is great\n\nAnswer: Strongly AGREE\n\n"
            + "Question: name pet sounds\n\nAnswer: woo~ or hmm\n\n"
            + "Question: you, is this wrong?\n\nAnswer: \n\n"
            + "Question: hello, is this right?\n\nAnswer: yes\n\n"
            + "Question: folk music is the best\n\nAnswer: \n\n"
            + "Question: Bob, name your favourite color\n\nAnswer: ";
    assertEquals(expected2, test1.toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidSort() {
    test1.sort(null);
  }

  @Test
  public void testFold() {
    BiFunction<Question, String, String> bf = (x, y) -> x.getPrompt() + "\n" + y;
    String result = "you will survive\nTaylor is great\nhello, is this right?\nthe end";
    assertEquals(result, test3.fold(bf, "the end"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidFold() {
    test1.fold(null, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidFold1() {
    test1.fold(null, "answer");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidFold2() {
    BiFunction<Question, String, String> bf = (x, y) -> x.getPrompt() + "\nNo answer\n";
    test1.fold(bf, null);
  }

  @Test
  public void testString() {
    assertEquals("", test2.toString());

    String expected1 = "Question: hello, is this right?\n\nAnswer: yes\n\n"
            + "Question: Taylor is great\n\nAnswer: Strongly AGREE\n\n"
            + "Question: you will survive\n\nAnswer: agree";
    assertEquals(expected1, test3.toString());
  }
}
