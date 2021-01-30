package com.sky.tickety.ui.connections;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.sky.tickety.R;
import com.sky.tickety.model.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Connections extends AppCompatActivity {

    RecyclerView recyclerView;
    ConnectionsAdapter connectionsAdapter;
    String startStation, endStation;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connections);

        recyclerView = findViewById(R.id.connections_recycle);
        startStation = this.getIntent().getStringExtra("start");
        endStation = this.getIntent().getStringExtra("end");
        date = this.getIntent().getStringExtra("date");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        String title = startStation + "-" + endStation;
        setTitle(title.substring(0, Math.min(title.length(), 28)) + "...");
        new getRoute().execute("http://192.168.55.109:3000/routes/route/" + startStation + "-" + endStation);


    }

    public void setDim(float dim) {
//        search.setAlpha(dim);
//        clear.setAlpha(dim);
//        options.setAlpha(dim);
//        possibleInvalid.setAlpha(dim);
        getWindow().setDimAmount(dim);
    }


    private class getRoute extends AsyncTask<String, String, String> {
        View progressBar;

        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            setDim(0.2f);
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String errorMessage;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(6000);
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                return buffer.toString();

            } catch (java.net.SocketTimeoutException e) {
                e.printStackTrace();
                errorMessage = "Nie udało się połączyć z serwerem API:\nCzas połączenia zbyt długi, sprawdź swoje połączenie z internetem!";
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
            List<Route> data = new ArrayList<>();
            if (result != null) {
                try {
                    jArray = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (jArray != null) {
                try {
                    DecimalFormat priceFormat = new DecimalFormat("0.00");
                    JSONArray msg = jArray.getJSONArray("Message");
                    for (int i = 0; i < msg.length(); i++) {

                        List<List<String>> prices = new ArrayList<>();
                        JSONArray pricesJSON = msg.getJSONObject(i).getJSONArray("prices");

                        for (int j = 0; j < pricesJSON.length(); j++) {
                            prices.add(new ArrayList<>(
                                Arrays.asList(
                                    (String) pricesJSON.getJSONObject(j).get("id"),
                                    (String) pricesJSON.getJSONObject(j).get("name"),
                                    priceFormat.format(pricesJSON.getJSONObject(j).get("price"))
                                )
                            ));
                        }
                        DecimalFormat timePattern = new DecimalFormat("00");
                        String time;
                        List<List<String>> subStations = new ArrayList<>();
                        JSONArray subStationsJSON =
                                msg.getJSONObject(i).getJSONArray("subStations");

                        for (int j = 0; j < subStationsJSON.length(); j++) {
                            time = subStationsJSON.getJSONObject(j).getString("arrivalTime");
                            String arrTime =
                                    timePattern.format(ZonedDateTime.parse(time).getHour())
                                    + ":" + timePattern.format(ZonedDateTime.parse(time).getMinute());
                            time = subStationsJSON.getJSONObject(j).getString("departureTime");
                            String depTime =
                                    timePattern.format(ZonedDateTime.parse(time).getHour())
                                    + ":" + timePattern.format(ZonedDateTime.parse(time).getMinute());
                            subStations.add(new ArrayList<>(
                                Arrays.asList(
                                    (String) subStationsJSON.getJSONObject(j).get("name"),
                                    arrTime,
                                    depTime,
                                    (String) subStationsJSON.getJSONObject(j).get("platform"),
                                    subStationsJSON.getJSONObject(j).get("track").toString()
                                )
                            ));
                        }

                        String startTime = "";
                        time = msg.getJSONObject(i).getString("endTime");
                        String endTime =
                                timePattern.format(ZonedDateTime.parse(time).getHour())
                                + ":"
                                + timePattern.format(ZonedDateTime.parse(time).getMinute());
                        int st = 0, end = 1;
                        for(int x = 0; x < subStations.size(); x++) {
                            if(subStations.get(x).get(0).equals(startStation)){
                                st = x;
                                startTime = subStations.get(x).get(2);
                            }
                            if(subStations.get(x).get(0).equals(endStation)) {
                                end = x;
                            }
                        }
                        time = msg.getJSONObject(i).getString("duration");
                        String duration =
                                ZonedDateTime.parse(time).getHour() + "h "
                                + ZonedDateTime.parse(time).getMinute() + "min";

                        if(st < end) {
                            data.add(
                                new Route(
                                    msg.getJSONObject(i).getString("trainID"),
                                    startTime,
                                    endTime,
                                    duration,
                                    msg.getJSONObject(i).getString("provider"),
                                    msg.getJSONObject(i).getInt("distance"),
                                    new ArrayList<>(prices),
                                    new ArrayList<>(subStations),
                                    msg.getJSONObject(i).getBoolean("wifi"),
                                    msg.getJSONObject(i).getBoolean("disabilitySupp"),
                                    msg.getJSONObject(i).getBoolean("ac"),
                                    msg.getJSONObject(i).getBoolean("machine"),
                                    startStation,
                                    endStation
                                )
                            );
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                connectionsAdapter = new ConnectionsAdapter(data, getApplicationContext(), startStation, endStation, date);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(connectionsAdapter);
                connectionsAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
                setDim(1f);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    }
}