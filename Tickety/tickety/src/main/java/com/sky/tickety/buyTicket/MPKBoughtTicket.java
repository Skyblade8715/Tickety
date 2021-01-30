package com.sky.tickety.buyTicket;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sky.tickety.R;
import com.sky.tickety.SendMessageToPhone;
import com.sky.tickety.model.Code128;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MPKBoughtTicket extends AppCompatActivity {

    TextView name, start, end, discount;
    ImageView barcodeIV;
    Button activate;
    String barcodeString, startTime, endTime;
    DateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mpk_bought_ticket);

        name = findViewById(R.id.mpk_boughtTicket_name);
        start = findViewById(R.id.mpk_boughtTicket_start);
        end = findViewById(R.id.mpk_boughtTicket_end);
        discount = findViewById(R.id.mpk_boughtTicket_discount);
        barcodeIV = findViewById(R.id.mpk_boughtTicket_barcode);
        activate = findViewById(R.id.mpk_boughtTicket_activate);

        Intent i = getIntent();
        startTime = i.getStringExtra("startTime");
        name.setText(i.getStringExtra("name"));
        barcodeString = getIntent().getStringExtra("barcode");

        Code128 code = new Code128(this);
        code.setData(barcodeString);
        Bitmap bitmap = code.getBitmap(320, 320);
        barcodeIV.setImageBitmap(bitmap);

        discount.setText(String.format("Wybrany rodzaj biletu: %s", i.getStringExtra("discount")));
        formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");

        barcodeIV.setOnClickListener(view -> {
            barcodeIV.setVisibility(View.GONE);
        });

        activate.setOnClickListener(v -> {
            if (startTime == null) {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Aktywacja biletu")
                    .setMessage(
                            "Czy chcesz aktywować " + i.getStringExtra("name") + " bilet?"
                    )
                    .setPositiveButton("Aktywuj", (dialog, which) -> {
                        final Calendar currentDate = Calendar.getInstance();
                        startTime = formatter.format(currentDate.getTime());
                        endTime = new Date(currentDate.getTimeInMillis() + (60000 * 20)).toString();

                        JSONObject postData = new JSONObject();
                        try {
                            postData.put("startTime", startTime);
                            postData.put("id", barcodeString);
                            new updateMPKTicket().execute("http://192.168.55.109:3000/routes/tickets/" + barcodeString, postData.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    })
                    .setNegativeButton("Zamknij", dialogClickListener)
                    .show();
            } else {
                activate.setText("Pokaż kod kreskowy");
                barcodeIV.setVisibility(View.VISIBLE);
            }
        });

        if(startTime != null){
            activate.setText("Pokaż kod kreskowy");
            start.setText(String.format("Aktywowany: %s", startTime));
            try {
                end.setText(String.format("Wygasa: %s",
                    formatter.format(
                        new Date(
                            formatter.parse(startTime).getTime()
                                + (60000 * Integer.parseInt(getIntent().getStringExtra("duration")
                            ))))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            start.setText("Bilet nie został jeszcze aktywowany.");
            end.setText("Nie zapomnij aktywować biletu przed skorzystaniem!");
            barcodeIV.setVisibility(View.GONE);
            activate.setVisibility(View.VISIBLE);
        }
    }

    private class updateMPKTicket extends AsyncTask<String, Void, String> {

        ProgressDialog pd;
        int HttpResult;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(MPKBoughtTicket.this);
            pd.setMessage("Proszę czekać, trwa zakup biletu...");
            pd.setCancelable(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            URL url;

            // Create connection
            try {

                url = new URL(params[0]);
                HttpURLConnection con;
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("PUT");

                // Send request
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(params[1]);
                wr.flush();

                HttpResult = con.getResponseCode();
                return String.valueOf(HttpResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Couldn't connect to server";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (HttpResult != HttpURLConnection.HTTP_OK) {
                Toast.makeText(MPKBoughtTicket.this, "Nie udało się aktywować biletu!" +
                        " Upewnij się, że masz połączenie z internetem!", Toast.LENGTH_LONG).show();
            } else {
                new SendMessageToPhone("/my_path/activate", barcodeString + "&" + startTime, getApplicationContext()).start();
                Toast.makeText(MPKBoughtTicket.this, "Pomyślnie aktywowano bilet!",
                        Toast.LENGTH_SHORT).show();
                if(pd.isShowing()){
                    pd.dismiss();
                }
                start.setText(String.format("Aktywowany: %s", startTime));
                try {
                    end.setText(String.format("Wygasa: %s",
                        formatter.format(
                            new Date(
                                formatter.parse(startTime).getTime()
                                    + (60000 * Integer.parseInt(getIntent().getStringExtra("duration")
                                ))))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                activate.setText("Pokaż kod kreskowy");
            }

            if(pd.isShowing()){
                pd.dismiss();
            }
        }
    }
}