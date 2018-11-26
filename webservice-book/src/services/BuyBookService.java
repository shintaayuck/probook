package services;

import models.BuyStatus;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface BuyBookService {
    
    @WebMethod
    public BuyStatus buyBook(String bookID, Integer bookAmount, String senderCardNumber);
}
