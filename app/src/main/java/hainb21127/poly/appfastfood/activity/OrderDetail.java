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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.adapter.LineItemAdapter;
import hainb21127.poly.appfastfood.config.Utilities;
import hainb21127.poly.appfastfood.model.Lineitem;
import hainb21127.poly.appfastfood.model.Product;
import hainb21127.poly.appfastfood.model.User;

public class OrderDetail extends AppCompatActivity {
    TextView tvName, tvEmail, tvPhone, tvAddress, tvTongtien, tvDate, tvTrangthai,tvThanhtoan;
    ImageView btnBack, imgTrangthai;
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
        imgTrangthai = findViewById(R.id.img_trangthai_order_detail);
        tvThanhtoan = findViewById(R.id.tv_pttt_order_detail);

        database = FirebaseDatabase.getInstance();

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
                                    lineitem.setTongmathang(dataSnapshot.child("tongmathang").getValue(Integer.class));

                                    listLine.add(lineitem);
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
        String pttt = intent.getStringExtra("pttt_order");
        String date = intent.getStringExtra("date_order");
        int tongtien = intent.getIntExtra("tongtien_order", 0);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String id = user.getUid();
            DatabaseReference reference = database.getReference("users").child(id);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        User user1 = snapshot.getValue(User.class);
                        String email = user1.getEmail();
                        String name = user1.getFullname();
                        int phone = user1.getPhone();
                        String address = user1.getAddress();
                        tvName.setText(name);
                        tvEmail.setText(email);
                        tvPhone.setText("0"+phone);
                        tvAddress.setText(address);
                    }else{
                        Log.i("TAG", "onDataChange: Không lấy được thông tin người dùng");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("TAG", "onCancelled: "+error.toString());
                }
            });
        }

        tvTongtien.setText(Utilities.addDots(tongtien) + "đ");
        tvDate.setText(date);
        tvTrangthai.setText(trangthai);
        tvThanhtoan.setText(pttt);

        if (trangthai.equals("Chờ xác nhận")) {
            imgTrangthai.setImageResource(R.drawable.ic_preparing);
            tvTrangthai.setTextColor(Color.YELLOW);
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String trangthaihuy = " Đã hủy đơn hàng";
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference reference = firebaseDatabase.getReference("orders").child(idOrder);
                    DatabaseReference reference1 = reference.child("trangthai");
                    reference1.setValue(trangthaihuy).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            btnHuy.setText("Đã hủy đơn hàng");
                            btnHuy.setEnabled(false);
                        }
                    });
                }
            });
        } else if (trangthai.equals("Đang giao hàng")) {
            imgTrangthai.setImageResource(R.drawable.ic_shipping);
            tvTrangthai.setTextColor(Color.GREEN);
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String trangthaidagiaohang = "Đã giao hàng";
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference reference = firebaseDatabase.getReference("orders").child(idOrder);
                    DatabaseReference reference1 = reference.child("trangthai");
                    reference1.setValue(trangthaidagiaohang).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            btnHuy.setText("Đã giao hàng");
                            btnHuy.setEnabled(false);
                        }
                    });
                }
            });
        } else if (trangthai.equals("Đã giao hàng")) {
            imgTrangthai.setImageResource(R.drawable.ic_finish);
            tvTrangthai.setTextColor(Color.BLUE);
            btnHuy.setEnabled(false);
        } else {
            imgTrangthai.setImageResource(R.drawable.ic_cancel);
            tvTrangthai.setTextColor(Color.RED);
            btnHuy.setEnabled(false);
        }
    }

}