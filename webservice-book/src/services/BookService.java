package services;

import models.Book;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface BookService {
    
    /**
     * Get array of book based on query, fetch the data from Google Books API. Save top 5 result to database.
     * @param query
     * @return Book[]
     */
    @WebMethod
    public Book[] searchBook(String query);
    
    /**
     * Get Book based on bookID, fetch book from GoogleBooksAPI, check if exist in database.
     * price will be -1 if price or id doesn't exist in database, indicating not for sale.
     * @param bookID
     * @return Book
     */
    @WebMethod
    public Book getBook(String bookID);
    
    
}
