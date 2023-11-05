import java.util.*;
import java.io.*;

public class SlangDictionary {
    public static String DATA_FILE = "./assets/slang.txt";
    private TreeMap<String, Set<String>> dictionary;
    public SlangDictionary() {
        this(DATA_FILE);
    }
    public SlangDictionary(String DATA_FILE) {
        dictionary = new TreeMap<String, Set<String>>();

        try(BufferedReader reader = new BufferedReader(new FileReader(new File(DATA_FILE)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("`", 2);
                if (tokens.length != 2) continue;
                String[] definitions = tokens[1].split("\\|");
                Set<String> definitions_set = new HashSet<>(Arrays.asList(definitions));
                dictionary.put(tokens[0], definitions_set);
            }
            reader.close();
        } catch (IOException e){
            System.out.println("Error message: " + e);
        }
    }
}
