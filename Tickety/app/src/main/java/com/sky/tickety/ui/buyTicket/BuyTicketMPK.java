package com.sky.tickety.ui.buyTicket;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.tickety.R;
import com.sky.tickety.api.API_MPKBuyTicket;

public class BuyTicketMPK extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_ticket_mpk);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Zakup biletów MPK");

        recyclerView = findViewById(R.id.mpk_tickets_recycle);
        new API_MPKBuyTicket()
                .API_getTickets(this, this.getWindow(), recyclerView, false, null);

    }

}