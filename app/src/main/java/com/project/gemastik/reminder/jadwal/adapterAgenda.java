package com.project.gemastik.reminder.jadwal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.gemastik.reminder.R;

import java.util.List;

public class adapterAgenda extends RecyclerView.Adapter<adapterAgenda.Myholder> {

    Context context;
    List<dataAgenda> agendas;

    public adapterAgenda(Context context, List<dataAgenda> agendas) {
        this.context = context;
        this.agendas = agendas;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.agenda_item, parent,false);

        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myholder holder, int position) {

        final String judul = agendas.get(position).getJudul();
        final String waktu = agendas.get(position).getWaktu();
        final String timeStamp = agendas.get(position).getTimeStamp();

        holder.mJudul.setText(judul);
        holder.mWaktu.setText(waktu);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option[] = {"Edit Agenda","Hapus Agenda"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Choose Action");
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1) {

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference().child("Agenda");
                            reference.child(timeStamp).removeValue();
                            Toast.makeText(context, "Agenda Berhasil Dihapus", Toast.LENGTH_LONG).show();


                        }else if (which == 0){
                            Intent intent = new Intent(context,UpdateAgenda.class);
                            intent.putExtra("timeStamp",timeStamp);
                            context.startActivity(intent);

                        }
                    }
                });
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return agendas.size();
    }

    class Myholder extends RecyclerView.ViewHolder{

        TextView mJudul;
        TextView mWaktu;
        LinearLayout linearLayout;


        public Myholder(@NonNull View itemView) {
            super(itemView);

            mJudul = itemView.findViewById(R.id.judul);
            mWaktu = itemView.findViewById(R.id.waktu);
            linearLayout =(LinearLayout) itemView.findViewById(R.id.linearAgenda);
        }
    }
}