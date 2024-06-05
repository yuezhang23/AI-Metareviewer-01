package cs5004.questionnaire;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class implements the Questionnaire interface.
 */
public class QuestionnaireImpl implements Questionnaire {
  private Map<String, Question> mapQ;

  /**
   * Constructs a LinkedHashMap object to store Question and its identifier.
   */
  public QuestionnaireImpl() {
    mapQ = new LinkedHashMap<>();
  }

  @Override
  public void addQuestion(String identifier, Question q) {
    if (identifier == null || q == null) {
      throw new IllegalArgumentException("null input");
    }
    if (identifier.equals("")) {
      throw new IllegalArgumentException("every question needs a name");
    }
    if (mapQ.containsKey(identifier) || mapQ.containsValue(q)) {
      throw new IllegalArgumentException("question already exists");
    }
    mapQ.put(identifier, q);
  }

  @Override
  public void removeQuestion(String identifier) {
    if (identifier == null) {
      throw new NoSuchElementException("null input");
    }
    if (identifier.equals("")) {
      throw new NoSuchElementException("empty input");
    }
    if (mapQ.get(identifier) == null) {
      throw new NoSuchElementException("question not found");
    }
    mapQ.remove(identifier);
  }

  @Override
  public Question getQuestion(int num) {
    List<String> id;
    id = new ArrayList<>(this.mapQ.keySet());
    if (num < 1 || num > id.size()) {
      throw new IndexOutOfBoundsException("index out of list bound");
    }
    String identifier = id.get(num - 1);
    return mapQ.get(identifier);
  }

  @Override
  public Question getQuestion(String identifier) {
    if (identifier == null) {
      throw new NoSuchElementException("null input");
    }
    if (identifier.equals("")) {
      throw new NoSuchElementException("empty input");
    }
    if (mapQ.get(identifier) == null) {
      throw new NoSuchElementException("question not found");
    }
    return mapQ.get(identifier);
  }

  @Override
  public List<Question> getRequiredQuestions() {
    if (this.mapQ.isEmpty()) {
      return List.of();
    }
    return this.mapQ.values().stream().filter(Question::isRequired).collect(Collectors.toList());
  }

  @Override
  public List<Question> getOptionalQuestions() {
    if (this.mapQ.isEmpty()) {
      return List.of();
    }
    return this.mapQ.values().stream().filter(b -> !b.isRequired()).collect(Collectors.toList());
  }

  @Override
  public boolean isComplete() {
    if (this.mapQ.isEmpty()) {
      return false;
    }
    return this.getRequiredQuestions().stream().noneMatch(b -> b.getAnswer().equals(""));
  }

  @Override
  public List<String> getResponses() {
    if (this.mapQ.isEmpty()) {
      return List.of();
    }
    return this.mapQ.values().stream().map(Question::getAnswer).collect(Collectors.toList());
  }

  @Override
  public Questionnaire filter(Predicate<Question> pq) {
    if (pq == null) {
      throw new IllegalArgumentException("null");
    }
    List<Question> selectedQ;
    selectedQ = this.mapQ.values().stream().filter(pq).collect(Collectors.toList());

    Questionnaire newList = new QuestionnaireImpl();
    Set<String> id = this.mapQ.keySet();
    for (String identifier : id) {
      if (selectedQ.contains(this.mapQ.get(identifier))) {
        newList.addQuestion(identifier, this.mapQ.get(identifier).copy());
      }
    }
    return newList;
  }

  @Override
  public void sort(Comparator<Question> comp) {
    if (comp == null) {
      throw new IllegalArgumentException("null");
    }
    Map<String, Question> newMap;
    newMap = mapQ.entrySet().stream().sorted(Map.Entry.comparingByValue(comp)).collect(Collectors
            .toMap(Map.Entry :: getKey, Map.Entry :: getValue, (oldValue, newValue) ->
                    oldValue, LinkedHashMap :: new));
    mapQ.clear();
    mapQ.putAll(newMap);
  }

  @Override
  public <R> R fold(BiFunction<Question, R, R> bf, R seed) {
    if (bf == null || seed == null) {
      throw new IllegalArgumentException("null");
    }
    R result = bf.apply(this.getQuestion(1), seed);
    for (int i = 2; i <= this.mapQ.size(); i++) {
      result = bf.apply(this.getQuestion(i), result);
    }
    return result;
  }

  @Override
 public String toString() {
    StringBuilder result = new StringBuilder();
    if (mapQ.isEmpty()) {
      return "";
    }
    for (Question ques : mapQ.values()) {
      result.append("\n\n").append("Question: ").append(ques.getPrompt())
              .append("\n\n").append("Answer: ").append(ques.getAnswer());
    }
    return result.substring(2);
  }
}

