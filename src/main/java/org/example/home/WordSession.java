package org.example.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import java.util.ArrayList;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.example.data.DBLoader;

public class WordSession {

    private final ArrayList<String> originalWords;
    static final ObjectMapper objectMapper = new ObjectMapper();

    public WordSession() {
        this.originalWords = loadWordsFromDB();
    }

    public static ArrayList<String> loadWordsFromDB() {
        DBLoader db = new DBLoader();

        return db.getWords();
    }

//    public void getWords(Context context){
//        System.out.println("Get Words Request");
//
//        ArrayList<String> words = this.activeWords;
//
//        context.status(HttpCode.OK);
//        context.json(words.toString());
//        System.out.println("Response: List containing: " + activeWords);
//        System.out.println("Response: Word list with size of " + words.size());
//    }

    public void putWords(Context context) {
        System.out.println("Calculate Words Request: " + context.body());

        JSONArray letterList = new JSONArray(context.body());

        ArrayList<String> words = calculateWords(letterList);
        context.status(HttpCode.OK);
        context.json(words.toString());
//        System.out.println("Response: List containing: " + activeWords);
        System.out.println("Response: Word list with size of " + words.size());
    }


    private ArrayList<String> calculateWords(JSONArray letterList) {
        ArrayList<String> words = new ArrayList<>(originalWords);

//        System.out.println("Words Size Before: " + words.size());

        for (int i = 0; i < letterList.length() ; i++){
            JSONObject letter = letterList.getJSONObject(i);
            switch (letter.get("colour").toString()) {
                case "rgb(85, 85, 85)":
                    words.removeIf(word -> word.contains(letter.get("letter").toString()));
                    break;
                case "rgb(106, 170, 100)":
                    words.removeIf(word ->
                        Integer.parseInt(letter.get("index").toString()) != word.indexOf(letter.get("letter").toString()));
                    break;
                case "rgb(201, 180, 88)":
                    words.removeIf(word ->
                        word.contains(letter.get("letter").toString()) &&
                            (Integer.parseInt(letter.get("index").toString()) == word.indexOf(letter.get("letter").toString())));
                    break;
            }
        }
//        System.out.println("Words Size After: " + words.size());
        return words;
    }

}
