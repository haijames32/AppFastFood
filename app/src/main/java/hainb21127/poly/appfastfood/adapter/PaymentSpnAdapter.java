package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.model.Payment;

public class PaymentSpnAdapter extends BaseAdapter {
    Context context;
    private List<String> list;

    public PaymentSpnAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list != null)
            list.size();
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
            view = mInflater.inflate(R.layout.cart_item, null);

            ImageView img = view.findViewById(R.id.img_payment_item);
            TextView tv = view.findViewById(R.id.tv_payment_item);

            tv.setText(list.get(i));
            if(tv.getText().toString().equalsIgnoreCase("Thanh toán khi nhận hàng"))
                img.setImageResource(R.drawable.ic_pay);
            else if(tv.getText().toString().equalsIgnoreCase("Thanh toán qua Momo"))
                img.setImageResource(R.drawable.ic_momo);
            else if(tv.getText().toString().equalsIgnoreCase("Thanh toán qua ZaloPay"))
                img.setImageResource(R.drawable.ic_zalopay);
        }
        return view;
    }


}
