package com.example.app.bloodbank;

/**
 * Created by Ayman on 4/9/2018.
 */

public class test {


    public String Id;
    public String email;

    private String test;
    private String type;
    private String age;
    private String address;
    private String number;
    private String date;


    public test(String test, String age, String address, String type,String date,String number) {
        this.test = test;
        this.age = age;
        this.address = address;
        this.type = type;
        this.date=date;
        this.number=number ;
    }

    public test(){

    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test ) {
        this.test = test;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "Name : "+ test + "\n\n" + "Type blood : " + type +" \n \n "+ "Address : "+ address + " \n \n "+ "Age : "+ age
                +" \n\n "+"details :"+date+" \n\n "+"number :"+number+" \n\n"+"                                Click To Accept  ";


    }

}
