package myMovie.bean;

import java.io.Serializable;

public class Business extends User {


    private String shopName;   // name of the business
    private String location;



    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
