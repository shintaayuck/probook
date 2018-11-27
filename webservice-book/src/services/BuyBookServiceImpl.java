package services;

import models.BuyStatus;
import utilities.Transfer;
import org.json.JSONObject;
import org.json.JSONException;

import javax.jws.WebService;

@WebService
public class BuyBookServiceImpl implements BuyBookService {
    @Override
    public BuyStatus buyBook(String bookID, Integer bookAmount, String senderCardNumber) {
        String receiverCardNumber = "1111222233334444";
        //SELECT price FROM book WHERE book_id = bookID
        Integer price = 50000;
        Integer amount = bookAmount * price;
        Transfer transfer = new Transfer(senderCardNumber, receiverCardNumber, amount);
        JSONObject hasilJSON = transfer.transferToBuy();
        try {
            String strcode = hasilJSON.get("code").toString();
            Integer code = Integer.parseInt(strcode);
            BuyStatus resultStatus = new BuyStatus(code);
            if (code == 0){
                //UPDATE table book
            }
            return resultStatus;
        } catch (JSONException err) {
            System.out.println(err);
        }
        return null;
    }
}
