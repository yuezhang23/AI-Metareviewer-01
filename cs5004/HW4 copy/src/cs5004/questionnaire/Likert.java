package cs5004.questionnaire;

/**
 * This class is Likert class, a type of Question, where the answer is
 * a fixed, 5-point Likert scale. (Strongly Agree, Agree,
 * Neither Agree nor Disagree, Disagree, Strongly Disagree)
 */
public class Likert extends AbstractQuestion {

  public Likert(String que, boolean status) {
    super(que, status);
  }

  @Override
  public void answer(String ans) {
    super.answer(ans);
    boolean check = false;
    for (LikertResponseOption option : LikertResponseOption.values()) {
      if (ans.equalsIgnoreCase(option.getText())) {
        ans = option.getText();
        check = true;
        break;
      }
    }
    if (!ans.equals("") && !check) {
      throw new IllegalArgumentException("invalid likert option");
    }
  }

  @Override
  public Question copy() {
    Question copyL = new Likert(this.prompt, this.status);
    if (!this.answer.equals("")) {
      copyL.answer(this.answer);
    }
    return copyL;
  }
}
