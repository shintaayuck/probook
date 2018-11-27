package services;

import models.Book;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class SOAPPublisherClient {
    public static void main(String[] args) throws MalformedURLException {
        URL wsdlURL = new URL("http://localhost:8000/api/books?wsdl");
    
        QName qName = new QName("http://services/", "BookServiceImplService");
    
        Service service = Service.create(wsdlURL, qName);
        
        BookService bs = service.getPort(BookService.class);
        
        
        bs.searchBook("Shinta");
        System.out.println("WEY");
    }
}

