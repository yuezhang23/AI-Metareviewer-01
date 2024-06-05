package cs5004.questionnaire;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class QuestionnaireImpl implements Questionnaire {
  private List<Question> questionList;
  private Map<String, Question> hashQ;

  public QuestionnaireImpl() {
    questionList = new ArrayList<>();
    hashQ = new HashMap<>();
  }

  @Override
  public void addQuestion(String identifier, Question q) {
    if (hashQ.containsKey(identifier) || hashQ.containsValue(q)) {
      throw new IllegalStateException("question already exists");
    }
    if (identifier == null || q == null || identifier.equals("")) {
      throw new IllegalArgumentException("every question needs a name");
    }
    questionList.add(q);
    hashQ.put(identifier, q);
  }

  @Override
  public void removeQuestion(String identifier) {
    if (identifier == null || hashQ.get(identifier) == null || identifier.equals("")) {
      throw new NoSuchElementException("question not found");
    }
    questionList.remove(hashQ.get(identifier));
    hashQ.remove(identifier);
  }

  @Override
  public Question getQuestion(int num) {
    if (num < 1 || num > questionList.size()) {
      throw new IndexOutOfBoundsException("index out of list bound");
    }
    return questionList.get(num - 1);
  }

  @Override
  public Question getQuestion(String identifier) {
    if (identifier == null || hashQ.get(identifier) == null || identifier.equals("")) {
      throw new NoSuchElementException("question not found");
    }
    return hashQ.get(identifier);
  }

  @Override
  public List<Question> getRequiredQuestions() {
    List<Question> getRequired;
    getRequired = questionList.stream().filter(Question::isRequired).collect(Collectors.toList());
    return getRequired;
  }

  @Override
  public List<Question> getOptionalQuestions() {
    List<Question> getOptional;
    getOptional = questionList.stream().filter(b -> !b.isRequired()).collect(Collectors.toList());
    return getOptional;
  }

  @Override
  public boolean isComplete() {
    if (questionList.isEmpty()) {
      return false;
    }
    boolean noResponse;
    noResponse = this.getRequiredQuestions().stream().anyMatch(b -> b.getAnswer().equals(""));
    return !noResponse;
  }

  @Override
  public List<String> getResponses() {
    List<String> response;
    response = questionList.stream().map(Question::getAnswer).collect(Collectors.toList());
    return response;
  }

  @Override
  public Questionnaire filter(Predicate<Question> pq) {
    List<Question> selectedQ;
    selectedQ = this.questionList.stream().filter(pq).collect(Collectors.toList());

    Questionnaire newList = new QuestionnaireImpl();
    Set<String> id = this.hashQ.keySet();
    for (String identifier : id) {
      if (selectedQ.contains(this.hashQ.get(identifier))) {
        newList.addQuestion(identifier, this.hashQ.get(identifier).copy());
      }
    }
    return newList;
  }

  @Override
  public void sort(Comparator<Question> comp) {
    questionList.sort(comp);
  }

  @Override
  public <R> R fold(BiFunction<Question, R, R> bf, R seed) {
    R result = bf.apply(questionList.get(0), seed);
    for (int i = 1; i < questionList.size(); i++) {
      result = bf.apply(questionList.get(i), result);
    }
    return result;
  }

  @Override
 public String toString() {
   StringBuilder result = new StringBuilder();
   if (questionList.isEmpty()) {
     return "";
   }
   for (Question ques : questionList) {
     result.append("\n\n").append("Question: ").append(ques.getPrompt())
             .append("\n\n").append("Answer: ").append(ques.getAnswer());
   }
   return result.substring(2);
 }
}

