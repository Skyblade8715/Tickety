package com.sky.tickety.ui.connections;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.tickety.R;

import java.util.List;
import java.util.Set;

public class LastConnAdapter extends RecyclerView.Adapter<LastConnAdapter.CustomViewHolder> {
    List<String> data;
    AutoCompleteTextView startStation, endStation;

    public LastConnAdapter(List<String> data, AutoCompleteTextView startStation, AutoCompleteTextView endStation) {
        this.data = data;
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView start, end;
        public CardView cardView;

        public CustomViewHolder(View view) {
            super(view);
            start = view.findViewById(R.id.last_chosen_start);
            end = view.findViewById(R.id.last_chosen_end);
            cardView = view.findViewById(R.id.last_chosen_conn);
        }
    }

    @Override
    public LastConnAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.last_connection_item, parent, false);
        return new LastConnAdapter.CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LastConnAdapter.CustomViewHolder holder, int position) {
        holder.start.setText(data.get(position).split("&")[0]);
        holder.end.setText(data.get(position).split("&")[1]);
        holder.cardView.setOnClickListener(v -> {
            this.startStation.setText(data.get(position).split("&")[0]);
            this.endStation.setText(data.get(position).split("&")[1]);
        });
    }

    @Override
    public int getItemCount() {
        if(data != null) {
            return data.size();
        }
        return 0;
    }
}