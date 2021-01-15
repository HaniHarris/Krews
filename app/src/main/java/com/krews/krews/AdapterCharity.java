package com.krews.krews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterCharity extends RecyclerView.Adapter<AdapterCharity.ViewHolder> {

    final List<ModelCharity> list;
    final Context context;

    public AdapterCharity(List<ModelCharity> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_charity,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tvName.setText(list.get(position).getCharityName());
        String textAddress=list.get(position).getCity()+","+list.get(position).getState()+"-"+list.get(position).getZipCode();
        holder.tvAddress.setText(textAddress);

        holder.tvUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    Uri uri = Uri.parse(list.get(position).getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
                catch (Exception ignored)
                {

                }
            }
        });

        holder.layItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    Intent intent = new Intent(context,DetailsActivity.class);
                    intent.putExtra("ein",list.get(position).getEin());
                    context.startActivity(intent);
                }
                catch (Exception ignored)
                {

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        final LinearLayout layItem;
        final TextView tvName;
        final TextView tvAddress;
        final TextView tvUrl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layItem=itemView.findViewById(R.id.layItem);
            tvName=itemView.findViewById(R.id.tvName);
            tvAddress=itemView.findViewById(R.id.tvAddress);
            tvUrl=itemView.findViewById(R.id.tvUrl);
        }
    }
}
