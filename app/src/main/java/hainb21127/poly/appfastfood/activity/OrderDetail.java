package hainb21127.poly.appfastfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.adapter.LineItemAdapter;
import hainb21127.poly.appfastfood.config.Utilities;
import hainb21127.poly.appfastfood.model.Lineitem;
import hainb21127.poly.appfastfood.model.Product;

public class OrderDetail extends AppCompatActivity {
    TextView tvName, tvEmail, tvPhone, tvAddress, tvTongtien, tvDate, tvTrangthai;
    ImageView btnBack;
    Button btnHuy;
    RecyclerView rcv;
    Context context;
    String idOrder;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        rcv = findViewById(R.id.rcv_order_detail);
        tvName = findViewById(R.id.tv_fullname_order_detail);
        tvEmail = findViewById(R.id.tv_email_order_detail);
        tvPhone = findViewById(R.id.tv_phone_order_detail);
        tvAddress = findViewById(R.id.tv_address_order_detail);
        tvTongtien = findViewById(R.id.tv_tongtien_order_detail);
        tvDate = findViewById(R.id.tv_date_order_detail);
        tvTrangthai = findViewById(R.id.tv_trangthai_order_detail);
        btnHuy = findViewById(R.id.btn_huy_order_detail);
        btnBack = findViewById(R.id.btn_back_order_detail);



        List<Lineitem> listLine = new ArrayList<>();
        LineItemAdapter adapter = new LineItemAdapter(getApplicationContext(), listLine);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getInfo();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("lineitems");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DatabaseReference referenceOd = dataSnapshot.child("id_order").getRef();
                    DatabaseReference referenceSp = dataSnapshot.child("id_sanpham").getRef();
                    Product product = new Product();
                    referenceSp.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshotsp : snapshot.getChildren()) {
                                product.setId(dataSnapshotsp.getKey());
                                product.setTensp(dataSnapshotsp.child("tensp").getValue(String.class));
                                product.setGiasp(dataSnapshotsp.child("giasp").getValue(Integer.class));
                                product.setImage(dataSnapshotsp.child("image").getValue(String.class));
                                product.setMota(dataSnapshotsp.child("mota").getValue(String.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.i("referenceSp", "onCancelled: " + error.toString());
                        }
                    });
                    referenceOd.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshotod : snapshot.getChildren()) {
                                String idOd = dataSnapshotod.getKey();
                                if (idOd.equals(idOrder)) {
                                    Lineitem lineitem = new Lineitem();
                                    lineitem.setId(dataSnapshot.getKey());
                                    lineitem.setId_order(idOd);
                                    lineitem.setId_sanpham(product);
                                    lineitem.setSoluong(dataSnapshot.child("soluong").getValue(Integer.class));
                                    lineitem.setGiatien(dataSnapshot.child("giatien").getValue(Integer.class));
                                    lineitem.setTongtien(dataSnapshot.child("tongmathang").getValue(Integer.class));

                                    listLine.add(lineitem);

                                    Log.i("TAG", "sai: " + listLine.size());
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.i("referenceOd", "onCancelled: " + error.toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("lineitems", "onCancelled: " + error.toString());
            }
        });
    }

    private void getInfo() {
        Intent intent = getIntent();
        idOrder = intent.getStringExtra("id_order");
        String trangthai = intent.getStringExtra("trangthai_order");
        String date = intent.getStringExtra("date_order");
        int tongtien = intent.getIntExtra("tongtien_order", 0);
        String nameU = intent.getStringExtra("nameUser_order");
        String emailU = intent.getStringExtra("emailUser_order");
        int phoneU = intent.getIntExtra("phoneUser_order", 0);
        String addressU = intent.getStringExtra("addressUser_order");

        tvName.setText(nameU);
        tvEmail.setText(emailU);
        tvPhone.setText("0" + phoneU);
        tvAddress.setText(addressU);
        tvTongtien.setText(Utilities.addDots(tongtien) + "đ");
        tvDate.setText(date);
        tvTrangthai.setText(trangthai);
        if (trangthai.equals("Chờ xác nhận")) {
            tvTrangthai.setTextColor(Color.YELLOW);
        } else if (trangthai.equals("Đang giao hàng")) {
            tvTrangthai.setTextColor(Color.GREEN);
            btnHuy.setEnabled(false);
        } else if (trangthai.equals("Đã giao hàng")) {
            tvTrangthai.setTextColor(Color.BLUE);
            btnHuy.setEnabled(false);
        } else {
            tvTrangthai.setTextColor(Color.RED);
            btnHuy.setEnabled(false);
        }
    }

    private void getListProductbyOrder(String id) {

    }
}