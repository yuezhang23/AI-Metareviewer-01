package cs5004.questionnaire;

/**
 * This is ShortAnswer class, a type of Question, where the answer length could be
 * at most 280 words, including spaces.
 */
public class ShortAnswer extends AbstractQuestion {
  public ShortAnswer(String que, boolean status) {
    super(que, status);
  }

  @Override
  public void answer(String ans) {
    super.answer(ans);
    if (ans.length() > 280) {
      throw new IllegalArgumentException("answer too long");
    }
  }

  @Override
  public Question copy() {
    Question copyS = new ShortAnswer(this.prompt, this.status);
    if (!this.answer.equals("")) {
      copyS.answer(this.answer);
    }
    return copyS;
  }
}
