package com.example.busproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddBus extends AppCompatActivity implements View.OnClickListener {

    EditText etbusid,etliveip;
    Button btnsave,btncancel;

    DatabaseReference databaseReference = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl("https://fir-for-esp32-de10-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etbusid=findViewById(R.id.busidet);
        etliveip=findViewById(R.id.liveipet);
        btnsave=findViewById(R.id.savebusbtn);
        btncancel=findViewById(R.id.cancelbusbtn);

        btnsave.setOnClickListener(this);
        btncancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==btnsave){
            String busid=etbusid.getText().toString();
            String liveip=etliveip.getText().toString();
            if(busid.isEmpty() || liveip.isEmpty()){
                Toast.makeText(AddBus.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
            else{
                databaseReference.child("Buses").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if(snapshot.hasChild(busid))
                            Toast.makeText(AddBus.this, "Bus ID already exists", Toast.LENGTH_SHORT).show();
                        else{
                            Bus bus = new Bus(liveip);
                            databaseReference.child("Buses").child(busid).setValue(bus);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        }
        if(view==btncancel){
            finish();
        }
    }
}