package com.stoe.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Quiz_Page extends AppCompatActivity {

    TextView time, correct, wrong;
    TextView question, a,b,c,d;
    Button next, finish;

    FirebaseDatabase database  = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("Questions");  //this is how we reach the DB

    //pt trimis scorul in DB
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference databaseReferenceForUserScore = database.getReference();

    String quizQuestion;
    String quizAnswerA;
    String quizAnswerB;
    String quizAnswerC;
    String quizAnswerD;
    String quizCorrectAnswer;
    int questionCount;
    int questionNumber = 1;

    String userAnswer;

    int userCorrect = 0;
    int userWrong = 0;

    CountDownTimer countDownTimer;
    private static final long TOTAL_TIME = 25000;
    Boolean timerContinue;
    long leftTime = TOTAL_TIME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        time = findViewById(R.id.textViewTime);
        correct = findViewById(R.id.textViewCorrect);
        wrong = findViewById(R.id.textViewWrong);

        question = findViewById(R.id.textViewQuestion);
        a = findViewById(R.id.textViewA);
        b = findViewById(R.id.textViewB);
        c = findViewById(R.id.textViewC);
        d = findViewById(R.id.textViewD);

        next = findViewById(R.id.buttonNext);
        finish = findViewById(R.id.buttonFinish);

        game();

        Log.d("wtf", "vad daca merge" );

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
                game();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendScore();
                Intent i = new Intent(getApplicationContext(), Score_Page.class);
                startActivity(i);
                finish();
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
                userAnswer = "a";
                if(quizCorrectAnswer.equals(userAnswer)){
                    a.setTextColor(Color.GREEN);
                    userCorrect++;
                    correct.setText("" + userCorrect);
                }else{
                    a.setTextColor(Color.RED);
                    userWrong++;
                    wrong.setText("" + userWrong);
                    findAnswer();
                }
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
                userAnswer = "b";
                if(quizCorrectAnswer.equals(userAnswer)){
                    b.setTextColor(Color.GREEN);
                    userCorrect++;
                    correct.setText("" + userCorrect);
                }else{
                    b.setTextColor(Color.RED);
                    userWrong++;
                    wrong.setText("" + userWrong);
                    findAnswer();
                }
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
                userAnswer = "c";
                if(quizCorrectAnswer.equals(userAnswer)){
                    c.setTextColor(Color.GREEN);
                    userCorrect++;
                    correct.setText("" + userCorrect);
                }else{
                    c.setTextColor(Color.RED);
                    userWrong++;
                    wrong.setText("" + userWrong);
                    findAnswer();
                }
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
                userAnswer = "d";
                if(quizCorrectAnswer.equals(userAnswer)){
                    d.setTextColor(Color.GREEN);
                    userCorrect++;
                    correct.setText("" + userCorrect);
                }else{
                    d.setTextColor(Color.RED);
                    userWrong++;
                    wrong.setText("" + userWrong);
                    findAnswer();
                }
            }
        });
//        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
//        connectedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//                if (connected) {
//                    Log.d("lol", "connected");
//                } else {
//                    Log.d("nope", "not connected");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("caancel", "Listener was cancelled");
//            }
//        });


    }

    public void game(){

        startTimer();

        a.setTextColor(Color.BLACK);
        a.setTextColor(Color.BLACK);
        a.setTextColor(Color.BLACK);
        a.setTextColor(Color.BLACK);
//        a.setBackgroundResource(R.drawable.text_background);
//        b.setBackgroundResource(R.drawable.text_background2);
//        c.setBackgroundResource(R.drawable.text_background3);
//        d.setBackgroundResource(R.drawable.text_background4);
//        d.setBackgroundColor(Color.WHITE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                String value = dataSnapshot.getValue(String.class);
//                Log.d("lolo", "Value is: " + value);

                questionCount = (int) dataSnapshot.getChildrenCount();    //ca sa vedem cate intrebari sunt

                quizQuestion = dataSnapshot.child(String.valueOf(questionNumber)).child("q").getValue().toString();
                quizAnswerA = dataSnapshot.child(String.valueOf(questionNumber)).child("a").getValue().toString();
                quizAnswerB = dataSnapshot.child(String.valueOf(questionNumber)).child("b").getValue().toString();
                quizAnswerC = dataSnapshot.child(String.valueOf(questionNumber)).child("c").getValue().toString();
                quizAnswerD = dataSnapshot.child(String.valueOf(questionNumber)).child("d").getValue().toString();
                quizCorrectAnswer = dataSnapshot.child(String.valueOf(questionNumber)).child("answer").getValue().toString();

                question.setText(quizQuestion);
                a.setText(quizAnswerA);
                b.setText(quizAnswerB);
                c.setText(quizAnswerC);
                d.setText(quizAnswerD);

                if(questionNumber < questionCount){
                    questionNumber++;
                } else {
                    Toast.makeText(getApplicationContext(), "You answered all the questions", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("forerror", "not connected");
                Toast.makeText(getApplicationContext(), "There is an error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void findAnswer(){
        if(quizCorrectAnswer.equals("a")){
            a.setTextColor(Color.GREEN);
        }else if(quizCorrectAnswer.equals("b")){
            b.setTextColor(Color.GREEN);
        }else if(quizCorrectAnswer.equals("c")){
            c.setTextColor(Color.GREEN);
        }else if(quizCorrectAnswer.equals("d")){
            d.setTextColor(Color.GREEN);
        }
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(leftTime, 1000) {
            @Override
            public void onTick(long l) {
                leftTime = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerContinue = false;
                pauseTimer();
                question.setText("Sorry, time is up");
            }
        }.start();

        timerContinue = true;
    }

    public void resetTimer(){
        leftTime = TOTAL_TIME;
        updateCountDownText();
    }

    public void updateCountDownText(){
        int second = ((int)leftTime / 1000) % 60;
        time.setText("" + second);
    }

    public void pauseTimer(){
        countDownTimer.cancel();
        timerContinue = false;
    }

    public void sendScore(){
        String userUID = user.getUid();
        databaseReferenceForUserScore.child("scores").child(userUID).child("correct").setValue(userCorrect)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "scores sent succesfully", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReferenceForUserScore.child("scores").child(userUID).child("wrong").setValue(userWrong);
        //puteam si aici
    }

}