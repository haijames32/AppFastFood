package hainb21127.poly.appfastfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.config.Utilities;

public class ProductDetail extends AppCompatActivity {
    TextView tvName, tvPrice, tvSoluong, tvMota, tvTongtien;
    ImageButton btnBack;
    ImageView imgSp, minus, plus;
    Button btnAddcart;
    private int so = 1;
    private int tong = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        tvName = findViewById(R.id.tv_name_detailsp);
        tvPrice = findViewById(R.id.tv_gia_detailsp);
        tvSoluong = findViewById(R.id.tv_soluong_detailsp);
        tvTongtien = findViewById(R.id.tv_tongtien_detailsp);
        tvMota = findViewById(R.id.tv_mota_detailsp);
        imgSp = findViewById(R.id.img_detailsp);
        minus = findViewById(R.id.btn_minus_detailsp);
        plus = findViewById(R.id.btn_plus_detailsp);
        btnAddcart = findViewById(R.id.btn_addcart_detailsp);
        btnBack = findViewById(R.id.btn_back_sp_detail);

        Intent intent = getIntent();
        String id = intent.getStringExtra("idPro");
        String name = intent.getStringExtra("namePro");
        int gia = intent.getIntExtra("pricePro",0);
        String mota = intent.getStringExtra("motaPro");
        String img = intent.getStringExtra("imgPro");

        Log.i("idsanpham", "onCreate: "+id);

        tvName.setText(name);
        tvPrice.setText(Utilities.addDots(gia)+"đ");
        tvMota.setText(mota);
        tvSoluong.setText(so+"");
        Picasso.get().load(img).into(imgSp);
        tvTongtien.setText(Utilities.addDots(gia * so)+"đ");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(so > 1){
                    so--;
                    tvSoluong.setText(String.valueOf(so));
                    tong = gia * so;
                    tvTongtien.setText(Utilities.addDots(Integer.parseInt(String.valueOf(tong)))+"đ");
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                so++;
                tvSoluong.setText(String.valueOf(so));
                tong = gia * so;
                tvTongtien.setText(Utilities.addDots(Integer.parseInt(String.valueOf(tong)))+"đ");
            }
        });
    }
}