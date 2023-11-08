package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.activity.ProductDetail;
import hainb21127.poly.appfastfood.config.Utilities;
import hainb21127.poly.appfastfood.model.Cart;
import hainb21127.poly.appfastfood.model.Cart2;

public class ThanhToanAdapter extends BaseAdapter {
    Context context;
    private List<Cart2> list;

    public ThanhToanAdapter(Context context, List<Cart2> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = mInflater.inflate(R.layout.item_thanhtoan, null);
        }
        Cart2 cart = list.get(i);

        TextView tv_item_name = view.findViewById(R.id.tv_name_product_lineitem_item);
        TextView tv_item_price = view.findViewById(R.id.tv_price_product_lineitem_item);
        TextView tv_soluong = view.findViewById(R.id.tv_soluong_lineitem_item);
        TextView tv_tongtien = view.findViewById(R.id.tv_tongtien_lineitem_item);
        ImageView id_image = view.findViewById(R.id.img_product_lineitem_item);

        tv_item_name.setText(cart.getId_sanpham().getTensp());
        tv_item_price.setText(Utilities.addDots(cart.getId_sanpham().getGiasp()) + "đ");
        tv_soluong.setText("x"+cart.getSoluong());
        tv_tongtien.setText(Utilities.addDots(cart.getTongtien()) + "đ");
        Picasso.get().load(cart.getId_sanpham().getImage()).into(id_image);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProductDetail.class);
                intent.putExtra("idPro", cart.getId_sanpham().getId());
                intent.putExtra("namePro", cart.getId_sanpham().getTensp());
                intent.putExtra("pricePro", cart.getId_sanpham().getGiasp());
                intent.putExtra("imagePro", cart.getId_sanpham().getImage());
                intent.putExtra("motaPro", cart.getId_sanpham().getMota());
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }
}
