package com.example.busproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieConfig;
import com.airbnb.lottie.RenderMode;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import pl.droidsonroids.gif.GifImageView;

public class QrActivity extends AppCompatActivity {

    CodeScannerView codeScannerView;
    CodeScanner codeScanner;
    GifImageView qrgif;
    //LottieAnimationView qrlottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        codeScannerView = findViewById(R.id.qrview);
        checkPermission();
        qrgif = findViewById(R.id.qranim);

//        qrlottie = findViewById(R.id.qranim);
//        qrlottie.setRenderMode(RenderMode.AUTOMATIC);

        codeScanner = new CodeScanner(this, codeScannerView);
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.isAutoFocusEnabled();
        codeScanner.isFlashEnabled();
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(Result result) {
                Intent intent = getIntent();
                intent.putExtra("BusID", result.getText());
                setResult(1, intent);
                finish();
            }
        });


        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    public void checkPermission() {
        // Checking if permission is not granted
        if (ActivityCompat.checkSelfPermission(QrActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QrActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else
                Toast.makeText(QrActivity.this, "Need to allow permission", Toast.LENGTH_SHORT).show();

        }
    }

}