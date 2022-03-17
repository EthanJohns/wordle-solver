package org.example.home;

import io.javalin.http.Context;

import java.util.Map;

public class HomeController {
    public static final String ROOT_PATH = "/index.html";
    public static final String HOME_PATH = "/home";

    public static void renderHomePage(Context context){

        Map<String, Object> viewModel = Map.of();

        context.render("home.html", viewModel);
    }
}