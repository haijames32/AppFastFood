package hainb21127.poly.appfastfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.adapter.AllProductAdapter;
import hainb21127.poly.appfastfood.database.FirebaseDB;
import hainb21127.poly.appfastfood.model.Product;

public class AllProduct extends AppCompatActivity {
    GridView grv;
    EditText edSearch;
    ImageView btnBack;
    LinearLayout ll_nopro;
    AllProductAdapter adapter;
    List<Product> list;
    String idCat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);
        btnBack = findViewById(R.id.btn_back_all_product);
        grv = findViewById(R.id.grv_all_detail);
        edSearch = findViewById(R.id.ed_search_all_product);
        ll_nopro = findViewById(R.id.no_product);

        list = new ArrayList<>();
        adapter = new AllProductAdapter(getApplicationContext());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getListProduct();

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                list.clear();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myref = database.getReference("products");
                myref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Product product = new Product();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    idCat = dataSnapshot2.getKey();
                                }
                            }
                            if (dataSnapshot.child("tensp").getValue(String.class).contains(editable)) {
                                product.setId_theloai(idCat);
                                product.setId(dataSnapshot.getKey());
                                product.setTensp(dataSnapshot.child("tensp").getValue(String.class));
                                product.setGiasp(dataSnapshot.child("giasp").getValue(Integer.class));
                                product.setMota(dataSnapshot.child("mota").getValue(String.class));
                                product.setImage(dataSnapshot.child("image").getValue(String.class));

                                list.add(product);
                                ll_nopro.setVisibility(View.INVISIBLE);
                            } else if (list.size() == 0) {
                                ll_nopro.setVisibility(View.VISIBLE);
                            }
                        }
                        grv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("listsp", "onCancelled: " + error.toString());
                    }
                });
            }
        });
    }

    private void getListProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("products");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = new Product();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            String idCat = dataSnapshot2.getKey();
                            product.setId_theloai(idCat);
                        }
                    }

                    product.setId(dataSnapshot.getKey());
                    product.setTensp(dataSnapshot.child("tensp").getValue(String.class));
                    product.setGiasp(dataSnapshot.child("giasp").getValue(Integer.class));
                    product.setMota(dataSnapshot.child("mota").getValue(String.class));
                    product.setImage(dataSnapshot.child("image").getValue(String.class));

                    list.add(product);
                    Log.i("sai", "onDataChange: "+list.size());
                }
                grv.setAdapter(adapter);
                adapter.setData(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("listsp", "onCancelled: " + error.toString());
            }
        });
    }
}