import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        SlangDictionary dict = new SlangDictionary();
//        System.out.println(dict.queryFromSlang("*_*"));
//        System.out.println(dict.queryFromSlang("/:)"));
//        System.out.println(dict.queryFromSlang("lol"));
//        System.out.println(dict.queryFromSlang("HK"));
//        System.out.println(dict.getRandomSlang());
//        System.out.println(dict.getRandomDefinition());

//        SlangQuiz quiz = dict.getSlangQuiz(4);
//        System.out.println(quiz.getQuestion());
//        System.out.println(quiz.getAnswers());
//        System.out.println(quiz.getCorrectAnswer());

        SlangQuiz quiz = dict.getDefinitionQuiz(4);
        System.out.println(quiz.getQuestion());
        System.out.println(quiz.getAnswers());
        System.out.println(quiz.getCorrectAnswer());
    }
}