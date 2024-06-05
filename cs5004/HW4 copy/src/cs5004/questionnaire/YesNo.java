package cs5004.questionnaire;

/**
 * This class is YesNo class, a type of Question, where the answer to
 * the question can only be yes or no.
 */
public class YesNo extends AbstractQuestion {

  public YesNo(String que, boolean status) {
    super(que, status);
  }

  @Override
  public void answer(String ans) {
    super.answer(ans);
    if (!ans.equalsIgnoreCase("YES") && !ans.equalsIgnoreCase("NO") && !ans.equals("")) {
      throw new IllegalArgumentException("not valid answer to Y/N question");
    }
  }

  @Override
  public Question copy() {
    Question copyY = new YesNo(this.prompt, this.status);
    if (!this.answer.equals("")) {
      copyY.answer(this.answer);
    }
    return copyY;
  }
}
