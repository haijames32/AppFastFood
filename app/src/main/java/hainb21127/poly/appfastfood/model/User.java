package hainb21127.poly.appfastfood.model;

public class User {
    private String id;
    private String email;
    private String fullname;
    private int phone;
    private String address;
    private String image;

    public User() {
    }

    public User(String email, String fullname, int phone, String address, String image) {
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}