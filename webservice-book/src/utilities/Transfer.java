package utilities;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.slf4j.LoggerFactory;


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
            httppost.addHeader("content-type", "application/json");
            httppost.setEntity(pars);
            Header[] headers = httppost.getAllHeaders();
            String content = EntityUtils.toString(pars);
            System.out.println(httppost.toString());
            for (Header header : headers) {
                System.out.println(header.getName() + ": " + header.getValue());
            }
            System.out.println();
            System.out.println(content);
            System.out.println("executing request");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String strentity = EntityUtils.toString(entity);
            JSONObject JSONresponse = new JSONObject(strentity);
            System.out.println("sending JSON");
            return JSONresponse;
        } catch (Exception err) {
            System.out.println(err);
        }
        System.out.println("sending null");
        return null;
    }
}