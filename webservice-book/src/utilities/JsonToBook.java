package utilities;

import models.Book;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonToBook {
    public Book translateToBook(JSONObject jsonObject) throws JSONException {
        try {
            String id = "";
            String title = "";
            String[] authors = {""};
            String description = "";
            Integer price = 0;
    
    
            id = jsonObject.get("id").toString();
    
    
            JSONObject volumeInfo = new JSONObject(jsonObject.get("volumeInfo").toString());
            if (volumeInfo.has("title")) {
                title = volumeInfo.get("title").toString();
            }
            if (volumeInfo.has("authors")) {
                JSONArray authorsJSON = new JSONArray(volumeInfo.get("authors").toString());
                authors = new String[authorsJSON.length()];
                for(int j = 0; j < authorsJSON.length(); j++) {
                    authors[j] = authorsJSON.get(j).toString();
                }
            }
            if (volumeInfo.has("description")) {
                description = volumeInfo.get("description").toString();
            }
    
    
            if (jsonObject.has("saleInfo")) {
                JSONObject saleInfo = new JSONObject(jsonObject.get("saleInfo").toString());
                if (saleInfo.has("listPrice")) {
                    JSONObject listPrice = new JSONObject(saleInfo.get("listPrice").toString());
                    if (listPrice.has("amount")) {
                        price = listPrice.getInt("amount");
                    }
                }
            }
            Book bookResult = new Book(id, title, authors, description, price);
            return bookResult;
            
        } catch (JSONException e) {
        
        }
        return null;
       
    }
}
