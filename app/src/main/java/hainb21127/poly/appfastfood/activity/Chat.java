package hainb21127.poly.appfastfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.adapter.ChatAdapter;
import hainb21127.poly.appfastfood.adapter.MessageAdapter;
import hainb21127.poly.appfastfood.model.Chats;
import hainb21127.poly.appfastfood.model.Messager;
import hainb21127.poly.appfastfood.model.User;

public class Chat extends AppCompatActivity {
    ImageView btnBack;
    ListView lv;
    List<Chats> listChat;
    FirebaseDatabase database;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    ChatAdapter adapter;
    String nameU, idChat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        btnBack = findViewById(R.id.btn_back_chat);
        lv = findViewById(R.id.lv_chat);
        swipeRefreshLayout = findViewById(R.id.refresh_chat);
        context = this;

        database = FirebaseDatabase.getInstance();
        listChat = new ArrayList<>();
        adapter = new ChatAdapter(getApplicationContext(),listChat);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idU = user.getUid();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getListChat(idU);
    }

    private void getListChat(String idU){
        DatabaseReference reference = database.getReference("chats");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DatabaseReference reference1 = dataSnapshot.child("id_user").getRef();
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                if(dataSnapshot1.getKey().equals(idU)){
                                    Chats chats = new Chats();
                                    chats.setId(dataSnapshot.getKey());
                                    listChat.add(chats);
                                }
                            }
                            lv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}