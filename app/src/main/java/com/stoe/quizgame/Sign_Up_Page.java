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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_Up_Page extends AppCompatActivity {

    EditText mail, password;
    Button signUp;
    ProgressBar progressBar;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mail = findViewById(R.id.editTextSignUpMail);
        password = findViewById(R.id.editTextSignUpPassword);
        signUp = findViewById(R.id.buttonSignUp);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp.setClickable(false);  //ca sa poata sa creeze un singur profil, sa nu imi umple mie bd

                String userEmail = mail.getText().toString();
                String userPassword = password.getText().toString();
                signUpFirebase(userEmail, userPassword);
            }
        });

    }

    public void signUpFirebase(String userEmail, String userPassword){

        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(userEmail, userPassword)  //asta e tot hazul ca sa faca un cont in firebase
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Your account is created", Toast.LENGTH_SHORT).show();
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}