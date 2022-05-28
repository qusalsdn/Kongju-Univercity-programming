package com.example.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter extends BaseAdapter {
    ArrayList<ListItem> items = null;
    Context context;

    public ListItemAdapter(Context context, ArrayList<ListItem> items){
        this.items = (ArrayList<ListItem>) items;
        this.context =context;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.listview_item, parent, false);

        TextView number = view.findViewById(R.id.list_item_number);
        TextView title = view.findViewById(R.id.list_item_title);

        ListItem item = items.get(position);
        number.setText(String.valueOf(position+1));
        title.setText(item.getTitle());

        return view;
    }
}