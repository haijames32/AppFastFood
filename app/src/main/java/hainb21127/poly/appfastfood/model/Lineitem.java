package hainb21127.poly.appfastfood.model;

public class Lineitem {
    String id;
    String id_order;
    Product id_sanpham;
    int soluong;
    int giatien;
    int tongtien;

    public Lineitem() {
    }

    public Lineitem(String id,String id_order, Product id_sanpham, int soluong, int giatien, int tongtien) {
        this.id = id;
        this.id_order = id_order;
        this.id_sanpham = id_sanpham;
        this.soluong = soluong;
        this.giatien = giatien;
        this.tongtien = tongtien;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public Product getId_sanpham() {
        return id_sanpham;
    }

    public void setId_sanpham(Product id_sanpham) {
        this.id_sanpham = id_sanpham;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getGiatien() {
        return giatien;
    }

    public void setGiatien(int giatien) {
        this.giatien = giatien;
    }

    public int getTongtien() {
        return tongtien;
    }

    public void setTongtien(int tongtien) {
        this.tongtien = tongtien;
    }
}
