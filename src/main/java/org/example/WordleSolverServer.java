package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.jetbrains.annotations.NotNull;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.example.app.Sessions;
import org.example.home.HomeController;

import static io.javalin.apibuilder.ApiBuilder.*;

public class WordleSolverServer {
    private static final int DEFAULT_PORT = 7070;
    private static final String STATIC_DIR = "/html";

    /**
     * The main class starts the server on the default port 7070.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Javalin app = getInstance();
        app.start(DEFAULT_PORT);
    }

    /**
     * This is a convenience for running Selenium tests.
     * It allows the test to get access to the server to start and stop it.
     * @return a configured server for the app
     */
    public static Javalin getInstance() {
        configureThymeleafTemplateEngine();
        Javalin server = createAndConfigureServer();
        setupRoutes(server);
        return server;
    }

    /**
     * Setup the Thymeleaf template engine to load templates from 'resources/templates'
     */
    private static void configureThymeleafTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateEngine.setTemplateResolver(templateResolver);

        templateEngine.addDialect(new LayoutDialect());
        JavalinThymeleaf.configure(templateEngine);
    }

    private static void setupRoutes(Javalin server) {
        server.routes(() -> {
            homeRoute();
        });
    }

    private static void homeRoute() {
        get(HomeController.HOME_PATH, HomeController::renderHomePage);
    }

    @NotNull
    private static Javalin createAndConfigureServer() {
        return Javalin.create(config -> {
            config.addStaticFiles(STATIC_DIR, Location.CLASSPATH);
            config.sessionHandler(Sessions::nopersistSessionHandler);
        });
    }
}
