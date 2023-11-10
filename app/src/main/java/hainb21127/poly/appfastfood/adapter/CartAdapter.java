package hainb21127.poly.appfastfood.adapter;

import static java.security.AccessController.getContext;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.activity.ProductDetail;
import hainb21127.poly.appfastfood.config.Utilities;
import hainb21127.poly.appfastfood.inter.MyInterface;
import hainb21127.poly.appfastfood.model.Cart;
import hainb21127.poly.appfastfood.model.Cart2;
import hainb21127.poly.appfastfood.model.Category;
import hainb21127.poly.appfastfood.model.Product;

public class CartAdapter extends BaseAdapter {
    Context context;
    private List<Cart2> list;
    private MyInterface myInterface;
    int number = 1;
    int pricesp;

    public CartAdapter(Context context, List<Cart2> list,MyInterface myInterface) {
        this.context = context;
        this.list = list;
        this.myInterface = myInterface;
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
            view = mInflater.inflate(R.layout.cart_item, null);
            Cart2 cart = list.get(i);

            TextView tv_item_name = view.findViewById(R.id.tv_name_cart_item);
            TextView tv_item_price = view.findViewById(R.id.tv_price_cart_item);
            TextView tv_soluong = view.findViewById(R.id.tv_soluong_cart_item);
            TextView tv_tongtien = view.findViewById(R.id.tv_tongtien_cart_item);
            CardView tv_plus = view.findViewById(R.id.btn_plus_cart_item);
            CardView tv_minus = view.findViewById(R.id.btn_minus_cart_item);
            ImageView id_image = view.findViewById(R.id.img_cart_item);

            tv_item_name.setText(cart.getId_sanpham().getTensp());
            tv_item_price.setText(Utilities.addDots(cart.getId_sanpham().getGiasp()) + "đ");
            tv_soluong.setText(cart.getSoluong() + "");
            tv_tongtien.setText(Utilities.addDots(cart.getTongtien()) + "đ");
            Picasso.get().load(cart.getId_sanpham().getImage()).into(id_image);

            number = cart.getSoluong();
            pricesp = cart.getId_sanpham().getGiasp();
            cart.setNumber(number);
            cart.setPricesp(pricesp);
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
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_confirm);
                    TextView tvConfirm = dialog.findViewById(R.id.tv_confirm);
                    Button btnCancel = dialog.findViewById(R.id.btn_cancel_dialog_confirm);
                    Button btnAgree = dialog.findViewById(R.id.btn_agree_dialog_confirm);
                    tvConfirm.setText("Bạn muốn xóa sản phẩm khỏi giỏ hàng?");
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    btnAgree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                myInterface.onDelete(cart);
//                            FirebaseDatabase database = FirebaseDatabase.getInstance();
//                            DatabaseReference reference = database.getReference("cart").child(cart.getId());
//                            reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(view.getContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(view.getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("cart").child(cart.getId());

            tv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cart.getNumber() > 1) {
                        cart.setNumber(cart.getNumber() - 1);
                        cart.setSum(cart.getNumber() * cart.getPricesp());
                        reference.child("soluong").setValue(cart.getNumber());
                        reference.child("tongtien").setValue(cart.getSum());
                        tv_soluong.setText(String.valueOf(cart.getNumber()));
                        tv_tongtien.setText(Utilities.addDots(cart.getSum()) + "đ");
                    }
                }
            });
            tv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cart.setNumber(cart.getNumber() + 1);
                    cart.setSum(cart.getNumber() * cart.getPricesp());
                    reference.child("soluong").setValue(cart.getNumber());
                    reference.child("tongtien").setValue(cart.getSum());
                    tv_soluong.setText(String.valueOf(cart.getNumber()));
                    tv_tongtien.setText(Utilities.addDots(cart.getSum()) + "đ");
                }
            });
        }
        return view;
    }
}
