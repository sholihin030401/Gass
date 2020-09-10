package com.project.gemastik.reminder.impian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.gemastik.reminder.R;

import java.util.List;

public class adapterDetailImpian extends RecyclerView.Adapter<adapterDetailImpian.MyHolder> {

    Context context;
    List<dataDetailImpian> histori;

    public adapterDetailImpian(Context context, List<dataDetailImpian> histori) {
        this.context = context;
        this.histori = histori;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_impian_item, parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String judul = histori.get(position).getMisi();
        String timeStamp = histori.get(position).getTimeStamp();

        holder.mJudul.setText(judul);


    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView mJudul;
        public LinearLayout linearLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mJudul = itemView.findViewById(R.id.detail);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearDetail);
        }
    }
}