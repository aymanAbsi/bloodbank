package com.example.app.bloodbank;

public class acceptclass {
    private String accName;
    private String accPhone;
    private String accEmail;
    public acceptclass()
    {

    }

    public acceptclass(String accName, String accPhone, String accEmail) {
        this.accName = accName;
        this.accPhone = accPhone;
        this.accEmail = accEmail;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAccPhone() {
        return accPhone;
    }

    public void setAccPhone(String accPhone) {
        this.accPhone = accPhone;
    }

    public String getAccEmail() {
        return accEmail;
    }

    public void setAccEmail(String accEmail) {
        this.accEmail = accEmail;
    }
}
