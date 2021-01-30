package com.sky.tickety.ui.connections;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.tickety.R;
import com.sky.tickety.model.Conn;
import com.sky.tickety.model.Route;
import com.sky.tickety.ui.buyTicket.BuyTicketTrain;

import java.io.Serializable;
import java.util.List;

public class MPKConnAdapter extends RecyclerView.Adapter<MPKConnAdapter.CustomViewHolder> {

    List<Conn> data;
    String start, end;
    Context context;
    String date;

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView line, time, startStation, endStation;
        ConstraintLayout additionalInfo;
        RecyclerView recyclerView;
        SubStationsAdapter subStationsAdapter;
        CardView cardView;

        public CustomViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.mpkConn_time);
            line = view.findViewById(R.id.mpkConn_line);
            startStation = view.findViewById(R.id.mpkConn_start);
            endStation = view.findViewById(R.id.mpkConn_end);
            cardView = view.findViewById(R.id.connItemCard);

            additionalInfo = view.findViewById(R.id.mpkConn_addInfo);
            recyclerView = view.findViewById(R.id.connItemSubStations);
        }

        private void bind(Conn conn) {
            boolean expanded = conn.isExpanded;
            additionalInfo.setVisibility(expanded ? View.VISIBLE : View.GONE);
        }
    }

    public MPKConnAdapter(List<Conn> data, Context context, String startStation, String endStation, String date) {
        this.data = data;
        this.context = context;
        this.start = startStation;
        this.end = endStation;
        this.date = date;
    }

    @Override
    public MPKConnAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mpk_conn_item, parent, false);
        return new MPKConnAdapter.CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MPKConnAdapter.CustomViewHolder holder, int position) {
        holder.bind(data.get(position));
        if(data.get(position).type.equals("Tram")){
            holder.time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tram_colored_icon, 0, 0, 0);
        } else {
            holder.time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bus_colored_icon, 0, 0, 0);
        }
        holder.time.setText(data.get(position).startTime + " -> " + data.get(position).endTime);
        holder.startStation.setText(data.get(position).startStation);
        holder.endStation.setText(data.get(position).endStation);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(mLayoutManager);
        holder.subStationsAdapter = new SubStationsAdapter(data.get(position).subStations, context, false);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(holder.subStationsAdapter);
        holder.subStationsAdapter.notifyDataSetChanged();
        holder.cardView.setOnClickListener(v -> {
            data.get(position).isExpanded = !data.get(position).isExpanded;
            notifyItemChanged(position);
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