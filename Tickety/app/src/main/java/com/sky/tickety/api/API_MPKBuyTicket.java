package com.sky.tickety.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.JsonUtils;
import com.sky.tickety.R;
import com.sky.tickety.backend.DBAdapter;
import com.sky.tickety.ui.buyTicket.BuyTicketMPK;
import com.sky.tickety.ui.buyTicket.BuyTicketTrain;
import com.sky.tickety.ui.buyTicket.MPKTicketsAdapter;
import com.sky.tickety.ui.connections.HomeFragment;
import com.sky.tickety.ui.tickets.MPKBoughtTicket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class API_MPKBuyTicket {

    Context context;
    Window window;
    RecyclerView recyclerView;
    Boolean isForWatch;
    Handler handler;
    SharedPreferences prefs;
    String time, ticketName;

    public void API_getTickets(Context context, Window window, RecyclerView recyclerView, Boolean isForWatch, Handler handler){
        this.context = context;
        this.window = window;
        this.recyclerView = recyclerView;
        this.isForWatch = isForWatch;
        this.handler = handler;
        prefs = context.getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE);
        new getTickets().execute("http://192.168.55.109:3000/ticketTypes/" + prefs.getString("MPK", "XX"));
    }

    public void API_buyTickets(JSONObject postData, Context context, String time, String ticketName, boolean isForWatch, Handler handler){
        this.time = time;
        this.context = context;
        this.ticketName = ticketName;
        this.handler = handler;
        this.isForWatch = isForWatch;
        prefs = context.getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE);
        new postBuyTicket().execute("http://192.168.55.109:3000/routes", postData.toString());
    }


    ProgressDialog pd;

    private class postBuyTicket extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            if(!isForWatch) {
                pd = new ProgressDialog(context);
                pd.setMessage("Proszę czekać, trwa zakup biletu...");
                pd.setCancelable(false);
                pd.show();
            }
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            URL url;

            // Create connection
            try {

                url = new URL(params[0]);
                HttpURLConnection con = null;
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                // Send request
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(params[1]);
                wr.flush();

                // Get Response
                BufferedReader reader;
                InputStream stream = con.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);
            JSONObject jArray = null;
            String barcode = null;
            if (result != null) {
                try {
                    jArray = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if(!isForWatch) {
                    Toast.makeText(context, "Nie udało się zakupić biletu!" +
                            " Upewnij się, że masz połączenie z internetem!", Toast.LENGTH_LONG).show();
                }
            }
            if (jArray != null) {
                try {
                    barcode = jArray.getString("data");
                    DBAdapter
                            .getInstance(context)
                            .insert_MPK_Ticket(
                                    ticketName, Integer.parseInt(time),
                                    null, barcode);
                    if(isForWatch){
                        Bundle bundle = new Bundle();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", ticketName);
                        jsonObject.put("duration", time);
                        jsonObject.put("startTime", "");
                        jsonObject.put("barcode", barcode);
                        jsonObject.put("discount", prefs.getString("MPK", "XX"));
                        jsonObject.put("single", true);
                        bundle.putString("messageText", jsonObject.toString());
                        Message msg = handler.obtainMessage();
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        new SendDataToWatch("/my_path/boughtBarcode", jsonObject.toString(), context).start();
                    } else {
                        if(pd.isShowing()){
                            pd.dismiss();
                        }
                        Toast.makeText(context, "Pomyślnie zakupiono bilet!",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(context, MPKBoughtTicket.class);
                        intent.putExtra("barcode", barcode);
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Nie udało się zakupić biletu, " +
                        "sprawdź swoje połączenie z internetem!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class getTickets extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isForWatch){
                window.setDimAmount(0.2f);
            }
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(6000);
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                return buffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject jArray = null;
            if (result != null) {
                try {
                    jArray = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (jArray != null) {
                List<List<String>> data = new ArrayList<>();
                try {
                    DecimalFormat priceFormat = new DecimalFormat("0.00");
                    JSONArray msg = jArray.getJSONArray("Message");
                    for (int i = 0; i < msg.length(); i++) {
                        data.add(new ArrayList<>(
                                Arrays.asList(
                                    msg.getJSONObject(i).getString("Name"),
                                    priceFormat.format(Double.valueOf(msg.getJSONObject(i).getString("Price"))),
                                    msg.getJSONObject(i).getString("Time")
                                )
                            )
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(isForWatch){
                    JSONArray postData = new JSONArray();
                    postData.put(prefs.getString("MPK", "XX"));
                    try {
                        for(List<String> list : data){
                            JSONObject object = new JSONObject();
                            object.put("name", list.get(0));
                            object.put("price", list.get(1));
                            object.put("time", list.get(2));
                            postData.put(object);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("messageText", postData.toString());
                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    new SendDataToWatch("/my_path", postData.toString(), context).start();
                } else {
                    MPKTicketsAdapter ticketsAdapter = new MPKTicketsAdapter(data, context);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(ticketsAdapter);
                    ticketsAdapter.notifyDataSetChanged();
                    window.setDimAmount(1f);
                }
            }
        }


    }
}
