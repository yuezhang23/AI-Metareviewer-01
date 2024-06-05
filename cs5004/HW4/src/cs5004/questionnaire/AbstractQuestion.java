package cs5004.questionnaire;

import java.util.NoSuchElementException;

public abstract class AbstractQuestion implements Question {
  protected String prompt;
  protected Boolean status;
  protected String answer;

  public AbstractQuestion(String que, boolean status) {
    if (que == null || que.equals("")) {
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
    if (status && ans.equals("")) {
      throw new NoSuchElementException("require an answer");
    }
    if (ans == null) {
      throw new IllegalArgumentException("invalid input");
    }
    this.answer = ans;
  }
}
