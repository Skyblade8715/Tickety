package com.sky.tickety.ui.buyTicket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.tickety.R;
import com.sky.tickety.backend.DBAdapter;
import com.sky.tickety.model.Code128;
import com.sky.tickety.ui.connections.SubStationsAdapter;

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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class BuyTicketTrain extends AppCompatActivity {

    String name, surname, provider, trainID,
            startTime, endTime, startStation,
            endStation, discountName, chosenStartTime;
    double price;
    TextView trainIDTV, timeTV,
            startStationTV, endStationTV,
            nameTV, discountTV,
            bought_bike, bought_dog, bought_bag;
    Button buy;
    ImageButton minus_bike, minus_dog, minus_bag, add_bike, add_dog, add_bag;
    int bike = 0,
            dog = 0,
            bag = 0;
    private int HttpResult;
    DecimalFormat priceFormat = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_ticket);

        SharedPreferences prefs = this.getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE);
        Intent i = getIntent();
        setup();
        provider = i.getStringExtra("provider");
        name = prefs.getString("name", "X");
        surname = prefs.getString("surname", "X");
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number number = null;

        if(i.getBooleanExtra("bought", false)){
            String barcode = i.getStringExtra("barcode");
            ConstraintLayout constraintLayout = findViewById(R.id.buy_options);
            NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
            nestedScrollView.setVisibility(View.GONE);
            buy.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.GONE);
            ImageView barcodeIV = findViewById(R.id.buy_barcode);
            barcodeIV.setVisibility(View.VISIBLE);
            String temp = i.getStringExtra("barcode");
            Code128 code = new Code128(this);
            code.setData(temp);
            Bitmap bitmap = code.getBitmap(1080, 700);
            barcodeIV.setImageBitmap(bitmap);
            List<String> data = DBAdapter.getInstance(this).select_Ticket_ByIDAndProvider(barcode, provider);
            System.out.println(data);
            trainID = data.get(4);
            startTime = data.get(5);
            endTime = data.get(6);
            startStation = data.get(7);
            endStation = data.get(8);
            discountName = data.get(9);
            timeTV.setText("Wyjazd: " + startTime + "\nPrzyjazd: " + endTime);

            bought_bag.setVisibility(View.VISIBLE);
            bought_bike.setVisibility(View.VISIBLE);
            bought_dog.setVisibility(View.VISIBLE);

            bought_bike.setText("Przewóz rowerów: " + data.get(10));
            bought_dog.setText("Przewóz psów: " + data.get(11));
            bought_bag.setText("Przewóz bagażu: " + data.get(12));

            try {
                number = format.parse(data.get(13));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            price = number.doubleValue();
        } else {
            trainID = i.getStringExtra("trainID");
            startTime = i.getStringExtra("startTime");
            endTime = i.getStringExtra("endTime");
            startStation = i.getStringExtra("start");
            endStation = i.getStringExtra("end");
            discountName = i.getStringExtra("discountName");
            chosenStartTime = i.getStringExtra("chosenStartTime");
            try {
                number = format.parse(i.getStringExtra("price"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            price = number.doubleValue();
            timeTV.setText(startTime + " - " + endTime);
            List<List<String>> subStations = (List<List<String>>) i.getSerializableExtra("subStations");

            setupActivity();

            RecyclerView recyclerView = findViewById(R.id.buy_substations);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            SubStationsAdapter subStationsAdapter = new SubStationsAdapter(subStations, this, true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(subStationsAdapter);
            subStationsAdapter.notifyDataSetChanged();


            buy.setOnClickListener(view -> {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Zakup biletu")
                        .setMessage(
                                "Czy chcesz zakupić bilet za: " + priceFormat.format(price) + "zł?" +
                                        "\nLiczba rowerów: " + bike +
                                        "\nLiczba psów: " + dog +
                                        "\nLiczba dodatkowych bagaży: " + bag
                        )
                        .setPositiveButton("Zakup", (dialog, which) -> {
                            JSONObject postData = new JSONObject();
                            try {
                                postData.put("name", name);
                                postData.put("surname", surname);
                                postData.put("provider", provider);
                                postData.put("trainID", trainID);
                                postData.put("startTime", startTime);
                                postData.put("endTime", endTime);
                                postData.put("chosenStartTime", chosenStartTime);
                                postData.put("startStation", startStation);
                                postData.put("endStation", endStation);
                                postData.put("discountName", discountName);
                                postData.put("price", priceFormat.format(price));
                                postData.put("bike", bike);
                                postData.put("dog", dog);
                                postData.put("bag", bag);
                                new postBuyTicket().execute("http://192.168.55.109:3000/routes", postData.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        })
                        .setNegativeButton("Zamknij", dialogClickListener)
                        .show();
            });
        }

        trainIDTV.setText(trainID);
        if (provider.equals("REGIO")) {
            trainIDTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.regio_icon, 0, 0, 0);
        } else {
            trainIDTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lka_icon, 0, 0, 0);
        }
        startStationTV.setText(startStation);
        endStationTV.setText(endStation);
        nameTV.setText(name + " " + surname);
        discountTV.setText(discountName);
    }

    private void setup(){
        trainIDTV = findViewById(R.id.buy_trainID);
        timeTV = findViewById(R.id.buy_time);
        startStationTV = findViewById(R.id.buy_startStation);
        endStationTV = findViewById(R.id.buy_endStation);
        nameTV = findViewById(R.id.buy_name);
        discountTV = findViewById(R.id.buy_discount);
        buy = findViewById(R.id.buy_buy);
        bought_bike = findViewById(R.id.bought_bike);
        bought_dog = findViewById(R.id.bought_dog);
        bought_bag = findViewById(R.id.bought_bag);
    }

    private void setupActivity() {
        minus_bike = findViewById(R.id.buy_minus_bike);
        minus_dog = findViewById(R.id.buy_minus_dog);
        minus_bag = findViewById(R.id.buy_minus_bag);
        add_bike = findViewById(R.id.buy_add_bike);
        add_dog = findViewById(R.id.buy_add_dog);
        add_bag = findViewById(R.id.buy_add_bag);

        minus_bike.setOnClickListener(view -> {
            if(bike > 0){
                bike--;
                price -= 7;
                buy.setText("Zakup za: " + priceFormat.format(price) + "zł!");
            } else {
                Toast.makeText(this, "Liczba rowerów to: 0!", Toast.LENGTH_SHORT).show();
            }
        });
        minus_dog.setOnClickListener(view -> {
            if(dog > 0){
                dog--;
                price -= 4;
                buy.setText("Zakup za: " + priceFormat.format(price) + "zł!");
            } else {
                Toast.makeText(this, "Liczba psów to: 0!", Toast.LENGTH_SHORT).show();
            }
        });
        minus_bag.setOnClickListener(v -> {
            if(bag > 0){
                price -= 7;
                bag--;
                buy.setText("Zakup za: " + priceFormat.format(price) + "zł!");
            } else {
                Toast.makeText(this, "Liczba bagażów to: 0!", Toast.LENGTH_SHORT).show();
            }
        });
        add_bike.setOnClickListener(view -> {
            bike++;
            price += 7;
            buy.setText("Zakup za: " + priceFormat.format(price) + "zł!");
        });
        add_dog.setOnClickListener(view -> {
            dog++;
            price += 4;
            buy.setText("Zakup za: " + priceFormat.format(price) + "zł!");
        });
        add_bag.setOnClickListener(view -> {
            bag++;
            price += 7;
            buy.setText("Zakup za: " + priceFormat.format(price) + "zł!");
        });
        buy.setText("Zakup za: " + priceFormat.format(price) + "zł!");
    }


    private class postBuyTicket extends AsyncTask<String, Void, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(BuyTicketTrain.this);
            pd.setMessage("Proszę czekać, trwa zakup biletu...");
            pd.setCancelable(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            URL url;

            try {
                url = new URL(params[0]);
                HttpURLConnection con;
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(6000);
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
                Toast.makeText(BuyTicketTrain.this, "Nie udało się zakupić biletu!" +
                        " Upewnij się, że masz połączenie z internetem!", Toast.LENGTH_LONG).show();
            }
            if (jArray != null) {
                try {
                    barcode = jArray.getString("data");
                    DBAdapter
                            .getInstance(BuyTicketTrain.this)
                            .insert_Ticket(name, surname, provider,
                                    trainID, startTime, endTime,
                                    chosenStartTime.split(" ")[0] + " " + endTime, startStation,
                                    endStation, discountName, price,
                                    bike, dog, bag, barcode);
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(BuyTicketTrain.this, BuyTicketTrain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("bought", true);
                intent.putExtra("barcode", barcode);
                intent.putExtra("provider", provider);
                startActivity(intent);
                Toast.makeText(BuyTicketTrain.this, "Pomyślnie zakupiono bilet!",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(BuyTicketTrain.this, "Nie udało się zakupić biletu, " +
                        "sprawdź swoje połączenie z internetem!", Toast.LENGTH_LONG).show();
            }
            if(pd.isShowing()){
                pd.dismiss();
            }
        }
    }
}