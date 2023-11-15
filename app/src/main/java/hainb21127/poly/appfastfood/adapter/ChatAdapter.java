package hainb21127.poly.appfastfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.activity.Messagers;
import hainb21127.poly.appfastfood.model.Chats;

public class ChatAdapter extends BaseAdapter {
    Context context;
    List<Chats> list;

    public ChatAdapter(Context context, List<Chats> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        if(list != null)
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
            view = mInflater.inflate(R.layout.chat_item, null);

            Chats chats = list.get(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), Messagers.class);
                    intent.putExtra("idChat",chats.getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
            });
        }

        return view;
    }
}
