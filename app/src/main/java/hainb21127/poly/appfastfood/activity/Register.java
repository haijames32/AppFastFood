package hainb21127.poly.appfastfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.model.User;
import hainb21127.poly.appfastfood.model.User_Register;

public class Register extends AppCompatActivity {
    TextInputEditText edName, edEmail, edPhone, edPass, edRepass, edAddress;
    Button btnRegister;
    ImageView btn_back;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edName = findViewById(R.id.ed_name_register);
        edEmail = findViewById(R.id.ed_email_register);
        edPhone = findViewById(R.id.ed_sdt_register);
        edPass = findViewById(R.id.ed_passwd_register);
        edRepass = findViewById(R.id.ed_checkpw_register);
        edAddress = findViewById(R.id.ed_address_register);
        btnRegister = findViewById(R.id.btn_signup_register);
        btn_back = findViewById(R.id.btn_back_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString().trim();
                String password = edPass.getText().toString().trim();
                String repass = edRepass.getText().toString().trim();
                String displayName = edName.getText().toString().trim();
                int phoneNumber = Integer.parseInt(edPhone.getText().toString());
                String address = edAddress.getText().toString().trim();

                if (TextUtils.isEmpty(displayName)){
                    edName.setError("Không để trôống tên");
                    edName.requestFocus();
                }else if(TextUtils.isEmpty(email)){
                    edEmail.setError("Emailkhoongg được để trống");
                    edEmail.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edEmail.setError("Email không đúng định dạng");
                    edEmail.requestFocus();
                }else if (edPhone.length()==0){
                    edPhone.setError("Không được để trống số điện thoai");
                    edPhone.requestFocus();
                }else if (edPhone.getText().toString().length() !=10) {
                    edPhone.setError("Phone number should be 10 digits");
                    edPhone.requestFocus();
                }else if (TextUtils.isEmpty(address)){
                    edAddress.setError("Không để trống địa chỉ");
                    edAddress.requestFocus();
                } else if (TextUtils.isEmpty(password)){
                    edPass.setError("Password Không để trống");
                    edPass.requestFocus();
                }else if (password.length() <6 || password.length() >6){
                    edPass.setError("Password phải cos 6 ký tự");
                    edPass.requestFocus();
                }else if (TextUtils.isEmpty(repass)){
                    edRepass.setError("Password Không để trống");
                    edRepass.requestFocus();
                }else if (!password.equals(repass)){
                    edRepass.setError("Password không trùng");
                    edRepass.requestFocus();
                }else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(email,displayName, phoneNumber, address);
                                String uid = task.getResult().getUser().getUid();
                                DatabaseReference userRef = firebaseDatabase.getReference("users").child(uid);
                                userRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.i("user", "onComplete: "+task.toString());
                                            Toast.makeText(Register.this, "Đăng ký thành công" + task.toString(), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Register.this, success.class);
                                            intent.putExtra("checkman",1);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(Register.this, "Lỗi lưu thông tin người dùng" + task.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Register.this, "Đăng ký thất bại " + task.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}