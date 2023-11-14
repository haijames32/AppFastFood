package hainb21127.poly.appfastfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.adapter.ChatAdapter;
import hainb21127.poly.appfastfood.adapter.MessageAdapter;
import hainb21127.poly.appfastfood.model.Messager;
import hainb21127.poly.appfastfood.model.User;

public class Chat extends AppCompatActivity {
    Button btnSend;
    ImageView btnBack;
    TextInputEditText edContent;
    ListView lv;
    List<Messager> listMess;
    FirebaseDatabase database;
    int idMember;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    MessageAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        edContent = findViewById(R.id.ed_message_chat);
        btnBack = findViewById(R.id.btn_back_chat);
        btnSend = findViewById(R.id.btn_send_chat);
        lv = findViewById(R.id.lv_chat);
        swipeRefreshLayout = findViewById(R.id.refresh_chat);
        context = this;

        database = FirebaseDatabase.getInstance();
        listMess = new ArrayList<>();
        adapter = new MessageAdapter(getApplicationContext(), listMess);
//        ChatAdapter adapter = new ChatAdapter(getApplicationContext(),listMess);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//        lv.setLayoutManager(linearLayoutManager);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idU = user.getUid();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DatabaseReference reference = database.getReference("messages");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listMess.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Messager messager = new Messager();
                            messager.setId(dataSnapshot.getKey());
                            messager.setContent(dataSnapshot.child("content").getValue(String.class));
                            messager.setTime(dataSnapshot.child("time").getValue(String.class));

                            listMess.add(messager);
                            Log.i("sai", "onDataChange: " + listMess.size());
                        }
                        lv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("message", "onCancelled: " + error);
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getListMess(idU);
//        DatabaseReference reference = database.getReference("messages");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listMess.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Messager messager = new Messager();
//                    messager.setId(dataSnapshot.getKey());
//                    messager.setContent(dataSnapshot.child("content").getValue(String.class));
//                    messager.setTime(dataSnapshot.child("time").getValue(String.class));
//
//                    listMess.add(messager);
//                    Log.i("sai", "onDataChange: " + listMess.size());
//                    Log.i("id", "onDataChange: "+messager.getId());
//                    Log.i("content", "onDataChange: "+messager.getContent());
//                    Log.i("time", "onDataChange: "+messager.getTime());
//                }
//                lv.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.i("message", "onCancelled: " + error);
//            }
//        });
    }

//    private void getListMessage() {
//        DatabaseReference reference = database.getReference("messages");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    DatabaseReference refReceived = dataSnapshot.child("id_receiver").getRef();
//                    DatabaseReference refSend = dataSnapshot.child("id_sender").getRef();
//
//                    Messager messager = new Messager();
//                    messager.setId(dataSnapshot.getKey());
//                    messager.setContent(dataSnapshot.child("content").getValue(String.class));
//                    messager.setTime(dataSnapshot.child("time").getValue(String.class));
//
////                    refSend.addValueEventListener(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot snapshot) {
////                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
////                                User user = new User();
////                                user.setId(dataSnapshot1.getKey());
////                                user.setFullname(dataSnapshot1.child("fullname").getValue(String.class));
////                                messager.setId_sender(user);
////                            }
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError error) {
////                            Log.i("sender", "onCancelled: " + error);
////                        }
////                    });
////                    refReceived.addValueEventListener(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//////                            idMember = snapshot.child("idMem").getValue(Integer.class);
////                            messager.setId_receiver(snapshot.child("idMem").getValue(String.class));
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError error) {
////                            Log.i("receiver", "onCancelled: " + error);
////                        }
////                    });
//
//                    listMess.add(messager);
//                    Log.i("sai", "onDataChange: "+listMess.size());
//                }
//                lv.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.i("message", "onCancelled: " + error);
//            }
//        });
//    }

    private void getListMess(String idU){
        DatabaseReference reference = database.getReference("messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DatabaseReference reference1 = dataSnapshot.child("id_chat").getRef();
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                String id = dataSnapshot1.getKey();
                                if(id.equals(idU)){
                                    User user1 = new User();
                                    Messager messager = new Messager();
                                    user1.setId(dataSnapshot1.getKey());
                                    user1.setFullname(dataSnapshot1.child("fullname").getValue(String.class));

                                    messager.setId_sender(user1);
                                    messager.setContent(dataSnapshot.child("content").getValue(String.class));
                                    messager.setTime(dataSnapshot.child("time").getValue(String.class));

                                    listMess.add(messager);
                                }
                            }
                            Log.i("sai", "onDataChange: "+listMess.size());
                            lv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.i("err", "onCancelled: "+error);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("err", "onCancelled: "+error);
            }
        });
    }
}