package hainb21127.poly.appfastfood.model;

public class Order {
    String id;
    User id_user;
    String date;
    String thanhtoan;
    String trangthai;
    int tongdonhang;

    public Order() {
    }

    public Order(String date, String thanhtoan, String trangthai, int tongdonhang) {
        this.date = date;
        this.thanhtoan = thanhtoan;
        this.trangthai = trangthai;
        this.tongdonhang = tongdonhang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getId_user() {
        return id_user;
    }

    public void setId_user(User id_user) {
        this.id_user = id_user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public int getTongdonhang() {
        return tongdonhang;
    }

    public void setTongdonhang(int tongdonhang) {
        this.tongdonhang = tongdonhang;
    }

    public String getThanhtoan() {
        return thanhtoan;
    }

    public void setThanhtoan(String thanhtoan) {
        this.thanhtoan = thanhtoan;
    }
}
