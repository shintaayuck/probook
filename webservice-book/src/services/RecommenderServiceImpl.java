package services;

import models.Book;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import utilities.GoogleBookAPI;
import utilities.JsonToBook;

import javax.jws.WebService;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Random;
import java.sql.*;

import static utilities.ConnectionMySQL.closeConnection;
import static utilities.ConnectionMySQL.getConnection;

@WebService
public class RecommenderServiceImpl implements RecommenderService {
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
            String queryGetBookID = "SELECT bookid FROM books NATURAL JOIN (SELECT bookid FROM book_category WHERE" + queryCategory + ") AS res WHERE bookid <> \"" + bookID + "\" AND boughtqty <> 0 ORDER BY boughtqty DESC LIMIT 1;";
            System.out.println(queryGetBookID);
            ResultSet resultSet = p.executeQuery(queryGetBookID);
            if (resultSet.next()){
                String idres = resultSet.getString("bookid");
                System.out.println(idres);
                result = bookservice.getBook(idres);
                System.out.println("Hasil");
                System.out.println(result);
            }
            closeConnection(con);
            return result;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected Book getRecommendFromRandom(String[] categories) {
        try {
            System.out.println("Masuk recommend from random");
            BookServiceImpl bookservice = new BookServiceImpl();
            Book result = new Book();
            Random rand = new Random();
            int i = rand.nextInt(categories.length);
            String cat = categories[i];
            cat = cat.replace("'", "%27");
            cat = cat.replace("(", "%28");
            cat = cat.replace(")", "%29");
            cat = cat.replace(",", "%2C");
            cat = cat.replace(" ", "%20");
            System.out.println(cat);
            GoogleBookAPI googleBookAPI = new GoogleBookAPI("categories:" + cat);
            JSONObject semiResult = googleBookAPI.searchBook();
            JSONArray arrayResult = new JSONArray(semiResult.get("items").toString());
            boolean found = false;
            int j = 0;
            while (!(found)){
                JSONObject resjson = new JSONObject(arrayResult.get(j).toString());
                JsonToBook translator = new JsonToBook();
                result = translator.translateToBook(resjson);
                String[] res_cat = result.getCategories();
                for (int k = 0; k < res_cat.length ; k ++){
                    if (res_cat[k] == categories[i]) {
                        found = true;
                        break;
                    }
                }
            }
            if (found){
                result.setBookPrice(bookservice.getPrice(result.getBookID()));
                System.out.println("Get book from google books api");
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
        // Check the books from database
        result = getRecommendFromDatabase(categories, bookID);
        // If there is no result from database, get one random books from googlebookapi
        String book_id = result.getBookID();
        if (book_id == null) {
            result = getRecommendFromRandom(categories);
        }
        return result;
    }
}
