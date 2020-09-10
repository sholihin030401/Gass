package com.project.gemastik.reminder.jadwal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.project.gemastik.reminder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class UpdateAgenda extends AppCompatActivity {

    EditText nama_agenda;
    ImageButton btn_time;
    TextView tx_time;
    CardView btn_simpan;
    String tipe, personEmail;
    SwitchDateTimeDialogFragment dateTimeDialogFragment;

    private FirebaseAuth mAuth;
    FirebaseUser mUser;

    RadioGroup radioGroup;
    RadioButton alarm, notif;

    private static final String TAG_DATETIME = "TAG_DATETIME";
    private static final String TAG = "DateTimePicker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_agenda);

        nama_agenda = findViewById(R.id.judul_agenda);
        tx_time = findViewById(R.id.mulai_agenda);
        btn_time = findViewById(R.id.btn_mulai_agenda);
        final Spinner ulangi = findViewById(R.id.spin_agenda);
        btn_simpan = findViewById(R.id.btnupdate_agenda);

        radioGroup = findViewById(R.id.radiogrup_agenda);
        alarm = findViewById(R.id.alarm_agenda);
        notif = findViewById(R.id.notif_agenda);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Intent intent = getIntent();
        final String timeStamp = intent.getStringExtra("timeStamp");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Agenda");
        Query query =reference.orderByChild("timeStamp").equalTo(timeStamp);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String judul = "" + ds.child("judul").getValue();
                    String email = "" + ds.child("email").getValue();
                    String waktu = "" + ds.child("waktu").getValue();
                    String tipe = "" + ds.child("tipe").getValue();
                    String kulangi = "" + ds.child("ulangi").getValue();

                    final int UlangiKode;

                    if (kulangi.equals("Satu Kali")){
                        UlangiKode = 0;
                    }else if (kulangi.equals("Setiap Hari")){
                        UlangiKode = 1;
                    }else if (kulangi.equals("Setiap Pekan")){
                        UlangiKode = 2;
                    }else {
                        UlangiKode = 3;
                    }

                    nama_agenda.setText(judul);
                    tx_time.setText(waktu);
                    ulangi.setSelection(UlangiKode);
                    if (tipe.equals("Alarm")){
                        alarm.setChecked(true);
                    }else {
                        notif.setChecked(true);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateAgenda.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



        dateTimePicker();

        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialogFragment.startAtCalendarView();
                dateTimeDialogFragment.show(getSupportFragmentManager(),TAG_DATETIME);
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(UpdateAgenda.this);
                if (acct != null) {
                    personEmail = acct.getEmail();

                }else {
                    personEmail = mUser.getEmail();
                }

                tipePengingat();

                String judul = nama_agenda.getText().toString().trim();
                String waktu = tx_time.getText().toString().trim();
                String Ulangi =ulangi.getSelectedItem().toString();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("email",personEmail);
                hashMap.put("judul", judul);
                hashMap.put("waktu", waktu);
                hashMap.put("tipe", tipe);
                hashMap.put("ulangi",Ulangi);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("Agenda");

                databaseReference.child(timeStamp).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateAgenda.this, "Agenda Berhasil Diupdate", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateAgenda.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }

    private void tipePengingat() {
        if (alarm.isChecked()){
            tipe = "Alarm";
        }else {
            tipe = "Notifikasi";
        }
    }

    private void dateTimePicker(){
        dateTimeDialogFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME);
        if (dateTimeDialogFragment == null){
            dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.mulai_agenda),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel)
            );
        }

        dateTimeDialogFragment.setTimeZone(TimeZone.getDefault());

        final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm", java.util.Locale.getDefault());
        dateTimeDialogFragment.setHighlightAMPMSelection(false);
        dateTimeDialogFragment.set24HoursMode(true);
        dateTimeDialogFragment.setMinimumDateTime(new GregorianCalendar(2016, Calendar.JANUARY,1).getTime());
        dateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2045, Calendar.DECEMBER,31).getTime());

        try {
            dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMM", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e){
            Log.e(TAG, e.getMessage());
        }

        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                tx_time.setText(dateFormat.format(date));
            }

            @Override
            public void onNegativeButtonClick(Date date) {

            }
        });
    }
}