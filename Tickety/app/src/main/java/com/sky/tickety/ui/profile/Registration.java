package com.sky.tickety.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sky.tickety.MainActivity;
import com.sky.tickety.R;
import com.sky.tickety.model.Discount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Registration extends Activity {

    AutoCompleteTextView LKA, REGIO, MPK;
    EditText name, surname;
    Button save;
    List<Discount> discounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_form);

        Intent intent = new Intent(Registration.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        SharedPreferences prefs =
                this.getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE);
        if(prefs.contains("name")){
            startActivity(intent);
            finish();
        } else {
            LKA = findViewById(R.id.registrationDiscountLKAEdit);
            REGIO = findViewById(R.id.registrationDiscountREGIOEdit);
            MPK = findViewById(R.id.registrationDiscountMPKEdit);
            name = findViewById(R.id.registrationName);
            surname = findViewById(R.id.registrationSurname);
            save = findViewById(R.id.discountSave);
            save.setOnClickListener(view -> {
                if( !("").equals(name.getText().toString())
                        && !("").equals(surname.getText().toString())
                        && !("").equals(LKA.getText().toString())
                        && !("").equals(REGIO.getText().toString())
                        && !("").equals(MPK.getText().toString())) {
                    prefs.edit().putString("name", name.getText().toString()).apply();
                    prefs.edit().putString("surname", surname.getText().toString()).apply();
                    prefs.edit().putString("LKA", LKA.getText().toString().split(":")[0]).apply();
                    prefs.edit().putString("REGIO", REGIO.getText().toString().split(":")[0]).apply();
                    prefs.edit().putString("MPK", MPK.getText().toString().split(":")[0]).apply();
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Proszę wypełnić wszystkie pola!", Toast.LENGTH_LONG).show();
                }
            });
            new getDiscounts().execute("http://192.168.55.109:3000/discounts/all");
        }

    }

    public void setDim(float dim){
        LKA.setAlpha(dim);
        REGIO.setAlpha(dim);
        MPK.setAlpha(dim);
        name.setAlpha(dim);
        surname.setAlpha(dim);
        getWindow().setDimAmount(dim);
    }

    public void setupAdapter(List<String> lkaDiscounts, List<String> regioDiscounts, List<String> mpkDiscounts){
        ArrayAdapter<String> adapterLKA =
            new ArrayAdapter<>(
                this,
                R.layout.dropdown_menu_popup_item,
                lkaDiscounts);
        ArrayAdapter<String> adapterREGIO =
            new ArrayAdapter<>(
                this,
                R.layout.dropdown_menu_popup_item,
                regioDiscounts);
        ArrayAdapter<String> adapterMPK =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        mpkDiscounts);
        LKA.setAdapter(adapterLKA);
        REGIO.setAdapter(adapterREGIO);
        MPK.setAdapter(adapterMPK);
    }

    private class getDiscounts extends AsyncTask<String, String, String> {
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
            List<String> discountsLKA = new ArrayList<>();
            List<String> discountsREGIO = new ArrayList<>();
            List<String> discountsMPK = new ArrayList<>();
            if (result != null) {
                try {
                    jArray = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (jArray != null) {
                try {
                    JSONArray msg = jArray.getJSONArray("Message");
                    for (int i = 0; i < msg.length(); i++) {
                        discounts.add(
                            new Discount(
                                msg.getJSONObject(i).getString("provider"),
                                msg.getJSONObject(i).getString("id"),
                                msg.getJSONObject(i).getString("name"),
                                (double) Math.round(msg.getJSONObject(i).getDouble("percentage"))
                            )
                        );
                    }
                    for(Discount discount: discounts){
                        if(("LKA").equals(discount.provider)) {
                            discountsLKA.add(discount.name + ": " + discount.percentage + "%");
                        }
                        if(("REGIO").equals(discount.provider)) {
                            discountsREGIO.add(discount.name + ": " + discount.percentage + "%");
                        }
                        if(("MPK").equals(discount.provider)) {
                            discountsMPK.add(discount.name + ": " + discount.percentage + "%");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            setupAdapter(discountsLKA, discountsREGIO, discountsMPK);
            progressBar.setVisibility(View.GONE);
            setDim(1f);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}