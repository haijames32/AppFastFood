package hainb21127.poly.appfastfood.activity;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import hainb21127.poly.appfastfood.adapter.ThanhToanAdapter;
import hainb21127.poly.appfastfood.model.Cart;
import hainb21127.poly.appfastfood.model.Product;
import hainb21127.poly.appfastfood.model.User;

public class ThanhToan extends AppCompatActivity {
    TextView tvName, tvEmail, tvPhone, tvAddress, tvTongtien;
    ListView listView;
    Spinner spn;
    Button btnDathang;
    ImageView btnBack;
    List<Cart> listCat;

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
        listView = findViewById(R.id.lv_thanhtoan);
        spn = findViewById(R.id.spn_thanhtoan);
        btnDathang = findViewById(R.id.btn_dathang_thanhtoan);
        btnBack = findViewById(R.id.btn_back_thanhtoan);

        listCat = new ArrayList<>();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idU = user.getUid();

        getInfoUser(idU);
//        getListCartbyUser(idU);
        getListCart(idU);
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

    private void getListCartbyUser(String idU) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("cart");
        ThanhToanAdapter adapter = new ThanhToanAdapter(getApplicationContext(), listCat);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DatabaseReference refSp = dataSnapshot.child("id_sanpham").getRef();
                    DatabaseReference refU = dataSnapshot.child("id_user").getRef();
                    Product product = new Product();
                    refSp.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            Log.i("sanpham", "onCancelled: " + error.toString());
                        }
                    });
                    refU.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshotu : snapshot.getChildren()) {
                                String iduser = dataSnapshotu.getKey();
                                if (iduser.equals(idU)) {
                                    User user1 = new User();
                                    user1.setEmail(dataSnapshotu.child("email").getValue(String.class));
                                    user1.setFullname(dataSnapshotu.child("fullname").getValue(String.class));
                                    user1.setPhone(dataSnapshotu.child("phone").getValue(Integer.class));
                                    user1.setAddress(dataSnapshotu.child("address").getValue(String.class));

                                    Cart cart = new Cart();
                                    cart.setId(dataSnapshot.getKey());
                                    cart.setId_sanpham(product);
                                    cart.setId_user(user1);
                                    cart.setSoluong(dataSnapshot.child("soluong").getValue(Integer.class));
                                    cart.setTongtien(dataSnapshot.child("tongtien").getValue(Integer.class));

                                    listCat.add(cart);
                                }
                            }
                            listView.setAdapter(adapter);
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

    private void getListCart(String idU) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("cart");

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DatabaseReference refSp = dataSnapshot.child("id_sanpham").getRef();
                    DatabaseReference refU = dataSnapshot.child("id_user").getRef();
                    CartAdapter adapter = new CartAdapter(getApplicationContext());
                    Product product = new Product();
                    refSp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshotsp : snapshot.getChildren()) {
//                                product = new Product();
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
                                    Cart cart = new Cart();
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
                                    listCat.add(cart);
                                }
                            }
                            adapter.setData(listCat);
                            listView.setAdapter(adapter);
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