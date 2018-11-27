package services;

import javax.xml.ws.Endpoint;

public class SOAPPublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8000/api/books", new BookServiceImpl());
        Endpoint.publish("http://localhost:8000/api/buy-books", new BuyBookServiceImpl());
    }
}