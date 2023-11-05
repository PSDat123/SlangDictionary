import java.util.*;

public class SlangQuiz {
    String question;
    List<String> answers = new ArrayList<>();
    int correctAnswer;
    public SlangQuiz(String word, List<String> questions, int correctAnswer) {
        this.question = word;
        this.answers = questions;
        this.correctAnswer = correctAnswer;
    }
    public boolean checkAnswer(int ans) {
        return ans == correctAnswer;
    }
    public boolean checkAnswer(String ans) {
        return Objects.equals(answers.get(correctAnswer), ans);
    }
    public void shuffle() {
        String answerString = answers.get(correctAnswer);
        Collections.shuffle(answers);
        correctAnswer = answers.indexOf(answerString);
    }
    public String getQuestion() {
        return question;
    }
    public List<String> getAnswers() {
        return answers;
    }
    public int getCorrectAnswer() {
        return correctAnswer;
    }
}
