package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.model.Cart;
import hainb21127.poly.appfastfood.model.Category;
import hainb21127.poly.appfastfood.model.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder>{
    private List<Product> carts;
    Context context;

    public CartAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<Product> arrayList) {
      this.carts = arrayList;
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
    Product cart = carts.get(position);
    if (cart == null){
        return;
    }
    holder.tv_item_name.setText(cart.getTensp());
    holder.tv_item_price.setText(cart.getGiasp());
    }

    @Override
    public int getItemCount() {
        if (carts != null){
            carts.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView id_image;
        private TextView tv_item_name, tv_item_price, tv_soluong, tv_plus, tv_minus;

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
