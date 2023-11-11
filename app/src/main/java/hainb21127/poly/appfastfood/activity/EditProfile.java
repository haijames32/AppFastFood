package hainb21127.poly.appfastfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.model.User;

public class EditProfile extends AppCompatActivity {
    TextInputEditText ed_edit_name, ed_edit_phone, ed_edit_address;
    ImageView img_back, btn_edit_img_profile;
    Button btn_save;
    FirebaseDatabase database;
    DatabaseReference reference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri; // Để lưu trữ đường dẫn hình ảnh đã chọn
    String fullname, address;
    int phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        img_back = findViewById(R.id.img_back);
        ed_edit_name = findViewById(R.id.ed_edit_fullname);
        ed_edit_phone = findViewById(R.id.ed_edit_phone);
        ed_edit_address = findViewById(R.id.ed_edit_address);
        btn_save = findViewById(R.id.btn_edit_save_profile);
        btn_edit_img_profile = findViewById(R.id.id_img_edit_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        database = FirebaseDatabase.getInstance();
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
            btn_edit_img_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImagePicker();
                }
            });
            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fullname = ed_edit_name.getText().toString();
                    phone = Integer.parseInt(ed_edit_phone.getText().toString());
                    address = ed_edit_address.getText().toString();
                    if (fullname.isEmpty()) {
                        ed_edit_name.setError("Không để trống");
                    } else if (TextUtils.isEmpty(phone + "")) {
                        ed_edit_phone.setError("Không để trống");
                    } else if (address.isEmpty()) {
                        ed_edit_address.setError("Không để trống");
                    } else {
                        uploadImageToFirebase(selectedImageUri);
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

    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            String imageName = System.currentTimeMillis() + ".jpg";
            StorageReference imageRef = storageRef.child("images/" + imageName);

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Lấy URL của hình ảnh sau khi tải lên thành công
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    String imageUrl = downloadUrl.toString();

                                    // Tiến hành lưu thông tin sản phẩm vào Firebase Realtime Database
//                                saveProductToDatabase(name, price, description, category, imageUrl);
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("fullname", fullname);
                                    updates.put("phone", phone);
                                    updates.put("address", address);
                                    updates.put("image", imageUrl);
                                    reference.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(EditProfile.this, Success.class);
                                                intent.putExtra("checkman", 3);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(EditProfile.this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfile.this, "Lỗi khi tải ảnh lên Firebase Storage: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            // Hiển thị hình ảnh đã chọn nếu cần
            btn_edit_img_profile.setImageURI(selectedImageUri);
        }
    }
}