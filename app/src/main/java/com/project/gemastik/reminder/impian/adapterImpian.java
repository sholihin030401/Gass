package com.project.gemastik.reminder.impian;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.gemastik.reminder.R;

import java.util.List;

public class adapterImpian extends RecyclerView.Adapter<adapterImpian.MyHolder> {

    Context context;
    List<dataImpian> agendas;

    public adapterImpian(Context context, List<dataImpian> agendas) {
        this.context = context;
        this.agendas = agendas;
    }

    @NonNull
    @Override
    public adapterImpian.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.add_impian_item, parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterImpian.MyHolder holder, int position) {

        final String judul = agendas.get(position).getJudul();
        final String waktu = agendas.get(position).getWaktu();
        final String timeStamp = agendas.get(position).getTimeStamp();

        holder.mJudul.setText(judul);
        holder.mWaktu.setText(waktu);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,detailHabits.class);
                intent.putExtra("timeStamp",timeStamp);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return agendas.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView mJudul;
        TextView mWaktu;
        LinearLayout linearLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mJudul = itemView.findViewById(R.id.impian);
            mWaktu = itemView.findViewById(R.id.waktuImpian);
            linearLayout =(LinearLayout) itemView.findViewById(R.id.linearImpian );
        }
    }
}