package com.project.gemastik.reminder.impian;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.project.gemastik.reminder.R;
import com.project.gemastik.reminder.cnbfragment.ImpianFragment;
import com.project.gemastik.reminder.jadwal.AddJadwalActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class AddImpianActivity extends AppCompatActivity {

    LinearLayout layoutList;
    TextView addList;
    CardView btnSimpan;

    EditText namaImpian;
    ImageButton btn_time;
    TextView tx_time;
    String personEmail, time;
    SwitchDateTimeDialogFragment dateTimeDialogFragment;
    private static final String TAG_DATETIME = "TAG_DATETIME";
    private static final String TAG = "DateTimePicker";

    private FirebaseAuth mAuth;
    FirebaseUser mUser;

    int tambah = 0;

    ArrayList<HabitsItem> impianItems = new ArrayList<HabitsItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_impian);

        layoutList = findViewById(R.id.layout_list);
        addList = findViewById(R.id.btnTambahList);
        btnSimpan = findViewById(R.id.btnsimpan_impian);

        namaImpian = findViewById(R.id.nama_impian);
        tx_time = findViewById(R.id.mulai_impian);
        btn_time = findViewById(R.id.btn_mulai_impian);

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

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addListHabits();
            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNullData()){
//                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    Fragment aktifFragment = new ImpianFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("Data",impianItems);
//                    aktifFragment.setArguments(bundle);
//                    ft.replace(R.id.impian_container,aktifFragment);
//                    ft.addToBackStack(null);
//                    ft.commit();
                    Toast.makeText(AddImpianActivity.this, "Impian Berhasil Disimpan", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
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

    private boolean checkNullData(){
        impianItems.clear();
        boolean result = true;

        for(int i=0;i<layoutList.getChildCount();i++){

            View cricketerView = layoutList.getChildAt(i);

            EditText editTextName = (EditText)cricketerView.findViewById(R.id.edt_habits);

            HabitsItem item = new HabitsItem();

            String timeStamp = String.valueOf(System.currentTimeMillis());

            HashMap<Object, String> hashMap = new HashMap<>();
            hashMap.put("timeStamp",time);
            hashMap.put("email",personEmail);
            hashMap.put("misi",editTextName.getText().toString());

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference("DetailImpian");

            databaseReference.child(timeStamp).setValue(hashMap);


            if(!editTextName.getText().toString().equals("")){
                item.setTextHabits(editTextName.getText().toString());
            }else {
                result = false;
                break;
            }

            impianItems.add(item);

        }

        if (impianItems.size()==0){
            result = false;
            Toast.makeText(this,"Tambahkan data!",Toast.LENGTH_SHORT).show();
        } else if (!result){
            Toast.makeText(this,"Masukkan Semua Data!",Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    private void addListHabits(){
        final View habitsView = getLayoutInflater().inflate(R.layout.add_layout_habits,null,false);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(AddImpianActivity.this);
        if (acct != null) {
            personEmail = acct.getEmail();

        }else {
            personEmail = mUser.getEmail();
        }

        String judul = namaImpian.getText().toString().trim();
        String waktu = tx_time.getText().toString().trim();
        String timeStamp = String.valueOf(System.currentTimeMillis());

        tambah += 1;

        if (tambah==1){
            HashMap<Object, String> hashMap = new HashMap<>();
            hashMap.put("email",personEmail);
            hashMap.put("judul", judul);
            hashMap.put("waktu", waktu);
            hashMap.put("timeStamp",timeStamp);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference("Impian");

            databaseReference.child(timeStamp).setValue(hashMap);
            time = timeStamp;
        }

        EditText editText = habitsView.findViewById(R.id.edt_habits);

        ImageButton imageButton = habitsView.findViewById(R.id.close_habits);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(habitsView);
            }
        });

        layoutList.addView(habitsView);
    }

    private void removeView(View view){
        layoutList.removeView(view);
    }
}