package org.example.home;
import static org.example.home.WordSession.loadWordsFromDB;

import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Map;

public class HomeController {
    public static final String ROOT_PATH = "/index.html";
    public static final String HOME_PATH = "/home";

    public static void renderHomePage(Context context) {

        ArrayList<String> words = loadWordsFromDB();

        Map<String, Object> viewModel = Map.of("words", words);

        context.render("home.html", viewModel);
    }
}