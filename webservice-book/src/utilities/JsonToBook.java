package utilities;

import models.Book;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonToBook {
    public Book translateToBook(JSONObject jsonObject) throws JSONException {
        String id = "";
        String title = "";
        String[] authors = {""};
        String description = "";
        Integer price = 0;
        String[] categories = {""};
        String imgsrc = "";
        

        id = jsonObject.get("id").toString();


        JSONObject volumeInfo = new JSONObject(jsonObject.get("volumeInfo").toString());
        if (volumeInfo.has("title")) {
            title = volumeInfo.get("title").toString();
        } else {
            title = "Untitled";
        }
        
        if (volumeInfo.has("authors")) {
            JSONArray authorsJSON = new JSONArray(volumeInfo.get("authors").toString());
            authors = new String[authorsJSON.length()];
            for(int j = 0; j < authorsJSON.length(); j++) {
                authors[j] = authorsJSON.get(j).toString();
            }
        } else {
            authors = new String[1];
            authors[0] = "Anonymous";
        }
        
        if (volumeInfo.has("description")) {
            description = volumeInfo.get("description").toString();
        } else {
            description = "No description provided.";
        }
        
        if (volumeInfo.has("categories")) {
            JSONArray categoriesJSON = new JSONArray(volumeInfo.get("categories").toString());
            categories = new String[categoriesJSON.length()];
            for (int j = 0; j < categoriesJSON.length(); j++) {
                categories[j] = categoriesJSON.get(j).toString();
            }
        } else {
            categories = new String[1];
            categories[0] = "Uncategorized";
        }
        
        if (volumeInfo.has("imageLinks")) {
            JSONObject imageLinks = new JSONObject(volumeInfo.get("imageLinks").toString());
            if (imageLinks.has("thumbnail")) {
                imgsrc = imageLinks.get("thumbnail").toString();
            } else {
                imgsrc = null;
            }
        } else {
            imgsrc = null;
        }
        


        if (jsonObject.has("saleInfo")) {
            JSONObject saleInfo = new JSONObject(jsonObject.get("saleInfo").toString());
            if (saleInfo.has("listPrice")) {
                JSONObject listPrice = new JSONObject(saleInfo.get("listPrice").toString());
                if (listPrice.has("amount")) {
                    price = listPrice.getInt("amount");
                } else {
                    price = -1;
                }
            }
        }
        
        Book bookResult = new Book(id, title, authors, description, price, categories, imgsrc);
        return bookResult;
       
    }
}
