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

    protected Book getRecommendFromDatabase(String[] categories, String bookID){
        try {
            Book result = new Book();
            BookServiceImpl bookservice = new BookServiceImpl();
            // Get books from database
            Connection con = getConnection();
            String queryCategory = " ";
            for (int i = 0; i < categories.length ; i++) {
                if (i == (categories.length-1)) {
                    queryCategory = queryCategory.concat("category = \"" + categories[i] + "\"");
                } else {
                    queryCategory = queryCategory.concat("category = \"" + categories[i] + "\" OR ");
                }
            }
            Statement p = con.createStatement();
            String queryGetBookID = "SELECT bookid FROM books NATURAL JOIN (SELECT bookid FROM book_category WHERE" + queryCategory + ") AS res WHERE bookid <> \"" + bookID + "\" ORDER BY boughtqty DESC LIMIT 1;";
            System.out.println(queryGetBookID);
            ResultSet resultSet = p.executeQuery(queryGetBookID);
            if (resultSet.next()){
                String idres = resultSet.getString("bookid");
                System.out.println(idres);
                result = bookservice.getBook(idres);
            }
            closeConnection(con);
            return result;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected Book getRecommendFromRandom(String[] categories) {
        BookServiceImpl bookservice = new BookServiceImpl();
        Book result = new Book();
        Random rand = new Random();
        int i = rand.nextInt(categories.length);
        GoogleBookAPI googleBookAPI = new GoogleBookAPI("subject:" + categories[i]);
        JSONObject semiResult = googleBookAPI.searchBook();
        try {
            JSONArray arrayResult = new JSONArray(semiResult.get("items").toString());
            int j = rand.nextInt(arrayResult.length());
            JSONObject resjson = new JSONObject(arrayResult.get(j).toString());
            JsonToBook translator = new JsonToBook();
            result = translator.translateToBook(resjson);
            if (result != null){
                result.setBookPrice(bookservice.getPrice(result.getBookID()));
                return result;
            }
            System.out.println("No book from google books api");
            return null;
        } catch (JSONException | NullPointerException err) {
            System.out.println(err);
            return null;
        }
    }

    @Override
    public Book getRecommendedBook(String[] categories, String bookID){
        // Initialize categories from bookID, results container and bookservice
        Book result = new Book();
        /*
        ArrayList<String> categories = getCategories(bookID);
        if (categories == null){
            return null;
        }
        else {
        */
        // Check the books from database
        result = getRecommendFromDatabase(categories, bookID);
        // If there is no result from database, get one random books from googlebookapi
        if (result == null) {
            result = getRecommendFromRandom(categories);
        }

        if (result == null) {
            System.out.println("null");
            return null;
        }
        return result;
    }
}
