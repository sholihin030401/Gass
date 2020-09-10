package com.project.gemastik.reminder.verify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.project.gemastik.reminder.MainActivity;
import com.project.gemastik.reminder.R;
import com.subhrajyoti.passwordview.PasswordView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username, email;
    PasswordView pw, cpw;
    CardView signin, googleSign;

    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.usersign);
        email = findViewById(R.id.mailsign);
        pw = findViewById(R.id.pwsign);
        cpw = findViewById(R.id.cpwsign);

        mAuth = FirebaseAuth.getInstance();

        signin = findViewById(R.id.btnsignup);
        signin.setOnClickListener(this);

        googleSign = findViewById(R.id.btnsignup_google);
        createRequest();
        googleSign.setOnClickListener(this);
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }

    private void signIn(){
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUpActivity.this,"Autentikasi gagal",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void registUser(){
        String txuser, txmail, txpass, txcpass;

        txuser = username.getText().toString();
        txmail = email.getText().toString();
        txpass = pw.getText().toString();
        txcpass = cpw.getText().toString();


        if (TextUtils.isEmpty(txuser)){
            Toast.makeText(getApplicationContext(),"Username tidak boleh kosong!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(txmail)){
            Toast.makeText(getApplicationContext(),"Email tidak boleh kosong!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(txpass)){
            Toast.makeText(getApplicationContext(),"Password tidak boleh kosong!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(txcpass)){
            Toast.makeText(getApplicationContext(),"Konfirmasi Password tidak boleh kosong!",Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(txmail, txpass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intentup = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intentup);
                            Toast.makeText(SignUpActivity.this,"Berhasil!",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this,"Gagal membuat akun",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnsignup:
                registUser();
                break;
            case R.id.btnsignup_google:
                signIn();
                break;
        }
    }
}
