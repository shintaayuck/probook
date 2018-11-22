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
    
        QName qName = new QName("http://services", "BookServiceImplService");
    
        Service service = Service.create(wsdlURL, qName);
        
        BookService bs = service.getPort(BookService.class);
    
        Book b1 = new Book();
        b1.setBookID("1");
        b1.setTitle("Buku Pertama");
        String[] a1 = {"Author pertama"};
        b1.setAuthors(a1);
        b1.setDescription("Ini buku pertama lho");
        b1.setBookPrice(10000);
    
        Book b2 = new Book();
        b2.setBookID("2");
        b2.setTitle("Buku Kedua");
        String[] a2 = {"Author kedua"};
        b2.setAuthors(a2);
        b2.setDescription("Ini buku kedua lho");
        b2.setBookPrice(20000);
        
        
        System.out.println("Add book status="+bs.addBook(b1));
        System.out.println("Add book status="+bs.addBook(b2));
        
        System.out.println(bs.getBook("1"));
    }
}

