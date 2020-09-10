package com.project.gemastik.reminder.cnbfragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.gemastik.reminder.R;
import com.project.gemastik.reminder.impian.AddImpianActivity;
import com.project.gemastik.reminder.impian.adapterImpian;
import com.project.gemastik.reminder.impian.dataImpian;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImpianFragment extends Fragment {

    String personEmail;
    FirebaseUser mUser;
    private DatabaseReference reference;


    List<dataImpian> list;
    adapterImpian adapter;

    RecyclerView rv_impian;
    public ImpianFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewFrag = inflater.inflate(R.layout.fragment_impian, container, false);

        FloatingActionButton fab = viewFrag.findViewById(R.id.fabAddImpian);
//        btn_goals = viewFrag.findViewById(R.id.ask_goals);

        rv_impian = viewFrag.findViewById(R.id.rv_impian);
        rv_impian.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        rv_impian.setLayoutManager(manager);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            personEmail = acct.getEmail();

        }else {
            personEmail = mUser.getEmail();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddImpianActivity.class);
                startActivity(intent);
            }
        });



        reference = FirebaseDatabase.getInstance().getReference().child("Impian");
        Query query = reference.orderByChild("email").equalTo(personEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    dataImpian dataku = ds.getValue(dataImpian.class);
                    list.add(dataku);

                }
                adapter = new adapterImpian(getActivity(),list);
                rv_impian.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(),""+databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        return viewFrag;
    }

}