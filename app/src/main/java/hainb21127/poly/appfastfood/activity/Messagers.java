package hainb21127.poly.appfastfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.adapter.MessageAdapter;
import hainb21127.poly.appfastfood.model.Messager;
import hainb21127.poly.appfastfood.model.User;

public class Messagers extends AppCompatActivity {
    Button btnSend;
    ImageView btnBack;
    TextInputEditText edContent;
    ListView lv;
    List<Messager> listMess;
    FirebaseDatabase database;
    Context context;
    MessageAdapter adapter;
    String nameU;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagers);
        edContent = findViewById(R.id.ed_message_msg);
        btnBack = findViewById(R.id.btn_back_msg);
        btnSend = findViewById(R.id.btn_send_msg);
        lv = findViewById(R.id.lv_msg);
        context = this;

        database = FirebaseDatabase.getInstance();
        listMess = new ArrayList<>();
        adapter = new MessageAdapter(getApplicationContext(), listMess);

        Intent intent = getIntent();
        String idchat = intent.getStringExtra("idChat");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idU = user.getUid();
        if (user != null) {
            DatabaseReference reference = database.getReference("users").child(idU);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    nameU = snapshot.child("fullname").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("err", "onCancelled: " + error);
                }
            });
        }

        getListMess(idchat);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = edContent.getText().toString().trim();
                if (content.length() == 0) {
                    edContent.setError("Hãy nhập tin nhắn");
                } else {
                    DatabaseReference ref = database.getReference("messages").push();
                    Messager messager = new Messager(content, getCurrentTime(), idU);
                    ref.setValue(messager).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference ref1 = ref.child("id_chat");
                                DatabaseReference ref2 = ref1.child(idchat);
                                DatabaseReference ref3 = ref2.child("id_user");
                                DatabaseReference ref4 = ref3.child(idU);
                                DatabaseReference ref5 = ref4.child("fullname");
                                ref5.setValue(nameU);
                                edContent.setText("");
                            }
                        }
                    });
                }
            }
        });
    }

    private void getListMess(String idChat) {
        DatabaseReference reference = database.getReference("messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMess.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DatabaseReference reference1 = dataSnapshot.child("id_chat").getRef();
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                if (dataSnapshot1.getKey().equals(idChat)) {
                                    Messager messager = new Messager();
                                    messager.setContent(dataSnapshot.child("content").getValue(String.class));
                                    messager.setTime(dataSnapshot.child("time").getValue(String.class));
                                    messager.setStatus(dataSnapshot.child("status").getValue(String.class));

                                    listMess.add(messager);
                                }
                            }
                            lv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
//                            lv.setAdapter(new MessageAdapter(getApplicationContext(), listMess));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.i("err", "onCancelled: " + error);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("err", "onCancelled: " + error);
            }
        });
    }

    public String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date currentTime = new Date();
        return dateFormat.format(currentTime);
    }
}