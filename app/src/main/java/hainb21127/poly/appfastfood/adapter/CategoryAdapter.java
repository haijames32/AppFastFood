package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{

    private List<Category> arrayList;
    private Context context;
    public void setData(List<Category> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }
    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView id_image;
        private TextView tv_item_name;
        private TextView tv_item_price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            tv_item_name = itemView.findViewById(R.id.tv_item_name);
//            tv_item_price = itemView.findViewById(R.id.tv_item_price);
//            id_image = itemView.findViewById(R.id.item_img);
        }
    }
}
