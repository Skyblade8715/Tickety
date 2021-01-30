package com.sky.tickety.ui.tickets;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.tickety.R;
import com.sky.tickety.backend.DBAdapter;
import com.sky.tickety.model.Code128;
import com.sky.tickety.ui.buyTicket.BuyTicketTrain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MPKBoughtTicket extends AppCompatActivity {

    TextView name, start, end, discount;
    ImageView barcodeIV;
    Button activate;
    String barcodeString, startTime, endTime;

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

        SharedPreferences prefs = this.getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE);
        Intent i = getIntent();
        barcodeString = i.getStringExtra("barcode");
        String discountString = prefs.getString("MPK", "X");
        List<String> ticketData = DBAdapter.getInstance(this).select_MPK_Ticket_ByBarcode(barcodeString);

//        + KEY_MPK_TICKET_NAME + " TEXT NOT NULL,"
//        + KEY_DURATION + " TEXT NOT NULL,"
//        + KEY_MPK_START_TIME + " TEXT,"
//        + KEY_MPK_TICKET_BARCODE + " TEXT NOT NULL,"
//        + KEY_MPK_TICKET_DISCOUNT_NAME + " TEXT NOT NULL);

        name.setText(ticketData.get(0));
        discount.setText("Wybrany rodzaj biletu: " + discountString);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");

        if(ticketData.get(2) != null){
            start.setText("Aktywowany: " + ticketData.get(2));

            try {
                end.setText(String.format("Wygasa: %s", formatter.format(new Date(formatter.parse(ticketData.get(2)).getTime() + (60000 * Integer.parseInt(ticketData.get(1)))))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            start.setText("Aktywowany: " + ticketData.get(2));
            barcodeIV.setVisibility(View.VISIBLE);
            activate.setVisibility(View.GONE);
            Code128 code = new Code128(this);
            code.setData(barcodeString);
            Bitmap bitmap = code.getBitmap(1080, 700);
            barcodeIV.setImageBitmap(bitmap);
        } else {
            start.setText("Bilet nie został jeszcze aktywowany.");
            end.setText("Nie zapomnij aktywować biletu przed skorzystaniem!");
            barcodeIV.setVisibility(View.GONE);
            activate.setVisibility(View.VISIBLE);
            activate.setOnClickListener(v -> {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Aktywacja biletu")
                        .setMessage(
                                "Czy chcesz aktywować " + ticketData.get(0) + " bilet?"
                        )
                        .setPositiveButton("Aktywuj", (dialog, which) -> {
                            final Calendar currentDate = Calendar.getInstance();
                            startTime = formatter.format(currentDate.getTime());
                            endTime = new Date(currentDate.getTimeInMillis()  + (60000 * 20)).toString();

                            JSONObject postData = new JSONObject();
                            try {
                                postData.put("startTime", startTime);
                                postData.put("id", barcodeString);
                                new updateMPKTicket().execute("http://192.168.55.109:3000/routes/tickets/" + barcodeString, postData.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(this, MPKBoughtTicket.class);
                            intent.putExtra("barcode", barcodeString);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("Zamknij", dialogClickListener)
                        .show();
            });
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
                DBAdapter
                        .getInstance(MPKBoughtTicket.this)
                        .update_MPKTicket(barcodeString, startTime);
                Toast.makeText(MPKBoughtTicket.this, "Pomyślnie aktywowano bilet!",
                        Toast.LENGTH_LONG).show();
                Intent intentNew = new Intent(MPKBoughtTicket.this, MPKBoughtTicket.class);
                intentNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentNew.putExtra("barcode", barcodeString);
                if(pd.isShowing()){
                    pd.dismiss();
                }
                MPKBoughtTicket.this.startActivity(intentNew);
            }
            if(pd.isShowing()){
                pd.dismiss();
            }
        }
    }

}