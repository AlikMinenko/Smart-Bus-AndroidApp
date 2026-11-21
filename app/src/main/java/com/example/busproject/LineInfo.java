package com.example.busproject;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.droidsonroids.gif.GifTextureView;
import pl.droidsonroids.gif.InputSource;

import static java.lang.Integer.parseInt;


public class LineInfo extends AppCompatActivity implements View.OnClickListener {

    TextView tvnum, tvdes, tvprice, tvdeptime, tvendtime, tvcount, tvtemp;
    Button btnremove, btnbuy;
    ListView liststations;
    ArrayAdapter arrayAdapter;


    Dialog d;
    ImageButton place1, place2, place3, place4, place5, place6, place7, place8,
            place9, place10, place11, place12, place13, place14, place15;
    boolean[] helps;
    Button btnfinalbuy;
    int counter = 0;
    boolean[] busplaces;
    ImageButton[] buttonplaces;
    AutoCompleteTextView dropdown1, dropdown2;
    ArrayAdapter<String> dropItems1, dropItems2;
    String firststation, laststation;
    List<String> AfterStations;

    ArrayList<String> LineStations;
    String LineNum;
    String LineBus;

    GifTextureView buybutton;

    DatabaseReference databaseReference = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl("https://fir-for-esp32-de10-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        helps = new boolean[15];
        Arrays.fill(helps, false);

        tvnum = findViewById(R.id.numinfotv);
        tvdes = findViewById(R.id.descinfotv);
        tvprice = findViewById(R.id.priceinfotv);
        tvdeptime = findViewById(R.id.depinfotv);
        tvendtime = findViewById(R.id.endinfotv);
        tvcount = findViewById(R.id.peopleinfotv);
        tvtemp = findViewById(R.id.tempinfotv);

        btnremove = findViewById(R.id.removeinfobtn);
        btnbuy = findViewById(R.id.buyinfobtn);
        btnremove.setOnClickListener(this);
        btnbuy.setOnClickListener(this);

        liststations = findViewById(R.id.stationsinfolist);

        if (User.getUserName().equals("admin")) {
            btnremove.setVisibility(View.VISIBLE);
            btnbuy.setVisibility(View.INVISIBLE);
        } else {
            btnremove.setVisibility(View.INVISIBLE);
            btnbuy.setVisibility(View.VISIBLE);
        }
        LineStations = new ArrayList<String>();
        LineNum = "";
        String LineDes = "";
        LineBus = "";
        String LinePrice = "";
        String TimeDep = "";
        String TimeTotal = "";
        String TimeEnd = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            LineNum = extras.getString("LineNum");
            LineDes = extras.getString("LineDesc");
            LineStations = extras.getStringArrayList("Stations");
            LineBus = extras.getString("Bus");
            LinePrice = extras.getString("LinePrice");
            TimeDep = extras.getString("LineDepTime");
            TimeTotal = extras.getString("LineTotalTime");
            TimeEnd = extras.getString("LineEndTime");
        }

        tvnum.setText("LineNumber: " + LineNum);
        tvdes.setText(LineDes);
        tvprice.setText("Price: " + LinePrice + "₪");
        tvdeptime.setText("Departure Time: " + TimeDep);
        tvendtime.setText("End Time: " + TimeEnd);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LineStations);
        liststations.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        databaseReference.child("Buses").child(LineBus).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tvcount.setText("Current Number of people: " + snapshot.child("totalpeople").getValue().toString());
                int Numbertmp = parseInt(snapshot.child("temp").getValue().toString());
                tvtemp.setText(DecToDeg(Numbertmp) + "°");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("Email", error.toString());
            }
        });

        databaseReference.child("Users").child(User.getUserName()).child("Tickets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(LineNum)) {
                    btnbuy.setVisibility(View.INVISIBLE);
                    Toast.makeText(LineInfo.this, "You already have a ticket for this line", Toast.LENGTH_SHORT).show();
                } else {
                    if (User.getUserName().equals("admin"))
                        btnbuy.setVisibility(View.INVISIBLE);
                    else
                        btnbuy.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    public void BuyDialog() {
        d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.buy_dialog);
        d.setCancelable(true);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        d.getWindow().setLayout((18*width)/19, (15*height)/16);

        AfterStations = new ArrayList<String>();
        firststation = "";
        laststation = "";
        dropdown1 = d.findViewById(R.id.firststation);
        dropItems1 = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, LineStations.subList(0, LineStations.size() - 1));
        dropdown1.setAdapter(dropItems1);
        dropdown1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                firststation = adapterView.getItemAtPosition(position).toString();
                int index1 = LineStations.indexOf(firststation) + 1;
                int index2 = LineStations.size();
                dropdown2.setText("", false);
                AfterStations.clear();
                AfterStations.addAll(LineStations.subList(index1, index2));
                dropItems2.notifyDataSetChanged();
            }
        });

        dropdown2 = d.findViewById(R.id.laststation);
        dropItems2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, AfterStations);
        dropdown2.setAdapter(dropItems2);
        dropdown2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                laststation = adapterView.getItemAtPosition(position).toString();
            }
        });

//        buybutton=d.findViewById(R.id.gifbuy);
//        buybutton.setSpeed(0.00000000000000001f);
//        buybutton.setOnClickListener(this);

        btnfinalbuy = d.findViewById(R.id.finalbuybtn);
        btnfinalbuy.setOnClickListener(this);

        {
            place1 = d.findViewById(R.id.chair1);
            place2 = d.findViewById(R.id.chair2);
            place3 = d.findViewById(R.id.chair3);
            place4 = d.findViewById(R.id.chair4);
            place5 = d.findViewById(R.id.chair5);
            place6 = d.findViewById(R.id.chair6);
            place7 = d.findViewById(R.id.chair7);
            place8 = d.findViewById(R.id.chair8);
            place9 = d.findViewById(R.id.chair9);
            place10 = d.findViewById(R.id.chair10);
            place11 = d.findViewById(R.id.chair11);
            place12 = d.findViewById(R.id.chair12);
            place13 = d.findViewById(R.id.chair13);
            place14 = d.findViewById(R.id.chair14);
            place15 = d.findViewById(R.id.chair15);

            place1.setOnClickListener(this);
            place2.setOnClickListener(this);
            place3.setOnClickListener(this);
            place4.setOnClickListener(this);
            place5.setOnClickListener(this);
            place6.setOnClickListener(this);
            place7.setOnClickListener(this);
            place8.setOnClickListener(this);
            place9.setOnClickListener(this);
            place10.setOnClickListener(this);
            place11.setOnClickListener(this);
            place12.setOnClickListener(this);
            place13.setOnClickListener(this);
            place14.setOnClickListener(this);
            place15.setOnClickListener(this);
        }

        buttonplaces = new ImageButton[]{place1, place2, place3, place4, place5,
                place6, place7, place8, place9, place10, place11, place12, place13, place14, place15};

        databaseReference.child("Buses").child(LineBus).child("places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                busplaces = new boolean[15];
                counter = 0;
                Arrays.fill(helps, false);
                for (int i = 0; i < 15; i++) {
                    busplaces[i] = (boolean) snapshot.child(String.valueOf(i)).getValue();
                    if (busplaces[i] && i != 14) {
                        buttonplaces[i].setEnabled(false);
                        buttonplaces[i].setBackgroundResource(R.drawable.chair_red);
                    } else if (busplaces[i] && i == 14) {
                        buttonplaces[i].setEnabled(false);
                        buttonplaces[i].setBackgroundResource(R.drawable.chair_red_dis);
                    } else if (!busplaces[i] && i == 14) {
                        buttonplaces[i].setEnabled(true);
                        buttonplaces[i].setBackgroundResource(R.drawable.chair_blue_dis);
                    } else {
                        buttonplaces[i].setEnabled(true);
                        buttonplaces[i].setBackgroundResource(R.drawable.chair_blue);
                    }
                    Log.d("Email", String.valueOf(counter));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        d.show();
    }

    @Override
    public void onClick(View view) {

        if (view == btnremove) {
            databaseReference.child("Lines").child(LineNum).removeValue();
            databaseReference.child("Buses").child(LineBus).child("available").setValue(true);
            finish();
        }

        if (view == btnbuy) {
            BuyDialog();
        }

        if (view == btnfinalbuy) {
//            buybutton.releasePointerCapture();
//            buybutton.setInputSource(null);
//            buybutton.setSpeed(1f);

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
                    if (counter == 0 || firststation.isEmpty() || laststation.isEmpty())
                        Toast.makeText(LineInfo.this, "You must select a sit and stations", Toast.LENGTH_SHORT).show();
                    else {
                        ArrayList<String> sitpos = new ArrayList<String>();
                        for (int i = 0; i < helps.length; i++) {
                            if (helps[i]) {
                                databaseReference.child("Buses").child(LineBus).child("places").child(String.valueOf(i)).setValue(true);
                                sitpos.add(String.valueOf(i));
                            }
                        }
                        Ticket ticket = new Ticket(LineBus, firststation, laststation, String.valueOf(counter), LineNum, sitpos,true);
                        databaseReference.child("Users").child(User.getUserName()).child("Tickets").child(LineNum).setValue(ticket);

                        d.dismiss();
                        finish();
                    }
//                }
//            },2000);
        }

        if (buttonplaces != null) {

            for (int i = 0; i < buttonplaces.length; i++) {
                if (view == buttonplaces[i]) {
                    if (!helps[i] && counter < 4) {
                        if (i == 14)
                            buttonplaces[i].setBackgroundResource(R.drawable.chair_green_dis);
                        else
                            buttonplaces[i].setBackgroundResource(R.drawable.chair_green);
                        counter++;
                        helps[i] = true;
                    } else if (!helps[i] && counter == 4)
                        Toast.makeText(this, "You can choose only up to 4 sits", Toast.LENGTH_SHORT).show();
                    else {
                        if (i == 14)
                            buttonplaces[i].setBackgroundResource(R.drawable.chair_blue_dis);
                        else
                            buttonplaces[i].setBackgroundResource(R.drawable.chair_blue);
                        helps[i] = false;
                        counter--;
                    }
                }
            }
        }
    }

    public String DecToBinary8(int num) {
        int x = 0;
        String BinNum = "";
        while (num > 0) {
            x = num % 2;
            if (x == 0)
                BinNum = "0" + BinNum;
            else
                BinNum = "1" + BinNum;
            num = num / 2;
        }
        while (BinNum.length() < 8)
            BinNum = "0" + BinNum;
        return BinNum;
    }

    public double DecToDeg(int num) {
        String bin = DecToBinary8(num);
        //Log.d("Email", bin);
        double temp = 0;
        int i = -1;
        while (bin.length() > 0) {
            double x = Math.pow(2, i);
            int y = parseInt(String.valueOf(bin.charAt(bin.length() - 1)));
            temp += x * y;
            i++;
            bin = bin.substring(0, bin.length() - 1);
        }
        return temp;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}