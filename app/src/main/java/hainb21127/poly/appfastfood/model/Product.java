package hainb21127.poly.appfastfood.model;

public class Product {
    String id;
    String tensp;
    int giasp;
    String image;
    String mota;
    String id_theloai;

    public Product() {
    }

    public Product(String id, String tensp, int giasp, String image, String mota, String id_theloai) {
        this.id = id;
        this.tensp = tensp;
        this.giasp = giasp;
        this.image = image;
        this.mota = mota;
        this.id_theloai = id_theloai;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public int getGiasp() {
        return giasp;
    }

    public void setGiasp(int giasp) {
        this.giasp = giasp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getId_theloai() {
        return id_theloai;
    }

    public void setId_theloai(String id_theloai) {
        this.id_theloai = id_theloai;
    }
}
