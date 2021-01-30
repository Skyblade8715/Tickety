    package com.sky.tickety.ui.tickets;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.tickety.MainActivity;
import com.sky.tickety.R;
import com.sky.tickety.backend.DBAdapter;
import com.sky.tickety.model.OnSwipeTouchListener;

    public class Tickets extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TicketsAdapter ticketsAdapter;
    MPKBoughtTicketsAdapter mpkAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater,  @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets_wallet, container, false);

        Button trainPart, mpkPart;
        View trainDivider, mpkDivider;
        trainPart = view.findViewById(R.id.home_trainPart2);
        mpkPart = view.findViewById(R.id.home_mpkPart2);
        trainDivider = view.findViewById(R.id.home_divider_train2);
        mpkDivider = view.findViewById(R.id.home_divider_mpk2);

        recyclerView = view.findViewById(R.id.tickets_recycleView);
        ticketsAdapter = new TicketsAdapter(DBAdapter.getInstance(getContext()).select_Tickets(), getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ticketsAdapter);
        ticketsAdapter.notifyDataSetChanged();

        trainPart.setOnClickListener(v -> {
            ((MainActivity) getActivity()).setActionBarTitle("Bilety kolei");
            trainDivider.setBackgroundColor(getResources().getColor(R.color.purple_200));
            trainDivider.setElevation(10);
            mpkDivider.setBackgroundColor(Color.parseColor("#503C3C"));
            mpkDivider.setElevation(0);
            ticketsAdapter = new TicketsAdapter(DBAdapter.getInstance(getContext()).select_Tickets(), getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(ticketsAdapter);
            ticketsAdapter.notifyDataSetChanged();

        });
        mpkPart.setOnClickListener(v -> {
            ((MainActivity) getActivity()).setActionBarTitle("Bilety MPK");
            trainDivider.setBackgroundColor(Color.parseColor("#503C3C"));
            trainDivider.setElevation(0);
            mpkDivider.setBackgroundColor(getResources().getColor(R.color.purple_200));
            mpkDivider.setElevation(10);
            mpkAdapter = new MPKBoughtTicketsAdapter(DBAdapter.getInstance(getContext()).select_MPK_Tickets(), getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mpkAdapter);
            mpkAdapter.notifyDataSetChanged();
        });

        view.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                trainPart.performClick();
            }
            public void onSwipeLeft() {
                mpkPart.performClick();
            }
        });


        return view;
    }

        @Override
        public void onResume() {
            super.onResume();
            if(mpkAdapter != null) {
                mpkAdapter = new MPKBoughtTicketsAdapter(DBAdapter.getInstance(getContext()).select_MPK_Tickets(), getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mpkAdapter);
                mpkAdapter.notifyDataSetChanged();
            }
        }
    }