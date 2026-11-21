package com.example.busproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import static java.lang.Integer.parseInt;


public class LinesFragment extends Fragment {

    RecyclerView recylines;
    ArrayList<Line> linelist;
    LineAdapter lineAdapter;
    LineAdapter.RecyclerViewClickListener listener;

    DatabaseReference databaseReference = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl("https://fir-for-esp32-de10-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lines, container, false);
        recylines=view.findViewById(R.id.linesrecy);
        recylines.setHasFixedSize(true);
        recylines.setLayoutManager(new LinearLayoutManager(getActivity()));
        recylines.setItemAnimator(new DefaultItemAnimator());
        setOnItemClickListener();

        DisplayMetrics metrics = getResources().getDisplayMetrics(); ///change later (recycleview height change)
        int width = metrics.widthPixels;                             //
        int height = metrics.heightPixels;                           //
        ViewGroup.LayoutParams params=recylines.getLayoutParams();   //
        params.height= (int) ((5.4 * height)/7);                     ///
        recylines.setLayoutParams(params);                          ///

        linelist = new ArrayList<>();
        lineAdapter = new LineAdapter(getActivity(),linelist,listener);
        recylines.setAdapter(lineAdapter);


        databaseReference.child("Lines").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                linelist.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    linelist.add(ds.getValue(Line.class));
                }
                lineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        return view;
    }

    private void setOnItemClickListener() {
        listener=new LineAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent toLineInfo=new Intent(getActivity(),LineInfo.class);
                toLineInfo.putExtra("LineNum",linelist.get(position).getNumber());
                toLineInfo.putExtra("LineDesc",linelist.get(position).getDescription());
                toLineInfo.putExtra("Stations",linelist.get(position).getStations());
                toLineInfo.putExtra("Bus",linelist.get(position).getBus());
                toLineInfo.putExtra("LineDepTime",linelist.get(position).getDeptime());
                toLineInfo.putExtra("LineTotalTime",linelist.get(position).getTotaltime());
                toLineInfo.putExtra("LineEndTime",linelist.get(position).getEndtime());
                toLineInfo.putExtra("LinePrice",linelist.get(position).getPrices());
                startActivity(toLineInfo);
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        };
    }
}