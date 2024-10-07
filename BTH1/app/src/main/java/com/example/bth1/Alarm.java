package com.example.bth1;

import java.util.List;

public class Alarm {

    private String time;  // Thời gian của báo thức
    private boolean enabled;  // Trạng thái bật/tắt báo thức
    private List<String> repeatDays;  // Các ngày lặp lại

    public Alarm(String time, boolean enabled, List<String> repeatDays) {
        this.time = time;
        this.enabled = enabled;
        this.repeatDays = repeatDays;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(List<String> repeatDays) {
        this.repeatDays = repeatDays;
    }
}