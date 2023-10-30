package hainb21127.poly.appfastfood.fragment;

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
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.adapter.OrderAdapter;
import hainb21127.poly.appfastfood.model.Order;

public class OrderFragment extends Fragment {


    public OrderFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
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
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    LinearLayout no_order;
    RecyclerView rcv_order;
    OrderAdapter orderAdapter;
    List<Order> listOrder;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        no_order = view.findViewById(R.id.no_order_order);
        rcv_order = view.findViewById(R.id.rcv_order);

        orderAdapter = new OrderAdapter(getContext());
        listOrder = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv_order.setLayoutManager(linearLayoutManager);

        getListOrder();
    }

    private void getListOrder() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("orders");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            String idOrder = dataSnapshot2.getKey();
                            Log.i("TAG", "onDataChange: "+idOrder);
                            if (idOrder.equals(id)) {
                                Order order = new Order();
                                order.setId_user(dataSnapshot2.getKey());
                                order.setId(dataSnapshot.getKey());
                                order.setTrangthai(dataSnapshot.child("trangthai").getValue(String.class));
                                order.setDate(dataSnapshot.child("date").getValue(String.class));
                                order.setTongtien(dataSnapshot.child("tongtien").getValue(Integer.class));
                                listOrder.add(order);
                                orderAdapter.setData(listOrder);
                                rcv_order.setAdapter(orderAdapter);
                                if(listOrder.size() == 0){
                                    no_order.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("listorder", "onCancelled: " + error.toString());
            }
        });
    }
}