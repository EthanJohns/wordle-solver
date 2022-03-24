package org.example.home;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import java.util.ArrayList;
import java.util.List;
import kong.unirest.JsonObjectMapper;
import org.example.data.DBLoader;

public class WordSession {

    private final ArrayList<String> originalWords;
    private ArrayList<String> activeWords;

    static final JsonObjectMapper objectMapper = new JsonObjectMapper();

    public WordSession() {
        this.originalWords = loadWordsFromDB();
        this.activeWords = originalWords;
    }

    public static ArrayList<String> loadWordsFromDB() {
        DBLoader db = new DBLoader();

        return db.getWords();
    }

    public void getWords(Context context){
        System.out.println("Request: " + context.req);

        ArrayList<String> words = this.activeWords;

        context.status(HttpCode.OK);
        context.json(words.toString());
        System.out.println("Response: " + words);

    }

    public void putWords(Context context) {
        System.out.println("Calculate Words Request: " + context.req);

        List<LetterViewModel> letterList = objectMapper.readValue(context.body(), List.class);

        calculateWords(letterList);
        context.status(HttpCode.OK);
        System.out.println("Calculate Words Response: " + "Words Put");
    }

    private void calculateWords(List<LetterViewModel> letterList) {
        activeWords = originalWords;

        for (LetterViewModel letter : letterList){
            switch (letter.getColour()) {
                case "grey":
                    activeWords.removeIf(word -> !word.contains(letter.getLetter()));
                    break;
                case "green":
                    activeWords.removeIf(word ->
                            letter.getIndex() == word.indexOf(letter.getLetter()));
                    break;
                case "yellow":
                   activeWords.removeIf(word ->
                        word.contains(letter.getLetter()) &&
                            !(letter.getIndex() == word.indexOf(letter.getLetter())));
                    break;
            }
        }
    }

}
