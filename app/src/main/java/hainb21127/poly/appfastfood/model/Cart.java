package hainb21127.poly.appfastfood.model;

public class Cart {
    String id;
    Product id_sanpham;
    User id_user;
    int soluong;
    int tongtien;

    public Cart() {
    }

    public Cart(int soluong, int tongtien) {
        this.soluong = soluong;
        this.tongtien = tongtien;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getId_sanpham() {
        return id_sanpham;
    }

    public void setId_sanpham(Product id_sanpham) {
        this.id_sanpham = id_sanpham;
    }

    public User getId_user() {
        return id_user;
    }

    public void setId_user(User id_user) {
        this.id_user = id_user;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getTongtien() {
        return tongtien;
    }

    public void setTongtien(int tongGiohang) {
        this.tongtien = tongGiohang;
    }
}
