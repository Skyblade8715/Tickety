package com.sky.tickety.boughtTicket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sky.tickety.R;
import com.sky.tickety.model.Code128;

import java.text.DecimalFormat;

public class BuyTicketTrain extends AppCompatActivity {

    String name, surname, provider, trainID,
            startTime, endTime, startStation,
            endStation, discountName;
    TextView trainIDTV, timeTV,
            startStationTV, endStationTV,
            nameTV, discountTV,
            bought_bike, bought_dog, bought_bag;
    Button showBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_ticket);

        Intent i = getIntent();
        setup();
        provider = i.getStringExtra("provider");
        name = i.getStringExtra("name");
        surname = i.getStringExtra("surname");

        ImageView barcodeIV = findViewById(R.id.buy_barcode);
        String temp = i.getStringExtra("barcode");
        Code128 code = new Code128(this);
        code.setData(temp);
        Bitmap bitmap = code.getBitmap(320, 320);
        barcodeIV.setImageBitmap(bitmap);

        showBarcode.setOnClickListener(v -> barcodeIV.setVisibility(View.VISIBLE));
        barcodeIV.setOnClickListener(v -> barcodeIV.setVisibility(View.GONE));


        trainID = i.getStringExtra("trainID");
        startTime = i.getStringExtra("startTime");
        endTime = i.getStringExtra("endTime");
        startStation = i.getStringExtra("startStation");
        endStation = i.getStringExtra("endStation");
        discountName = i.getStringExtra("discountName");
        timeTV.setText(String.format("Wyjazd: %s\nPrzyjazd: %s", startTime, endTime));


        bought_bike.setText(String.format("Przewóz rowerów: %s", i.getStringExtra("bought_bike")));
        bought_dog.setText(String.format("Przewóz psów: %s", i.getStringExtra("bought_dog")));
        bought_bag.setText(String.format("Przewóz bagażu: %s", i.getStringExtra("bought_bag")));
        trainIDTV.setText(trainID);
        startStationTV.setText(startStation);
        endStationTV.setText(endStation);
        nameTV.setText(String.format("%s %s", name, surname));
        discountTV.setText(discountName);
        if (provider.equals("REGIO")) {
            trainIDTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.regio_icon, 0, 0, 0);
        } else {
            trainIDTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lka_icon, 0, 0, 0);
        }
    }

    private void setup(){
        showBarcode = findViewById(R.id.train_showBarcode);
        trainIDTV = findViewById(R.id.buy_trainID);
        timeTV = findViewById(R.id.buy_time);
        startStationTV = findViewById(R.id.buy_startStation);
        endStationTV = findViewById(R.id.buy_endStation);
        nameTV = findViewById(R.id.buy_name);
        discountTV = findViewById(R.id.buy_discount);
        bought_bike = findViewById(R.id.bought_bike);
        bought_dog = findViewById(R.id.bought_dog);
        bought_bag = findViewById(R.id.bought_bag);
    }
}