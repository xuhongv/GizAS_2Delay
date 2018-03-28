package com.gizwits.opensource.appkit.ControlModule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.utils.EventBusMessage;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AddTimerTaskActivity extends GosControlModuleBaseActivity implements NumberPicker.Formatter, View.OnClickListener, OnDateSetListener, CompoundButton.OnCheckedChangeListener {


    private RadioGroup mRgRepe;
    private RadioButton mRbOnce;
    private RadioButton mRbWeek;

    private CheckBox mCbWeek1;
    private CheckBox mCbWeek2;
    private CheckBox mCbWeek3;
    private CheckBox mCbWeek4;
    private CheckBox mCbWeek5;
    private CheckBox mCbWeek6;
    private CheckBox mCbWeek7;
    private RadioGroup mRgAction1;
    private RadioButton mRbRelay1Open;
    private RadioButton mRbRelay1Off;
    private RadioGroup mRgAction2;
    private RadioButton mRbRelay2Open;
    private RadioButton mRbRelay2O;

    private List<CheckBox> checkBoxList;

    private TextView tvTimesShow;

    private Switch mSwYear;
    private Button mBtnSure;

    private LinearLayout ll;
    private RelativeLayout rlTimes;

    private boolean isOnceExceute = true;
    private boolean isFirstActionOn = true;//继电器1
    private boolean isSecondActionOn = true;//继电器2

    private int sendDays = 0;
    private int sendMinute = 0;


    SimpleDateFormat mill = new SimpleDateFormat("MM");
    SimpleDateFormat dd = new SimpleDateFormat("dd");
    SimpleDateFormat hh = new SimpleDateFormat("HH");
    SimpleDateFormat mm = new SimpleDateFormat("mm");
    SimpleDateFormat yy = new SimpleDateFormat("yyyy");

    SimpleDateFormat caluter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat showCaluter = new SimpleDateFormat("yyyy-MM-dd HH-mm");


    //时间采集弹窗
    private TimePickerDialog mDialogMonthDayHourMinute;
    private GizWifiDevice mDevice;


    private int sendType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_add);
        setActionBar(true, true, "添加定时任务");
        EventBus.getDefault().register(this);
        initDevice();
        bindViews();
    }

    private void initDevice() {

        Intent intent = getIntent();
        mDevice = (GizWifiDevice) intent.getParcelableExtra("_device");
        sendType = intent.getIntExtra("_delayType", 1);
        Log.e("==w", "sendType:" + sendType);
    }

    private void bindViews() {

        ll = (LinearLayout) findViewById(R.id.ll);
        mRgRepe = (RadioGroup) findViewById(R.id.rgRepe);
        mRgRepe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbOnce) {
                    ll.setVisibility(View.GONE);
                    isOnceExceute = true;
                } else if (R.id.rbWeek == checkedId) {
                    ll.setVisibility(View.VISIBLE);
                    isOnceExceute = false;
                }
            }
        });


        mCbWeek1 = (CheckBox) findViewById(R.id.cbWeek1);
        mCbWeek2 = (CheckBox) findViewById(R.id.cbWeek2);
        mCbWeek3 = (CheckBox) findViewById(R.id.cbWeek3);
        mCbWeek4 = (CheckBox) findViewById(R.id.cbWeek4);
        mCbWeek5 = (CheckBox) findViewById(R.id.cbWeek5);
        mCbWeek6 = (CheckBox) findViewById(R.id.cbWeek6);
        mCbWeek7 = (CheckBox) findViewById(R.id.cbWeek7);

        checkBoxList = new ArrayList<>();
        checkBoxList.add(mCbWeek1);
        checkBoxList.add(mCbWeek2);
        checkBoxList.add(mCbWeek3);
        checkBoxList.add(mCbWeek4);
        checkBoxList.add(mCbWeek5);
        checkBoxList.add(mCbWeek6);
        checkBoxList.add(mCbWeek7);


        mRbRelay1Open = (RadioButton) findViewById(R.id.rbRelay1Open);
        mRbRelay1Off = (RadioButton) findViewById(R.id.rbRelay1Off);
        mRgAction1 = (RadioGroup) findViewById(R.id.rgAction1);
        mRgAction1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbRelay1Open) {
                    isFirstActionOn = true;
                } else if (R.id.rbRelay1Off == checkedId) {
                    isFirstActionOn = false;
                }
            }
        });


        mRbRelay2Open = (RadioButton) findViewById(R.id.rbRelay2Open);
        mRbRelay2O = (RadioButton) findViewById(R.id.rbRelay2Off);
        mRbRelay2O.setOnCheckedChangeListener(this);
        mRgAction2 = (RadioGroup) findViewById(R.id.rgAction2);
        mRgAction2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbRelay2Open) {
                    isSecondActionOn = true;
                } else if (R.id.rbRelay2Off == checkedId) {
                    isSecondActionOn = false;
                }
            }
        });


        rlTimes = (RelativeLayout) findViewById(R.id.rlTimes);
        rlTimes.setOnClickListener(this);

        mSwYear = (Switch) findViewById(R.id.swYear);
        mBtnSure = (Button) findViewById(R.id.btnSure);

        mBtnSure.setOnClickListener(this);

        tvTimesShow = (TextView) findViewById(R.id.tvTimesShow);

    }


    private void btnConfim() {

        int sendActionData = 0;

        //任务开关动作
        if (isFirstActionOn && isSecondActionOn) { //1开2开
            sendActionData = 6;
        } else if (!isFirstActionOn && !isSecondActionOn) {//1关2关
            sendActionData = 5;
        } else if (isFirstActionOn && !isSecondActionOn) {//1开2关
            sendActionData = 14;
        } else if (!isFirstActionOn && isSecondActionOn) {//1关2开
            sendActionData = 23;
        }

        Log.e("xuhong", "isOnceExceute:" + isOnceExceute);



        String KEY_TASK_ENABLE = "Task" + sendType + "_Enable";
        String KEY_TASK_ONOFF = "Task" + sendType + "_onoff";
        String KEY_TASK_TYPE = "Task" + sendType + "_type";
        String KEY_TASK_DAY = "Task" + sendType + "_day";
        String KEY_TASK_MINUTE = "Task" + sendType + "_minute";
        String KEY_TASK_WEEK_REPEAT = "Task" + sendType + "_Week_Repeat";


        //执行一次
        if (isOnceExceute) {
            //任务允许
            ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<>();
            hashMap.put(KEY_TASK_ENABLE, true);
            hashMap.put(KEY_TASK_ONOFF, sendActionData);
            hashMap.put(KEY_TASK_TYPE, 1);
            hashMap.put(KEY_TASK_DAY, sendDays);
            hashMap.put(KEY_TASK_MINUTE, sendMinute);
            sendCommand(hashMap);

            //循环执行
        } else {

            int Task1_Week_Repeat = 0;
            for (int i = 0; i < checkBoxList.size(); i++) {
                if (checkBoxList.get(i).isChecked()) {
                    Task1_Week_Repeat = Task1_Week_Repeat + (int) Math.pow(10, (i));
                }
            }

            //任务允许
            ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<>();
            hashMap.put(KEY_TASK_ENABLE, true);
            hashMap.put(KEY_TASK_ONOFF, sendActionData);
            hashMap.put(KEY_TASK_TYPE, 2);
            hashMap.put(KEY_TASK_DAY, sendDays);
            hashMap.put(KEY_TASK_MINUTE, sendMinute);

            //十进制转二进制
            BigInteger src = new BigInteger(Task1_Week_Repeat + "", 2);
            int intValue = src.intValue();

            hashMap.put(KEY_TASK_WEEK_REPEAT, intValue);
            sendCommand(hashMap);


        }


        myToast("发送成功！");
        finish();

    }


    @Override
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlTimes:
                showTimerPickerDialog();
                break;
            case R.id.btnSure:
                if (sendDays == 0) {
                    myToast("设置失败，请选择定时时间！");
                    return;
                }
                btnConfim();
                break;
        }
    }

    private void showTimerPickerDialog() {
        mDialogMonthDayHourMinute = new TimePickerDialog.Builder()
                .setType(Type.MONTH_DAY_HOUR_MIN)
                // .setType(Type.ALL)
                .setMinMillseconds(System.currentTimeMillis())
                //  .setMaxMillseconds(System.currentTimeMillis() + 308217597777l)
                .setThemeColor(getResources().getColor(R.color.yellow))
                .setCallBack(this)
                .setTitleStringId("选择你的定时时间")
                .build();
        mDialogMonthDayHourMinute.show(getSupportFragmentManager(), "month_day_hour_minute");
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {


//        Log.e("==w", "年:" + getDateNumber(yy, millseconds));
//        Log.e("==w", "月:" + getDateNumber(mill, millseconds));
//        Log.e("==w", "日:" + getDateNumber(dd, millseconds));
//        Log.e("==w", "时:" + getDateNumber(hh, millseconds));
//        Log.e("==w", "分:" + getDateNumber(mm, millseconds));

        Date d = new Date(millseconds);
        caluter.format(d);
        //  Log.e("==w", "一年中的第几天:" + orderDate(caluter.format(d)));

        Date y = new Date(millseconds);
        int days = parseNumberDaysTo2000(yy.format(y));
        //  Log.e("==w", "距离2000年有多少天:" + days);

        //日数值
        sendDays = orderDate(caluter.format(d)) + days;
        sendMinute = getDateNumber(hh, millseconds) * 60 + getDateNumber(mm, millseconds);

        tvTimesShow.setText(showCaluter.format(d));

        // Log.e("==w", "采集到的总日:" + sendDays);
        // Log.e("==w", "采集到的总分钟:" + sendMinute);

    }

    public int getDateNumber(SimpleDateFormat yy, long time) {
        Date d = new Date(time);
        return Integer.parseInt(yy.format(d));
    }


    // 请输入年月日(格式2015-02-11)
    public int orderDate(String date) {
        int dateSum = 0;
        int year = Integer.valueOf(date.substring(0, 4));
        int month = Integer.valueOf(date.substring(5, 7));
        int day = Integer.valueOf(date.substring(8, 10));
        for (int i = 1; i < month; i++) {
            switch (i) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    dateSum += 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    dateSum += 30;
                    break;
                case 2:
                    if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {//判断是否是闰年
                        dateSum += 29;
                    } else {
                        dateSum += 28;
                    }
            }
        }

        return (dateSum + day);
    }

    /**
     * @param data 格式 2018
     * @return 求此年到2000年一共有多少日
     */
    private int parseNumberDaysTo2000(String data) {

        int currentYear = Integer.parseInt(data);
        int dateSum = 0;

        for (int i = 2000; i < currentYear + 1; i++) {
            if (((2000 + i) % 4 == 0 && (2000 + i) % 100 != 0) || ((2000 + i) % 400 == 0)) {//判断是否是闰年
                dateSum = dateSum + 366; //闰年366天
            } else {
                dateSum = dateSum + 365; //平年365天
            }
        }
        return dateSum;
    }


    private void sendCommand(ConcurrentHashMap<String, Object> hashMap) {
        if (hashMap == null)
            return;
        int sn = 5;
        mDevice.write(hashMap, sn);
        Log.i("==w", "下发命令：" + hashMap.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage messageEvent) {
        if (messageEvent.getEventCode() == 1) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
