package com.sky.tickety.ui.connections;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sky.tickety.R;

import java.util.List;

public class SubStationsAdapter extends RecyclerView.Adapter<SubStationsAdapter.CustomViewHolder> {
    List<List<String>> data;
    Context context;
    Boolean train;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView station, time, track;

        public CustomViewHolder(View view) {
            super(view);
            station = view.findViewById(R.id.subStationItem_Station);
            time = view.findViewById(R.id.subStationItem_Time);
            track = view.findViewById(R.id.subStationItem_Track);
        }
    }

    public SubStationsAdapter(List<List<String>> data, Context context, Boolean train) {
        this.data = data;
        this.context = context;
        this.train = train;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_station_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        if(train){
            holder.station.setText(data.get(position).get(0));
            holder.time.setText(data.get(position).get(1) + " | " + data.get(position).get(2));
            holder.track.setText(data.get(position).get(3) + " / " + data.get(position).get(4));
        } else {
            holder.station.setText(data.get(position).get(0));
            holder.time.setText("Odjazd: " + data.get(position).get(1));
        }
    }

    @Override
    public int getItemCount() {
        if(data != null) {
            return data.size();
        }
        return 0;
    }
}