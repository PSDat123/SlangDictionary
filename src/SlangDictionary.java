import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class SlangDictionary {
    public static String INIT_DATA_FILE = "./assets/slang.txt";
    private final TreeMap<String, Set<String>> dictionary;
    public SlangDictionary() {
        this(INIT_DATA_FILE);
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

    public List<String> queryFromSlang(String slang) {
        slang = slang.trim().toUpperCase();
        if (dictionary.containsKey(slang)) {
            return new ArrayList<String>(dictionary.get(slang));
        }
        return new ArrayList<String>();
    }

    public List<String> queryFromDefinition(String definition) {
        String normalized_definition = definition.trim().toLowerCase();
        return dictionary.entrySet()
                .stream()
                .filter(e->e.getValue()
                        .stream()
                        .anyMatch(def -> def.toLowerCase().contains(normalized_definition)))
                .map(Map.Entry::getKey)
                .toList();
    }


}
