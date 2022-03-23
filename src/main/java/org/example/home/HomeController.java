package org.example.home;
import static org.example.data.LoadWords.loadWords;

import io.javalin.http.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class HomeController {
    public static final String ROOT_PATH = "/index.html";
    public static final String HOME_PATH = "/home";

    public static void renderHomePage(Context context) throws IOException {

        ArrayList<String> words = loadWords();

        Map<String, Object> viewModel = Map.of();

        context.render("home.html", viewModel);
    }
}