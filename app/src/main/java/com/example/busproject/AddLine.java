package com.example.busproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddLine extends AppCompatActivity implements View.OnClickListener {

    EditText etnum,etdes,etstation,etdep,ettime,etprice; //edittexts of addline activity
    Button btnaddstations,btnsave,btncancel; //buttons of addline activity
    TextView tvnum; //textview of addline activity

    AutoCompleteTextView dropdown;
    ArrayAdapter<String> dropItems;
    ArrayList<String> items;
    String busitem="";

    Button btndialogadd,btndialogremove,btndialogcancel; //buttons of stations dialog

    ArrayList<String> Stations;
    int numofstations=0;
    Dialog d;
    ListView liststations;
    ArrayAdapter arrayAdapter;

    TimePickerDialog timePickerDialog;

    DatabaseReference databaseReference = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl("https://fir-for-esp32-de10-default-rtdb.europe-west1.firebasedatabase.app/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_line);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        items=new ArrayList<String>();
        databaseReference.child("Buses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("available").getValue().equals(true))
                        items.add(ds.getKey());
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        dropdown=findViewById(R.id.dropdowntxt);
        dropItems = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,items);
        dropdown.setAdapter(dropItems);
        dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                busitem = adapterView.getItemAtPosition(position).toString();
            }
        });

        etnum=findViewById(R.id.linenumet);
        etdes=findViewById(R.id.linedeset);
        etprice=findViewById(R.id.linepriceet);
        etdep=findViewById(R.id.linedepet);
        ettime=findViewById(R.id.linetimeet);

        btnaddstations=findViewById(R.id.addstationbtn);
        btnsave=findViewById(R.id.savebtn);
        btncancel=findViewById(R.id.cancelbtn);

        tvnum=findViewById(R.id.numofstattv);

        etdep.setOnClickListener(this);

        btnaddstations.setOnClickListener(this);
        btnsave.setOnClickListener(this);
        btncancel.setOnClickListener(this);

        Stations=new ArrayList<>();
        tvnum.setText("Number of stations: "+numofstations);

    }

    public void StationsDialog(){
        d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.stations_dialog);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        d.getWindow().setLayout(width,height);

        btndialogadd=d.findViewById(R.id.dialogaddbtn);
        btndialogremove=d.findViewById(R.id.dialogremovebtn);
        btndialogcancel=d.findViewById(R.id.dialogcancelbtn);
        etstation=d.findViewById(R.id.linestationet);
        liststations=d.findViewById(R.id.stationslist);

        arrayAdapter=new ArrayAdapter (this, android.R.layout.simple_list_item_1,Stations);
        liststations.setAdapter(arrayAdapter);

        btndialogadd.setOnClickListener(this);
        btndialogremove.setOnClickListener(this);
        btndialogcancel.setOnClickListener(this);

        d.show();
    }

    public void TimeDialog(){
        timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                if(hour<10 && minutes<10)
                    etdep.setText("0"+hour+":0"+minutes);
                else if(minutes<10)
                    etdep.setText(hour+":0"+minutes);
                else if(hour<10)
                    etdep.setText("0"+hour+":"+minutes);
                else
                    etdep.setText(hour+":"+minutes);
            }
        },0,0,false);
        timePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view==btnaddstations){
            StationsDialog();
        }

        if(view==btndialogadd){
            if(etstation.getText().toString().isEmpty())
                Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();
            else{
                Stations.add(etstation.getText().toString());
                arrayAdapter.notifyDataSetChanged();
                etstation.getText().clear();
                numofstations=Stations.size();
                tvnum.setText("Number of stations: "+numofstations);
            }
        }

        if(view == btndialogremove){
            if(Stations.isEmpty())
                Toast.makeText(this,"Nothing to remove",Toast.LENGTH_SHORT).show();
            else {
                Stations.remove(Stations.size() - 1);
                arrayAdapter.notifyDataSetChanged();
                numofstations=Stations.size();
                tvnum.setText("Number of stations: "+numofstations);
            }
        }

        if(view== btndialogcancel){
            d.dismiss();
        }

        if(view==etdep){
            TimeDialog();
        }

        if(view==btnsave){
            String LineNumber=etnum.getText().toString();
            String LineDesc=etdes.getText().toString();
            String LineBus=busitem;
            String LineDepTime=etdep.getText().toString();
            String LineTotalTime=ettime.getText().toString();
            String LinePrice=etprice.getText().toString();

            if(LineNumber.isEmpty() || LineDesc.isEmpty() || LineBus.isEmpty()
                    || Stations.isEmpty() || LinePrice.isEmpty() || LineDepTime.isEmpty() || LineTotalTime.isEmpty())
                Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();
            else{
                Line line=new Line(LineNumber,LineDesc,LineBus,Stations,LinePrice,LineDepTime,LineTotalTime);
                databaseReference.child("Lines").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild(line.getNumber())) {
                            Toast.makeText(AddLine.this, "Line is already Created", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            databaseReference.child("Lines").child(line.getNumber()).setValue(line);
                            databaseReference.child("Buses").child(line.getBus()).child("available").setValue(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
                finish();
            }

        }

        if(view==btncancel){
            finish();
        }
    }

}