package com.sky.tickety.ui.connections;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.tickety.R;
import com.sky.tickety.model.Route;
import com.sky.tickety.ui.buyTicket.BuyTicketTrain;

import java.io.Serializable;
import java.util.List;

public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.CustomViewHolder> {

    List<Route> data;
    String start, end;
    Context context;
    String date;

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView time, sth, trainID, wifi, machine, disability, ac;
        ConstraintLayout additionalInfo;
        Button buy;
        RecyclerView recyclerView;
        SubStationsAdapter subStationsAdapter;

        public CustomViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.mpkConn_line);
            sth = view.findViewById(R.id.connItemSth);
            buy = view.findViewById(R.id.connItemBuy);

            additionalInfo = view.findViewById(R.id.mpkConn_addInfo);
            recyclerView = view.findViewById(R.id.connItemSubStations);
            trainID = view.findViewById(R.id.connItemTrainID);
            wifi = view.findViewById(R.id.connItemWifi);
            machine = view.findViewById(R.id.connItemMachine);
            disability = view.findViewById(R.id.connItemDisability);
            ac = view.findViewById(R.id.connItemAC);
        }

        private void bind(Route route) {
            boolean expanded = route.isExpanded;
            additionalInfo.setVisibility(expanded ? View.VISIBLE : View.GONE);

            trainID.setText(route.trainID);
            if(route.wifi){
                wifi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wifi_icon, 0, R.drawable.checkmark_icon, 0);
            } else {
                wifi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wifi_icon, 0, R.drawable.block_icon, 0);
            }
            if(route.ticketMachine){
                machine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.machine_icon, 0, R.drawable.checkmark_icon, 0);
            } else {
                machine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.machine_icon, 0, R.drawable.block_icon, 0);
            }
            if(route.disabilitySupp){
                disability.setCompoundDrawablesWithIntrinsicBounds(R.drawable.disability_icon, 0, R.drawable.checkmark_icon, 0);
            } else {
                disability.setCompoundDrawablesWithIntrinsicBounds(R.drawable.disability_icon, 0, R.drawable.block_icon, 0);
            }
            if(route.ac){
                ac.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ac_icon, 0, R.drawable.checkmark_icon, 0);
            } else {
                ac.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ac_icon, 0, R.drawable.block_icon, 0);
            }
        }
    }

    public ConnectionsAdapter(List<Route> data, Context context, String startStation, String endStation, String date) {
        this.data = data;
        this.context = context;
        this.start = startStation;
        this.end = endStation;
        this.date = date;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.connection_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.bind(data.get(position));
        if(data.get(position).provider.equals("REGIO")){
            holder.sth.setCompoundDrawablesWithIntrinsicBounds(R.drawable.regio_icon, 0, 0, 0);
        } else {
            holder.sth.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lka_icon, 0, 0, 0);
        }
        holder.time.setText(data.get(position).startTime + " -> " + data.get(position).endTime);
        holder.sth.setText("Bezpośredni: " + data.get(position).duration);

        SharedPreferences prefs =
                context.getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE);
        String discName = prefs.getString(data.get(position).provider, "Brak zniżki");
        String percentage = "";
        for(List<String> price : data.get(position).prices){
            if(price.get(1).equals(discName)){
                holder.buy.setText("Kup za\n" + price.get(2) + "zł");
                percentage = price.get(2);
                break;
            }
        }
        String finalPercentage = percentage;
        holder.buy.setOnClickListener(view -> {
            Intent intent = new Intent(context, BuyTicketTrain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("provider", data.get(position).provider);
            intent.putExtra("discountName", prefs.getString(data.get(position).provider, "Brak zniżki"));
            intent.putExtra("price", finalPercentage);
            intent.putExtra("trainID", data.get(position).trainID);
            intent.putExtra("startTime", data.get(position).startTime);
            intent.putExtra("endTime", data.get(position).endTime);
            intent.putExtra("chosenStartTime", date);
            intent.putExtra("start", start);
            intent.putExtra("end", end);
            intent.putExtra("subStations", (Serializable) data.get(position).subStations);
            context.startActivity(intent);
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(mLayoutManager);
        holder.subStationsAdapter = new SubStationsAdapter(data.get(position).subStations, context, true);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(holder.subStationsAdapter);
        holder.subStationsAdapter.notifyDataSetChanged();
        holder.itemView.setOnClickListener(v -> {
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
