package com.example.busproject;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.RenderMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import pl.droidsonroids.gif.GifImageView;
import pl.droidsonroids.gif.GifTextureView;

import static java.lang.Integer.parseInt;


public class TicketsFragment extends Fragment implements View.OnClickListener {

    ImageButton btnqr;
    String busid = "";

    RecyclerView recylines;
    ArrayList<Ticket> ticketlist;
    TicketAdapter ticketAdapter;
    TicketAdapter.RecyclerViewClickListener listener;

    Dialog d;
    ImageView place1, place2, place3, place4, place5, place6, place7, place8, place9, place10, place11, place12, place13, place14, place15;
    ImageView[] imgplaces;
    TextView tvfirstst, tvlastst;
    Button btnlineinfo;
    int ticketpos;

    boolean hasticket;
    int helper;

    Dialog d2;
    LottieAnimationView qrlottie,xlottie;

    GifTextureView giftickets;
    TextView tvnotic1,tvnotic2;


    DatabaseReference databaseReference = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl("https://fir-for-esp32-de10-default-rtdb.europe-west1.firebasedatabase.app/");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);

        recylines = view.findViewById(R.id.ticketsrecy);
        recylines.setHasFixedSize(true);
        recylines.setLayoutManager(new LinearLayoutManager(getActivity()));
        recylines.setItemAnimator(new DefaultItemAnimator());
        setOnItemClickListener();

        DisplayMetrics metrics = getResources().getDisplayMetrics(); ///change later (recycleview height change)
        int width = metrics.widthPixels;                             //
        int height = metrics.heightPixels;                           //
        ViewGroup.LayoutParams params = recylines.getLayoutParams(); //
        params.height = (int) ((4.5 * height) / 7);                  //
        recylines.setLayoutParams(params);                           //

        giftickets=view.findViewById(R.id.ticketsgif);
        tvnotic1=view.findViewById(R.id.notic1);
        tvnotic2=view.findViewById(R.id.notic2);

        ticketlist = new ArrayList<>();
        ticketAdapter = new TicketAdapter(getActivity(), ticketlist, listener);
        recylines.setAdapter(ticketAdapter);

        databaseReference.child("Users").child(User.getUserName()).child("Tickets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ticketlist.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ticketlist.add(ds.getValue(Ticket.class));
                }
                ticketAdapter.notifyDataSetChanged();
                if(ticketlist.size()!=0 || User.getUserName().equals("admin")){
                    giftickets.setVisibility(View.GONE);
                    tvnotic1.setVisibility(View.GONE);
                    tvnotic2.setVisibility(View.GONE);
                    btnqr.setVisibility(View.VISIBLE);
                    recylines.setVisibility(View.VISIBLE);
                }
                else
                {
                    giftickets.setVisibility(View.VISIBLE);
                    tvnotic1.setVisibility(View.VISIBLE);
                    tvnotic2.setVisibility(View.VISIBLE);
                    btnqr.setVisibility(View.GONE);
                    recylines.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        btnqr = view.findViewById(R.id.qrbtn);
        btnqr.setOnClickListener(this);

        return view;
    }


    public void TicketDialog(int position) {
        d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.ticket_dialog);
        d.setCancelable(true);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        d.getWindow().setLayout((18*width)/19, (15*height)/16);

        tvfirstst = d.findViewById(R.id.firststtv);
        tvlastst = d.findViewById(R.id.laststtv);
        btnlineinfo = d.findViewById(R.id.lineinfobtn);
        btnlineinfo.setOnClickListener(this);

        tvfirstst.setText("Your pick-up station:" + ticketlist.get(position).getFirststation());
        tvlastst.setText("Your drop-off station:" + ticketlist.get(position).getLaststation());


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
        }

        imgplaces = new ImageView[]{place1, place2, place3, place4, place5,
                place6, place7, place8, place9, place10, place11, place12, place13, place14, place15};

        String currentbus=ticketlist.get(position).getBus();
        databaseReference.child("Buses").child(currentbus).child("places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(d.isShowing()){
                    ArrayList<Boolean> busplaces = new ArrayList<Boolean>();
                    busplaces= (ArrayList<Boolean>) snapshot.getValue();
                    ArrayList<String> userpos = ticketlist.get(position).getSitpositions();
                    for (int i = 0; i < 15; i++) {
                        if (busplaces.get(i) && i != 14) {
                            imgplaces[i].setBackgroundResource(R.drawable.chair_red);
                        } else if (busplaces.get(i) && i == 14) {
                            imgplaces[i].setBackgroundResource(R.drawable.chair_red_dis);
                        } else if (!busplaces.get(i) && i == 14) {
                            imgplaces[i].setBackgroundResource(R.drawable.chair_blue_dis);
                        } else {
                            imgplaces[i].setBackgroundResource(R.drawable.chair_blue);
                        }
                    }
                    for (int j = 0; j < userpos.size(); j++) {
                        int urplace = parseInt(userpos.get(j));
                        if (urplace == 14) {
                            imgplaces[parseInt(userpos.get(j))].setBackgroundResource(R.drawable.chair_orange_dis);
                        } else
                            imgplaces[parseInt(userpos.get(j))].setBackgroundResource(R.drawable.chair_orange);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        d.show();
    }

    public void QrAnimDialog(boolean worked) {
        d2 = new Dialog(getActivity());
        d2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d2.setContentView(R.layout.qranim_dialog);
        d2.setCancelable(false);
        d2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        qrlottie = d2.findViewById(R.id.qranim);
        xlottie = d2.findViewById(R.id.xanim);
        qrlottie.setRenderMode(RenderMode.AUTOMATIC);
        xlottie.setRenderMode(RenderMode.AUTOMATIC);
        if(worked){
            qrlottie.setAnimation(R.raw.qr);
            xlottie.setVisibility(View.INVISIBLE);
            qrlottie.setMinAndMaxProgress(0f,1f);
            qrlottie.playAnimation();
        }
        else {
            qrlottie.setAnimation(R.raw.qrred);
            qrlottie.setMinAndMaxProgress(0f,0.72f);
            qrlottie.playAnimation();
            qrlottie.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    Log.e("Animation:","start");
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    xlottie.setVisibility(View.VISIBLE);
                    xlottie.setMinAndMaxProgress(0.69f,1f);
                    xlottie.playAnimation();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    Log.e("Animation:","cancel");
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    Log.e("Animation:","repeat");
                }
            });
        }



        d2.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                d2.dismiss();
            }
        },2700);
    }


    private void setOnItemClickListener() {
        listener = new TicketAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                TicketDialog(position);
                ticketpos = position;
            }
        };
    }

    @Override
    public void onClick(View view) {
        if (view == btnqr) {
            if(ticketlist.size() == 0 && !User.getUserName().equals("admin")){
                Toast.makeText(getActivity(), "You don't have any tickets", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(getActivity(), QrActivity.class);
                startActivityForResult(intent, 1);
            }
        }
        if (view == btnlineinfo) {
            Intent toLineInfo = new Intent(getActivity(), LineInfo.class);
            databaseReference.child("Lines")
                    .child(ticketlist.get(ticketpos).getLinenum()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.d("Email", ticketlist.get(ticketpos).getLinenum());
                    Line line = snapshot.getValue(Line.class);
                    toLineInfo.putExtra("LineNum", line.getNumber());
                    toLineInfo.putExtra("LineDesc", line.getDescription());
                    toLineInfo.putExtra("Stations", line.getStations());
                    toLineInfo.putExtra("Bus", line.getBus());
                    toLineInfo.putExtra("LineDepTime", line.getDeptime());
                    toLineInfo.putExtra("LineTotalTime", line.getTotaltime());
                    toLineInfo.putExtra("LineEndTime", line.getEndtime());
                    toLineInfo.putExtra("LinePrice", line.getPrices());
                    Log.d("Email", line.getDescription());
                    startActivity(toLineInfo);
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    d.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {

                Log.d("Email", "YES1");
                if (!String.valueOf(data.getExtras().getString("BusID")).equals("null"))
                    busid = data.getExtras().getString("BusID");

                if (User.getUserName().equals("admin")) {
                    if (busid.equals("1bus1")) {
                        databaseReference.child("Buses").child("1bus1").child("functions").setValue(5);
                        QrAnimDialog(true);
                    }
                    else
                        QrAnimDialog(false);
                } else {

                    hasticket = false;
                    for (int i = 0; i < ticketlist.size(); i++) {
                        if (ticketlist.get(i).getBus().equals(busid)) {
                            helper = i;
                            hasticket = true;
                            break;
                        }
                    }

                    String upstation = ticketlist.get(helper).getFirststation();
                    Boolean allowed = ticketlist.get(helper).getAllowed();
                    databaseReference.child("Buses").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            ArrayList<String> busesids = new ArrayList<String>();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                busesids.add(ds.getKey());
                            }
                            if (busesids.contains(busid)) {
                                String cst = snapshot.child(busid).child("currentST").getValue().toString();
                                Log.d("Email", upstation);
                                for (int i = 0; i < 25; i++) {
                                    if (hasticket == true && upstation.equals(cst) && !snapshot.child(busid).child("goingdown").getValue(Boolean.class) && allowed == true) {
                                        databaseReference.child("Buses").child(busid).child("functions").setValue(5);
                                        databaseReference.child("Buses").child(busid).child("currentUser").setValue(User.getUserName());
                                        databaseReference.child("Users").child(User.getUserName()).child("Tickets").child(ticketlist.get(helper).getLinenum()).child("allowed").setValue(false);
                                        QrAnimDialog(true);
                                        break;
                                    }
                                    else{
                                        QrAnimDialog(false);
                                        break;
                                    }
                                }
                            }
                            else
                                QrAnimDialog(false);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }
            }
        }
    }
}