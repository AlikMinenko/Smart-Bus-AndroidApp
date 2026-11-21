package com.example.busproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnsubmit, btnregister; //Buttons of Login activity
    CheckBox boxsave;             //CheckBox of Login activity
    TextInputLayout tilpass,tiluser;
    TextInputEditText etpw,etun;
    boolean isChecked;

    Dialog d;                                        //Dialog Register
    Button btnregdialog, btncandialog;                //Buttons of Register Dialog
    TextInputEditText etdialun, etdialem, etdialps, etdialpscon;
    TextInputLayout tilnewpass, tilpscon;

    public static final String SHARED_PREFERENCE = "LoggedIn";

    DatabaseReference databaseReference = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl("https://fir-for-esp32-de10-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tilpass=findViewById(R.id.inputpass);
        tiluser=findViewById(R.id.inputuser);
        etun = findViewById(R.id.unet);
        etpw = findViewById(R.id.pwet);
        boxsave = findViewById(R.id.savebox);
        btnsubmit = findViewById(R.id.submitbtn);
        btnregister = findViewById(R.id.registerbtn);

        btnsubmit.setOnClickListener(this);
        btnregister.setOnClickListener(this);

        boxsave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked = b;
            }
        });

        tilpass.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etpw != null) {
                    if (etpw.getTransformationMethod() instanceof PasswordTransformationMethod) {
                        etpw.setTransformationMethod(null);
                        tilpass.setEndIconDrawable(R.drawable.eye_crossed);
                    } else {
                        etpw.setTransformationMethod(new PasswordTransformationMethod());
                        tilpass.setEndIconDrawable(R.drawable.eye);
                    }
                    etpw.setSelection(etpw.length());
                }
            }
        });

    }

    public void CreateRegisterDialog() {
        d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.register_dialog);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        d.getWindow().setLayout((9*width)/10, (9*height)/10);

        tilnewpass=d.findViewById(R.id.inputnewpass);
        tilpscon=d.findViewById(R.id.inputconpass);
        btnregdialog = d.findViewById(R.id.dialogregisterbtn);
        btncandialog = d.findViewById(R.id.dialogcancelbtn);
        etdialun = d.findViewById(R.id.dialunet);
        etdialem = d.findViewById(R.id.dialemet);
        etdialps = d.findViewById(R.id.dialpwet);
        etdialpscon = d.findViewById(R.id.dialpwconet);

        tilnewpass.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etdialps != null) {
                    if (etdialps.getTransformationMethod() instanceof PasswordTransformationMethod) {
                        etdialps.setTransformationMethod(null);
                        tilnewpass.setEndIconDrawable(R.drawable.eye_crossed);
                    } else {
                        etdialps.setTransformationMethod(new PasswordTransformationMethod());
                        tilnewpass.setEndIconDrawable(R.drawable.eye);
                    }
                    etdialps.setSelection(etdialps.length());
                }
            }
        });

        tilpscon.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etdialpscon != null) {
                    if (etdialpscon.getTransformationMethod() instanceof PasswordTransformationMethod) {
                        etdialpscon.setTransformationMethod(null);
                        tilpscon.setEndIconDrawable(R.drawable.eye_crossed);
                    } else {
                        etdialpscon.setTransformationMethod(new PasswordTransformationMethod());
                        tilpscon.setEndIconDrawable(R.drawable.eye);
                    }
                    etdialpscon.setSelection(etdialpscon.length());
                }
            }
        });

        btnregdialog.setOnClickListener(this);
        btncandialog.setOnClickListener(this);
        d.show();
    }

    @Override
    public void onClick(View view) {
        if (view == btnsubmit) {
            String UserName = etun.getText().toString();
            String PassWord = etpw.getText().toString();
            if (UserName.isEmpty() || PassWord.isEmpty())
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            else {
                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild(UserName)) {
                            String getpassword = snapshot.child(UserName).child("Password").getValue(String.class);

                            if (getpassword.equals(PassWord)) {
                                Toast.makeText(LoginActivity.this, "Welcome Back " + UserName, Toast.LENGTH_SHORT).show();
                                User.setUserName(UserName);
                                User.setEmail(snapshot.child(UserName).child("Email").getValue(String.class));
                                User.setPassword(getpassword);
                                if (isChecked) {
                                    SharedPreferences sp = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putBoolean("loggedin", true);
                                    editor.putString("username", UserName);
                                    editor.putString("email", User.getEmail());
                                    editor.apply();
                                }
                                if (UserName.equals("server")) {
                                    Intent ToServer = new Intent(LoginActivity.this, ServerActivity.class);
                                    startActivity(ToServer);
                                    finish();
                                } else {
                                    Intent ToMain = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(ToMain);
                                    finish();
                                }
                            } else
                                Toast.makeText(LoginActivity.this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(LoginActivity.this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        }

        if (view == btnregister) {
            CreateRegisterDialog();
            etun.getText().clear();
            etpw.getText().clear();
        }
        if (view == btnregdialog) {
            String UserNameDial = etdialun.getText().toString();
            String EmailDial = etdialem.getText().toString();
            String PasswordDial = etdialps.getText().toString();
            String PasswordConDial = etdialpscon.getText().toString();
            if (UserNameDial.isEmpty() || EmailDial.isEmpty() || PasswordDial.isEmpty() || PasswordConDial.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!PasswordConDial.equals(PasswordDial))
                Toast.makeText(this, "Password typo", Toast.LENGTH_SHORT).show();
            else {
                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean help = true;
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String usersmail = ds.child("Email").getValue().toString();
                            Log.d("Email", usersmail);  //show all emails in database
                            if (usersmail.equals(EmailDial)) {
                                Toast.makeText(LoginActivity.this, "Email is already used", Toast.LENGTH_SHORT).show();
                                help = false;
                                break;
                            }
                        }
                        if (help == true) {
                            if (snapshot.hasChild(UserNameDial)) {
                                Toast.makeText(LoginActivity.this, "Username is already used", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("Users").child(UserNameDial).child("UserName").setValue(UserNameDial);
                                databaseReference.child("Users").child(UserNameDial).child("Email").setValue(EmailDial);
                                databaseReference.child("Users").child(UserNameDial).child("Password").setValue(PasswordDial);
                                Toast.makeText(LoginActivity.this, "User registered successfully, now login", Toast.LENGTH_SHORT).show();
                                d.dismiss();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        }

        if (view == btncandialog) {
            Toast.makeText(this, "Registration canceled", Toast.LENGTH_SHORT).show();
            d.dismiss();
        }
    }
}
