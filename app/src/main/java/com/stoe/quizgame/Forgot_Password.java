package com.stoe.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    EditText mail;
    Button button;
    ProgressBar progressBar;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mail = findViewById(R.id.editTextEmailPassword);
        button = findViewById(R.id.buttonContinue);
        progressBar = findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = mail.getText().toString();
                resetPassword(userEmail);
            }
        });

    }

    public void resetPassword(String userEmail){
        progressBar.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "We sent an email to your address.", Toast.LENGTH_SHORT).show();
                    button.setClickable(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}