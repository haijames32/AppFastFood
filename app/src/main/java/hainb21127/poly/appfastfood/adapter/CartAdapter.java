package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.activity.ProductDetail;
import hainb21127.poly.appfastfood.model.Cart;
import hainb21127.poly.appfastfood.model.Category;
import hainb21127.poly.appfastfood.model.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    List<Cart> list;
    Context context;
    int so = 0;

    public CartAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Cart> arrayList) {
        this.list = arrayList;
        notifyDataSetChanged();
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder holder, int position) {
        Cart cart = list.get(position);
        if (cart == null) {
            return;
        }
        holder.tv_item_name.setText(cart.getId_sanpham().getTensp());
        holder.tv_item_price.setText(cart.getId_sanpham().getGiasp());
        holder.tv_soluong.setText(cart.getSoluong());
        Log.i("adapter_tensp", "onBindViewHolder: "+cart.getId_sanpham().getTensp());
        Log.i("adapter_giasp", "onBindViewHolder: "+cart.getId_sanpham().getGiasp());
        Log.i("adapter_soluong", "onBindViewHolder: "+cart.getSoluong());
        Log.i("adapter_image", "onBindViewHolder: "+cart.getId_sanpham().getImage());
        Picasso.get().load(cart.getId_sanpham().getImage()).into(holder.id_image);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("carts").child(cart.getId());
        DatabaseReference reference1 = reference.child("soluong");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                holder.tv_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(so > 1){
                            so = snapshot.getValue(Integer.class);
                            reference1.setValue(so);
                            so--;
                        }
                    }
                });
                holder.tv_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        so = snapshot.getValue(Integer.class);
                        reference1.setValue(so);
                        so++;
                    }
                });
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                Log.i("cartadapter", "Lỗi tăng giảm số lượng: "+error.toString());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProductDetail.class);
                intent.putExtra("idPro",cart.getId_sanpham().getId());
                intent.putExtra("namePro",cart.getId_sanpham().getTensp());
                intent.putExtra("pricePro",cart.getId_sanpham().getGiasp());
                intent.putExtra("imagePro",cart.getId_sanpham().getImage());
                intent.putExtra("motaPro",cart.getId_sanpham().getMota());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null)
            list.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView id_image;
        TextView tv_item_name, tv_item_price, tv_soluong, tv_plus, tv_minus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_name_cart_item);
            tv_item_price = itemView.findViewById(R.id.tv_price_cart_item);
            tv_soluong = itemView.findViewById(R.id.tv_soluong_cart_item);
            tv_plus = itemView.findViewById(R.id.tv_plus_cart_item);
            tv_minus = itemView.findViewById(R.id.tv_minus_cart_item);
            id_image = itemView.findViewById(R.id.img_cart_item);
        }
    }
}
