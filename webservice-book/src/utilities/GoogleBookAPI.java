package utilities;
import org.json.JSONObject;


public class GoogleBookAPI {
    private String queryTitle;
    private String API_KEY = "AIzaSyBZmgEtwukg-N_eCzML5wQiKHcOKEOEu1Q";
    
    
    public GoogleBookAPI(String queryTitle) {
        this.queryTitle = queryTitle;
    }
    
    public JSONObject searchBook() {
        try {
            HTTPRequest googleBookAPIRequest = new HTTPRequest(String.format("https://www.googleapis.com/books/v1/volumes?q=%s&key=%s", this.queryTitle, this.API_KEY));
            String resultRequest = googleBookAPIRequest.doRequest("GET");
            JSONObject hasiljson = new JSONObject(resultRequest);
            return hasiljson;
        } catch (Exception err) {
            System.out.println(err);
        }
        
        
        return null;
    }
}