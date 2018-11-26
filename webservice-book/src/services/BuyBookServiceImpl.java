package services;

import models.BuyStatus;
import utilities.Transfer;

import javax.jws.WebService;

@WebService
public class BuyBookServiceImpl implements BuyBookService {
    @Override
    public BuyStatus buyBook(String bookID, Integer bookAmount, String senderCardNumber) {
        String receiverCardNumber = "0000000000000000";
        //SELECT price FROM book WHERE book_id = bookID
        Integer amount = bookAmount * price;
        Transfer transfer = new Transfer(senderCardNumber, receiverCardNumber, amount);
        JSONObject hasilJSON = new JSONObject();
        hasilJSON = transfer.transferToBuy();
        try {
            Integer code = hasilJSON.get("code");
            if (code != 0) {
                //error
            } else {
                //UPDATE BOOK bought = bought + amount;
                BuyStatus resultStatus = new BuyStatus(1);
                return resultStatus;
            }
        } catch (JSONException err) {
            System.out.println(err);
        }
        return null;
    }
}
