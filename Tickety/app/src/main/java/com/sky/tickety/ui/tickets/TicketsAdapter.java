package com.sky.tickety.ui.tickets;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.tickety.R;
import com.sky.tickety.ui.buyTicket.BuyTicketTrain;

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
    public TicketsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_item, parent, false);
        return new TicketsAdapter.CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TicketsAdapter.CustomViewHolder holder, int position) {
        if(data.get(position).get(3).equals("LKA")){
            holder.stations.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lka_icon, 0, 0, 0);
        } else {
            holder.stations.setCompoundDrawablesWithIntrinsicBounds(R.drawable.regio_icon, 0, 0, 0);
        }
        holder.stations.setText("   " + data.get(position).get(7) + " -> " + data.get(position).get(8));
        holder.time.setText("Wyjazd: " + data.get(position).get(5) + "\nPrzyjazd: " + data.get(position).get(6));
        holder.item.setOnClickListener(view -> {
            Intent intent = new Intent(context, BuyTicketTrain.class);
            intent.putExtra("bought", true);
            intent.putExtra("barcode", data.get(position).get(14));
            intent.putExtra("provider", data.get(position).get(3));
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