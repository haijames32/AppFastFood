package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.activity.OrderDetail;
import hainb21127.poly.appfastfood.config.Utilities;
import hainb21127.poly.appfastfood.model.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    Context context;
    List<Order> list;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Order> arrayList) {
        this.list = arrayList;
        notifyDataSetChanged();
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder holder, int position) {
        int i = position;
        Order order = list.get(i);
        if (order == null)
            return;
        holder.tvTrangthai.setText(order.getTrangthai());
        holder.tvDate.setText(order.getDate());
        holder.tvTongtien.setText(Utilities.addDots(order.getTongtien()) + "đ");
        if (holder.tvTrangthai.getText().toString().equals("Chờ xác nhận")) {
//            #FF9800
            holder.lo_bg_status.setBackgroundColor(Color.YELLOW);
            holder.img_status.setImageResource(R.drawable.ic_preparing);
            holder.tvTrangthai.setTextColor(Color.YELLOW);
        } else if (holder.tvTrangthai.getText().toString().equals("Đang giao hàng")) {
//            #8BC34A
            holder.lo_bg_status.setBackgroundColor(Color.GREEN);
            holder.img_status.setImageResource(R.drawable.ic_shipping);
            holder.tvTrangthai.setTextColor(Color.GREEN);
        } else if (holder.tvTrangthai.getText().toString().equals("Hoàn thành")) {
//            #03A9F4
            holder.lo_bg_status.setBackgroundColor(Color.BLUE);
            holder.img_status.setImageResource(R.drawable.ic_checkmark);
            holder.tvTrangthai.setTextColor(Color.BLUE);
        } else if (holder.tvTrangthai.getText().toString().equals("Đã hủy")) {
            holder.lo_bg_status.setBackgroundColor(Color.RED);
            holder.tvTrangthai.setTextColor(Color.RED);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), OrderDetail.class);
                intent.putExtra("id_order", order.getId());
                intent.putExtra("trangthai_order", order.getTrangthai());
                intent.putExtra("date_order", order.getDate());
                intent.putExtra("tongtien_order", order.getTongtien());
                intent.putExtra("nameUser_order", order.getId_user().getFullname());
                intent.putExtra("emailUser_order", order.getId_user().getEmail());
                intent.putExtra("phoneUser_order", order.getId_user().getPhone());
                intent.putExtra("addressUser_order", order.getId_user().getAddress());
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
        TextView tvTrangthai, tvDate, tvTongtien;
        LinearLayout lo_bg_status;
        ImageView img_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrangthai = itemView.findViewById(R.id.tv_trangthai_order_item);
            tvDate = itemView.findViewById(R.id.tv_date_order_item);
            tvTongtien = itemView.findViewById(R.id.tv_tongtien_order_item);
            lo_bg_status = itemView.findViewById(R.id.lo_bg_status_order_item);
            img_status = itemView.findViewById(R.id.img_status_order_item);
        }
    }
}
