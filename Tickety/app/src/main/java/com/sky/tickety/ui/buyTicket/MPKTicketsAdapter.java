package com.sky.tickety.ui.buyTicket;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.tickety.R;
import com.sky.tickety.api.API_MPKBuyTicket;
import com.sky.tickety.backend.DBAdapter;
import com.sky.tickety.ui.connections.MPKConnAdapter;
import com.sky.tickety.ui.tickets.MPKBoughtTicket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MPKTicketsAdapter extends RecyclerView.Adapter<MPKTicketsAdapter.CustomViewHolder> {

    List<List<String>> data;
    Context context;
    String ticketName, time;
    SharedPreferences prefs;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView time, time2, price;
        public CardView item;

        public CustomViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.mpk_item_time);
            time2 = view.findViewById(R.id.mpk_item_time2);
            price = view.findViewById(R.id.mpk_item_price);
            item = view.findViewById(R.id.mpk_item);
        }
    }

    public MPKTicketsAdapter(List<List<String>> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public MPKTicketsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mpk_ticket_item, parent, false);
        return new MPKTicketsAdapter.CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MPKTicketsAdapter.CustomViewHolder holder, int position) {
        String timeTemp = data.get(position).get(0).split(" ")[0];
        prefs  = context.getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE);
        holder.time.setText(timeTemp);
        holder.time2.setText(data.get(position).get(0).split(" ")[1]);
        holder.price.setText("Kup za " + data.get(position).get(1) + " zł!");
        holder.item.setOnClickListener(view -> {

            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Zakup biletu")
                    .setMessage(
                            "Czy chcesz zakupić " + data.get(position).get(0)
                                    + " bilet za: " + data.get(position).get(1) + "zł?"
                    )
                    .setPositiveButton("Zakup", (dialog, which) -> {
                        JSONObject postData = new JSONObject();
                        try {
                            postData.put("discountName", prefs.getString("MPK", "XX"));
                            postData.put("time", data.get(position).get(2));
                            postData.put("provider", "MPK");
                            ticketName = data.get(position).get(0);
                            new API_MPKBuyTicket().API_buyTickets(postData, context, data.get(position).get(2), ticketName, false, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    })
                    .setNegativeButton("Zamknij", dialogClickListener)
                    .show();
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
