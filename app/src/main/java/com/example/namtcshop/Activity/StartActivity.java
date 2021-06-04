package com.example.namtcshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.namtcshop.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        CountDownTimer countDownTimer = new CountDownTimer(2700,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Intent intent= new Intent(StartActivity.this, DangNhapActivity.class);
                startActivity(intent);
            }
        };
        countDownTimer.start();
    }
}