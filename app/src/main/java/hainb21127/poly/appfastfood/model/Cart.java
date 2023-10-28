package hainb21127.poly.appfastfood.model;

public class Cart {
    String id_sanpham;
    String id_user;
    int soluong;
    int tongGiohang;

    public Cart() {
    }

    public Cart(String id_sanpham, String id_user, int soluong, int tongGiohang) {
        this.id_sanpham = id_sanpham;
        this.id_user = id_user;
        this.soluong = soluong;
        this.tongGiohang = tongGiohang;
    }

    public String getId_sanpham() {
        return id_sanpham;
    }

    public void setId_sanpham(String id_sanpham) {
        this.id_sanpham = id_sanpham;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getTongGiohang() {
        return tongGiohang;
    }

    public void setTongGiohang(int tongGiohang) {
        this.tongGiohang = tongGiohang;
    }
}
