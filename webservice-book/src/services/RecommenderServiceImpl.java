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
import javax.jws.WebService;

import static utilities.ConnectionMySQL.closeConnection;
import static utilities.ConnectionMySQL.getConnection;
@WebService(endpointInterface = "services.RecommenderService")
public class RecommenderServiceImpl implements RecommenderService {
    @Override
    public Book[] getRecommendedBook(String bookID){
        try {
            // Create categories array
            String queryGetCategory = "SELECT category FROM book_category WHERE bookid = (?);";
            Connection con = getConnection();
            ArrayList<String> categories = new ArrayList<String>();
            PreparedStatement pc = con.prepareStatement(queryGetCategory);
            pc.setString(1, bookID);

            boolean hasResults = pc.execute();
            while (hasResults){
                ResultSet resultSetCategory = pc.getResultSet();
                String category = resultSetCategory.getString("category");
                categories.add(category);
                hasResults = pc.getMoreResults();
            }
            ArrayList<Book> results = new ArrayList<Book>();
            BookServiceImpl bookservice = new BookServiceImpl();

            // Get books from database
            String queryGetBookID = "SELECT bookid FROM books NATURAL JOIN (SELECT bookid FROM book_category WHERE category = (?)) AS res WHERE boughtqty = (SELECT MAX(boughtqty) FROM books NATURAL JOIN book_category WHERE category = (?) AND boughtqty <> 0);";
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

            // If there is no result from database, get one random books from googlebookapi
            if (results.isEmpty()) {
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
                } catch (JSONException err) {
                    System.out.println(err);
                    return null;
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
