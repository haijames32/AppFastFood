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
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
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
import hainb21127.poly.appfastfood.activity.Messagers;
import hainb21127.poly.appfastfood.adapter.CategoryAdapter;
import hainb21127.poly.appfastfood.adapter.ProductAdapter;
import hainb21127.poly.appfastfood.adapter.SliderAdapter;
import hainb21127.poly.appfastfood.model.Category;
import hainb21127.poly.appfastfood.model.Product;
import hainb21127.poly.appfastfood.model.Slider;
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
    LinearLayout btn_seemore, btnCart, btnChat;
    ProductAdapter productAdapter;
    CategoryAdapter categoryAdapter;
    Context context;
    List<Product> mpProducts;
    List<Category> mCategories;
    TextView tvName;
    CardView img_home;
    FirebaseDatabase database;
    ViewPager2 viewPager2;
    Handler sliderHandler;
    List<Slider> listSlider;
    SliderAdapter sliderAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcv_recommended = view.findViewById(R.id.rcv_recommended);
        rcv_cate = view.findViewById(R.id.rcv_cate);
        tvName = view.findViewById(R.id.tv_name_user_home);
        btn_seemore = view.findViewById(R.id.btn_seemore);
        swipeRefreshLayout = view.findViewById(R.id.refresh_home);
        img_home = view.findViewById(R.id.img_home);
//        btnCart = view.findViewById(R.id.btn_cart);
        btnChat = view.findViewById(R.id.btn_chat);
        viewPager2 = view.findViewById(R.id.viewPager2_slider_home);

        database = FirebaseDatabase.getInstance();

        mpProducts = new ArrayList<>();
        mCategories = new ArrayList<>();
        listSlider = new ArrayList<>();
        productAdapter = new ProductAdapter(context);
        categoryAdapter = new CategoryAdapter(context);
        sliderAdapter = new SliderAdapter(listSlider, viewPager2);
        sliderHandler = new Handler();

        listSlider.add(new Slider(R.drawable.img_slide1));
        listSlider.add(new Slider(R.drawable.img_slide2));
        listSlider.add(new Slider(R.drawable.img_slide3));
        listSlider.add(new Slider(R.drawable.img_slide4));
        listSlider.add(new Slider(R.drawable.img_slide5));
        listSlider.add(new Slider(R.drawable.img_slide6));
        listSlider.add(new Slider(R.drawable.img_slide7));
        listSlider.add(new Slider(R.drawable.img_slide8));
        listSlider.add(new Slider(R.drawable.img_slide9));
        listSlider.add(new Slider(R.drawable.img_slide10));
        viewPager2.setAdapter(sliderAdapter);

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 4000);
            }
        });


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

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.isLoggedIn) startActivity(new Intent(getContext(), Messagers.class));
                else startActivity(new Intent(getContext(), Login.class));
            }
        });

//        btnCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (MainActivity.isLoggedIn) startActivity(new Intent(getContext(), CartActivity.class));
//                else startActivity(new Intent(getContext(), Login.class));
//            }
//        });

        btn_seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AllProduct.class));
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = database.getReference("users").child(user.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user1 = snapshot.getValue(User.class);
                        String name = user1.getFullname();
                        tvName.setText("Hi " + name);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
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

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };
}