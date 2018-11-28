package services;

import models.Book;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import utilities.GoogleBookAPI;
import utilities.JsonToBook;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Random;
import java.sql.*;

import static utilities.ConnectionMySQL.closeConnection;
import static utilities.ConnectionMySQL.getConnection;

@WebService
public class RecommenderServiceImpl implements RecommenderService {
    protected ArrayList<String> getCategories(String bookID){
        try {
            // Create categories array
            ArrayList<String> categories = new ArrayList<String>();
            String queryGetCategory = "SELECT category FROM book_category WHERE bookid = (?);";
            Connection con = getConnection();

            PreparedStatement pc = con.prepareStatement(queryGetCategory);
            pc.setString(1, bookID);

            // SALAH DISINI COYYYYYY
            boolean hasResults = pc.execute();
            while (hasResults) {
                System.out.println("Masih ada hasil");
                ResultSet resultSetCategory = pc.getResultSet();
                String category = resultSetCategory.getString("category");
                System.out.println(category);
                categories.add(category);
                hasResults = pc.getMoreResults();
            }
            System.out.println("Categories' size");
            System.out.println(categories.size());

            closeConnection(con);
            return categories;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Gabisa konek SQL");
            e.printStackTrace();
            return null;
        }
    }

    protected ArrayList<Book> getRecommendFromDatabase(ArrayList<String> categories){
        ArrayList<Book> results = new ArrayList<Book>();
        try {
            BookServiceImpl bookservice = new BookServiceImpl();
            // Get books from database
            String queryGetBookID = "SELECT bookid FROM books NATURAL JOIN (SELECT bookid FROM book_category WHERE category = (?)) AS res WHERE boughtqty = (SELECT MAX(boughtqty) FROM books NATURAL JOIN book_category WHERE category = (?) AND boughtqty <> 0);";
            Connection con = getConnection();
            for (String category : categories) {
                PreparedStatement p = con.prepareStatement(queryGetBookID);
                p.setString(1, category);
                p.setString(2, category);
                ResultSet resultSet = p.executeQuery();

                String idres = resultSet.getString("bookid");
                Book bookres = bookservice.getBook(idres);
                if (bookres != null){
                    results.add(bookres);
                }
            }
            closeConnection(con);
            return results;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected ArrayList<Book> getRecommendFromRandom(ArrayList<String> categories) {
        BookServiceImpl bookservice = new BookServiceImpl();
        ArrayList<Book> results = new ArrayList<Book>();
        Random rand = new Random();
        int i = rand.nextInt(categories.size());
        GoogleBookAPI googleBookAPI = new GoogleBookAPI("subject:" + categories.get(i));
        JSONObject semiResult = googleBookAPI.searchBook();
        try {
            JSONArray arrayResult = new JSONArray(semiResult.get("items").toString());
            int j = rand.nextInt(arrayResult.length());
            JSONObject resjson = new JSONObject(arrayResult.get(j).toString());
            JsonToBook translator = new JsonToBook();
            Book book = translator.translateToBook(resjson);
            book.setBookPrice(bookservice.getPrice(book.getBookID()));
            results.add(book);
            return results;
        } catch (JSONException err) {
            System.out.println(err);
            return null;
        }
    }

    @Override
    public Book[] getRecommendedBook(String bookID){
        System.out.println("Book ID");
        System.out.println(bookID);
        // Initialize categories from bookID, results container and bookservice
        ArrayList<Book> results = new ArrayList<Book>();
        ArrayList<String> categories = getCategories(bookID);
        if (categories == null){
            return null;
        }
        else {
            System.out.println("Categories");
            System.out.println(categories.size());
            // Check the books from database
            results = getRecommendFromDatabase(categories);

            // If there is no result from database, get one random books from googlebookapi
            if (results == null) {
                System.out.println("Not found in database");
                results = getRecommendFromRandom(categories);
                if (results == null){
                    System.out.println("Not found in google books api");
                }
            }

            // ArrayList of Result to Array of Book
            Book[] arrResult = new Book[results.size()];
            for (int k = 0 ; k < arrResult.length ; k++){
                Book bookelmt = results.get(k);
                System.out.println(bookelmt.getTitle());
                arrResult[k] = bookelmt;
            }
            return arrResult;
        }
    }
}