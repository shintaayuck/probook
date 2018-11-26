package models;

import java.io.Serializable;

public class BuyStatus implements Serializable {
    private Integer statusCode;
    
    public BuyStatus() {}
    public BuyStatus(Integer statusCode){
        this.statusCode = statusCode;
    }
    
    public Integer getStatusCode() { return statusCode; }
    
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    
    @Override
    public String toString() {
        return "statusCode = " + statusCode;
    }
}
