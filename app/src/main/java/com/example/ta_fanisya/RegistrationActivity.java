package com.example.ta_fanisya;

import static com.example.ta_fanisya.LoginActivity.user;
import static com.example.ta_fanisya.LoginActivity.userName;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView, pwsCheck, regisTV;
    private TextView logintxt;
    private Button Btn;
    private ProgressBar progressbar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.regis_email);
        passwordTextView = findViewById(R.id.passwd);
        pwsCheck = findViewById(R.id.conf_pwd);
        Btn = findViewById(R.id.btnregister);
        progressbar = findViewById(R.id.progressbar);
        logintxt = findViewById(R.id.txtLogin);
        regisTV = findViewById(R.id.regis_name);


        logintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        // Set on Click Listener on Registration button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Write a message to the database
                registerNewUser();
            }
        });
    }

    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password,conPwd;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        conPwd = pwsCheck.getText().toString();
        userName = regisTV.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Silahkan isi email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Silahkan isi password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if(password.length()< 6){
            Toast.makeText(
                            getApplicationContext(),
                            "Password terlalu pendek",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if(!password.equals(conPwd)){
            Toast.makeText(
                            getApplicationContext(),
                            "Password tidak cocok",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        user = email.substring(0,5);
        int saldo = 100000;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user").child(user).child("name");
        myRef.setValue(userName);
        myRef = database.getReference("user").child(user).child("saldo");
        myRef.setValue(saldo);

        // create new user or register new user
         mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Registrasi Berhasil!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);

                            // if the user created intent to login activity
                            Intent i = new Intent(RegistrationActivity.this,
                                    LoginActivity.class);

                                startActivity(i);


                        }
                        else {

                            // Registration failed
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Registrasi Gagal!!"
                                            + " Silahkan Coba lagi",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}