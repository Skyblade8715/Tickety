package com.sky.tickety.buyTicket;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.tickety.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuyTicketMPK extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_ticket_mpk);
        recyclerView = findViewById(R.id.mpk_tickets_recycle);

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(getIntent().getStringExtra("message"));
            SharedPreferences prefs =
                    this.getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE);
            prefs.edit().putString("MPK", jsonArray.getString(0)).apply();
            List<List<String>> data = new ArrayList<>();
            for(int i = 1; i < jsonArray.length(); i++){
                JSONObject msg = (JSONObject) jsonArray.get(i);
                data.add(new ArrayList<>(
                    Arrays.asList(
                        msg.getString("name"),
                        msg.getString("price"),
                        msg.getString("time")
                    )));
            }
            MPKTicketsAdapter ticketsAdapter = new MPKTicketsAdapter(data, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(ticketsAdapter);
            ticketsAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}