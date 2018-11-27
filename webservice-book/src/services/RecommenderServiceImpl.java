package services;

import models.Book;
import org.json.JSONObject;
import org.json.JSONException;
import utilities.ConnectionMySQL;
import utilities.GoogleBookAPI;
import utilities.JsonToBook;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@WebService(endpointInterface = "services.RecommenderService")
public class RecommenderServiceImpl implements RecommenderService {
    @Override
    public List<Book> getRecommendedBook(String[] categories){
        found = true;
        List<Book> results = new ArrayList<Book>();
        BookServiceImpl bookservice = new BookServiceImpl();
        // Get books from database
        try {
            String queryGetCategory = "SELECT bookid FROM books NATURAL JOIN (SELECT bookid FROM book_category WHERE category = (?)) AS res WHERE boughtqty = (SELECT MAX(boughtqty) FROM books NATURAL JOIN book_category WHERE category = (?));";
            for (String category : categories) {
                p = con.prepareStatement(queryGetCategory);
                p.setString(1, category);
                p.setString(2, category);
                ResultSet resultSet = p.executeQuery();

                String idres = resultSet.getString("bookid");
                Book bookres = new Book();
                bookres = bookservice.getBook(idres);
                if (bookres != null){
                    results.add(bookres);
                }
            }
            closeConnection(con);

            if (results.isEmpty()){
                // Get one random books from googlebookapi
                Random rand = new Random();
                int i = rand.nextInt(categories.size());

            }
            else {
                return results;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}