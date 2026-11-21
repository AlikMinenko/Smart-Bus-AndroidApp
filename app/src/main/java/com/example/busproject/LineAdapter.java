package com.example.busproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.LineViewHolder> {
    private Context context;
    private ArrayList<Line> linelist;
    private RecyclerViewClickListener listener;

    public LineAdapter(Context context, ArrayList<Line> linelist,RecyclerViewClickListener listener) {
        this.context = context;
        this.linelist = linelist;
        this.listener=listener;
    }

    @Override
    public LineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_layout,parent,false);
        return new LineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LineAdapter.LineViewHolder holder, int position) {
        Line line = linelist.get(position);
        holder.tvline.setText(line.getNumber());
        holder.tvdes.setText(line.getDescription());
    }

    @Override
    public int getItemCount() {
        return linelist.size();
    }

    public class LineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvline, tvdes;
        public LineViewHolder(View itemView) {
            super(itemView);
            tvline = itemView.findViewById(R.id.numtv);
            tvdes = itemView.findViewById(R.id.destv);
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
