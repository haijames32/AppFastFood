package hainb21127.poly.appfastfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.squareup.picasso.Picasso;

import hainb21127.poly.appfastfood.MainActivity;
import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.config.Utilities;
import hainb21127.poly.appfastfood.model.Cart;
import hainb21127.poly.appfastfood.model.Product;
import hainb21127.poly.appfastfood.model.User;

public class ProductDetail extends AppCompatActivity {
    TextView tvName, tvPrice, tvSoluong, tvMota, tvTongtien;
    ImageView btnBack;
    ImageView imgSp, minus, plus;
    Button btnAddcart;
    int number = 1;
    int sum = 0;
    String email, name, address, img;
    int phone;
    String idUser;

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
        String idPro = intent.getStringExtra("idPro");
        String namesp = intent.getStringExtra("namePro");
        int giasp = intent.getIntExtra("pricePro", 0);
        String motasp = intent.getStringExtra("motaPro");
        String imgsp = intent.getStringExtra("imagePro");
        sum = giasp * number;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            idUser = user.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("users").child(idUser);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user1 = snapshot.getValue(User.class);
                        email = user1.getEmail();
                        name = user1.getFullname();
                        phone = user1.getPhone();
                        address = user1.getAddress();
                        img = user1.getImage();
                    } else {
                        Log.i("TAG", "onDataChange: Không lấy được thông tin người dùng");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("TAG", "onCancelled: " + error.toString());
                }
            });
        }


        tvName.setText(namesp);
        tvPrice.setText(Utilities.addDots(giasp) + "đ");
        tvMota.setText(motasp);
        tvSoluong.setText(number + "");
        Picasso.get().load(imgsp).into(imgSp);
        tvTongtien.setText(Utilities.addDots(giasp * number) + "đ");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number > 1) {
                    number--;
                    tvSoluong.setText(String.valueOf(number));
                    sum = giasp * number;
                    tvTongtien.setText(Utilities.addDots(Integer.parseInt(String.valueOf(sum))) + "đ");
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number++;
                tvSoluong.setText(String.valueOf(number));
                sum = giasp * number;
                tvTongtien.setText(Utilities.addDots(Integer.parseInt(String.valueOf(sum))) + "đ");
            }
        });

        btnAddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.isLoggedIn) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("cart").push();
                    Cart cart = new Cart(number, sum);
                    reference.setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference referencesp = reference.child("id_sanpham");
                                DatabaseReference referencesp1 = referencesp.child(idPro);
                                Product product = new Product();
                                product.setTensp(namesp);
                                product.setGiasp(giasp);
                                product.setMota(motasp);
                                product.setImage(imgsp);
                                referencesp1.setValue(product);

                                DatabaseReference referenceu = reference.child("id_user");
                                DatabaseReference referenceu1 = referenceu.child(idUser);
                                User user1 = new User(email, name, phone, address, img);
                                referenceu1.setValue(user1);

//                                Dialog dialog = new Dialog(getApplicationContext());
//                                dialog.setContentView(R.layout.dialog_success);
//                                TextView tvConfirm = dialog.findViewById(R.id.tv_success_dialog_success);
//                                Button btnAgree = dialog.findViewById(R.id.btn_agree_dialog_success);
//                                tvConfirm.setText("Đã thêm vào giỏ hàng");
//                                btnAgree.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                                dialog.show();
                                Toast.makeText(ProductDetail.this, "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProductDetail.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                Log.i("cart", "onComplete: " + task.toString());
                            }
                        }
                    });
                } else {
                    Dialog dialog = new Dialog(view.getContext());
                    dialog.setContentView(R.layout.dialog_confirm);
                    TextView tvConfirm = dialog.findViewById(R.id.tv_confirm);
                    Button btnCancel = dialog.findViewById(R.id.btn_cancel_dialog_confirm);
                    Button btnAgree = dialog.findViewById(R.id.btn_agree_dialog_confirm);
                    tvConfirm.setText("Bạn chưa đăng nhập!");
                    btnAgree.setText("Login");
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    btnAgree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(ProductDetail.this, Login.class));
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }

            }
        });
    }
}