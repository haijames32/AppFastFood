package hainb21127.poly.appfastfood.model;

public class Order {
    String id;
    String id_user;
    String date;
    String trangthai;
    int tongtien;

    public Order() {
    }

    public Order(String id, String id_user, String date, String trangthai, int tongtien) {
        this.id = id;
        this.id_user = id_user;
        this.date = date;
        this.trangthai = trangthai;
        this.tongtien = tongtien;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
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

    public int getTongtien() {
        return tongtien;
    }

    public void setTongtien(int tongtien) {
        this.tongtien = tongtien;
    }
}
