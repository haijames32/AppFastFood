package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.activity.CategoryDetail;
import hainb21127.poly.appfastfood.fragment.HomeFragment;
import hainb21127.poly.appfastfood.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category> arrayList;
    Context context;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Category> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category = arrayList.get(position);
        if (category == null) {
            return;
        }
        holder.tv_item_name.setText(category.getNameCat());
        Picasso.get().load(category.getImageCat()).into(holder.id_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CategoryDetail.class);
                intent.putExtra("id", category.getId());
                intent.putExtra("nameCat", category.getNameCat());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView id_image;
        private TextView tv_item_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.name_cat_item);
            id_image = itemView.findViewById(R.id.img_cat_item);
        }
    }
}
