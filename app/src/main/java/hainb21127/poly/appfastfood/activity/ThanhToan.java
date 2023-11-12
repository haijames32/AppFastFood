package hainb21127.poly.appfastfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hainb21127.poly.appfastfood.MainActivity;
import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.adapter.ThanhToanAdapter;
import hainb21127.poly.appfastfood.config.Utilities;
import hainb21127.poly.appfastfood.model.Cart2;
import hainb21127.poly.appfastfood.model.CreateOrder;
import hainb21127.poly.appfastfood.model.Lineitem;
import hainb21127.poly.appfastfood.model.Order;
import hainb21127.poly.appfastfood.model.Product;
import hainb21127.poly.appfastfood.model.User;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class ThanhToan extends AppCompatActivity {
    TextView tvName, tvEmail, tvPhone, tvAddress, tvTongtien;
    ListView lv;
    Spinner spn;
    Button btnDathang;
    ImageView btnBack;
    List<Cart2> listCart;
    ThanhToanAdapter adapter;
    Product product;
    FirebaseDatabase database;
    String[] pttt = {"Thanh toán khi nhận hàng", "Thanh toán qua ZaloPay"};
    String getPttt, name, email, address, image, idsp, tensp, imagesp, motasp;
    int phone, giasp, soluongsp, tongmathang;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    int finalTotal;

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

        //zalopay
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2554, Environment.SANDBOX);

        database = FirebaseDatabase.getInstance();

//        listPttt.add("Thanh toán khi nhận hàng");
//        listPttt.add("Thanh toán qua ZaloPay");
//        listPttt.add("Thanh toán qua Momo");

        listCart = new ArrayList<>();
        adapter = new ThanhToanAdapter(getApplicationContext(), listCart);
//        spnAdapter = new PaymentSpnAdapter(getApplicationContext(),listPttt);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idU = user.getUid();

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
                getPttt = (String) adapterView.getItemAtPosition(i);
                Log.i("spn_thanhtoan", "onItemClick: " + getPttt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i("spn_thanhtoan", "onNothingSelected: " + adapterView.toString());
            }
        });


        DatabaseReference ref = database.getReference("users").child(idU);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("fullname").getValue(String.class);
                email = snapshot.child("email").getValue(String.class);
                phone = snapshot.child("phone").getValue(Integer.class);
                address = snapshot.child("address").getValue(String.class);
                image = snapshot.child("image").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getInfoUser(idU);
        getListCart(idU);

//        DatabaseReference myref = database.getReference("cart");
//        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    DatabaseReference refSp = dataSnapshot.child("id_sanpham").getRef();
//                    DatabaseReference refU = dataSnapshot.child("id_user").getRef();
//                    refSp.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for (DataSnapshot dataSnapshotsp : snapshot.getChildren()) {
//                                product = new Product();
//                                product.setId(dataSnapshotsp.getKey());
//                                product.setTensp(dataSnapshotsp.child("tensp").getValue(String.class));
//                                product.setGiasp(dataSnapshotsp.child("giasp").getValue(Integer.class));
//                                product.setImage(dataSnapshotsp.child("image").getValue(String.class));
//                                product.setMota(dataSnapshotsp.child("mota").getValue(String.class));
//
//                                idsp = dataSnapshotsp.getKey();
//                                tensp = dataSnapshotsp.child("tensp").getValue(String.class);
//                                giasp = dataSnapshotsp.child("giasp").getValue(Integer.class);
//                                imagesp = dataSnapshotsp.child("image").getValue(String.class);
//                                motasp = dataSnapshotsp.child("mota").getValue(String.class);
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Log.i("sanpham", "onCancelled: " + error.toString());
//                        }
//                    });
//                    refU.addValueEventListener(new ValueEventListener() {
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
//
//                                    soluongsp = dataSnapshot.child("soluong").getValue(Integer.class);
//                                    tongmathang = dataSnapshot.child("tongtien").getValue(Integer.class);
//
//                                    listCart.add(cart);
//                                    Log.i("listcart", "onDataChange: " + listCart.size());
//                                }
//                            }
//                            lv.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//
//                            int total = 0;
//                            for (int i = 0; i < adapter.getCount(); i++) {
//                                Cart2 cart = (Cart2) adapter.getItem(i);
//                                total += cart.getTongtien();
//                                Log.i("tong", "onDataChange: " + cart.getTongtien());
//                            }
//                            tvTongtien.setText(Utilities.addDots(total) + "đ");
//                            finalTotal = total;
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Log.i("user", "onCancelled: " + error.toString());
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("TAG", "onCancelled: " + error.toString());
//            }
//        });


        btnDathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPttt.equalsIgnoreCase("Thanh toán khi nhận hàng")) {
                    try {
                        DatabaseReference reference = database.getReference("orders").push();
                        String idO = reference.getKey();
                        Order order = new Order(dateFormat.format(new Date()), getPttt, "Chờ xác nhận", finalTotal);
                        reference.setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DatabaseReference reference1 = reference.child("id_user");
                                    DatabaseReference reference2 = reference1.child(idU);
                                    User user1 = new User(email, name, phone, address, image);
                                    reference2.setValue(user1);
                                }
                            }
                        });

                        for (int i = 0; i < listCart.size(); i++) {
                            DatabaseReference lineItemsRef = database.getReference("lineitems").push();
                            Lineitem lineitem = new Lineitem();
                            lineitem.setGiatien(listCart.get(i).getId_sanpham().getGiasp());
                            lineitem.setSoluong(listCart.get(i).getSoluong());
                            lineitem.setTongmathang(listCart.get(i).getTongtien());
                            lineItemsRef.setValue(lineitem);

                            DatabaseReference idOrderRef = lineItemsRef.child("id_order");
                            DatabaseReference idUserRef = idOrderRef.child(idO);
                            DatabaseReference idUserRef2 = idUserRef.child("id_user");
                            DatabaseReference idUserRef3 = idUserRef2.child(idU);
                            User user1 = new User(email, name, phone, address, image);
                            idUserRef3.setValue(user1);

                            DatabaseReference idSanPhamRef = lineItemsRef.child("id_sanpham");
                            DatabaseReference idSanPhamRef2 = idSanPhamRef.child(listCart.get(i).getId_sanpham().getId());
                            idSanPhamRef2.setValue(listCart.get(i).getId_sanpham());
                        }

                        DatabaseReference refCart = database.getReference("cart");
                        refCart.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    DatabaseReference refu = dataSnapshot.child("id_user").getRef();
                                    refu.orderByValue().equalTo(idU).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                                String cartItemId = dataSnapshot1.getKey();
                                                dataSnapshot1.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.i("zzzzz", "xóa tc: " + cartItemId);
                                                        }
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Xử lý khi có lỗi xảy ra
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Xử lý khi có lỗi xảy ra
                            }
                        });


                        Intent intent = new Intent(ThanhToan.this, Success.class);
                        intent.putExtra("checkman", 4);
                        startActivity(intent);
                        finish();
                    } catch (Exception ex) {
                        Log.i("zzzzz", "onClick: " + ex.toString());
                    }

                } else if (getPttt.equalsIgnoreCase("Thanh toán qua ZaloPay")) {
                    CreateOrder orderApi = new CreateOrder();
                    try {
                        JSONObject data = orderApi.createOrder(String.valueOf(finalTotal));
                        String code = data.getString("return_code");
                        Log.i("code", "onClick: " + code.toString());
                        if (code.equals("1")) {
                            String token = data.getString("zp_trans_token");
                            Log.i("token", "onClick: " + token.toString());
                            ZaloPaySDK.getInstance().payOrder(ThanhToan.this, token, "demozpdk://app", new PayOrderListener() {
                                @Override
                                public void onPaymentSucceeded(String s, String s1, String s2) {
                                    try {
                                        DatabaseReference reference = database.getReference("orders").push();
                                        String idO = reference.getKey();
                                        Order order = new Order(dateFormat.format(new Date()), getPttt, "Đã thanh toán và chờ xác nhận", finalTotal);
                                        reference.setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DatabaseReference reference1 = reference.child("id_user");
                                                    DatabaseReference reference2 = reference1.child(idU);
                                                    User user1 = new User(email, name, phone, address, image);
                                                    reference2.setValue(user1);
                                                }
                                            }
                                        });

                                        for (int i = 0; i < listCart.size(); i++) {
                                            DatabaseReference lineItemsRef = database.getReference("lineitems").push();
                                            Lineitem lineitem = new Lineitem();
                                            lineitem.setGiatien(listCart.get(i).getId_sanpham().getGiasp());
                                            lineitem.setSoluong(listCart.get(i).getSoluong());
                                            lineitem.setTongmathang(listCart.get(i).getTongtien());
                                            lineItemsRef.setValue(lineitem);

                                            DatabaseReference idOrderRef = lineItemsRef.child("id_order");
                                            DatabaseReference idUserRef = idOrderRef.child(idO);
                                            DatabaseReference idUserRef2 = idUserRef.child("id_user");
                                            DatabaseReference idUserRef3 = idUserRef2.child(idU);
                                            User user1 = new User(email, name, phone, address, image);
                                            idUserRef3.setValue(user1);

                                            DatabaseReference idSanPhamRef = lineItemsRef.child("id_sanpham");
                                            DatabaseReference idSanPhamRef2 = idSanPhamRef.child(listCart.get(i).getId_sanpham().getId());
                                            idSanPhamRef2.setValue(listCart.get(i).getId_sanpham());
                                        }

                                        DatabaseReference refCart = database.getReference("cart");
                                        refCart.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    DatabaseReference refu = dataSnapshot.child("id_user").getRef();
                                                    refu.orderByValue().equalTo(idU).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                                                String cartItemId = dataSnapshot1.getKey();
                                                                dataSnapshot1.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Log.i("zzzzz", "xóa tc: " + cartItemId);
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            // Xử lý khi có lỗi xảy ra
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Xử lý khi có lỗi xảy ra
                                            }
                                        });

                                        startActivity(new Intent(ThanhToan.this, MainActivity.class));
                                        finish();
                                    } catch (Exception ex) {
                                        Log.i("zzzzz", "onClick: " + ex.toString());
                                    }
                                    Toast.makeText(ThanhToan.this, "Thanh toán thành công: " + s.toString(), Toast.LENGTH_SHORT).show();
                                    Log.i("paysuccess", "onPaymentSucceeded: " + s);
                                }

                                @Override
                                public void onPaymentCanceled(String s, String s1) {
                                    Toast.makeText(ThanhToan.this, "Pay Canceled: " + s.toString(), Toast.LENGTH_SHORT).show();
                                    Log.i("paycancel", "onPaymentCanceled: " + s);
                                }

                                @Override
                                public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                    Toast.makeText(ThanhToan.this, "Lỗi: " + zaloPayError.toString(), Toast.LENGTH_SHORT).show();
                                    Log.i("payerror", "onPaymentError: " + zaloPayError);
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getInfoUser(String idU) {
        DatabaseReference reference = database.getReference("users").child(idU);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);
                String name = user1.getFullname();
                String email = user1.getEmail();
                int phone = user1.getPhone();
                String address = user1.getAddress();
                tvName.setText(name);
                tvEmail.setText(email);
                tvPhone.setText("0" + phone);
                tvAddress.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("thanhtoan", "onCancelled: " + error.toString());
            }
        });

    }

    private void getListCart(String idU) {
        DatabaseReference myref = database.getReference("cart");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DatabaseReference refSp = dataSnapshot.child("id_sanpham").getRef();
                    DatabaseReference refU = dataSnapshot.child("id_user").getRef();
                    refSp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshotsp : snapshot.getChildren()) {
                                product = new Product();
                                product.setId(dataSnapshotsp.getKey());
                                product.setTensp(dataSnapshotsp.child("tensp").getValue(String.class));
                                product.setGiasp(dataSnapshotsp.child("giasp").getValue(Integer.class));
                                product.setImage(dataSnapshotsp.child("image").getValue(String.class));
                                product.setMota(dataSnapshotsp.child("mota").getValue(String.class));

                                idsp = dataSnapshotsp.getKey();
                                tensp = dataSnapshotsp.child("tensp").getValue(String.class);
                                giasp = dataSnapshotsp.child("giasp").getValue(Integer.class);
                                imagesp = dataSnapshotsp.child("image").getValue(String.class);
                                motasp = dataSnapshotsp.child("mota").getValue(String.class);

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

                                    soluongsp = dataSnapshot.child("soluong").getValue(Integer.class);
                                    tongmathang = dataSnapshot.child("tongtien").getValue(Integer.class);

                                    listCart.add(cart);
                                    Log.i("listcart", "onDataChange: " + listCart.size());
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
                            finalTotal = total;
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}