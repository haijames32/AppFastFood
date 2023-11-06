package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.config.Utilities;
import hainb21127.poly.appfastfood.model.Cart2;

public class DatHangAdapter extends RecyclerView.Adapter<DatHangAdapter.MyViewHolder>{
    Context context;
    List<Cart2> list;

    public DatHangAdapter(Context context, List<Cart2> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cart2 cart = list.get(position);
        if(cart == null)
            return;
        holder.tv_item_name.setText(cart.getId_sanpham().getTensp());
        holder.tv_item_price.setText(Utilities.addDots(cart.getId_sanpham().getGiasp()) + "đ");
        holder.tv_soluong.setText(cart.getSoluong() + "");
        holder.tv_tongtien.setText(Utilities.addDots(cart.getTongtien()) + "đ");
        Picasso.get().load(cart.getId_sanpham().getImage()).into(holder.id_image);
    }

    @Override
    public int getItemCount() {
        if(list != null)
            list.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_name,tv_item_price,tv_soluong,tv_tongtien;
        ImageView id_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_name_cart_item);
            tv_item_price = itemView.findViewById(R.id.tv_price_cart_item);
            tv_soluong = itemView.findViewById(R.id.tv_soluong_cart_item);
            tv_tongtien = itemView.findViewById(R.id.tv_tongtien_cart_item);
            id_image = itemView.findViewById(R.id.img_cart_item);
        }
    }
}
