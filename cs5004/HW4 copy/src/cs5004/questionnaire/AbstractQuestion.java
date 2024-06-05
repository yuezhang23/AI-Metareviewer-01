package cs5004.questionnaire;

/**
 * This is an abstract class that implements the Question interface with common methods
 * that satisfy various Question types.
 */
public abstract class AbstractQuestion implements Question {
  protected String prompt;
  protected Boolean status;
  protected String answer;

  /**
   * Construct a question with prompt, status and answer, where answer is initialized
   * to be an empty string.
   * @param que the prompt text
   * @param status true if question requires an answer, otherwise false
   * @throws IllegalArgumentException is prompt is empty or null
   */
  public AbstractQuestion(String que, boolean status) {
    if (que == null) {
      throw new IllegalArgumentException("no prompt to pass in");
    }
    if (que.equals("")) {
      throw new IllegalArgumentException("question prompt should not be empty");
    }
    this.prompt = que;
    this.status = status;
    this.answer = "";
  }

  @Override
  public String getPrompt() {
    return this.prompt;
  }

  @Override
  public Boolean isRequired() {
    return this.status;
  }

  @Override
  public String getAnswer() {
    return this.answer;
  }

  @Override
  public void answer(String ans) {
    if (ans == null) {
      throw new IllegalArgumentException("invalid input");
    }
    if (status && ans.equals("")) {
      throw new IllegalArgumentException("require an answer");
    }
    this.answer = ans;
  }
}
