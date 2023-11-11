package hainb21127.poly.appfastfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hainb21127.poly.appfastfood.R;

public class ChangePass extends AppCompatActivity {
    TextInputEditText ed_edit_pass, ed_edit_passnew, ed_edit_changepass;
    ImageView btn_back;
    Button btn_save;
    String mk;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        ed_edit_pass = findViewById(R.id.ed_edit_pass);
        btn_back = findViewById(R.id.img_back_chagepass);
        ed_edit_passnew = findViewById(R.id.ed_edit_passnew);
        ed_edit_changepass = findViewById(R.id.ed_edit_checkpass);
        btn_save = findViewById(R.id.btn_edit_save_profile);

        database = FirebaseDatabase.getInstance();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String iduser = user.getUid();
                String pass = ed_edit_pass.getText().toString().trim();
                String passnew = ed_edit_passnew.getText().toString().trim();
                String checkpass = ed_edit_changepass.getText().toString().trim();
                DatabaseReference reference1 = database.getReference("users").child(iduser);
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mk = snapshot.child("passwd").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                if (pass.isEmpty()) {
                    ed_edit_pass.setError("không được để trống ");
                } else if (passnew.isEmpty()) {
                    ed_edit_passnew.setError("Không được để trống ");
                } else if (checkpass.isEmpty()) {
                    ed_edit_changepass.setError("không được để trống");
                } else if (!passnew.equals(checkpass)) {
                    ed_edit_changepass.setError("Chưa trùng mật khẩu");
                } else if (!pass.equals(mk)) {
                    ed_edit_pass.setError("Mật khẩu không tồn tại");
                } else {
                    user.updatePassword(passnew).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference = database.getReference("users").child(iduser);
                                DatabaseReference reference1 = reference.child("passwd");
                                reference1.setValue(passnew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(ChangePass.this, Success.class);
                                        intent.putExtra("checkman", 2);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(ChangePass.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}