import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TermFrequency {

    public static void main(String[] args) {
        new Quarantine<>(() -> args[0])
            .bind(TermFrequency::readFile)
            .map(TermFrequency::removeNonAlpha)
            .map(TermFrequency::convertToLowerCase)
            .map(TermFrequency::convertToArray)
            .map(TermFrequency::removeSingleLetter)
            .bind(text -> new Quarantine<>(() -> "stop_words.txt")
                .bind(TermFrequency::readFile)
                .map(TermFrequency::stopWordIntoArray)
                .map(stopWordsArray -> TermFrequency.filterStopWords(text, stopWordsArray)))
            .map(TermFrequency::countFrequency)
            .map(TermFrequency::sortFrequency)
            .bind(sortedFrequency -> new Quarantine<>(() -> Integer.parseInt(args[1]))
                .map(limit -> TermFrequency.findTopFrequencyWords(sortedFrequency, limit)))
            .bind(TermFrequency::printFrequency)
            .run();
    }

    //readFile
    private static Quarantine<String> readFile(String fileName) {
        return new Quarantine<>(() -> {
            try {
                return new String(Files.readAllBytes(Paths.get(fileName)));
            } catch (IOException e) {
                //no need to handle the exception here
                return "";
            }
        });
    }

    //remove non-alphanumeric characters
    private static String removeNonAlpha(String text) {
        return text.replaceAll("[^a-zA-Z0-9\\s]", " ");
    }

    //convert to lower case
    private static String convertToLowerCase(String textNoAlpha) {
        return textNoAlpha.toLowerCase();
    }

    //convert text into an array of words
    private static String[] convertToArray(String textLowerCase) {
        return textLowerCase.split("\\s+");
    }

    //remove single letter
    private static String[] removeSingleLetter(String[] array) {
            return Arrays.stream(array)
                .filter(word -> word.length() > 1)
                .toArray(String[]::new);
    }

    //stopWordIntoArray
    private static String[] stopWordIntoArray(String stopWords) {
        return stopWords.split(",");
    }

    //filter stop words
    private static String[] filterStopWords(String[] textArray, String[] stopWordsArray) {
            return Arrays.stream(textArray)
                .filter(word -> !Arrays.asList(stopWordsArray).contains(word))
                .toArray(String[]::new);
    }

    //count frequency
    private static Map<String, Integer> countFrequency(String[] textArray) {
            return Arrays.stream(textArray)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(e -> 1)));
    }

    //sort the frequency from the most frequent to the least frequent
    private static List<Map.Entry<String, Integer>> sortFrequency(Map<String, Integer> frequency) {
            return frequency.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                    .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toList());
    }

    //find the top n frequency words
    private static List<Map.Entry<String, Integer>> findTopFrequencyWords(List<Map.Entry<String, Integer>> sortedFrequency, int n) {
            return sortedFrequency.stream()
                .limit(n)
                .collect(Collectors.toList());
    }

    //print the frequency list
    private static Quarantine<Void> printFrequency(List<Map.Entry<String, Integer>> topFrequencyWords) {
        return new Quarantine<>(() -> {
            topFrequencyWords.forEach(entry -> System.out.println(entry.getKey() + "  -  " + entry.getValue()));
            return null;
        });
    }
}
