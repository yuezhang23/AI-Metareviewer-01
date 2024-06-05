package cs5004.questionnaire;

/**
 * Represents a collection of methods to form Question.
 */
public interface Question {

  /**
   * Get the prompt text from the question.
   * @return the prompt text
   */
  String getPrompt();

  /**
   * Return true if question is required to answer, otherwise return false.
   * @return the question state value
   */
  Boolean isRequired();

  /**
   * Enter an answer to the question from input, the contents of which is decided
   * by the question type.
   * @param input the answer
   * @throws IllegalArgumentException if input is null or is not consistent with question type
   *        or is empty when the question requires one
   */
  void answer(String input);

  /**
   * Return the current answer to the question.
   * @return answer to the question
   */
  String getAnswer();

  /**
   * Return a new question where the prompt, status and answer are copies of current
   * question, independent of current one.
   * @return a Question instance
   */
  Question copy();
}
