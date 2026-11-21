package com.example.busproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
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
import java.util.Arrays;

import static com.google.android.gms.common.ConnectionResult.TIMEOUT;
import static java.lang.Integer.parseInt;

public class DevTest extends AppCompatActivity implements View.OnClickListener {

    MjpegView liveview;
    Button btnlive, btnreset, btnsendfb;
    EditText etfb;
    String liveip;
    int TIMEOUT = 5;
    String text;
    InputImage image;
    int counter = 0;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-for-esp32-de10-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnlive = findViewById(R.id.startlive);
        btnreset = findViewById(R.id.reset);
        btnsendfb = findViewById(R.id.sendtofb);
        etfb = findViewById(R.id.fbet);
        liveview = findViewById(R.id.live_video_tab);
        liveview.getSurfaceView().setAlpha(0);

        btnlive.setOnClickListener(this);
        btnreset.setOnClickListener(this);
        btnsendfb.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnlive) {
            Mjpeg.newInstance()
                    .open("http://" + liveip + ":81/stream", TIMEOUT)
                    .subscribe(inputStream -> {
                        liveview.setSource(inputStream);
                        liveview.setDisplayMode(DisplayMode.FULLSCREEN);
                    });

            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

            liveview.setOnFrameCapturedListener(new OnFrameCapturedListener() {
                @Override
                public void onFrameCapturedWithHeader(@NonNull byte[] bytes, @NonNull byte[] bytes1) {

                }

                @Override
                public void onFrameCaptured(Bitmap bitmap) {
                    counter++;
                    image = InputImage.fromBitmap(bitmap, 0);
                    //Log.d("Email","captured");
                    if (image != null && counter == 60) {
                        counter = 0;
                        Task<Text> result =
                                recognizer.process(image)
                                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                                            @Override
                                            public void onSuccess(Text visionText) {
                                                text = visionText.getText();

                                                for (Text.TextBlock block : visionText.getTextBlocks()) {

                                                    for (Text.Line line : block.getLines()) {
                                                        Log.d("Email", line.getText());
                                                    }
                                                }
                                            }
                                        })
                                        .addOnFailureListener(
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(Exception e) {
                                                        // Task failed with an exception
                                                        // ...
                                                    }
                                                });
                    }
                }
            });
        }

        if (view == btnreset) {
            databaseReference.child("Users").child("user").child("Tickets").removeValue();
            databaseReference.child("Users").child("user2").child("Tickets").removeValue();
            ArrayList<Boolean> arr = new ArrayList<Boolean>();
            for (int i=0;i<15;i++){
                arr.add(false);
            }
            databaseReference.child("Buses").child("1bus1").child("places").setValue(arr);
            databaseReference.child("Buses").child("1bus1").child("goingdown").setValue(false);
            databaseReference.child("Buses").child("1bus1").child("functions").setValue(0);
            databaseReference.child("Buses").child("1bus1").child("currentUser").setValue("");
            databaseReference.child("Buses").child("2bus2").child("places").setValue(arr);
            databaseReference.child("Buses").child("2bus2").child("goingdown").setValue(false);
            databaseReference.child("Buses").child("2bus2").child("functions").setValue(0);
            databaseReference.child("Buses").child("2bus2").child("currentUser").setValue("");
        }
        if (view == btnsendfb) {
            if (!etfb.getText().equals(null) && !etfb.getText().toString().isEmpty())
                databaseReference.child("Project").child("SendToFPGA").setValue(parseInt(etfb.getText().toString()));
        }
    }
}