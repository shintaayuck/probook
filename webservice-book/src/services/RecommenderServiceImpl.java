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
    /*
    protected ArrayList<String> getCategories(String bookID){
        try {
            // Create categories array
            ArrayList<String> categories = new ArrayList<String>();
            String queryGetCategory = "SELECT category FROM book_category WHERE bookid = \""+ bookID +"\";";
            Connection con = getConnection();
            Statement pc = con.createStatement();
            ResultSet resultSetCategory = pc.executeQuery(queryGetCategory);
            while (resultSetCategory.next()) {
                String category = resultSetCategory.getString("category");
                categories.add(category);
            }
            closeConnection(con);
            return categories;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    */

    protected ArrayList<Book> getRecommendFromDatabase(String[] categories, String bookID){
        ArrayList<Book> results = new ArrayList<Book>();
        try {
            BookServiceImpl bookservice = new BookServiceImpl();
            // Get books from database
            Connection con = getConnection();
            for (String category : categories) {
                System.out.println(category);
                String queryGetBookID = "SELECT bookid FROM books NATURAL JOIN (SELECT bookid FROM book_category WHERE category = \""+ category +"\") AS res WHERE bookid <> \"" + bookID + "\" ORDER BY boughtqty DESC LIMIT 1;";
                System.out.println(queryGetBookID);
                Statement p = con.createStatement();
                ResultSet resultSetBookID = p.executeQuery(queryGetBookID);
                while (resultSetBookID.next()){
                    String idres = resultSetBookID.getString("bookid");
                    Book bookres = bookservice.getBook(idres);
                    if (bookres != null){
                        results.add(bookres);
                    } else {
                        System.out.println("No book fetch");
                    }
                }
            }
            closeConnection(con);
            return results;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected ArrayList<Book> getRecommendFromRandom(String[] categories) {
        BookServiceImpl bookservice = new BookServiceImpl();
        ArrayList<Book> results = new ArrayList<Book>();
        Random rand = new Random();
        int i = rand.nextInt(categories.length);
        GoogleBookAPI googleBookAPI = new GoogleBookAPI("subject:" + categories[i]);
        JSONObject semiResult = googleBookAPI.searchBook();
        try {
            JSONArray arrayResult = new JSONArray(semiResult.get("items").toString());
            int j = rand.nextInt(arrayResult.length());
            JSONObject resjson = new JSONObject(arrayResult.get(j).toString());
            JsonToBook translator = new JsonToBook();
            Book book = translator.translateToBook(resjson);
            if (book != null){
                book.setBookPrice(bookservice.getPrice(book.getBookID()));
                results.add(book);
                return results;
            }
            System.out.println("No book from google books api");
            return null;
        } catch (JSONException | NullPointerException err) {
            System.out.println(err);
            return null;
        }
    }

    @Override
    public Book[] getRecommendedBook(String[] categories, String bookID){
        // Initialize categories from bookID, results container and bookservice
        ArrayList<Book> results = new ArrayList<Book>();
        /*
        ArrayList<String> categories = getCategories(bookID);
        if (categories == null){
            return null;
        }
        else {
        */
        // Check the books from database
        results = getRecommendFromDatabase(categories, bookID);
        // If there is no result from database, get one random books from googlebookapi
        if (results.isEmpty() || results == null) {
            results = getRecommendFromRandom(categories);
        }

        // ArrayList of Result to Array of Book
        if (results == null) {
            System.out.println("null");
            return null;
        }

        Book[] arrResult = new Book[results.size()];
        for (int k = 0 ; k < arrResult.length ; k++){
            Book bookelmt = results.get(k);
            System.out.println(bookelmt.getTitle());
            arrResult[k] = bookelmt;
        }
        return arrResult;
        //}
    }
}
