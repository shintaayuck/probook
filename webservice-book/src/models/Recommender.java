package models;

import java.io.Serializable;
import java.util.Arrays;

public class Book implements Serializable {
    private String bookID;
    private String title;
    private String[] authors;
    private String description;
    private Long bookPrice;

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(Long bookPrice) {
        this.bookPrice = bookPrice;
    }

    @Override
    public String toString() {
        return "Book{" + "bookID='" + bookID + '\'' + ", title='" + title + '\'' + ", authors=" + Arrays.toString(authors) + ", description='" + description + '\'' + ", bookPrice=" + bookPrice + '}';
    }
}