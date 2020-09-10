package com.project.gemastik.reminder.jadwal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.project.gemastik.reminder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AddJadwalActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_add_jadwal);

        nama_agenda = findViewById(R.id.judul_agenda);
        tx_time = findViewById(R.id.mulai_agenda);
        btn_time = findViewById(R.id.btn_mulai_agenda);
        final Spinner ulangi = findViewById(R.id.spin_agenda);
        btn_simpan = findViewById(R.id.btnsimpan_agenda);

        radioGroup = findViewById(R.id.radiogrup_agenda);
        alarm = findViewById(R.id.alarm_agenda);
        notif = findViewById(R.id.notif_agenda);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

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
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(AddJadwalActivity.this);
                if (acct != null) {
                    personEmail = acct.getEmail();

                }else {
                    personEmail = mUser.getEmail();
                }

                tipePengingat();

                String judul = nama_agenda.getText().toString().trim();
                String waktu = tx_time.getText().toString().trim();
                String Ulangi =ulangi.getSelectedItem().toString();
                String timeStamp = String.valueOf(System.currentTimeMillis());

                HashMap<Object, String> hashMap = new HashMap<>();
                hashMap.put("email",personEmail);
                hashMap.put("judul", judul);
                hashMap.put("waktu", waktu);
                hashMap.put("tipe", tipe);
                hashMap.put("ulangi",Ulangi);
                hashMap.put("timeStamp",timeStamp);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("Agenda");

                databaseReference.child(timeStamp).setValue(hashMap);
                Toast.makeText(AddJadwalActivity.this, "Agenda Berhasil Disimpan", Toast.LENGTH_LONG).show();
                finish();

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