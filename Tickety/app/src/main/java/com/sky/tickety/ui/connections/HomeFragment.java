package com.sky.tickety.ui.connections;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.sky.tickety.MainActivity;
import com.sky.tickety.R;
import com.sky.tickety.api.API_MPKBuyTicket;
import com.sky.tickety.api.SendDataToWatch;
import com.sky.tickety.backend.DBAdapter;
import com.sky.tickety.model.OnSwipeTouchListener;
import com.sky.tickety.ui.buyTicket.BuyTicketMPK;
import com.sky.tickety.ui.buyTicket.MPKTicketsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    DBAdapter db;
    Boolean train = true;
    View view;
    String errorMessage = null, validDateChecker;
    String startTime;
    Button clear, options, search, swap, buy_mpk;
    ImageButton possibleInvalid;
    ArrayList<String> stationNames;
    SharedPreferences prefs;
    AutoCompleteTextView startStation, endStation;
    int state;
    Calendar date;
    RecyclerView recyclerView;
    LastConnAdapter lastConnAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    Button trainPart, mpkPart;

    protected Handler myHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        prefs = getContext().getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        db = DBAdapter.getInstance(requireContext());
        clear = view.findViewById(R.id.clearButton);
        possibleInvalid = view.findViewById(R.id.possiblyIncorrectData);
        options = view.findViewById(R.id.optionsButton);
        search = view.findViewById(R.id.searchButton);
        startStation = view.findViewById(R.id.startStationEdit);
        endStation = view.findViewById(R.id.endStationEdit);
        buy_mpk = view.findViewById(R.id.home_buyMPK);
        swap = view.findViewById(R.id.swap_btn);

        View trainDivider, mpkDivider;
        trainPart = view.findViewById(R.id.home_trainPart);
        mpkPart = view.findViewById(R.id.home_mpkPart);
        trainDivider = view.findViewById(R.id.home_divider_train);
        mpkDivider = view.findViewById(R.id.home_divider_mpk);

        Set<String> set = new HashSet<>();
        set = prefs.getStringSet("TrainLast", set);
        List<String> list = new ArrayList<>(set);
        recyclerView = view.findViewById(R.id.favorite_stations);
        lastConnAdapter = new LastConnAdapter(list, startStation, endStation);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(lastConnAdapter);
        lastConnAdapter.notifyDataSetChanged();

        setLastConn(train);

        buy_mpk.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), BuyTicketMPK.class);
            startActivity(intent);
        });

        trainPart.setOnClickListener(view -> {
            ((MainActivity) getActivity()).setActionBarTitle("Połączenia kolei");
            setupTrains();
            buy_mpk.setVisibility(View.GONE);
            train = true;
            setLastConn(train);
            trainDivider.setBackgroundColor(getResources().getColor(R.color.purple_200));
            trainDivider.setElevation(10);
            mpkDivider.setBackgroundColor(Color.parseColor("#503C3C"));
            mpkDivider.setElevation(0);
        });
        mpkPart.setOnClickListener(view -> {
            ((MainActivity) getActivity()).setActionBarTitle("Komunikacja miejska");
            setupMPK();
            buy_mpk.setVisibility(View.VISIBLE);
            train = false;
            setLastConn(train);
            trainDivider.setBackgroundColor(Color.parseColor("#503C3C"));
            trainDivider.setElevation(0);
            mpkDivider.setBackgroundColor(getResources().getColor(R.color.purple_200));
            mpkDivider.setElevation(10);
        });

        view.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                trainPart.performClick();
            }
            public void onSwipeLeft() {
                mpkPart.performClick();
            }
        });

        swap.setOnClickListener(view -> {
            String startTemp = "", endTemp = "";
            if (startStation.getText() != null) {
                startTemp = startStation.getText().toString();
            }
            if (endStation.getText() != null) {
                endTemp = endStation.getText().toString();
            }
            startStation.setText(endTemp);
            endStation.setText(startTemp);
        });

        options.setOnClickListener(v -> {
            showDateTimePicker();
        });

        clear.setOnClickListener(view -> {
            startStation.setText("");
            endStation.setText("");
            date = null;
            options.setText("Opcje");
        });

        if (train) {
            trainPart.performClick();
        } else {
            mpkPart.performClick();
        }

        search.setOnClickListener(view -> {
            if (!stationNames.contains(startStation.getText().toString()) || !stationNames.contains(endStation.getText().toString())) {
                Toast.makeText(getContext(), "Wybierz poprawne stacje!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent;
                Set<String> last = new HashSet<>();
                if (train) {
                    intent = new Intent(requireContext(), Connections.class);
                    last = prefs.getStringSet("TrainLast", last);
                    List<String> temp = new ArrayList<>(last);
                    if(temp.size() >= 6){
                        temp.remove(5);
                    }
                    temp.add(0, startStation.getText() + "&" + endStation.getText());
                    prefs.edit().putStringSet("TrainLast", new HashSet<>(temp)).apply();
                } else {
                    intent = new Intent(requireContext(), ConnectionsMPK.class);
                    last = prefs.getStringSet("MPKLast", last);
                    List<String> temp = new ArrayList<>(last);
                    if(temp.size() >= 6){
                        temp.remove(5);
                    }
                    temp.add(0, startStation.getText() + "&" + endStation.getText());
                    prefs.edit().putStringSet("MPKLast", new HashSet<>(temp)).apply();
                }
                intent.putExtra("date", startTime);
                intent.putExtra("start", startStation.getText().toString());
                intent.putExtra("end", endStation.getText().toString());
                startActivity(intent);
            }
        });

        myHandler = new Handler();
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(messageReceiver, messageFilter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(train) {
            trainPart.performClick();
        } else {
            mpkPart.performClick();
        }
        setLastConn(train);
    }

    public void setLastConn(boolean isTrain){
        Set<String> set = new HashSet<>();
        if(isTrain){
            set = prefs.getStringSet("TrainLast", set);
        } else {
            set = prefs.getStringSet("MPKLast", set);
        }
        lastConnAdapter = new LastConnAdapter(new ArrayList<>(set), startStation, endStation);
        recyclerView.setAdapter(lastConnAdapter);
        lastConnAdapter.notifyDataSetChanged();
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            String topic = intent.getStringExtra("topic");
            if("getTickets".equals(topic)){
                new API_MPKBuyTicket()
                        .API_getTickets(requireContext(), null, null, true, myHandler);
            } else if("buyMPKTicket".equals(topic)){
                String[] temp = message.split("&");
                final JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(temp[2]);
                    new API_MPKBuyTicket().API_buyTickets(jsonObject, getContext(), temp[0], temp[1], true, myHandler);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if("MPK".equals(topic)){
                new SendDataToWatch("/my_path/boughtTickets/dataMPK", message, context).start();
            } else if("Train".equals(topic)){
                new SendDataToWatch("/my_path/boughtTickets/dataTrain", message, context).start();
            }
        }
    }

    @Override
    public void onStart() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        validDateChecker = formatter.format(Calendar.getInstance().getTime());
        startTime = new SimpleDateFormat("yyyy-MM-dd kk:mm").format(Calendar.getInstance().getTime());
        super.onStart();
    }

    public void setupTrains(){
        if(prefs.contains("TABLE_STATIONS_NAMES")){
            if(prefs.getString("TABLE_STATIONS_NAMES", "xx").equals(validDateChecker)){
                state = 0;
                stationNames = db.select_Station_Names();
                setupAdapter(stationNames);
            } else {
                state = 1;
                new getStationsNames().execute("http://192.168.55.109:3000/stations/names");
            }
        } else {
            state = 2;
            new getStationsNames().execute("http://192.168.55.109:3000/stations/names");
        }
    }

    public void setupMPK(){
        if(prefs.contains("TABLE_STOPS_NAMES")){
            if(prefs.getString("TABLE_STOPS_NAMES", "xx").equals(validDateChecker)){
                state = 0;
                stationNames = db.select_Stops_Names();
                setupAdapter(stationNames);
            } else {
                state = 1;
                new getStationsNames().execute("http://192.168.55.109:3000/stops/names");
            }
        } else {
            state = 2;
            new getStationsNames().execute("http://192.168.55.109:3000/stops/names");
        }
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                DecimalFormat timePattern = new DecimalFormat("00");
                startTime = new SimpleDateFormat("yyyy-MM-dd kk:mm").format(date.getTime());
                options.setText(
                        String.format("%s.%s %s:%s",
                                timePattern.format(date.get(Calendar.DAY_OF_MONTH)),
                                timePattern.format((date.get(Calendar.MONTH) + 1)),
                                timePattern.format(date.get(Calendar.HOUR_OF_DAY)),
                                timePattern.format(date.get(Calendar.MINUTE))
                        ));
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void setDim(float dim){
        search.setAlpha(dim);
        clear.setAlpha(dim);
        options.setAlpha(dim);
        possibleInvalid.setAlpha(dim);
        getActivity().getWindow().setDimAmount(dim);
    }

    public void setupAdapter(ArrayList<String> names){
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        names);
        startStation.setAdapter(adapter);
        endStation.setAdapter(adapter);
    }

    private class getStationsNames extends AsyncTask<String, String, String> {
        View progressBar;
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = view.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
            ArrayList<String> names = new ArrayList<>();
            JSONObject jArray = null;
            if (result != null) {
                try {
                    jArray = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (jArray != null) {
                try {
                    for(int i = 0; i < jArray.getJSONArray("Message").length(); i++){
                        names.add((String) jArray.getJSONArray("Message").get(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(names.size() != 0){
                if(train) {
                    db.insert_Station_Names(names, validDateChecker);
                    stationNames = db.select_Station_Names();
                } else {
                    db.insert_Stops_Names(names, validDateChecker);
                    stationNames = db.select_Stops_Names();
                }
                setupAdapter(stationNames);
            }

            if(errorMessage != null){
                if(state == 1){
                    Toast toast = Toast.makeText(requireContext(), "Aplikacji może brakować danych, nie udało się pobrać aktualnych danych!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.START, ((int) possibleInvalid.getX()), ((int) possibleInvalid.getY()) + 88);
                    possibleInvalid.setVisibility(View.VISIBLE);
                    possibleInvalid.setOnClickListener(view -> toast.show());
                    if(train) {
                        stationNames = db.select_Station_Names();
                    } else {
                        stationNames = db.select_Stops_Names();
                    }
                    setupAdapter(stationNames);
                    toast.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Wystąpił błąd!");
                    builder.setMessage(errorMessage);
                    builder.setPositiveButton("Zamknij", (dialog, id) -> {});
                    builder.show();
                }
            }

            progressBar.setVisibility(View.GONE);
            setDim(1f);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}