package hainb21127.poly.appfastfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hainb21127.poly.appfastfood.database.FirebaseDB;
import hainb21127.poly.appfastfood.databinding.ActivityMainBinding;
import hainb21127.poly.appfastfood.fragment.CartFragment;
import hainb21127.poly.appfastfood.fragment.HomeFragment;
import hainb21127.poly.appfastfood.fragment.OrderFragment;
import hainb21127.poly.appfastfood.fragment.ProfileFragment;
import hainb21127.poly.appfastfood.url.URL;

public class MainActivity extends AppCompatActivity {
    //    ActivityMainBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;

    BottomNavigationView bottomNavigationView;
    Fragment fragment;
    public static boolean checkLogin = false;
    public static String frm = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set Tab home làm màn hình chính
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        replaceFragment(new HomeFragment());

        database = FirebaseDB.getDatabaseInstance();

        reference = database.getReference("product");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id = snapshot.child("id").getValue(String.class);
                String name = snapshot.child("name").getValue(String.class);
                Log.i("TAG", "onDataChange: "+id+" "+name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: "+ error.toString());
            }
        });
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                String name = snapshot.getValue(String.class);
////                Log.i("TAG", "onDataChange: "+name);
//                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                    // Lấy dữ liệu của từng user
//                    String id = userSnapshot.child("id").getValue(String.class);
//                    String name = userSnapshot.child("name").getValue(String.class);
//
//                    // Do something with the data
//                    Log.d("User", "Username: " + id + ", Email: " + name);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.i("TAG", "onCancelled: "+error.toString());
//            }
//        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_home ){
                    // Khi chọn Home
                    fragment = new HomeFragment();
                    replaceFragment(fragment);
                    return true;
                }
                if(item.getItemId() == R.id.nav_order ){
                    // Khi chọn Order
                    if(item.getItemId() == R.id.nav_order ){
                        // Khi chọn Home
                        fragment = new OrderFragment();
                        replaceFragment(fragment);
                        return true;
                    }
//                    if(checkLogin){
//                        fragment = new OrderFragment();
//                        replaceFragment(fragment);
//                        return true;
//                    }else{
//                        Intent intent = new Intent(MainActivity.this, SigninActivity.class);
//                        startActivity(intent);
//                    }

                }
                if(item.getItemId() == R.id.nav_cart ){
                    // Khi chọn Order
                    if(item.getItemId() == R.id.nav_cart ){
                        // Khi chọn Home
                        fragment = new CartFragment();
                        replaceFragment(fragment);
                        return true;
                    }
//                    if(checkLogin){
//                        fragment = new OrderFragment();
//                        replaceFragment(fragment);
//                        return true;
//                    }else{
//                        Intent intent = new Intent(MainActivity.this, SigninActivity.class);
//                        startActivity(intent);
//                    }

                }
                if(item.getItemId() == R.id.nav_profile ){
                    // Khi chọn Profile
                    if(item.getItemId() == R.id.nav_profile ){
                        // Khi chọn Home
                        fragment = new ProfileFragment();
                        replaceFragment(fragment);
                        return true;
                    }
//                    if(checkLogin){
//                        fragment = new ProfileFragment();
//                        replaceFragment(fragment);
//                        return true;
//                    }else{
//                        Intent intent = new Intent(MainActivity.this, SigninActivity.class);
//                        startActivity(intent);
//                    }
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout,fragment);
//        fragmentTransaction.commit();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (frm.equals("Đơn mua")) {
//            replaceFragment(new OrderFragment());
//            bottomNavigationView.setSelectedItemId(R.id.nav_order);
//            frm = "";
//        }
//    }
}