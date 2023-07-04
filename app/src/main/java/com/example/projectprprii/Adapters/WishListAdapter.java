package com.example.projectprprii.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.projectprprii.Activity.WishLists.ViewGifts;
import com.example.projectprprii.Entities.WishList;
import com.example.projectprprii.R;

import java.util.List;

public class WishListAdapter extends BaseAdapter {
    private Context context;
    private List<WishList> wishLists;

    public WishListAdapter(Context context, List<WishList> wishLists) {
        this.context = context;
        this.wishLists = wishLists;
    }

    @Override
    public int getCount() {
        return wishLists.size();
    }

    @Override
    public Object getItem(int position) {
        return wishLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.wish_list_item, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
            holder.viewButton = convertView.findViewById(R.id.viewButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final WishList wishList = wishLists.get(position);
        holder.nameTextView.setText(wishList.getName());
        holder.descriptionTextView.setText(wishList.getDescription());

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewGifts.class);
                intent.putExtra("id", wishList.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        Button viewButton;
    }
}
