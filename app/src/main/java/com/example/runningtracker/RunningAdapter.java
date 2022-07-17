package com.example.runningtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RunningAdapter extends RecyclerView.Adapter<RunningAdapter.RunningViewHolder> {

    private List<Running> data;
    private final LayoutInflater layoutInflater;

    public RunningAdapter(Context context) {
        this.data = new ArrayList<>();
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RunningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.db_layout_view, parent, false);
        return new RunningViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(RunningViewHolder holder, int position) {
        holder.bind(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Running> newData) {
        if (data != null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;
        }
    }

    static class RunningViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView timeStart;
        TextView distanceView;
        TextView timeView;
        TextView infoView;
        ImageView imageView;

        RunningViewHolder(View itemView) {
            super(itemView);

            timeStart = itemView.findViewById(R.id.adapDateTimeView);
            distanceView = itemView.findViewById(R.id.adapDistanceView);
            timeView = itemView.findViewById(R.id.adapDurationView);
            infoView = itemView.findViewById(R.id.adapNoteView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }
        private String startTimeText;
        private String idText;
        private String timeText;
        private String distanceText;
        @SuppressLint("SetTextI18n")
        void bind(final Running running) {

            if (running != null) {
                startTimeText = String.valueOf(running.getStartTime());
                idText = String.valueOf(running.getId());
                timeText = String.valueOf(running.getTime());
                distanceText = String.valueOf(running.getDistance());
                timeStart.setText(String.valueOf(running.getStartTime()));
                distanceView.setText(running.getDistance()+"km");
                timeView.setText(String.valueOf(running.getTime()));
                infoView.setText(String.valueOf(running.getInfo()));
                imageView.setImageDrawable(null);
                if(running.getUri()!= null){
                    imageView.setImageURI(Uri.parse(running.getUri()));
                }
            }
        }

//        onClick method to start intetn to UpdateActivity
        @Override
        public void onClick(View v) {
            Intent intent = new Intent (v.getContext(), UpdateActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", idText);
            bundle.putString("timeStart", startTimeText);
            bundle.putString("distance", distanceText);
            bundle.putString("time", timeText);
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);

        }



    }
}
