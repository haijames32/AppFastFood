package hainb21127.poly.appfastfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hainb21127.poly.appfastfood.activity.Login;
import hainb21127.poly.appfastfood.databinding.ActivityMainBinding;
import hainb21127.poly.appfastfood.fragment.CartFragment;
import hainb21127.poly.appfastfood.fragment.HomeFragment;
import hainb21127.poly.appfastfood.fragment.OrderFragment;
import hainb21127.poly.appfastfood.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    //    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    Fragment fragment;
    public static boolean isLoggedIn = false;
    public static String frm = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        replaceFragment(new HomeFragment());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            isLoggedIn = true;
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    fragment = new HomeFragment();
                    replaceFragment(fragment);
                    return true;
                }
                if (item.getItemId() == R.id.nav_order) {
                    if (isLoggedIn) {
                        fragment = new OrderFragment();
                        replaceFragment(fragment);
                        return true;
                    } else {
                        startActivity(new Intent(MainActivity.this, Login.class));
                    }

                }
                if (item.getItemId() == R.id.nav_cart) {
                    if (isLoggedIn) {
                        fragment = new CartFragment();
                        replaceFragment(fragment);
                        return true;
                    } else {
                        startActivity(new Intent(MainActivity.this, Login.class));
                    }

                }
                if (item.getItemId() == R.id.nav_profile) {
                    if (isLoggedIn) {
                        fragment = new ProfileFragment();
                        replaceFragment(fragment);
                        return true;
                    } else {
                        startActivity(new Intent(MainActivity.this, Login.class));
                    }
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
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