package com.gizwits.opensource.appkit.ControlModule.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class TimerListInfBean implements Parcelable {

    public boolean isTask_Enable() {
        return Task_Enable;
    }

    public void setTask_Enable(boolean task_Enable) {
        Task_Enable = task_Enable;
    }

    public int getTask_onoff() {
        return Task_onoff;
    }

    public void setTask_onoff(int task_onoff) {
        Task_onoff = task_onoff;
    }

    public int getTask_type() {
        return Task_type;
    }

    public void setTask_type(int task_type) {
        Task_type = task_type;
    }

    public int getTask_Week_Repeat() {
        return Task_Week_Repeat;
    }

    public void setTask_Week_Repeat(int task_Week_Repeat) {
        Task_Week_Repeat = task_Week_Repeat;
    }

    public int getTask_day() {
        return Task_day;
    }

    public void setTask_day(int task_day) {
        Task_day = task_day;
    }

    public int getTask_minute() {
        return Task_minute;
    }

    public void setTask_minute(int task_minute) {
        Task_minute = task_minute;
    }

    public int getTask_cycle_minute1() {
        return Task_cycle_minute1;
    }

    public void setTask_cycle_minute1(int task_cycle_minute1) {
        Task_cycle_minute1 = task_cycle_minute1;
    }

    public int getTask_cycle_minute2() {
        return Task_cycle_minute2;
    }

    public void setTask_cycle_minute2(int task_cycle_minute2) {
        Task_cycle_minute2 = task_cycle_minute2;
    }

    public int getTask_cycle_onoff1() {
        return Task_cycle_onoff1;
    }

    public void setTask_cycle_onoff1(int task_cycle_onoff1) {
        Task_cycle_onoff1 = task_cycle_onoff1;
    }

    public int getTask_cycle_onoff2() {
        return Task_cycle_onoff2;
    }

    public void setTask_cycle_onoff2(int task_cycle_onoff2) {
        Task_cycle_onoff2 = task_cycle_onoff2;
    }

    private boolean Task_Enable = false;

    private int Task_onoff = 0;

    private int Task_type = 0;

    private int Task_Week_Repeat = 0;

    private int Task_day = 0;

    private int Task_minute = 0;

    public TimerListInfBean(){
    }

    private int Task_cycle_minute1 = 0;

    private int Task_cycle_minute2 = 0;

    private int Task_cycle_onoff1 = 0;

    private int Task_cycle_onoff2 = 0;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.Task_Enable ? (byte) 1 : (byte) 0);
        dest.writeInt(this.Task_onoff);
        dest.writeInt(this.Task_type);
        dest.writeInt(this.Task_Week_Repeat);
        dest.writeInt(this.Task_day);
        dest.writeInt(this.Task_minute);
        dest.writeInt(this.Task_cycle_minute1);
        dest.writeInt(this.Task_cycle_minute2);
        dest.writeInt(this.Task_cycle_onoff1);
        dest.writeInt(this.Task_cycle_onoff2);
    }

    protected TimerListInfBean(Parcel in) {
        this.Task_Enable = in.readByte() != 0;
        this.Task_onoff = in.readInt();
        this.Task_type = in.readInt();
        this.Task_Week_Repeat = in.readInt();
        this.Task_day = in.readInt();
        this.Task_minute = in.readInt();
        this.Task_cycle_minute1 = in.readInt();
        this.Task_cycle_minute2 = in.readInt();
        this.Task_cycle_onoff1 = in.readInt();
        this.Task_cycle_onoff2 = in.readInt();
    }

    public static final Parcelable.Creator<TimerListInfBean> CREATOR = new Parcelable.Creator<TimerListInfBean>() {
        @Override
        public TimerListInfBean createFromParcel(Parcel source) {
            return new TimerListInfBean(source);
        }

        @Override
        public TimerListInfBean[] newArray(int size) {
            return new TimerListInfBean[size];
        }
    };
}
