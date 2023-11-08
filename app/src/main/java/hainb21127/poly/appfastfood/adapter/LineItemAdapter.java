package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import hainb21127.poly.appfastfood.activity.ProductDetail;
import hainb21127.poly.appfastfood.config.Utilities;
import hainb21127.poly.appfastfood.model.Lineitem;

public class LineItemAdapter extends RecyclerView.Adapter<LineItemAdapter.MyViewHolder> {
    Context context;
    List<Lineitem> list;

    public LineItemAdapter(Context context, List<Lineitem> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thanhtoan, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int i = position;
        Lineitem lineitem = list.get(i);
        if (lineitem == null)
            return;
        holder.tvNameSp.setText(lineitem.getId_sanpham().getTensp());
        holder.tvSoluong.setText("x" + lineitem.getSoluong());
        holder.tvPriceSp.setText(Utilities.addDots(lineitem.getGiatien()) + "đ");
        holder.tvTongtien.setText(Utilities.addDots(lineitem.getTongtien()) + "đ");
        Picasso.get().load(lineitem.getId_sanpham().getImage()).into(holder.img_Sp);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProductDetail.class);
                intent.putExtra("idPro", lineitem.getId_sanpham().getId());
                intent.putExtra("namePro", lineitem.getId_sanpham().getTensp());
                intent.putExtra("pricePro", lineitem.getId_sanpham().getGiasp());
                intent.putExtra("imagePro", lineitem.getId_sanpham().getImage());
                intent.putExtra("motaPro", lineitem.getId_sanpham().getMota());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameSp, tvPriceSp, tvSoluong, tvTongtien;
        ImageView img_Sp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameSp = itemView.findViewById(R.id.tv_name_product_lineitem_item);
            tvPriceSp = itemView.findViewById(R.id.tv_price_product_lineitem_item);
            tvSoluong = itemView.findViewById(R.id.tv_soluong_lineitem_item);
            tvTongtien = itemView.findViewById(R.id.tv_tongtien_lineitem_item);
            img_Sp = itemView.findViewById(R.id.img_product_lineitem_item);
        }
    }
}
