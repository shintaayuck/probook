package services;

import models.Book;

import javax.jws.WebService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebService(endpointInterface = "services.BookService")
public class BookServiceImpl implements BookService {
    private static Map<String, Book> books = new HashMap<String, Book>();

    @Override
    public boolean addBook(Book b) {
        if (books.get(b.getBookID()) != null) return false;
        books.put(b.getBookID(), b);
        return true;
    }

    @Override
    public Book getBook(String bookID) {
        return books.get(bookID);
    }

    @Override
    public Book[] getBookByQuery(String query) {
        Set<String> bookIDs = books.keySet();
        Book[] b = new Book[bookIDs.size()];
        int i = 0;
        for (String bookID : bookIDs) {
            b[i] = books.get(bookID);
            i++;
        }
        return b;
    }
}