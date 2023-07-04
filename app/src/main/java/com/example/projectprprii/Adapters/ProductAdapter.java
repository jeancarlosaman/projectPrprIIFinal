package com.example.projectprprii.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.projectprprii.Entities.Product;
import com.example.projectprprii.R;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> products;
    private String apiUrl = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products/{id}";


    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);


            holder = new ViewHolder();
            holder.id = products.get(position).getId();
            holder.priceTextView = convertView.findViewById(R.id.priceValueTextView);
            holder.descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);

            holder.layout = convertView.findViewById(R.id.product_layout);

            holder.layout.setOnClickListener(view -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("product_url", apiUrl.replace("{id}", Integer.toString(holder.id)));
                ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
                ((Activity) context).finish();
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Product product = products.get(position);
        holder.nameTextView.setText(product.getName());
        holder.descriptionTextView.setText(product.getDescription());
        holder.priceTextView.setText(String.valueOf(product.getPrice()));
        holder.id = product.getId();
        return convertView;
    }

    private static class ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        View layout;
        int id;

    }




}
