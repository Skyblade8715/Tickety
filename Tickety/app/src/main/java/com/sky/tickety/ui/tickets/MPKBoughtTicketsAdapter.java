package com.sky.tickety.ui.tickets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.tickety.R;
import com.sky.tickety.backend.DBAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MPKBoughtTicketsAdapter extends RecyclerView.Adapter<MPKBoughtTicketsAdapter.CustomViewHolder> {

    List<List<String>> data;
    Context context;
    SharedPreferences prefs;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView start, end, duration;
        public CardView item;

        public CustomViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.mpk_item);
            start = view.findViewById(R.id.mpk_bought_start);
            end = view.findViewById(R.id.mpk_bought_end);
            duration = view.findViewById(R.id.mpk_bought_duration);
        }
    }

    public MPKBoughtTicketsAdapter(List<List<String>> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mpk_bought_ticket_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        prefs  = context.getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE);
        holder.duration.setText(data.get(position).get(0));

        holder.item.setOnClickListener(v -> {
            Intent intent = new Intent(context, MPKBoughtTicket.class);
            intent.putExtra("barcode", data.get(position).get(3));
            context.startActivity(intent);
        });

        if(data.get(position).get(2) != null){
            holder.start.setText("Aktywowany: " + data.get(position).get(2));
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");
            try {
                holder.end.setText(
                        String.format("Wygasa: %s\n\nKliknij by wyświetlić kod kreskowy!", formatter.format(
                                new Date(
                                        formatter.parse(
                                                data.get(position).get(2)).getTime()
                                                + (60000 *
                                                Integer.parseInt(data.get(position).get(1))))
                        )));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            holder.start.setText("Bilet nie został jeszcze aktywowany!");
            holder.end.setText("Kliknij by wyświetlić szczegóły");
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
