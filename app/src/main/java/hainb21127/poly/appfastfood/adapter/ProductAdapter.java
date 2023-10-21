package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private List<Product> mProducts;
    private Context context;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Product> arrayList) {
        this.mProducts = arrayList;
        notifyDataSetChanged();
    }

    public ProductAdapter(List<Product> mProducts) {
        this.mProducts = mProducts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_product_home, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = mProducts.get(position);
        if (product == null) {
            return;
        }
        holder.tv_item_name.setText(product.getTensp());
        holder.tv_item_price.setText(product.getGiasp() + "");
        Picasso.get().load(product.getImage()).into(holder.id_image);
    }

    @Override
    public int getItemCount() {
        if (mProducts != null) {
            return mProducts.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView id_image;
        private TextView tv_item_name;
        private TextView tv_item_price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_item_price = itemView.findViewById(R.id.tv_item_price);
            id_image = itemView.findViewById(R.id.item_img);
        }
    }
}
