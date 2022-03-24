package org.example.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import kong.unirest.json.JSONArray;


public class DBLoader {
    public static final String dbName = "words.sqlite";
    public static final String dbUrl = "jdbc:sqlite:words.sqlite";

    public DBLoader() {
        openDBFile();
    }

    public int createDB() {
        String tableQuery;
        try (Connection connection = DriverManager.getConnection(dbUrl);
             final Statement statement = connection.createStatement()
        ) {
            tableQuery = new StringBuilder().append("CREATE TABLE words (")
                    .append("id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,")
                    .append("word TEXT")
                    .append(")")
                    .toString();

            statement.executeUpdate(tableQuery);
            addWords(statement);

            System.out.println("Table created successfully");
            return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return e.getErrorCode();
        }
    }

    public void addWords(Statement statement) {

        ArrayList<String> words = loadWords();

        for (String word : words){
            String addQuoteQuery = String.format("INSERT INTO words ( word ) VALUES ( '%s' )", word);
            try {
                statement.executeUpdate(addQuoteQuery);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static ArrayList<String> loadWords() {
        ArrayList<String> words = null;
        try {
            words = (ArrayList<String>) Files.readAllLines(Path.of("src/main/java/org/example/data/words.txt"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return words;
    }

    public ArrayList<String> getWords() {
        String getQuoteQuery = String.format("SELECT * FROM words");
        try (Connection connection = DriverManager.getConnection(dbUrl);
             final Statement stmt = connection.createStatement()
        ) {
            ResultSet res = stmt.executeQuery(getQuoteQuery);

            ArrayList<String> words = new ArrayList<>();

            while (res.next()) {
                String word = res.getString("word");
                words.add(word);
            }

            return words;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void openDBFile() {
        try {
            boolean dbFile = new File(dbName).createNewFile();
            if (dbFile) {
                System.out.printf("Database file: \"%s\" not found.\n", dbName);
                System.out.printf("Database file \"%s\" created.\n", dbName);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

//    public void addQuote(String text, String name) {
//        String addQuoteQuery = String.format("INSERT INTO quotes ( text, name ) VALUES ( '%s' , '%s' )", text, name);
//        try (Connection connection = DriverManager.getConnection(dbUrl);
//             final Statement stmt = connection.createStatement()
//        ) {
//            stmt.executeUpdate(addQuoteQuery);
//
//            System.out.println("Added Quote" + "{'text': '" + text + "','name': '" + name + "'}");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public QuoteDO getQuote(int id) {
//        String getQuoteQuery = String.format("SELECT * FROM quotes WHERE ID = %d", id);
//        try (Connection connection = DriverManager.getConnection(dbUrl);
//             final Statement stmt = connection.createStatement()
//        ) {
//            ResultSet res = stmt.executeQuery(getQuoteQuery);
//            QuoteDO quote = new QuoteDO(res.getInt("id"), res.getString("text"),
//                    res.getString("name"));
//            System.out.println("Got Quote: " + quote);
//            return quote;
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
//    }
//
//    public QuoteDO getLast() {
//        String getQuoteQuery = String.format("SELECT * FROM quotes ORDER BY ID DESC LIMIT 1");
//        try (Connection connection = DriverManager.getConnection(dbUrl);
//             final Statement stmt = connection.createStatement()
//        ) {
//            ResultSet res = stmt.executeQuery(getQuoteQuery);
//            QuoteDO quote = new QuoteDO(res.getInt("id"), res.getString("text"),
//                    res.getString("name"));
//            System.out.println("Got Quote: " + quote);
//            return quote;
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
//    }
//
//    public QuoteDO removeQuote(int id) {
//        String removeQuoteQuery = String.format("DELETE FROM quotes where id = %d", id);
//        QuoteDO quote = getQuote(id);
//        try (Connection connection = DriverManager.getConnection(dbUrl);
//             final Statement stmt = connection.createStatement()
//        ) {
//            stmt.execute(removeQuoteQuery);
//
//            System.out.println("Removed Quote: " + quote.toString());
//            return quote;
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
//    }
//
//    public List<QuoteDO> getQuotes() {
//        String getQuoteQuery = String.format("SELECT * FROM quotes");
//        try (Connection connection = DriverManager.getConnection(dbUrl);
//             final Statement stmt = connection.createStatement()
//        ) {
//            ResultSet res = stmt.executeQuery(getQuoteQuery);
//
//            List<QuoteDO> quotes = new ArrayList<QuoteDO>();
//
//            while (res.next()) {
//                QuoteDO quote = new QuoteDO(res.getInt("id"), res.getString("text"),
//                        res.getString("name"));
//                quotes.add(quote);
//            }
//
//            System.out.println("Got Quotes");
//            return quotes;
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
//    }


    }
