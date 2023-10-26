package hainb21127.poly.appfastfood.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import hainb21127.poly.appfastfood.adapter.CategoryAdapter;
import hainb21127.poly.appfastfood.adapter.ProductAdapter;
import hainb21127.poly.appfastfood.database.FirebaseDB;
import hainb21127.poly.appfastfood.model.Category;
import hainb21127.poly.appfastfood.model.Product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    ProductAdapter adapter;
    CategoryAdapter categoryAdapter;
    Context context;
    List<Product> mpProducts;
    List<Category> mCategories;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcv_recommended = view.findViewById(R.id.rcv_recommended);
        rcv_cate = view.findViewById(R.id.rcv_cate);

        mpProducts = new ArrayList<>();
        adapter = new ProductAdapter(context);

        categoryAdapter = new CategoryAdapter(context);
        mCategories = new ArrayList<>();



        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rcv_cate.setLayoutManager(linearLayoutManager1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rcv_recommended.setLayoutManager(linearLayoutManager);
    }

}