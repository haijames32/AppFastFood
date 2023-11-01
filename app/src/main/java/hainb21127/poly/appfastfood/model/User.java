package hainb21127.poly.appfastfood.model;

public class User {
    private String email;
    private String fullname;
    private int phone;
    private String address;

    public User() {
    }

    public User(String email, String fullname, int phone, String address) {
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}