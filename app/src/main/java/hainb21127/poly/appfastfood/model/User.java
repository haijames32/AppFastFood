package hainb21127.poly.appfastfood.model;

public class User_Register {
    private String fullname;
    private int phone;
    private String address;

    public User_Register() {
    }

    public User_Register(String fullname, int phone, String address) {
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
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
