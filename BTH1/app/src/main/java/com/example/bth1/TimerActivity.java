package com.example.bth1;

import android.os.Bundle;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import java.util.Calendar;

public class TimerActivity extends AppCompatActivity {

    private NumberPicker hourPicker, minutePicker, secondPicker;
    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;// Thời gian đếm ngược
    private EditText edtTimerMinutes;
    private Button btnStartTimer;
    private LinearLayout timerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // Thiết lập các NumberPickers
        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);
        secondPicker = findViewById(R.id.secondPicker);

        hourPicker.setMaxValue(99);
        hourPicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);
        secondPicker.setMaxValue(59);
        secondPicker.setMinValue(0);

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        timerContainer = findViewById(R.id.linearLayout);

        // Bắt đầu đếm ngược - không có điều hướng ở đây
        startButton.setOnClickListener(v -> startTimer());

        // Điều hướng
        TextView tvClock = findViewById(R.id.tvClock);
        TextView tvAlarm = findViewById(R.id.tvAlarm);
        TextView tvTimer = findViewById(R.id.tvTimer);

        // Điều hướng đến màn hình Clock
        tvClock.setOnClickListener(v -> {
            Intent intent = new Intent(TimerActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Điều hướng đến màn hình Alarm
        tvAlarm.setOnClickListener(v -> {
            Intent intent = new Intent(TimerActivity.this, AlarmActivity.class);
            startActivity(intent);
        });
    }

    // Bắt đầu đếm ngược
    private void startTimer() {
        int hours = hourPicker.getValue();
        int minutes = minutePicker.getValue();
        int seconds = secondPicker.getValue();

        // Tính tổng thời gian đếm ngược bằng milliseconds
        timeLeftInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;

        if (timeLeftInMillis == 0) {
            Toast.makeText(this, "Vui lòng nhập thời gian hẹn giờ hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy thời gian hiện tại và thêm thời gian hẹn giờ
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, (int) timeLeftInMillis);

        // Đặt báo thức khi hẹn giờ kết thúc
        Intent intent = new Intent(this, TimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        // Hiển thị thông báo cho người dùng
        TextView timerTextView = new TextView(this);
        timerTextView.setText("Timer set for " + hours + " hours, " + minutes + " minutes, and " + seconds + " seconds.");
        timerContainer.addView(timerTextView);
    }

    // Cập nhật giao diện Timer
    private void updateTimer() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}