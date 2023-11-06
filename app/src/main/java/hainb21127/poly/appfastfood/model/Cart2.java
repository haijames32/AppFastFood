package hainb21127.poly.appfastfood.model;

public class Cart2 {
    String id;
    Product id_sanpham;
    User id_user;
    int soluong;
    int tongtien;

    int number;
    int pricesp;
    int sum;

    public Cart2() {
    }

    public Cart2(int soluong, int tongtien) {
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPricesp() {
        return pricesp;
    }

    public void setPricesp(int pricesp) {
        this.pricesp = pricesp;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
