package hainb21127.poly.appfastfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.adapter.CategoryDetailAdapter;
import hainb21127.poly.appfastfood.database.FirebaseDB;
import hainb21127.poly.appfastfood.model.Category;
import hainb21127.poly.appfastfood.model.Product;

public class CategoryDetail extends AppCompatActivity {
   GridView gridView;
   List<Product> mProducts;
   Context context;
   CategoryDetailAdapter categoryDetailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        gridView =findViewById(R.id.grv_cat_detail);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("nameCat");
        categoryDetailAdapter = new CategoryDetailAdapter(context);
        Log.d("zzzzz", "onCreate: "+id);


//        // Lấy tham chiếu đến Firebase Realtime Database
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//
//// Lấy danh sách sản phẩm
//        DatabaseReference databaseReference = firebaseDatabase.getReference("products");
//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Lọc danh sách sản phẩm theo id của thể loại
//                mProducts = new ArrayList<>();
//               for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                   Product product = dataSnapshot.getValue(Product.class);
//                   if (product.getId_theloai().getId().equals(id)){
//                       mProducts.add(product);
//                   }
//               }
//                gridView =findViewById(R.id.grv_cat_detail);
//               categoryDetailAdapter = new CategoryDetailAdapter(context);
//               categoryDetailAdapter.setData(mProducts);
//               gridView.setAdapter(categoryDetailAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.i("zzzzzz", "lỗi: "+error.toString());
//            }
//        };
//        databaseReference.addValueEventListener(valueEventListener);
        FirebaseDatabase database = FirebaseDB.getDatabaseInstance();
        DatabaseReference myref = database.getReference("products").child("id_theloai").child("id");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProducts = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    if (myref.equals(id)) {
                        mProducts.add(product);
                    }
                    categoryDetailAdapter.setData(mProducts);
                    gridView.setAdapter(categoryDetailAdapter);
                    Log.d("zzzzz", "onCreate: "+id);
                }
                categoryDetailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: "+error.toString());
            }
        });

//        myref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mProducts = new ArrayList<>();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Product product = dataSnapshot.getValue(Product.class);
//                    if (product.getId_theloai().getId().equals(id)) {
//                        mProducts.add(product);
//                    }
//                    categoryDetailAdapter.setData(mProducts);
//                    gridView.setAdapter(categoryDetailAdapter);
//                    Log.d("zzzzz", "onCreate: "+id);
//                }
//                categoryDetailAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("TAG", "onCancelled: "+error.toString());
//            }
//        });
    }
    }
