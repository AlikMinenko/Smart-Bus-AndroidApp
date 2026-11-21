package com.example.busproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busproject.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.busproject.LoginActivity.SHARED_PREFERENCE;



public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ImageButton btnleftmenu;

    TextView tvmenuname,tvmenuemail;

    int helper=0;

    Dialog d;
    EditText etpasnew,etpasconnew;
    Button btnsave;

    DatabaseReference databaseReference = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl("https://fir-for-esp32-de10-default-rtdb.europe-west1.firebasedatabase.app/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        replaceFragments(new LinesFragment(),0,0);

        btnleftmenu=findViewById(R.id.navbtn);

        binding.bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.lines && helper!=0) {
                    replaceFragments(new LinesFragment(),R.anim.slide_in_left,R.anim.slide_out_right);
                    helper=0;
                }
                if (item.getItemId() == R.id.tickets && helper!=1) {
                    replaceFragments(new TicketsFragment(),R.anim.slide_in_right,R.anim.slide_out_left);
                    helper=1;
                }
                return true;
            }
        });


        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.leftnav);
        drawerToggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        drawerToggle.syncState();

        if(User.getUserName().equals("admin")){
            navigationView.getMenu().findItem(R.id.addline).setVisible(true);
            navigationView.getMenu().findItem(R.id.devtest).setVisible(true);
            navigationView.getMenu().findItem(R.id.addbus).setVisible(true);
            databaseReference.child("Users").child("admin").child("Mode").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(!snapshot.getValue(boolean.class)){
                        Toast.makeText(MainActivity.this, "You must change the current password before proceeding", Toast.LENGTH_LONG).show();
                        CreatePassWordDialog(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }
        else{
            navigationView.getMenu().findItem(R.id.addline).setVisible(false);
            navigationView.getMenu().findItem(R.id.devtest).setVisible(false);
            navigationView.getMenu().findItem(R.id.addbus).setVisible(false);
        }

        View header = navigationView.getHeaderView(0);
        tvmenuname=header.findViewById(R.id.usernametv);
        tvmenuemail=header.findViewById(R.id.emailtv);
        tvmenuname.setText(User.getUserName());
        tvmenuemail.setText(User.getEmail());

        btnleftmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(item.getItemId() == R.id.addline){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent toAddRow = new Intent(MainActivity.this,AddLine.class);
                    startActivity(toAddRow);
                }
                else if(item.getItemId() == R.id.devtest){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(MainActivity.this,DevTest.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.passchange){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    CreatePassWordDialog(true);
                }
                else if(item.getItemId() == R.id.addbus){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(MainActivity.this,AddBus.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.exit){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Log.d("Email", "click");
                    SharedPreferences sp = MainActivity.this.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putBoolean("loggedin",false);
                    editor.putString("username","");
                    editor.putString("email","");
                    editor.putString("password","");
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

    }

    public void CreatePassWordDialog(boolean x){
        d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.change_password_dialog);
        d.setCancelable(x);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        d.getWindow().setLayout((6*width)/7, (3 * height) / 5);

        etpasnew=d.findViewById(R.id.dialnewpw);
        etpasconnew=d.findViewById(R.id.dialnewpwcon);
        btnsave=d.findViewById(R.id.savebtn);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newpass=etpasnew.getText().toString();
                String connewpass=etpasconnew.getText().toString();
                if(newpass.isEmpty() || connewpass.isEmpty())
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                else if(!newpass.equals(connewpass))
                    Toast.makeText(MainActivity.this, "Both passwords are not the same", Toast.LENGTH_SHORT).show();
                else{
                    databaseReference.child("Users").child(User.getUserName()).child("Password").setValue(newpass);
                    if(User.getUserName().equals("admin")){
                        databaseReference.child("Users").child(User.getUserName()).child("Mode").setValue(true);
                    }
                    d.dismiss();
                }
            }
        });

        d.show();
    }

    private void replaceFragments(Fragment fragment, int anim1,int anim2){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(anim1,anim2);
        fragmentTransaction.replace(R.id.mainframe,fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}