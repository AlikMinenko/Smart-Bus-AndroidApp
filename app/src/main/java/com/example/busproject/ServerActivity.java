package com.example.busproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegSurfaceView;
import com.github.niqdev.mjpeg.MjpegView;
import com.github.niqdev.mjpeg.OnFrameCapturedListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class ServerActivity extends AppCompatActivity {

    Button btnback;
    MjpegView livevid;
    int TIMEOUT = 5;
    String liveip;
    int counter = 0;

    TextView tvcurrentst;

    DatabaseReference databaseReference = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl("https://fir-for-esp32-de10-default-rtdb.europe-west1.firebasedatabase.app/");


    boolean listfull = false;
    int cntppl;
    int counterup = 0;
    int counterdown = 0;
    String prevstation = "";
    LinkedHashMap<String, Pair<String, Integer>> stationdown;
    int totalppl = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvcurrentst = findViewById(R.id.currentsttv);
        livevid = findViewById(R.id.live_video_tab);
        btnback = findViewById(R.id.backbtn);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopStream();
                Intent intent = new Intent(ServerActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        stationdown = new LinkedHashMap<String, Pair<String, Integer>>();

        databaseReference.child("Buses").child("1bus1").child("live").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                liveip = snapshot.getValue(String.class);
                Log.d("Email", liveip);
                Log.d("Email", liveip);
                if (liveip != null) {
                    if (!liveip.equals("")) {
                        Mjpeg.newInstance()
                                .open("http://" + liveip + ":81/stream", TIMEOUT)
                                .subscribe(inputStream -> {
                                    livevid.setSource(inputStream);
                                    livevid.setDisplayMode(DisplayMode.FULLSCREEN);
                                }, throwable -> {Toast.makeText(ServerActivity.this, "Error", Toast.LENGTH_SHORT).show();});
                    }
                }

                TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
                livevid.setOnFrameCapturedListener(new OnFrameCapturedListener() {
                    @Override
                    public void onFrameCapturedWithHeader(@NonNull byte[] bytes, @NonNull byte[] bytes1) {

                    }

                    @Override
                    public void onFrameCaptured(Bitmap bitmap) {
                        counter++;
                        InputImage image = InputImage.fromBitmap(bitmap, 0);
                        if (counter == 60) {
                            counter = 0;
                            Task<Text> result =
                                    recognizer.process(image)
                                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                                @Override
                                                public void onSuccess(Text visionText) {
                                                    for (Text.TextBlock block : visionText.getTextBlocks()) {

                                                        for (Text.Line line : block.getLines()) {
                                                            databaseReference.child("Buses").child("1bus1").child("currentST").setValue(line.getText());
                                                            tvcurrentst.setText(line.getText());
                                                        }
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(
                                                    new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(Exception e) {
                                                        }
                                                    });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        databaseReference.child("Buses").child("1bus1").child("currentUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (!snapshot.getValue(String.class).equals("")) {
                        String cntuser = snapshot.getValue().toString();


                        databaseReference.child("Users").child(cntuser).child("Tickets").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot2) {
                                totalppl = 0;
                                for (DataSnapshot ds : snapshot2.getChildren()) {
                                    if (ds.child("bus").getValue().toString().equals("1bus1")) {
                                        totalppl = parseInt(ds.child("totalsits").getValue().toString());
                                        prevstation = ds.child("firststation").getValue(String.class);
                                        break;
                                    }
                                }

                                databaseReference.child("Buses").child("1bus1").child("totalpeople").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot3) {

                                        if (snapshot3.getValue(Integer.class) > cntppl && !cntuser.equals("")) {
                                            cntppl++;
                                            counterup++;
                                        }
                                        Log.d("Email", ""+cntppl);
                                        Log.d("Email", "userpps: "+totalppl);
                                        if (counterup == totalppl) {

                                            databaseReference.child("Lines").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot4) {
                                                    ArrayList<String> x = new ArrayList<String>();
                                                    for (DataSnapshot ds : snapshot4.getChildren()) {
                                                        if (ds.child("bus").getValue(String.class).equals("1bus1")) {
                                                            x = (ArrayList<String>) ds.child("stations").getValue();
                                                        }
                                                    }
                                                    int h = x.indexOf(prevstation) + 1;
                                                    if (stationdown != null  && h < stationdown.size()) {
                                                        if (stationdown.get(x.get(h)).getSecond() > 0) {
                                                            databaseReference.child("Buses").child("1bus1").child("functions").setValue(28);
                                                        } else
                                                            databaseReference.child("Buses").child("1bus1").child("functions").setValue(0);
                                                    }
                                                    Log.d("Email", ""+cntppl);
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError error) {

                                                }
                                            });
                                            databaseReference.child("Buses").child("1bus1").child("currentUser").setValue("");
                                            counterup = 0;
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError error) {

                                    }
                                });

                            }
                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        databaseReference.child("Lines").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot1) {

                ArrayList<String> helper = new ArrayList<String>();
                for (DataSnapshot ds : snapshot1.getChildren()) {
                    if (ds.child("bus").getValue(String.class).equals("1bus1")) {
                        helper = (ArrayList<String>) ds.child("stations").getValue();
                    }
                }

                Pair<String, Integer> pair = new Pair<String, Integer>("", 0);
                for (String station : helper) {
                    stationdown.put(station, pair);
                }

                databaseReference.child("Buses").child("1bus1").child("currentUser").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot2) {
                        Log.d("Email", ""+cntppl);
                        listfull = false;
                        String cntuser = "";
                        cntuser = snapshot2.getValue(String.class);
                        if (cntuser != null) {
                            String finalCntuser = cntuser;
                            databaseReference.child("Users").child(cntuser).child("Tickets").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot3) {
                                    int totaldown = 0;
                                    String lastst = "";
                                    for (DataSnapshot ds2 : snapshot3.getChildren()) {
                                        if (ds2.child("bus").getValue(String.class).equals("1bus1")) {
                                            totaldown = parseInt(ds2.child("totalsits").getValue(String.class));
                                            lastst = ds2.child("laststation").getValue(String.class);
                                            if (stationdown.containsKey(lastst)) {
                                                Pair<String, Integer> x = new Pair<String, Integer>("", 0);
                                                x.setFirst(stationdown.get(lastst).getFirst() + finalCntuser + ",");
                                                x.setSecond(stationdown.get(lastst).getSecond() + totaldown);
                                                stationdown.put(lastst, x);
                                                for (Map.Entry<String, Pair<String, Integer>> entry : stationdown.entrySet()) {
                                                    Log.d("Email", "{" + entry.getKey() + ": " + entry.getValue().getFirst() + "XXX" + entry.getValue().getSecond() + "}");
                                                }
                                            }
                                            break;

                                        }
                                    }
                                    listfull = true;

                                }

                                @Override
                                public void onCancelled(DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        databaseReference.child("Buses").child("1bus1").child("currentST").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (listfull) {
                    for (Map.Entry<String, Pair<String, Integer>> entry : stationdown.entrySet()) {
                        if (entry.getKey().equals(snapshot.getValue(String.class)) && entry.getValue().getSecond() > 0) {
                            databaseReference.child("Buses").child("1bus1").child("functions").setValue(10);
                            databaseReference.child("Buses").child("1bus1").child("goingdown").setValue(true);
                        }
                    }
                }
                Log.d("Email", ""+cntppl);

                databaseReference.child("Lines").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot2) {
                        if (prevstation != "") {
                            prevstation = snapshot.getValue(String.class);
                            ArrayList<String> x = new ArrayList<String>();
                            for (DataSnapshot ds : snapshot2.getChildren()) {
                                if (ds.child("bus").getValue(String.class).equals("1bus1")) {
                                    x = (ArrayList<String>) ds.child("stations").getValue();
                                }
                            }
                            int h = x.indexOf(prevstation) + 1;
                            if (stationdown != null && h < stationdown.size()) {
                                if (stationdown.get(x.get(h)).getSecond() > 0) {
                                    databaseReference.child("Buses").child("1bus1").child("functions").setValue(28);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        databaseReference.child("Buses").child("1bus1").child("totalpeople").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                databaseReference.child("Buses").child("1bus1").child("goingdown").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot2) {

                        databaseReference.child("Buses").child("1bus1").child("currentST").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot3) {
                                if (snapshot2.getValue(Boolean.class) == true) {
                                    if (cntppl > snapshot.getValue(Integer.class)) {
                                        cntppl--;
                                        counterdown++;
                                    }
                                    if (counterdown == stationdown.get(snapshot3.getValue(String.class)).getSecond()) {

                                        Pair<String, Integer> x = stationdown.get(snapshot3.getValue(String.class));
                                        String[] y = x.getFirst().split(",", 0);
                                        for (int i = 0; i < y.length; i++) {
                                            int finalI = i;
                                            databaseReference.child("Users").child(y[i]).child("Tickets").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot4) {
                                                    for (DataSnapshot ds : snapshot4.getChildren()) {
                                                        if (ds.child("bus").getValue(String.class).equals("1bus1")) {
                                                            Log.d("Email", "here");
                                                            ArrayList<String> newx = (ArrayList<String>) ds.child("sitpositions").getValue();
                                                            for (int j = 0; j < newx.size(); j++) {
                                                                databaseReference.child("Buses").child("1bus1").child("places").child(newx.get(j)).setValue(false);

                                                            }
                                                            databaseReference.child("Users").child(y[finalI]).child("Tickets").child(ds.getKey()).removeValue();
                                                            break;
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError error) {

                                                }
                                            });
                                        }
                                        databaseReference.child("Buses").child("1bus1").child("functions").setValue(0);
                                        databaseReference.child("Buses").child("1bus1").child("goingdown").setValue(false);
                                        stationdown.put(snapshot3.getValue(String.class), new Pair<String, Integer>("", 0));
                                        counterdown = 0;
                                    }
                                    for (Map.Entry<String, Pair<String, Integer>> entry : stationdown.entrySet()) {
                                        Log.d("Email", "{" + entry.getKey() + ": " + entry.getValue().getFirst() + "XXX" + entry.getValue().getSecond() + "}");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void StopStream(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                livevid.stopPlayback();
                livevid.clearStream();
            }
        }).start();
    }

    public class Pair<A, B> {
        A first = null;
        B second = null;

        Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public void setFirst(A first) {
            this.first = first;
        }

        public B getSecond() {
            return second;
        }

        public void setSecond(B second) {
            this.second = second;
        }

    }
}