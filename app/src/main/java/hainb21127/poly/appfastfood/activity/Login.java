package hainb21127.poly.appfastfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import hainb21127.poly.appfastfood.MainActivity;
import hainb21127.poly.appfastfood.R;

public class Login extends AppCompatActivity {
    TextView tv_dangky;
    TextInputEditText edEmail, edPass;
    ImageView btn_back;
    Button btn_login;
    public static final int RC_SIGN_IN = 9001;
    SignInButton btn_signin_google;
    private GoogleSignInClient mGoogleSignInClient;

    public void saveSharedPref(String idUser, String displayname, String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_signin_google", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idUser", idUser);
        editor.putString("displayname", displayname);
        editor.putString("email", email);
        editor.apply();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        btn_signin_google = findViewById(R.id.btn_signin_google);
        tv_dangky = findViewById(R.id.tv_dangky);
        edEmail = findViewById(R.id.ed_email_login);
        edPass = findViewById(R.id.ed_pw_login);
        btn_login = findViewById(R.id.btn_login);
        btn_back = findViewById(R.id.btn_back_login);
        tv_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        // Tạo client Google Sign In
        mGoogleSignInClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build());

        // Thêm sự kiện click cho button đăng nhập
        btn_signin_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String passwd = edPass.getText().toString();
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!passwd.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, passwd)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        MainActivity.isLoggedIn = true;
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        edPass.setError("Password không được để trống");
                    }
                } else if (email.isEmpty()) {
                    edEmail.setError("Email không được để trống");
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Lấy thông tin người dùng từ Google Sign In
            GoogleSignInAccount account = result.getSignInAccount();
            String idToken = account.getIdToken();
            String email = account.getEmail();
            String name = account.getDisplayName();
            saveSharedPref(idToken, name, email);
            MainActivity.isLoggedIn = true;
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        }
    }

}