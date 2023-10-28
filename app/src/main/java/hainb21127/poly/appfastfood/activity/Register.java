package hainb21127.poly.appfastfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
                if (email.isEmpty()) {
                    edEmail.setError("Không để trống Email");
                    return;
                }
                if (password.isEmpty()) {
                    edPass.setError("Không để trống Mật khẩu");
                    return;
                }
                if (displayName.isEmpty()) {
                    edName.setError("Không để trống Họ tên");
                    return;
                }
                if (phoneNumber == 0) {
                    edPhone.setError("Không để trống Số điện thoại");
                    return;
                }
                if (address.isEmpty()) {
                    edAddress.setError("Không để trống Địa chỉ");
                    return;
                }
                if (repass.isEmpty()) {
                    edRepass.setError("Không để trống");
                    return;
                }
// Kiểm tra xem mật khẩu và xác nhận mật khẩu có khớp hay không
                if (!repass.equals(password)) {
                    edRepass.setError("Chưa trùng mật khẩu");
                    return;
                }
                if (email.length() > 0 && password.length() > 0 && displayName.length() > 0 && phoneNumber > 0 && address.length() > 0 && repass.length() > 0 && repass.equals(password) ) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User_Register user = new User_Register(displayName, phoneNumber, address);
                                String uid = task.getResult().getUser().getUid();
                                DatabaseReference userRef = firebaseDatabase.getReference("users").child(uid);
                                userRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.i("user", "onComplete: "+task.toString());
                                            Toast.makeText(Register.this, "Đăng ký thành công" + task.toString(), Toast.LENGTH_SHORT).show();
                                            onBackPressed();
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
                }else{
                    Toast.makeText(Register.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}