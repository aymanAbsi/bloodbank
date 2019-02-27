package com.example.app.bloodbank;

/**
 * Created by Ayman on 4/9/2018.
 */

public class test2 {

private String city ;
private String name ;
private String phone ;
private String redio ;
private String type ;
private String user ;


public test2()
{

}

    public test2(String city, String name, String phone, String user) {
        this.city = city;
        this.name = name;
        this.phone = phone;
        this.user = user;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRedio() {
        return redio;
    }

    public void setRedio(String redio) {
        this.redio = redio;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return  "Name  :  "+name+"\n\n"+
                "Phone : "+phone+"\n\n"+
                "Type   : "+type+"\n\n";
    }
}
