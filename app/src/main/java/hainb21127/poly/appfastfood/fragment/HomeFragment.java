package hainb21127.poly.appfastfood.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.adapter.CategoryAdapter;
import hainb21127.poly.appfastfood.adapter.ProductAdapter;
import hainb21127.poly.appfastfood.adapter.SliderAdapter;
import hainb21127.poly.appfastfood.database.FirebaseDB;
import hainb21127.poly.appfastfood.model.Category;
import hainb21127.poly.appfastfood.model.Product;
import hainb21127.poly.appfastfood.model.User;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    RecyclerView rcv_recommended, rcv_cate;
    ProductAdapter productAdapter;
    CategoryAdapter categoryAdapter;
    SliderAdapter sliderAdapter;
    Context context;
    List<Product> mpProducts;
    List<Category> mCategories;
    ViewPager2 viewPager2;
    GestureDetector gestureDetector;
    Timer timer;
    private int currentPage = 0;
    TextView tvName;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcv_recommended = view.findViewById(R.id.rcv_recommended);
        rcv_cate = view.findViewById(R.id.rcv_cate);
        tvName = view.findViewById(R.id.tv_name_user_home);

        mpProducts = new ArrayList<>();
        mCategories = new ArrayList<>();
        productAdapter = new ProductAdapter(context);
        categoryAdapter = new CategoryAdapter(context);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rcv_cate.setLayoutManager(linearLayoutManager1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rcv_recommended.setLayoutManager(linearLayoutManager);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("users").child(user.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        User user1 = snapshot.getValue(User.class);
                        String name = user1.getFullname();
                        tvName.setText("Hi "+name);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        getListCate();
        getListProduct();

    }


    private void getListProduct() {
        FirebaseDatabase database = FirebaseDB.getDatabaseInstance();
        DatabaseReference myref = database.getReference("products");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

                    mpProducts.add(product);
                    productAdapter.setData(mpProducts);
                    rcv_recommended.setAdapter(productAdapter);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("listsp", "onCancelled: " + error.toString());
            }
        });
    }

    private void getListCate() {
        FirebaseDatabase database = FirebaseDB.getDatabaseInstance();
        DatabaseReference myref = database.getReference("category");
        Query query = myref.orderByChild("id");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mCategories.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = new Category();
                    category.setId(dataSnapshot.getKey());
                    category.setNameCat(dataSnapshot.child("nameCat").getValue(String.class));
                    category.setImageCat(dataSnapshot.child("imageCat").getValue(String.class));
                    mCategories.add(category);
                    categoryAdapter.setData(mCategories);
                    rcv_cate.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}