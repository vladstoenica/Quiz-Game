package com.stoe.quizgame;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login_Page extends AppCompatActivity {

    EditText mail, password;
    Button signIn;
    TextView signUp, forgotPassword;
    SignInButton signInGoogle;
    ProgressBar progressBarSignIn;

    GoogleSignInClient googleSignInClient;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //register activityResultLauncher
        registerActivityForGoogleSignIn();

        mail = findViewById(R.id.editTextLoginEmail);
        password = findViewById(R.id.editTextLoginPassword);
        signIn = findViewById(R.id.buttonLoginSignIn);
        signInGoogle = findViewById(R.id.buttonLoginGoogleSignIn);
        signUp = findViewById(R.id.textViewLoginSignUp);
        forgotPassword = findViewById(R.id.textViewLoginForgotPassword);
        progressBarSignIn = findViewById(R.id.progressBarSignIn);

        progressBarSignIn.setVisibility(View.INVISIBLE);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = mail.getText().toString();
                String userPassword = password.getText().toString();
                signInWithFirebase(userEmail, userPassword);
            }
        });

        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login_Page.this,Sign_Up_Page.class);
                startActivity(i);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login_Page.this, Forgot_Password.class);
                startActivity(i);

            }
        });
    }

    //pt registering activityResultLauncher
    public void registerActivityForGoogleSignIn(){
        activityResultLauncher = registerForActivityResult((new ActivityResultContracts.StartActivityForResult())
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultCode == RESULT_OK && data != null){
                            //cream un thread
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            firebaseSignInWithGoogle(task);
                        }
                    }
                });
    }

    //pt deasupra  -  aici punem ce se intampla cand dam click pe butonul de sign in with google
    private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task){
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Login_Page.this, MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account);
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //if a mers, else ce sa faca pt google
    private void firebaseGoogleAccount(GoogleSignInAccount account){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        //id token-ul de deasupra ne zice id-ul device-ului care se logheaza
        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();   //the logged in user is signed into the user obj

                        }else{

                        }
                    }
                });
    }

    //pt login cu mail
    public void signInWithFirebase(String email, String password){
        progressBarSignIn.setVisibility(View.VISIBLE);
        signIn.setClickable(false);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                            progressBarSignIn.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Email or password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //pt google
    public void signInGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("993368829827-p70ge0eulkmnt9l0n14prtab77aftog8.apps.googleusercontent.com")
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signin();  //creata de noi
    }

    //pt google
    public void signin(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }

    //ca sa nu ceara login daca te-ai logat deja
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}