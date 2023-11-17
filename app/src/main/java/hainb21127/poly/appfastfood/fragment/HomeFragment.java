package hainb21127.poly.appfastfood.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import hainb21127.poly.appfastfood.MainActivity;
import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.activity.AllProduct;
import hainb21127.poly.appfastfood.activity.CartActivity;
import hainb21127.poly.appfastfood.activity.Chat;
import hainb21127.poly.appfastfood.activity.Login;
import hainb21127.poly.appfastfood.adapter.CategoryAdapter;
import hainb21127.poly.appfastfood.adapter.ProductAdapter;
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
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout btn_seemore, btnCart;
    FloatingActionButton floating;
    ProductAdapter productAdapter;
    CategoryAdapter categoryAdapter;
    Context context;
    List<Product> mpProducts;
    List<Category> mCategories;
    TextView tvName;
    ImageView img_user;
    CardView img_home;
    FirebaseDatabase database;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcv_recommended = view.findViewById(R.id.rcv_recommended);
        rcv_cate = view.findViewById(R.id.rcv_cate);
        tvName = view.findViewById(R.id.tv_name_user_home);
        img_user = view.findViewById(R.id.img_user_home);
        btn_seemore = view.findViewById(R.id.btn_seemore);
        swipeRefreshLayout = view.findViewById(R.id.refresh_home);
        img_home = view.findViewById(R.id.img_home);
        floating = view.findViewById(R.id.fachatbot);
        btnCart = view.findViewById(R.id.btn_cart);

        mpProducts = new ArrayList<>();
        mCategories = new ArrayList<>();
        productAdapter = new ProductAdapter(context);
        categoryAdapter = new CategoryAdapter(context);

        database = FirebaseDatabase.getInstance();

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rcv_cate.setLayoutManager(linearLayoutManager1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rcv_recommended.setLayoutManager(linearLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListCate();
                getListProduct();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.isLoggedIn) startActivity(new Intent(getContext(), Chat.class));
                else startActivity(new Intent(getContext(), Login.class));
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.isLoggedIn) startActivity(new Intent(getContext(), CartActivity.class));
                else startActivity(new Intent(getContext(), Login.class));
            }
        });

        btn_seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AllProduct.class));
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            DatabaseReference reference = database.getReference("users").child(user.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        User user1 = snapshot.getValue(User.class);
                        String name = user1.getFullname();
                        String img = user1.getImage();
                        tvName.setText("Hi "+name);
                        Picasso.get().load(img).into(img_user);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            img_home.setVisibility(View.INVISIBLE);
        }

        getListCate();
        getListProduct();
    }


    private void getListProduct() {
        DatabaseReference myref = database.getReference("products");
        Query query = myref.limitToFirst(10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mpProducts.clear();
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
}