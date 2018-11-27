package services;

import models.Book;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utilities.ConnectionMySQL;
import utilities.GoogleBookAPI;
import utilities.JsonToBook;

import javax.jws.WebService;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static utilities.ConnectionMySQL.closeConnection;
import static utilities.ConnectionMySQL.getConnection;

@WebService()
public class BookServiceImpl implements BookService {
    private static Map<String, Book> books = new HashMap<String, Book>();
    
    
    @Override
    public Book getBook(String bookID) {
        GoogleBookAPI googleBookAPI = new GoogleBookAPI("id:" + bookID);
        JSONObject semiResult = googleBookAPI.searchBook();
        
        try {
            JSONArray arrayResult = new JSONArray(semiResult.get("items").toString());
            JSONObject hasilJSON = new JSONObject(arrayResult.get(0).toString());
            JsonToBook translator = new JsonToBook();
            Book book = translator.translateToBook(hasilJSON);
            book.setBookPrice(getPrice(bookID));
            return book;
        } catch (JSONException err) {
            System.out.println(err);
        }
        return null;
    }
    
    /**
     * Get price from database. If exist, return the price. If doesn't exist, return -1
     *
     * @param id
     * @return Integer price
     */
    protected Integer getPrice(String id) {
        try {
            String query = "SELECT price FROM books WHERE bookid = (?);";
            Connection con = getConnection();

            PreparedStatement p = con.prepareStatement(query);
            p.setString(1, id);

            ResultSet resultSet = p.executeQuery();

            Integer price;
            if (resultSet.next()) {
                price = resultSet.getInt("price");
            } else {
                price = -1;
            }

            closeConnection(con);

            return price;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Book[] searchBook(String query) {
        System.out.println(query);
        GoogleBookAPI googleBookAPI = new GoogleBookAPI(query);
        JSONObject hasilJSON = googleBookAPI.searchBook();
        try {
            JSONArray resultItems = new JSONArray(hasilJSON.get("items").toString());
            Book[] bookResults = new Book[resultItems.length()];
            
            for (int i = 0; i < Math.min(10, resultItems.length()); i++) {
                JsonToBook translator = new JsonToBook();
                Book bookResult = translator.translateToBook(new JSONObject(resultItems.get(i).toString()));
                bookResults[i] = bookResult;
                if (i < 5) {
                    Boolean isSuccess = insertBook(bookResult);
                }
            }
            return bookResults;
        } catch (JSONException | NullPointerException err) {
            System.out.println(err);
        }
    
    
        return null;
    }
    
    /**
     * Insert book to database. Return true if success, false if failed
     *
     * @param book
     * @return boolean
     */
    protected Boolean insertBook(Book book) {
    
        try {
            String queryInsertBook = "INSERT INTO books VALUES (?, ?, ? );";
            String queryGetBook = "SELECT COUNT(bookid) AS id FROM books WHERE bookid = (?);";
            Connection con = getConnection();
        
            PreparedStatement pget = con.prepareStatement(queryGetBook);
            pget.setString(1, book.getBookID());
            ResultSet res = pget.executeQuery();
        
            if (res.next()) {
                if (res.getInt("id") == 0) {
                    PreparedStatement p = con.prepareStatement(queryInsertBook);
                    p.setString(1, book.getBookID());
                    p.setInt(2, book.getBookPrice());
                    p.setInt(3, 0);
                    p.execute();
    
                    String queryGetCategory = "SELECT COUNT(bookid) as length FROM book_category WHERE bookid = (?) AND category = (?);";
                    for (String category : book.getCategories()) {
                        p = con.prepareStatement(queryGetCategory);
                        p.setString(1, book.getBookID());
                        p.setString(2, category);
                        ResultSet resultSet = p.executeQuery();
    
                        Integer categoryLength;
                        if (resultSet.next()) {
                            categoryLength = resultSet.getInt("length");
                        } else {
                            categoryLength = 0;
                        }
    
                        if (categoryLength == 0) {
                            String queryInsertCategory = "INSERT INTO book_category VALUES (?,?);";
                            p = con.prepareStatement(queryInsertCategory);
                            p.setString(1, book.getBookID());
                            p.setString(2, category);
                            p.execute();
                        }
                    }
                }
            }
        
            closeConnection(con);
        
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    
        return false;
    }
}
    