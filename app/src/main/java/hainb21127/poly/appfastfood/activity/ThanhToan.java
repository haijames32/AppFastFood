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
import hainb21127.poly.appfastfood.model.Cart;
import hainb21127.poly.appfastfood.model.Cart2;
import hainb21127.poly.appfastfood.model.Product;
import hainb21127.poly.appfastfood.model.User;

public class ThanhToan extends AppCompatActivity {
    TextView tvName, tvEmail, tvPhone, tvAddress, tvTongtien;
    RecyclerView rcv;
    Spinner spn;
    Button btnDathang;
    ImageView btnBack;
    List<Cart2> listCart;
    Product product;
    Context context;
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
        rcv = findViewById(R.id.lv_thanhtoan);
        spn = findViewById(R.id.spn_thanhtoan);
        btnDathang = findViewById(R.id.btn_dathang_thanhtoan);
        btnBack = findViewById(R.id.btn_back_thanhtoan);
        context = this;

        listCart = new ArrayList<>();
        DatHangAdapter adapter = new DatHangAdapter(getApplicationContext(),listCart);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setAdapter(adapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idU = user.getUid();

        getInfoUser(idU);


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
                    refU.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            adapter.notifyDataSetChanged();
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

//    private void getListCartbyUser() {
//        database = FirebaseDatabase.getInstance();
//        myref = database.getReference("cart");
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        String idU = user.getUid();
//
//        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    DatabaseReference refSp = dataSnapshot.child("id_sanpham").getRef();
//                    DatabaseReference refU = dataSnapshot.child("id_user").getRef();
//                    adapter = new DatHangAdapter(getApplicationContext(), listCart);
//
//                    refSp.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for (DataSnapshot dataSnapshotsp : snapshot.getChildren()) {
//                                product = new Product();
//                                product.setId(dataSnapshotsp.getKey());
//                                product.setTensp(dataSnapshotsp.child("tensp").getValue(String.class));
//                                product.setGiasp(dataSnapshotsp.child("giasp").getValue(Integer.class));
//                                product.setImage(dataSnapshotsp.child("image").getValue(String.class));
//                                product.setMota(dataSnapshotsp.child("mota").getValue(String.class));
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Log.i("sanpham", "onCancelled: " + error.toString());
//                        }
//                    });
//                    refU.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for (DataSnapshot dataSnapshotu : snapshot.getChildren()) {
//                                String iduser = dataSnapshotu.getKey();
//                                if (iduser.equals(idU)) {
//                                    Cart2 cart = new Cart2();
//                                    User user1 = new User();
//                                    user1.setEmail(dataSnapshotu.child("email").getValue(String.class));
//                                    user1.setFullname(dataSnapshotu.child("fullname").getValue(String.class));
//                                    user1.setPhone(dataSnapshotu.child("phone").getValue(Integer.class));
//                                    user1.setAddress(dataSnapshotu.child("address").getValue(String.class));
//
//                                    cart.setId(dataSnapshot.getKey());
//                                    cart.setId_sanpham(product);
//                                    cart.setId_user(user1);
//                                    cart.setSoluong(dataSnapshot.child("soluong").getValue(Integer.class));
//                                    cart.setTongtien(dataSnapshot.child("tongtien").getValue(Integer.class));
//                                    listCart.add(cart);
//                                }
//                            }
//                            adapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Log.i("user", "onCancelled: " + error.toString());
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("TAG", "onCancelled: " + error.toString());
//            }
//        });
//    }

//    private void getListCart(String idU) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myref = database.getReference("cart");
//
//        myref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    DatabaseReference refSp = dataSnapshot.child("id_sanpham").getRef();
//                    DatabaseReference refU = dataSnapshot.child("id_user").getRef();
//                    adapter = new ThanhToanAdapter(getApplicationContext(), listCart);
//
//                    refSp.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for (DataSnapshot dataSnapshotsp : snapshot.getChildren()) {
//                                product = new Product();
//                                product.setId(dataSnapshotsp.getKey());
//                                product.setTensp(dataSnapshotsp.child("tensp").getValue(String.class));
//                                product.setGiasp(dataSnapshotsp.child("giasp").getValue(Integer.class));
//                                product.setImage(dataSnapshotsp.child("image").getValue(String.class));
//                                product.setMota(dataSnapshotsp.child("mota").getValue(String.class));
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Log.i("sanpham", "onCancelled: " + error.toString());
//                        }
//                    });
//                    refU.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for (DataSnapshot dataSnapshotu : snapshot.getChildren()) {
//                                String iduser = dataSnapshotu.getKey();
//                                if (iduser.equals(idU)) {
//                                    Cart2 cart = new Cart2();
//                                    User user1 = new User();
//                                    user1.setEmail(dataSnapshotu.child("email").getValue(String.class));
//                                    user1.setFullname(dataSnapshotu.child("fullname").getValue(String.class));
//                                    user1.setPhone(dataSnapshotu.child("phone").getValue(Integer.class));
//                                    user1.setAddress(dataSnapshotu.child("address").getValue(String.class));
//
//                                    cart.setId(dataSnapshot.getKey());
//                                    cart.setId_sanpham(product);
//                                    cart.setId_user(user1);
//                                    cart.setSoluong(dataSnapshot.child("soluong").getValue(Integer.class));
//                                    cart.setTongtien(dataSnapshot.child("tongtien").getValue(Integer.class));
//                                    listCart.add(cart);
//                                }
//                                listView.setAdapter(adapter);
//                            }
//                            adapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Log.i("user", "onCancelled: " + error.toString());
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("TAG", "onCancelled: " + error.toString());
//            }
//        });
//    }
}