package utilities;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.commons.logging.LogFactory;

public class Transfer {
    private String card_no_sender;
    private String card_no_receiver;
    private Integer amount;

    public Transfer(String card_no_sender, String card_no_receiver, Integer amount) {
        this.card_no_sender = card_no_sender;
        this.card_no_receiver = card_no_receiver;
        this.amount = amount;
    }

    public JSONObject transferToBuy() {
        try {
            String urlpost = "http://localhost:3000/api/transfer";
            HttpClient httpclient = HttpClientBuilder.create().build();
            HttpPost httppost = new HttpPost(urlpost);

            StringEntity pars = new StringEntity(
                    "{\"card_no_sender\":\"" + this.card_no_sender +
                    "\",\"card_no_receiver\":\"" + this.card_no_receiver +
                    "\",\"amount\":" + this.amount + "}");
            httppost.addHeader("content-type", "application/x-www-form-urlencoded");
            httppost.setEntity(pars);
            HttpResponse response = httpclient.execute(httppost);
            JSONObject JSONresponse = new JSONObject(response);
            return JSONresponse;
        } catch (Exception err) {
            System.out.println(err);
        }
        return null;
    }
}