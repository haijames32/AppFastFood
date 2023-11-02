package hainb21127.poly.appfastfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.model.User;

public class EditProfile extends AppCompatActivity {
    TextInputEditText ed_edit_name, ed_edit_phone, ed_edit_address ;
    ImageView img_back;
    Button btn_save;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        img_back = findViewById(R.id.img_back);
        ed_edit_name = findViewById(R.id.ed_edit_fullname);
        ed_edit_phone = findViewById(R.id.ed_edit_phone);
        ed_edit_address = findViewById(R.id.ed_edit_address);
        btn_save = findViewById(R.id.btn_edit_save_profile);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        String id = user.getUid();
        reference = database.getReference("users").child(id);
        if (user != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user1 = snapshot.getValue(User.class);
                        String name = user1.getFullname();
                        int phone = user1.getPhone();
                        String address = user1.getAddress();
                        ed_edit_name.setText(name);
                        ed_edit_phone.setText("0" + phone);
                        ed_edit_address.setText(address);
                    } else {
                        Log.i("TAG", "onDataChange: Không lấy được thông tin người dùng");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("TAG", "onCancelled: " + error.toString());
                }
            });
            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String fullname = ed_edit_name.getText().toString();
                    int phone = Integer.parseInt(ed_edit_phone.getText().toString());
                    String address = ed_edit_address.getText().toString();
                    if (fullname.isEmpty()) {
                        ed_edit_name.setError("Không để trống");
                    }else if (TextUtils.isEmpty(phone+"")) {
                        ed_edit_phone.setError("Không để trống");
                    }
                    else if (address.isEmpty()) {
                        ed_edit_address.setError("Không để trống");
                    } else {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("fullname", fullname);
                        updates.put("phone", phone);
                        updates.put("address", address);
                        reference.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(EditProfile.this, Success.class);
                                    intent.putExtra("checkman",3);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(EditProfile.this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

        }
    }
}