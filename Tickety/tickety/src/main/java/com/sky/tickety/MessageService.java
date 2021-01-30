package com.sky.tickety;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.sky.tickety.boughtTicket.Tickets;
import com.sky.tickety.buyTicket.MPKBoughtTicket;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        switch (messageEvent.getPath()) {
            case "/my_path": {

                final String message = new String(messageEvent.getData());
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("message", message);
                messageIntent.putExtra("topic", "my_path");
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
                break;
            }
            case "/my_path/boughtBarcode": {

                final String message = new String(messageEvent.getData());
                Intent intention = new Intent(getApplicationContext(), MPKBoughtTicket.class);
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    intention.putExtra("name", jsonObject.getString("name"));
                    intention.putExtra("duration", jsonObject.getString("duration"));
                    if (jsonObject.getString("startTime").equals("")) {
                        intention.putExtra("startTime", (String) null);
                    } else {
                        intention.putExtra("startTime", jsonObject.getString("startTime"));
                    }
                    intention.putExtra("barcode", jsonObject.getString("barcode"));
                    intention.putExtra("discount", jsonObject.getString("discount"));
                    intention.putExtra("single", jsonObject.getBoolean("single"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intention.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intention);
                break;
            }
            case "/my_path/boughtTickets/dataMPK": {
                final String message = new String(messageEvent.getData());
                Intent intention = new Intent(getApplicationContext(), Tickets.class);
//            System.out.println(message);
                intention.putExtra("data", message);
                intention.putExtra("type", "MPK");
                intention.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intention);
                break;
            }
            case "/my_path/boughtTickets/dataTrain": {
                final String message = new String(messageEvent.getData());
                Intent intention = new Intent(getApplicationContext(), Tickets.class);
                intention.putExtra("data", message);
                intention.putExtra("type", "Train");
                intention.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intention);
                break;
            }
            default:
                super.onMessageReceived(messageEvent);
                break;
        }
    }

}