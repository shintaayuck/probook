package services;

import models.BuyStatus;
import utilities.Transfer;
import org.json.JSONObject;
import org.json.JSONException;
import utilities.ConnectionMySQL;

import java.sql.*;
import javax.jws.WebService;

import static utilities.ConnectionMySQL.closeConnection;
import static utilities.ConnectionMySQL.getConnection;

@WebService
public class BuyBookServiceImpl implements BuyBookService {

    /**
     * Get price from database. If exist, return the price. If doesn't exist, return -1
     *
     * @param id
     * @return Integer price
     */
    protected Integer getPrice(String id) {
        try {
            String query = "SELECT price FROM books WHERE bookid = (?);";
            Connection con = getConnection();

            PreparedStatement p = con.prepareStatement(query);
            p.setString(1, id);

            ResultSet resultSet = p.executeQuery();

            Integer price;
            if (resultSet.next()) {
                price = resultSet.getInt("price");
            } else {
                price = -1;
            }

            closeConnection(con);

            return price;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public BuyStatus buyBook(String bookID, Integer bookAmount, String senderCardNumber) {
        String receiverCardNumber = "0000000000000000";
        Integer price = getPrice(bookID);
        Integer amount = bookAmount * price;
        Transfer transfer = new Transfer(senderCardNumber, receiverCardNumber, amount);
        JSONObject hasilJSON = transfer.transferToBuy();
        try {
            String strcode = hasilJSON.get("code").toString();
            Integer code = Integer.parseInt(strcode);
            BuyStatus resultStatus = new BuyStatus(code);
            if (code == 0){
                String queryUpdateBook = "UPDATE books SET boughtqty = boughtqty + (?) WHERE bookid = (?);";
                Connection con = getConnection();
                PreparedStatement pupdate = con.prepareStatement(queryUpdateBook);
                pupdate.setInt(1,bookAmount);
                pupdate.setString(2,bookID);
                pupdate.execute();
            }
            return resultStatus;
        } catch (JSONException err) {
            System.out.println(err);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
