package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.activity.ProductDetail;
import hainb21127.poly.appfastfood.model.Category;
import hainb21127.poly.appfastfood.model.Product;

public class CategoryDetailAdapter extends BaseAdapter {
    private Context context;
    private List<Product> aProducts;

    public CategoryDetailAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<Product> arrayList) {
       this.aProducts = arrayList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return aProducts.size();
    }

    @Override
    public Object getItem(int i) {
        return aProducts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = mInflater.inflate(R.layout.category_detail_item, null);
            Product product = aProducts.get(i);
            TextView tv_name = view.findViewById(R.id.tv_name_detailtl_item);
            TextView tv_price = view.findViewById(R.id.tv_price_detailtl_item);
            TextView img_item = view.findViewById(R.id.img_detailtl_item);
            Picasso.get().load(product.getImage()).into((Target) img_item);
            tv_name.setText(product.getTensp());
            tv_price.setText(product.getGiasp() + "");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ProductDetail.class);
                    intent.putExtra("idPro", product.getId());
                    intent.putExtra("namePro", product.getTensp());
                    intent.putExtra("pricePro", product.getGiasp());
                    intent.putExtra("imagePro", product.getImage());
                    intent.putExtra("motaPro", product.getMota());
                    view.getContext().startActivity(intent);
                }
            });
        }
        return view;
    }
}
