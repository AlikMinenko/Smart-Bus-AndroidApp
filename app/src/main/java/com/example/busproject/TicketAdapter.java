package com.example.busproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private Context context;
    private ArrayList<Ticket> ticketlist;
    private RecyclerViewClickListener listener;

    public TicketAdapter(Context context, ArrayList<Ticket> ticketlist,RecyclerViewClickListener listener) {
        this.context = context;
        this.ticketlist = ticketlist;
        this.listener=listener;
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_ticket_layout,parent,false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketAdapter.TicketViewHolder holder, int position) {
        Ticket ticket = ticketlist.get(position);
        holder.tvline.setText(ticket.getLinenum());
    }

    @Override
    public int getItemCount() {
        return ticketlist.size();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvline;
        public TicketViewHolder(View itemView) {
            super(itemView);
            tvline = itemView.findViewById(R.id.numtv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAbsoluteAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v,int position);
    }
}
