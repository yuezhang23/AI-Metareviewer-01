package cs5004.questionnaire;

public interface Question {

  String getPrompt();

  Boolean isRequired();

  void answer(String input);

  String getAnswer();

  Question copy();

}
