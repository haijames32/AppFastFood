package hainb21127.poly.appfastfood.activity;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.adapter.CartAdapter;
import hainb21127.poly.appfastfood.config.Utilities;
import hainb21127.poly.appfastfood.inter.MyInterface;
import hainb21127.poly.appfastfood.model.Cart2;
import hainb21127.poly.appfastfood.model.Product;
import hainb21127.poly.appfastfood.model.User;

public class CartActivity extends AppCompatActivity implements MyInterface {
    ListView listView;
    TextView tvTongtien;
    List<Cart2> listCart;
    LinearLayout no_cart, lo_footer;
    Button btnMua;
    FirebaseDatabase database;
    FirebaseUser user;
    Product product;
    CartAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        listView = findViewById(R.id.lv_cart1);
        no_cart = findViewById(R.id.no_cart_cart1);
        lo_footer = findViewById(R.id.lo_footer_cart1);
        btnMua = findViewById(R.id.btn_mua_cart1);
        tvTongtien = findViewById(R.id.tv_tongtien_cart1);
        swipeRefreshLayout = findViewById(R.id.refresh_cart1);

        database = FirebaseDatabase.getInstance();

        listCart = new ArrayList<>();
        adapter = new CartAdapter(getApplicationContext(), listCart, this::onDelete);

        getListCartbyUser();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListCartbyUser();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btnMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ThanhToan.class));
            }
        });
    }

    private void getListCartbyUser() {
        DatabaseReference myref = database.getReference("cart");
        user = FirebaseAuth.getInstance().getCurrentUser();
        String idU = user.getUid();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCart.clear();
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

                                    lo_footer.setVisibility(View.VISIBLE);
                                    no_cart.setVisibility(View.INVISIBLE);
                                    Log.i("sai", "onDataChange: " + listCart.size());
                                } else if (listCart.size() == 0) {
                                    no_cart.setVisibility(View.VISIBLE);
                                    lo_footer.setVisibility(View.INVISIBLE);
                                }
                            }
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            int total = 0;
                            for (int i = 0; i < adapter.getCount(); i++) {
                                Cart2 cart = (Cart2) adapter.getItem(i);
                                total += cart.getTongtien();
                                Log.i("total", "onDataChange: " + total);
                                Log.i("sumcart", "onDataChange: " + cart.getTongtien());
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
    @Override
    public void onDelete(Cart2 cart) {
        DatabaseReference reference = database.getReference("cart").child(cart.getId());
        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                    getListCartbyUser();
                } else {
                    Toast.makeText(getApplicationContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}