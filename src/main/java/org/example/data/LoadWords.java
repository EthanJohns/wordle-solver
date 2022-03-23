package org.example.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class LoadWords {

    public static ArrayList<String> loadWords() throws IOException {
        ArrayList<String> words = (ArrayList<String>) Files.readAllLines(Path.of("src/main/java/org/example/data/words.txt"));
        return words;
    }

}
