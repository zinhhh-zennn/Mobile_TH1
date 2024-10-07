package com.example.bth1;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView clockTextView;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clockTextView = findViewById(R.id.clockTextView);

        // Cập nhật đồng hồ mỗi giây
        runnable = new Runnable() {
            @Override
            public void run() {
                clockTextView.setText(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

        // Set sự kiện click cho các TextView
        TextView tvClock = findViewById(R.id.tvClock);
        TextView tvAlarm = findViewById(R.id.tvAlarm);
        TextView tvTimer = findViewById(R.id.tvTimer);

        // TextView Clock: không làm gì (đã ở màn hình Clock)
        tvClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiện tại đang ở màn hình đồng hồ
            }
        });

        // TextView Alarm: chuyển đến AlarmActivity
        tvAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });

        // TextView Timer: chuyển đến TimerActivity
        tvTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}