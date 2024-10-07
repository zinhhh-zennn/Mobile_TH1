package com.example.bth1;

import android.os.Bundle;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;


public class AlarmActivity extends AppCompatActivity {

    private EditText edtHour, edtMinute;
    private CheckBox cbMon, cbTue, cbWed, cbThu, cbFri, cbSat, cbSun;
    private LinearLayout alarmContainer;
    private Button btnAddAlarm;
    private List<Alarm> alarmList;
    private int alarmIdCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // Tham chiếu đến các thành phần trong layout
        edtHour = findViewById(R.id.edtHour);
        edtMinute = findViewById(R.id.edtMinute);
        cbMon = findViewById(R.id.cbMon);
        cbTue = findViewById(R.id.cbTue);
        cbWed = findViewById(R.id.cbWed);
        cbThu = findViewById(R.id.cbThu);
        cbFri = findViewById(R.id.cbFri);
        cbSat = findViewById(R.id.cbSat);
        cbSun = findViewById(R.id.cbSun);
        alarmContainer = findViewById(R.id.alarmContainer);
        btnAddAlarm = findViewById(R.id.btnAddAlarm);

        // Khởi tạo danh sách báo thức
        alarmList = new ArrayList<>();

        // Khi nhấn nút Thêm báo thức
        btnAddAlarm.setOnClickListener(v -> addNewAlarm());

        // Set sự kiện click cho các TextView
        TextView tvClock = findViewById(R.id.tvClock);
        TextView tvAlarm = findViewById(R.id.tvAlarm);
        TextView tvTimer = findViewById(R.id.tvTimer);

        // TextView Clock: chuyển về MainActivity (Clock)
        tvClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // TextView Alarm: không làm gì (đã ở màn hình Alarm)
        tvAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiện tại đang ở màn hình báo thức
            }
        });

        // TextView Timer: chuyển đến TimerActivity
        tvTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmActivity.this, TimerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addNewAlarm() {
        // Lấy giờ và phút từ EditText
        String hour = edtHour.getText().toString();
        String minute = edtMinute.getText().toString();

        // Kiểm tra nếu chưa nhập đầy đủ giờ và phút
        if (hour.isEmpty() || minute.isEmpty()) {
            return; // Không thêm báo thức nếu giờ hoặc phút chưa được nhập
        }

        // Format thời gian
        String time = String.format("%02d:%02d", Integer.parseInt(hour), Integer.parseInt(minute));

        // Xác định các ngày lặp lại của báo thức
        List<String> repeatDays = new ArrayList<>();
        if (cbMon.isChecked()) repeatDays.add("Thứ 2");
        if (cbTue.isChecked()) repeatDays.add("Thứ 3");
        if (cbWed.isChecked()) repeatDays.add("Thứ 4");
        if (cbThu.isChecked()) repeatDays.add("Thứ 5");
        if (cbFri.isChecked()) repeatDays.add("Thứ 6");
        if (cbSat.isChecked()) repeatDays.add("Thứ 7");
        if (cbSun.isChecked()) repeatDays.add("Chủ nhật");

        // Tạo một Alarm object và thêm vào danh sách
        Alarm alarm = new Alarm(time, false, repeatDays);
        alarmList.add(alarm);

        // Đặt báo thức
        setAlarm(time);
    }

    // Đặt báo thức với AlarmManager
    private void setAlarm(String time) {
        String[] splitTime = time.split(":");
        int hour = Integer.parseInt(splitTime[0]);
        int minute = Integer.parseInt(splitTime[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Nếu giờ đã qua, đặt báo thức cho ngày hôm sau
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("alarm_time", time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmIdCounter++, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}

