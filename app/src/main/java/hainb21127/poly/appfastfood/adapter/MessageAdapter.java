package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.model.Messager;

public class MessageAdapter extends BaseAdapter {
    Context context;
    List<Messager> list;

    public MessageAdapter(Context context, List<Messager> arrayList) {
        this.context = context;
        this.list = arrayList;
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
        Messager message = list.get(i);
        if (view == null) {
            if(message.getStatus().equals("1")){
                view = mInflater.inflate(R.layout.chatreceived_item, null);

                TextView content = view.findViewById(R.id.tv_message_chat_receiver_item);
                TextView time = view.findViewById(R.id.tv_time_chat_receiver_item);

                content.setText(message.getContent());
                time.setText(message.getTime());
            }else{
                view = mInflater.inflate(R.layout.chatsend_item, null);

                TextView content = view.findViewById(R.id.tv_message_chat_sender_item);
                TextView time = view.findViewById(R.id.tv_time_chat_sender_item);

                content.setText(message.getContent());
                time.setText(message.getTime());
            }
        }
        return view;
    }
}
