package models;


import java.io.Serializable;
import java.util.Arrays;

public class Book {
    private String bookID;
    private String title;
    private String[] authors;
    private String description;
    private Integer bookPrice;
    private String[] categories;
    
    
    public Book() {}
    public Book(String bookID, String title, String[] authors, String description, Integer bookPrice, String[] categories) {
        this.bookID = bookID;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.bookPrice = bookPrice;
        this.categories = categories;
    }
    
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
    
    public Integer getBookPrice() {
        return bookPrice;
    }
    
    public void setBookPrice(Integer bookPrice) {
        this.bookPrice = bookPrice;
    }
    
    public String[] getCategories() {
        return categories;
    }
    
    public void setCategory(String[] categories) {
        this.categories = categories;
    }
    
    @Override
    public String toString() {
        return "Book{" + "bookID='" + bookID + '\'' + ", title='" + title + '\'' + ", authors=" + Arrays.toString(authors) + ", description='" + description + '\'' + ", bookPrice=" + bookPrice + '}';
    }
}