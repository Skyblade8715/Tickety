package com.sky.tickety.boughtTicket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.tickety.R;

import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.CustomViewHolder> {

    List<List<String>> data;
    Context context;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView stations, time;
        public ConstraintLayout item;

        public CustomViewHolder(View view) {
            super(view);
            stations = view.findViewById(R.id.ticket_item_stations);
            time = view.findViewById(R.id.ticket_item_time);
            item = view.findViewById(R.id.ticket_item_layout);
        }
    }

    public TicketsAdapter(List<List<String>> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        if(data.get(position).get(3).equals("LKA")){
            holder.stations.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lka_icon, 0, 0, 0);
        } else {
            holder.stations.setCompoundDrawablesWithIntrinsicBounds(R.drawable.regio_icon, 0, 0, 0);
        }
        holder.stations.setText("   " + data.get(position).get(7) + " -> " + data.get(position).get(8));
        holder.time.setText("Wyjazd: " + data.get(position).get(5) + "\nPrzyjazd: " + data.get(position).get(6));
        holder.item.setOnClickListener(view -> {
            Intent intent = new Intent(context, BuyTicketTrain.class);
            intent.putExtra("name", data.get(position).get(1));
            intent.putExtra("surname", data.get(position).get(2));
            intent.putExtra("provider", data.get(position).get(3));
            intent.putExtra("trainID", data.get(position).get(4));
            intent.putExtra("startTime", data.get(position).get(5));
            intent.putExtra("endTime", data.get(position).get(6));
            intent.putExtra("startStation", data.get(position).get(7));
            intent.putExtra("endStation", data.get(position).get(8));
            intent.putExtra("discountName", data.get(position).get(9));
            intent.putExtra("bought_bike", data.get(position).get(10));
            intent.putExtra("bought_dog", data.get(position).get(11));
            intent.putExtra("bought_bag", data.get(position).get(12));
            intent.putExtra("number", data.get(position).get(13));
            intent.putExtra("barcode", data.get(position).get(14));
            context.startActivity(intent);
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