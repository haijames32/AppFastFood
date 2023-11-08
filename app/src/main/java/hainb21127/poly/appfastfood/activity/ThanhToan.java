package hainb21127.poly.appfastfood.activity;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.adapter.CartAdapter;
import hainb21127.poly.appfastfood.adapter.DatHangAdapter;
import hainb21127.poly.appfastfood.adapter.ThanhToanAdapter;
import hainb21127.poly.appfastfood.config.Utilities;
import hainb21127.poly.appfastfood.model.Cart;
import hainb21127.poly.appfastfood.model.Cart2;
import hainb21127.poly.appfastfood.model.Product;
import hainb21127.poly.appfastfood.model.User;

public class ThanhToan extends AppCompatActivity {
    TextView tvName, tvEmail, tvPhone, tvAddress, tvTongtien;
    ListView lv;
    Spinner spn;
    Button btnDathang;
    ImageView btnBack;
    List<Cart2> listCart;
    ThanhToanAdapter adapter;
    Product product;
    String[] pttt = {"Thanh toán khi nhận hàng", "Thanh toán qua MoMo", "Thanh toán qua VNPay"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        tvName = findViewById(R.id.tv_fullname_thanhtoan);
        tvEmail = findViewById(R.id.tv_email_thanhtoan);
        tvPhone = findViewById(R.id.tv_phone_thanhtoan);
        tvAddress = findViewById(R.id.tv_address_thanhtoan);
        tvTongtien = findViewById(R.id.tv_tongtien_thanhtoan);
        lv = findViewById(R.id.lv_thanhtoan);
        spn = findViewById(R.id.spn_thanhtoan);
        btnDathang = findViewById(R.id.btn_dathang_thanhtoan);
        btnBack = findViewById(R.id.btn_back_thanhtoan);

        listCart = new ArrayList<>();
        adapter = new ThanhToanAdapter(getApplicationContext(), listCart);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idU = user.getUid();

        getInfoUser(idU);
        getListCart(idU);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ArrayAdapter<String> adapterSpn = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pttt);
        spn.setAdapter(adapterSpn);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String getPttt = (String) adapterView.getItemAtPosition(i);
                Log.i("spn_thanhtoan", "onItemClick: " + getPttt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i("spn_thanhtoan", "onNothingSelected: " + adapterView.toString());
            }
        });


    }

    private void getInfoUser(String idU) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(idU);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);
                String name = user1.getFullname();
                String email = user1.getEmail();
                int phong = user1.getPhone();
                String address = user1.getAddress();
                tvName.setText(name);
                tvEmail.setText(email);
                tvPhone.setText("0" + phong);
                tvAddress.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("thanhtoan", "onCancelled: " + error.toString());
            }
        });
    }

    private void getListCart(String idU) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("cart");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DatabaseReference refSp = dataSnapshot.child("id_sanpham").getRef();
                    DatabaseReference refU = dataSnapshot.child("id_user").getRef();
                    refSp.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshotsp : snapshot.getChildren()) {
                                product = new Product();
                                product.setId(dataSnapshotsp.getKey());
                                product.setTensp(dataSnapshotsp.child("tensp").getValue(String.class));
                                product.setGiasp(dataSnapshotsp.child("giasp").getValue(Integer.class));
                                product.setImage(dataSnapshotsp.child("image").getValue(String.class));
                                product.setMota(dataSnapshotsp.child("mota").getValue(String.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.i("sanpham", "onCancelled: " + error.toString());
                        }
                    });
                    refU.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshotu : snapshot.getChildren()) {
                                String iduser = dataSnapshotu.getKey();
                                if (iduser.equals(idU)) {
                                    Cart2 cart = new Cart2();
                                    User user1 = new User();
                                    user1.setEmail(dataSnapshotu.child("email").getValue(String.class));
                                    user1.setFullname(dataSnapshotu.child("fullname").getValue(String.class));
                                    user1.setPhone(dataSnapshotu.child("phone").getValue(Integer.class));
                                    user1.setAddress(dataSnapshotu.child("address").getValue(String.class));

                                    cart.setId(dataSnapshot.getKey());
                                    cart.setId_sanpham(product);
                                    cart.setId_user(user1);
                                    cart.setSoluong(dataSnapshot.child("soluong").getValue(Integer.class));
                                    cart.setTongtien(dataSnapshot.child("tongtien").getValue(Integer.class));
                                    listCart.add(cart);
                                }
                            }
                            lv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            int total = 0;
                            for (int i = 0; i < adapter.getCount(); i++) {
                                Cart2 cart = (Cart2) adapter.getItem(i);
                                total += cart.getTongtien();
                                Log.i("tong", "onDataChange: " + cart.getTongtien());
                            }
                            tvTongtien.setText(Utilities.addDots(total) + "đ");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.i("user", "onCancelled: " + error.toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: " + error.toString());
            }
        });
    }
}