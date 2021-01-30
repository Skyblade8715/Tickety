package com.sky.tickety;

import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.sky.tickety.api.API_MPKBuyTicket;
import com.sky.tickety.backend.DBAdapter;
import com.sky.tickety.ui.tickets.MPKBoughtTicket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals("/my_path")) {
            final String message = new String(messageEvent.getData());

            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);
            messageIntent.putExtra("topic", "getTickets");
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        } else if (messageEvent.getPath().equals("/my_path/buy")) {
            String received = new String(messageEvent.getData());
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", received);
            messageIntent.putExtra("topic", "buyMPKTicket");
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);

        } else if (messageEvent.getPath().equals("/my_path/activate")) {
            String received = new String(messageEvent.getData());
            DBAdapter
                    .getInstance(this)
                    .update_MPKTicket(received.split("&")[0], received.split("&")[1]);
        } else if (messageEvent.getPath().equals("/my_path/boughtTickets")){
            String received = new String(messageEvent.getData());
            List<List<String>> temp;
            if(received.equals("MPK")){
                temp = DBAdapter.getInstance(this).select_MPK_Tickets();
            } else {
                temp = DBAdapter.getInstance(this).select_Tickets();
            }
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            if(temp != null) {
                messageIntent.putExtra("message", serialize(temp));
            } else {
                messageIntent.putExtra("message", "");
            }
            messageIntent.putExtra("topic", received);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }


    public static String serialize(List<List<String>> dataList) {

        StringBuilder xmlBuilder = new StringBuilder("<root>");
        dataList.forEach((data) -> {
            xmlBuilder.append("<data>");
            data.forEach((item) -> {
                xmlBuilder.append(item).append("#");
            });
            xmlBuilder.deleteCharAt(xmlBuilder.length() - 1);
            xmlBuilder.append("</data>");
        });
        return xmlBuilder.append("</root>").toString();
    }
}