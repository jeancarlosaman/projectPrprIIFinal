package com.example.projectprprii.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projectprprii.Activity.ViewGiftActivity;
import com.example.projectprprii.Activity.VolleySingletone;
import com.example.projectprprii.Entities.Gift;
import com.example.projectprprii.Entities.User;
import com.example.projectprprii.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftAdapter extends BaseAdapter {
    private Context context;
    private List<Gift> gifts;
    private boolean isCurrentUser;
    private String apiUrl = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/{id}";


    public GiftAdapter(Context context, List<Gift> gifts,Boolean isCurrentUser) {
        this.context = context;
        this.gifts = gifts;
        this.isCurrentUser = isCurrentUser;
    }

    @Override
    public int getCount() {
        return gifts.size();
    }

    @Override
    public Object getItem(int position) {
        return gifts.get(position);
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
            if (isCurrentUser){
                convertView = LayoutInflater.from(context).inflate(R.layout.my_gift_list_item, parent, false);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.gift_list_item, parent, false);
            }

            holder = new ViewHolder();
            holder.id = gifts.get(position).getId();
            holder.context = context;
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.descriptionTextView = convertView.findViewById(R.id.priorityValueTextView);
            if (isCurrentUser){
                holder.editButton = convertView.findViewById(R.id.editGift);
                holder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ViewGiftActivity.class);
                        intent.putExtra("id", gifts.get(position).getId());
                        intent.putExtra("editGift", true);
                        v.getContext().startActivity(intent);
                    }
                });

                holder.deleteButton = convertView.findViewById(R.id.deleteGift);
                holder.deleteButton.setOnClickListener(view -> {
                    String tmp = apiUrl.replace("{id}",Integer.toString(gifts.get(position).getId()));
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,tmp, null,
                            response -> {
                                Toast.makeText(context, "Gift deleted", Toast.LENGTH_SHORT).show();
                                ((Activity)context).finish();
                                context.startActivity(((Activity)context).getIntent());
                            },
                            error -> {
                                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                            }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<>();
                            headers.put("Authorization", "Bearer " + User.getAuthenticatedUser().getToken());
                            return headers;
                        }
                    };
                    VolleySingletone.getInstance(context).addToRequestQueue(jsonObjectRequest);
                });
            } else {
                holder.bookButton = convertView.findViewById(R.id.bookGift);

                if (gifts.get(position).getBooked() == 1){
                    holder.bookButton.setText("Unbook");
                    holder.bookButton.setBackgroundColor(context.getResources().getColor(R.color.red));

                    holder.bookButton.setOnClickListener(view -> {
                        String tmp = apiUrl = apiUrl.replace("{id}",Integer.toString(gifts.get(position).getId()));
                        tmp+="/book";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, tmp, null,
                                response -> {
                                    Toast.makeText(context, "Gift unbooked", Toast.LENGTH_SHORT).show();
                                    ((Activity)context).finish();
                                    context.startActivity(((Activity)context).getIntent());
                                },
                                error -> {
                                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<>();
                                headers.put("Authorization", "Bearer " + User.getAuthenticatedUser().getToken());
                                return headers;
                            }
                        };
                        VolleySingletone.getInstance(context).addToRequestQueue(jsonObjectRequest);
                    });
                } else {
                    holder.bookButton.setText("Book");

                    holder.bookButton.setOnClickListener(view -> {
                        String tmp = apiUrl = apiUrl.replace("{id}",Integer.toString(gifts.get(position).getId()));
                        tmp+="/book";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, tmp, null,
                                response -> {
                                    Toast.makeText(context, "Gift booked", Toast.LENGTH_SHORT).show();
                                    ((Activity)context).finish();
                                    context.startActivity(((Activity)context).getIntent());
                                },
                                error -> {
                                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<>();
                                headers.put("Authorization", "Bearer " + User.getAuthenticatedUser().getToken());
                                return headers;
                            }
                        };
                        VolleySingletone.getInstance(context).addToRequestQueue(jsonObjectRequest);
                    });
                }
            }
            holder.layout = convertView.findViewById(R.id.giftItemContainer);
            holder.layout.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), ViewGiftActivity.class);
                intent.putExtra("id", gifts.get(position).getId());
                view.getContext().startActivity(intent);
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Gift gift = gifts.get(position);
        holder.nameTextView.setText(gift.getProduct_url());
        holder.descriptionTextView.setText(Integer.toString(gift.getPriority()));
        holder.currentUser = isCurrentUser;
        holder.id = gift.getId();
        getProduct_name(gift,holder.nameTextView);
        return convertView;
    }

    private static class ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView editButton;
        TextView deleteButton;
        TextView bookButton;
        View layout;
        boolean currentUser;
        int id;
        Context context;

    }



    public void getProduct_name(Gift gift,TextView pdName) {

        if (gift.getProduct_url().startsWith("https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products/")) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,gift.getProduct_url(), null,
                    response -> {
                        System.out.println(response);
                        String name = response.optString("name");
                        pdName.setText(name);
                    },
                    error -> {
                    } ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + User.getAuthenticatedUser().getToken());
                    return headers;
                }
            };
            VolleySingletone.getInstance(context).addToRequestQueue(request);

        }


    }
}
