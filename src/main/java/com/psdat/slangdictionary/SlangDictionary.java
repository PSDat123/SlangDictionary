package com.psdat.slangdictionary;

import javafx.util.Pair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class SlangDictionary {
    public static String DEFAULT_BASE_DATA_FILE = "./assets/slang_base.txt";
    public static String DEFAULT_DATA_FILE = "./assets/slang.txt";
    public static String DEFAULT_HISTORY_FILE = "./assets/history.txt";
    private TreeMap<String, Set<String>> dictionary;
    private final Deque<Pair<String, String>> history = new LinkedList<>();
    private String dataFile, historyFile;
    public SlangDictionary() {
        this(DEFAULT_DATA_FILE, DEFAULT_HISTORY_FILE);
    }
    public SlangDictionary(String dataFile, String historyFile) {
        this.dataFile = dataFile;
        this.historyFile = historyFile;
        if (Objects.equals(this.dataFile, DEFAULT_BASE_DATA_FILE)) {
            System.err.println("Data file cannot be the base data file");;
            return;
        }
        if (Objects.equals(this.dataFile, this.historyFile)) {
            System.err.println("Data file cannot be the same as history file");
            return;
        }
        importDictionary(this.dataFile);
        importHistory(this.historyFile);
    }
    private void importDictionary(String dataFile) {
        dictionary = new TreeMap<>();
        String tmpFile = dataFile;
        if (!new File(tmpFile).isFile()) {
            tmpFile = DEFAULT_BASE_DATA_FILE;
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(tmpFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("`", 2);
                if (tokens.length != 2) continue;
                String[] definitions = tokens[1].split("\\s*\\|\\s*");
                Set<String> definitions_set = new HashSet<>(Arrays.asList(definitions));
                dictionary.put(tokens[0].trim(), definitions_set);
            }
            reader.close();
        } catch (IOException e){
            System.err.println("Error message: " + e);
        }
    }
    private void importHistory(String historyFile) {
        try(BufferedReader reader = new BufferedReader(new FileReader(historyFile))){
            String line;
            while((line = reader.readLine()) != null){
                String[] hist = line.trim().split("\\s*\\|\\s*", 2);
                if (hist.length == 2) {
                    history.addLast(new Pair<>(hist[0], hist[1]));
                }
            }
            reader.close();
        } catch(IOException e){
            System.err.println("Error message: " + e);
        }
    }
    public boolean saveDictionary() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            dictionary.forEach((key, value) -> {
                String line = key + "`" + String.join("|", value);
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            writer.close();
            return true;
        } catch (IOException | RuntimeException e){
            System.err.println("Error message: " + e);
            return false;
        }
    }
    public void saveHistory(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile))){
            for (Pair<String,String> item : history){
                writer.write(item.getKey() + " | " + item.getValue());
                writer.newLine();
            }
            writer.close();
        }
        catch (IOException e){
            System.out.println("Error message: " + e);
        }
    }
    public List<String> queryFromSlang(String slang) {
        return queryFromSlang(slang, false);
    }
    public List<String> queryFromSlang(String slang, boolean addToHistory) {
        slang = slang.trim().toUpperCase();
        if (addToHistory) {
            if (history.size() > 50) {
                history.removeLast();
            }
            DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            String date = df.format(new Date());
            history.addFirst(new Pair<>(slang, date));
            saveHistory();
        }
        if (dictionary.containsKey(slang)) {
            return new ArrayList<>(dictionary.get(slang));
        }
        return new ArrayList<>();
    }
    public List<String> queryFromDefinition(String definition) {
        return queryFromDefinition(definition, false);
    }
    public List<String> queryFromDefinition(String definition, boolean exact) {
        String normalized_definition = definition.trim().toLowerCase();
        return dictionary.entrySet()
                .stream()
                .filter(e->e.getValue()
                        .stream()
                        .anyMatch(def -> exact ? Objects.equals(def.trim().toLowerCase(), normalized_definition) : def.trim().toLowerCase().contains(normalized_definition)))
                .map(Map.Entry::getKey)
                .toList();
    }
    public boolean containsSlang(String slang){
        return dictionary.containsKey(slang.trim().toUpperCase());
    }
    public boolean addSlang(String slang, List<String> definitions) {
        return addSlang(slang, definitions, false);
    }
    public boolean addSlang(String slang, List<String> definitions, boolean overWrite) {
        slang = slang.toUpperCase();
        Set<String> definitions_set = new HashSet<>(definitions);
        if (overWrite) {
            dictionary.put(slang, definitions_set);
            return saveDictionary();
        }
        if (containsSlang(slang)) {
            for (String s : definitions) {
                dictionary.get(slang).add(s);
            }
            return saveDictionary();
        }
        dictionary.put(slang, definitions_set);
        return saveDictionary();
    }
    public boolean editSlang(String oldSlang, String newSlang) {
        if (!dictionary.containsKey(oldSlang)) return false;
        List<String> definitions = queryFromSlang(oldSlang);
        dictionary.remove(oldSlang);
        addSlang(newSlang, definitions);
        return true;
    }
    public boolean deleteSlang(String slang) {
        if (dictionary.remove(slang) == null) return false;
        saveDictionary();
        return true;
    }
    public boolean factoryReset() {
        importDictionary(DEFAULT_BASE_DATA_FILE);
        return saveDictionary();
    }
    public String getRandomSlang() {
        List<String> keySet = new ArrayList<>(dictionary.keySet());
        Random rand = new Random();
        return keySet.get(rand.nextInt(keySet.size()));
    }
    public String getRandomDefinition() {
        String randomKey = getRandomSlang();
        Random rand = new Random();
        List<String> valueSet = new ArrayList<>(dictionary.get(randomKey));
        return valueSet.get(rand.nextInt(valueSet.size()));
    }
    public List<String> getRandomSlang(int n) {
        List<String> keySet = new ArrayList<>(dictionary.keySet());
        List<String> res = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < n; ++i) {
            res.add(keySet.get(rand.nextInt(keySet.size())));
        }
        return res;
    }
    public List<String> getRandomDefinition(int n) {
        List<String> randomKeys = getRandomSlang(n);
        List<String> res = new ArrayList<>();
        Random rand = new Random();
        for (String key: randomKeys) {
            List<String> valueSet = new ArrayList<>(dictionary.get(key));
            res.add(valueSet.get(rand.nextInt(valueSet.size())));
        }
        return res;
    }
    public SlangQuiz getSlangQuiz(int n) {
        if (n < 1) return null;
        n = n - 1;
        String questionSlang = getRandomSlang();
        Random rand = new Random();
        List<String> possibleCorrectAnswers = queryFromSlang(questionSlang);
        String correctAnswer = possibleCorrectAnswers.get(rand.nextInt(possibleCorrectAnswers.size()));

        List<String> quizAnswers = new ArrayList<>();
        quizAnswers.add(correctAnswer);
        List<String> dummyAnswers = getRandomDefinition(n);
        // Validate dummy answers so that they're not duplicate
        for (int i = 0; i < n; ++i) {
            String s = dummyAnswers.get(i);
            while (possibleCorrectAnswers.contains(s) || quizAnswers.contains(s)) {
                dummyAnswers.set(i, getRandomDefinition());
                s = dummyAnswers.get(i);
            }
            quizAnswers.add(s);
        }
        SlangQuiz quiz = new SlangQuiz(questionSlang, quizAnswers, 0);
        quiz.shuffle();
        return quiz;
    }
    public SlangQuiz getDefinitionQuiz(int n) {
        if (n < 1) return null;
        n = n - 1;
        String questionDefinition = getRandomDefinition();
        Random rand = new Random();
        List<String> possibleCorrectAnswers = queryFromDefinition(questionDefinition, true);
        String correctAnswer = possibleCorrectAnswers.get(rand.nextInt(possibleCorrectAnswers.size()));

        List<String> quizAnswers = new ArrayList<>();
        quizAnswers.add(correctAnswer);
        List<String> dummyAnswers = getRandomSlang(n);
        // Validate dummy answer so that they're not duplicate
        for (int i = 0; i < n; ++i) {
            String s = dummyAnswers.get(i);
            while (possibleCorrectAnswers.contains(s) || quizAnswers.contains(s)) {
                dummyAnswers.set(i, getRandomSlang());
                s = dummyAnswers.get(i);
            }
            quizAnswers.add(s);
        }

        SlangQuiz quiz = new SlangQuiz(questionDefinition, quizAnswers, 0);
        quiz.shuffle();
        return quiz;
    }
    public List<Pair<String,String>> getHistory() {
        return new ArrayList<>(history);
    }

}
