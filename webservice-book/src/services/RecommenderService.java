package services;

import models.Book;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface RecommenderService {
    /**
     * Get Recommended Book based on categories, fetch top bought book from database or find a random book from GoogleBooksAPI.
     * Recommended books might be multiple if it is fetch from database, but if there are no book foound from the database,
     * there will be one random book fetch from GoogleBooksAPI.
     * @param String
     * @return Book
     */
    @WebMethod
    public Book[] getRecommendedBook(String[] categories, String bookID);
}
