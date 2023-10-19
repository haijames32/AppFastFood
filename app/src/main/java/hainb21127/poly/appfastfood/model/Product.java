package hainb21127.poly.appfastfood.model;

public class Product {
    String id;
    String tensp;
    Float giasp;
    String image;
    String mota;

    public Product() {
    }

    public Product(String id, String tensp, Float giasp, String image, String mota) {
        this.id = id;
        this.tensp = tensp;
        this.giasp = giasp;
        this.image = image;
        this.mota = mota;
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

    public Float getGiasp() {
        return giasp;
    }

    public void setGiasp(Float giasp) {
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
}