package cs5004.questionnaire;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestQuestionnaire {
  private Questionnaire test1;
  private Questionnaire test2;

  @Before
  public void Setup() {
    test1 = new QuestionnaireImpl();
    test2 = new QuestionnaireImpl();
    Question que1 = new YesNo("is this right?",true);
    Question que2 = new YesNo("is this right?",false);
    Question que3 = new ShortAnswer("name your favourite color",true);
    Question que4 = new ShortAnswer("name pet sounds",false);
    Question que5 = new Likert("Taylor is great",true);
    Question que6 = new Likert("folk music is the best",false);
    test1.addQuestion("Q1", que1);
    test1.addQuestion("Q2", que2);
    test1.addQuestion("Q3", que3);
    test1.addQuestion("Q4", que4);
    test1.addQuestion("Q5", que5);
    test1.addQuestion("Q6", que6);
  }

  @Test
  public void testYesNo() {
    test1.getQuestion(5).answer("Strongly Agree");
    test1.getQuestion(6).answer("Agree");
    test2.toString();
    assertFalse(test2.isComplete());
  }
}
