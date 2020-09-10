package com.project.gemastik.reminder.impian;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.gemastik.reminder.R;

import java.util.ArrayList;
import java.util.List;

public class detailHabits extends AppCompatActivity {

    List<dataDetailImpian> list;
    adapterDetailImpian adapter;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private DatabaseReference mreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_habits);

        mRecycler = findViewById(R.id.rv_detail);
        mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        Intent intent = getIntent();
        String timeStamp = intent.getStringExtra("timeStamp");

        mreference = FirebaseDatabase.getInstance().getReference().child("DetailImpian");
        Query query = mreference.orderByChild("timeStamp").equalTo(timeStamp);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    dataDetailImpian data = ds.getValue(dataDetailImpian.class);
                    list.add(data);

                }
                adapter = new adapterDetailImpian(detailHabits.this,list);
                mRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(detailHabits.this,""+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}