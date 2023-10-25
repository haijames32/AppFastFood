package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.activity.ProductDetail;
import hainb21127.poly.appfastfood.config.Utilities;
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

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = mProducts.get(position);
        if (product == null) {
            return;
        }
        holder.tv_item_name.setText(product.getTensp());
        holder.tv_item_price.setText(Utilities.addDots(product.getGiasp())+ "đ");
        Picasso.get().load(product.getImage()).into(holder.id_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, ProductDetail.class);
//                intent.putExtra("idPro",product.getId());
//                intent.putExtra("namePro",product.getTensp());
//                intent.putExtra("pricePro",product.getGiasp());
//                intent.putExtra("imagePro",product.getImage());
//                intent.putExtra("motaPro",product.getMota());
//                context.startActivity(intent);
            }
        });
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
        LinearLayout lo_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_product_name_item);
            tv_item_price = itemView.findViewById(R.id.tv_product_price_item);
            id_image = itemView.findViewById(R.id.img_product_item);
            lo_item = itemView.findViewById(R.id.lo_product_item);
        }
    }
}
