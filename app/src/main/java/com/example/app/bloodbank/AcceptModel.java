package com.example.app.bloodbank;

public class AcceptModel {
    public String Id;
    public String emaill;

    private String email, phone, name,provider_name;

    private String num, age, date, address, type;

    public AcceptModel() {
    }

    public AcceptModel(String email, String phone, String name,String provider_name, String num, String age, String date, String address, String type) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.provider_name = provider_name;
        this.num = num;
        this.age = age;
        this.date = date;
        this.address = address;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Name : "+ name + "\n\n" + "Phone : "+ phone + "\n\n" + "Email : "+ email + "\n\n" +" \n\n";


    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }
}
