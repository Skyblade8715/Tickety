package com.sky.tickety;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.Button;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.sky.tickety.buyTicket.BuyTicketMPK;

public class WearConnections extends WearableActivity {

    Button buyMPK, mpkTickets, trainTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wear_connections_layout);
        buyMPK = findViewById(R.id.main_buyMPK);
        mpkTickets = findViewById(R.id.main_MPKTickets);
        trainTickets = findViewById(R.id.main_trainTickets);

        mpkTickets.setOnClickListener(v -> {
            new SendMessageToPhone("/my_path/boughtTickets", "MPK", this).start();
        });
        trainTickets.setOnClickListener(v -> {
            new SendMessageToPhone("/my_path/boughtTickets", "Train", this).start();
        });

        buyMPK.setOnClickListener(v -> {
            String datapath = "/my_path";
            new SendMessageToPhone(datapath, "buyMPKTickets", this).start();
        });

        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("topic").equals("my_path")) {
                String onMessageReceived = intent.getStringExtra("message");
                Intent intention = new Intent(WearConnections.this, BuyTicketMPK.class);
                intention.putExtra("message", onMessageReceived);
                startActivity(intention);
            }
//            textView.setText(onMessageReceived);
        }
    }


}