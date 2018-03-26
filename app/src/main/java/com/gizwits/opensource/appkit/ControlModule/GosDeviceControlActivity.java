package com.gizwits.opensource.appkit.ControlModule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.appkit.ControlModule.bean.TimerListInfBean;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.utils.EventBusMessage;

import org.greenrobot.eventbus.EventBus;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class GosDeviceControlActivity extends GosControlModuleBaseActivity
        implements OnClickListener, OnEditorActionListener, OnSeekBarChangeListener {

    /**
     * 设备列表传入的设备变量
     */
    private GizWifiDevice mDevice;


    private Badge badge;
    private Button btRecycle, btDelay, btTimer;
    private CheckBox mCbDelay1, mCbDelay2;
    private RelativeLayout mRL1, mRL2;

    private enum handler_key {

        /**
         * 更新界面
         */
        UPDATE_UI,

        DISCONNECT,
    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            if (isDeviceCanBeControlled()) {
                progressDialog.cancel();
            } else {
                toastDeviceNoReadyAndExit();
            }
        }

    };

    /**
     * The handler.
     */
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {
                case UPDATE_UI:
                    updateUI();
                    break;
                case DISCONNECT:
                    toastDeviceDisconnectAndExit();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_device_control);
        initDevice();
        setActionBar(true, true, getDeviceName());
        initView();
        initEvent();
    }

    private void initView() {
        btRecycle = (Button) findViewById(R.id.cbTimer3);
        btRecycle.setOnClickListener(this);

        btDelay = (Button) findViewById(R.id.cbTimer2);
        btDelay.setOnClickListener(this);

        btTimer = (Button) findViewById(R.id.cbTimer1);
        btTimer.setOnClickListener(this);


        mCbDelay1 = (CheckBox) findViewById(R.id.cbDelay1);
        mCbDelay1.setOnClickListener(this);

        mCbDelay2 = (CheckBox) findViewById(R.id.cbDelay2);
        mCbDelay2.setOnClickListener(this);

        mRL1 = (RelativeLayout) findViewById(R.id.rl1);
        mRL2 = (RelativeLayout) findViewById(R.id.rl2);

        // Log.e("==w", "mesh:" + timer1Bean.getTask_minute());

    }

    private void initEvent() {

        badge = new QBadgeView(this).bindTarget(btRecycle).setBadgeNumber(5).setBadgeTextSize(13f, true).setGravityOffset(-3, 0, true);


    }

    private void initDevice() {
        Intent intent = getIntent();
        mDevice = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        mDevice.setListener(gizWifiDeviceListener);
        Log.i("Apptest", mDevice.getDid());
    }

    private String getDeviceName() {
        if (TextUtils.isEmpty(mDevice.getAlias())) {
            return mDevice.getProductName();
        }
        return mDevice.getAlias();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStatusOfDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        // 退出页面，取消设备订阅
        mDevice.setSubscribe(false);
        mDevice.setListener(null);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //开关1
            case R.id.cbDelay1:
                if (mCbDelay1.isChecked()) {
                    sendCommand(KEY_ONOFF1, 1);
                    mRL1.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    sendCommand(KEY_ONOFF1, 0);
                    mRL1.setBackgroundColor(getResources().getColor(R.color.background_gray));
                }
                break;

            //开关2
            case R.id.cbDelay2:
                if (mCbDelay2.isChecked()) {
                    sendCommand(KEY_ONOFF2, 1);
                    mRL2.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    sendCommand(KEY_ONOFF2, 0);
                    mRL2.setBackgroundColor(getResources().getColor(R.color.background_gray));
                }
                break;
            //循环
            case R.id.cbTimer3:
                Intent intent = new Intent(this, TimerRecycleActivity.class);
                intent.putExtra("_device", mDevice);
                startActivityForResult(intent, 150);
                break;

            //定时
            case R.id.cbTimer2:
                Intent intent2 = new Intent(this, DelaySettingActivity.class);
                intent2.putExtra("_device", mDevice);
                startActivityForResult(intent2, 150);
                break;

            //定时
            case R.id.cbTimer1:

                ArrayList<TimerListInfBean> listInfBeans = new ArrayList<>();
                listInfBeans.add(timer1Bean);
                listInfBeans.add(timer2Bean);
                listInfBeans.add(timer3Bean);
                listInfBeans.add(timer4Bean);
                listInfBeans.add(timer5Bean);
                Log.e("==w", "跳转钱inf:" + timer1Bean);
                Intent intent3 = new Intent(this, TimerTaskActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("GizWifiDevice", mDevice);
                bundle.putParcelableArrayList("_listInfBean", listInfBeans);
                intent3.putExtras(bundle);
                startActivityForResult(intent3, 150);
                break;
            default:
                break;
        }
    }

    /*
     * ========================================================================
     * EditText 点击键盘“完成”按钮方法
     * ========================================================================
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        switch (v.getId()) {
            default:
                break;
        }
        hideKeyBoard();
        return false;

    }

    /*
     * ========================================================================
     * seekbar 回调方法重写
     * ========================================================================
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId()) {
//		case R.id.sb_data_Task1_onoff:
//			tv_data_Task1_onoff.setText(formatValue((progress + TASK1_ONOFF_OFFSET) * TASK1_ONOFF_RATIO + TASK1_ONOFF_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task1_type:
//			tv_data_Task1_type.setText(formatValue((progress + TASK1_TYPE_OFFSET) * TASK1_TYPE_RATIO + TASK1_TYPE_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task1_Week_Repeat:
//			tv_data_Task1_Week_Repeat.setText(formatValue((progress + TASK1_WEEK_REPEAT_OFFSET) * TASK1_WEEK_REPEAT_RATIO + TASK1_WEEK_REPEAT_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task2_onoff:
//			tv_data_Task2_onoff.setText(formatValue((progress + TASK2_ONOFF_OFFSET) * TASK2_ONOFF_RATIO + TASK2_ONOFF_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task2_type:
//			tv_data_Task2_type.setText(formatValue((progress + TASK2_TYPE_OFFSET) * TASK2_TYPE_RATIO + TASK2_TYPE_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task2_Week_Repeat:
//			tv_data_Task2_Week_Repeat.setText(formatValue((progress + TASK2_WEEK_REPEAT_OFFSET) * TASK2_WEEK_REPEAT_RATIO + TASK2_WEEK_REPEAT_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task3_onoff:
//			tv_data_Task3_onoff.setText(formatValue((progress + TASK3_ONOFF_OFFSET) * TASK3_ONOFF_RATIO + TASK3_ONOFF_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task3_type:
//			tv_data_Task3_type.setText(formatValue((progress + TASK3_TYPE_OFFSET) * TASK3_TYPE_RATIO + TASK3_TYPE_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task3_Week_Repeat:
//			tv_data_Task3_Week_Repeat.setText(formatValue((progress + TASK3_WEEK_REPEAT_OFFSET) * TASK3_WEEK_REPEAT_RATIO + TASK3_WEEK_REPEAT_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task4_onoff:
//			tv_data_Task4_onoff.setText(formatValue((progress + TASK4_ONOFF_OFFSET) * TASK4_ONOFF_RATIO + TASK4_ONOFF_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task4_type:
//			tv_data_Task4_type.setText(formatValue((progress + TASK4_TYPE_OFFSET) * TASK4_TYPE_RATIO + TASK4_TYPE_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task4_Week_Repeat:
//			tv_data_Task4_Week_Repeat.setText(formatValue((progress + TASK4_WEEK_REPEAT_OFFSET) * TASK4_WEEK_REPEAT_RATIO + TASK4_WEEK_REPEAT_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task5_onoff:
//			tv_data_Task5_onoff.setText(formatValue((progress + TASK5_ONOFF_OFFSET) * TASK5_ONOFF_RATIO + TASK5_ONOFF_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task5_type:
//			tv_data_Task5_type.setText(formatValue((progress + TASK5_TYPE_OFFSET) * TASK5_TYPE_RATIO + TASK5_TYPE_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task5_Week_Repeat:
//			tv_data_Task5_Week_Repeat.setText(formatValue((progress + TASK5_WEEK_REPEAT_OFFSET) * TASK5_WEEK_REPEAT_RATIO + TASK5_WEEK_REPEAT_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task1_day:
//			tv_data_Task1_day.setText(formatValue((progress + TASK1_DAY_OFFSET) * TASK1_DAY_RATIO + TASK1_DAY_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task1_minute:
//			tv_data_Task1_minute.setText(formatValue((progress + TASK1_MINUTE_OFFSET) * TASK1_MINUTE_RATIO + TASK1_MINUTE_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task1_cycle_minute1:
//			tv_data_Task1_cycle_minute1.setText(formatValue((progress + TASK1_CYCLE_MINUTE1_OFFSET) * TASK1_CYCLE_MINUTE1_RATIO + TASK1_CYCLE_MINUTE1_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task1_cycle_minute2:
//			tv_data_Task1_cycle_minute2.setText(formatValue((progress + TASK1_CYCLE_MINUTE2_OFFSET) * TASK1_CYCLE_MINUTE2_RATIO + TASK1_CYCLE_MINUTE2_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task2_day:
//			tv_data_Task2_day.setText(formatValue((progress + TASK2_DAY_OFFSET) * TASK2_DAY_RATIO + TASK2_DAY_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task2_minute:
//			tv_data_Task2_minute.setText(formatValue((progress + TASK2_MINUTE_OFFSET) * TASK2_MINUTE_RATIO + TASK2_MINUTE_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task2_cycle_minute1:
//			tv_data_Task2_cycle_minute1.setText(formatValue((progress + TASK2_CYCLE_MINUTE1_OFFSET) * TASK2_CYCLE_MINUTE1_RATIO + TASK2_CYCLE_MINUTE1_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task2_cycle_minute2:
//			tv_data_Task2_cycle_minute2.setText(formatValue((progress + TASK2_CYCLE_MINUTE2_OFFSET) * TASK2_CYCLE_MINUTE2_RATIO + TASK2_CYCLE_MINUTE2_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task3_day:
//			tv_data_Task3_day.setText(formatValue((progress + TASK3_DAY_OFFSET) * TASK3_DAY_RATIO + TASK3_DAY_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task3_minute:
//			tv_data_Task3_minute.setText(formatValue((progress + TASK3_MINUTE_OFFSET) * TASK3_MINUTE_RATIO + TASK3_MINUTE_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task3_cycle_minute1:
//			tv_data_Task3_cycle_minute1.setText(formatValue((progress + TASK3_CYCLE_MINUTE1_OFFSET) * TASK3_CYCLE_MINUTE1_RATIO + TASK3_CYCLE_MINUTE1_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task3_cycle_minute2:
//			tv_data_Task3_cycle_minute2.setText(formatValue((progress + TASK3_CYCLE_MINUTE2_OFFSET) * TASK3_CYCLE_MINUTE2_RATIO + TASK3_CYCLE_MINUTE2_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task4_day:
//			tv_data_Task4_day.setText(formatValue((progress + TASK4_DAY_OFFSET) * TASK4_DAY_RATIO + TASK4_DAY_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task4_minute:
//			tv_data_Task4_minute.setText(formatValue((progress + TASK4_MINUTE_OFFSET) * TASK4_MINUTE_RATIO + TASK4_MINUTE_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task4_cycle_minute1:
//			tv_data_Task4_cycle_minute1.setText(formatValue((progress + TASK4_CYCLE_MINUTE1_OFFSET) * TASK4_CYCLE_MINUTE1_RATIO + TASK4_CYCLE_MINUTE1_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task4_cycle_minute2:
//			tv_data_Task4_cycle_minute2.setText(formatValue((progress + TASK4_CYCLE_MINUTE2_OFFSET) * TASK4_CYCLE_MINUTE2_RATIO + TASK4_CYCLE_MINUTE2_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task5_day:
//			tv_data_Task5_day.setText(formatValue((progress + TASK5_DAY_OFFSET) * TASK5_DAY_RATIO + TASK5_DAY_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task5_minute:
//			tv_data_Task5_minute.setText(formatValue((progress + TASK5_MINUTE_OFFSET) * TASK5_MINUTE_RATIO + TASK5_MINUTE_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task5_cycle_minute1:
//			tv_data_Task5_cycle_minute1.setText(formatValue((progress + TASK5_CYCLE_MINUTE1_OFFSET) * TASK5_CYCLE_MINUTE1_RATIO + TASK5_CYCLE_MINUTE1_ADDITION, 1));
//			break;
//		case R.id.sb_data_Task5_cycle_minute2:
//			tv_data_Task5_cycle_minute2.setText(formatValue((progress + TASK5_CYCLE_MINUTE2_OFFSET) * TASK5_CYCLE_MINUTE2_RATIO + TASK5_CYCLE_MINUTE2_ADDITION, 1));
//			break;
            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.sb_data_Task1_onoff:
                sendCommand(KEY_TASK1_ONOFF, (seekBar.getProgress() + TASK1_ONOFF_OFFSET) * TASK1_ONOFF_RATIO + TASK1_ONOFF_ADDITION);
                break;
            case R.id.sb_data_Task1_type:
                sendCommand(KEY_TASK1_TYPE, (seekBar.getProgress() + TASK1_TYPE_OFFSET) * TASK1_TYPE_RATIO + TASK1_TYPE_ADDITION);
                break;
            case R.id.sb_data_Task1_Week_Repeat:
                sendCommand(KEY_TASK1_WEEK_REPEAT, (seekBar.getProgress() + TASK1_WEEK_REPEAT_OFFSET) * TASK1_WEEK_REPEAT_RATIO + TASK1_WEEK_REPEAT_ADDITION);
                break;
            case R.id.sb_data_Task2_onoff:
                sendCommand(KEY_TASK2_ONOFF, (seekBar.getProgress() + TASK2_ONOFF_OFFSET) * TASK2_ONOFF_RATIO + TASK2_ONOFF_ADDITION);
                break;
            case R.id.sb_data_Task2_type:
                sendCommand(KEY_TASK2_TYPE, (seekBar.getProgress() + TASK2_TYPE_OFFSET) * TASK2_TYPE_RATIO + TASK2_TYPE_ADDITION);
                break;
            case R.id.sb_data_Task2_Week_Repeat:
                sendCommand(KEY_TASK2_WEEK_REPEAT, (seekBar.getProgress() + TASK2_WEEK_REPEAT_OFFSET) * TASK2_WEEK_REPEAT_RATIO + TASK2_WEEK_REPEAT_ADDITION);
                break;
            case R.id.sb_data_Task3_onoff:
                sendCommand(KEY_TASK3_ONOFF, (seekBar.getProgress() + TASK3_ONOFF_OFFSET) * TASK3_ONOFF_RATIO + TASK3_ONOFF_ADDITION);
                break;
            case R.id.sb_data_Task3_type:
                sendCommand(KEY_TASK3_TYPE, (seekBar.getProgress() + TASK3_TYPE_OFFSET) * TASK3_TYPE_RATIO + TASK3_TYPE_ADDITION);
                break;
            case R.id.sb_data_Task3_Week_Repeat:
                sendCommand(KEY_TASK3_WEEK_REPEAT, (seekBar.getProgress() + TASK3_WEEK_REPEAT_OFFSET) * TASK3_WEEK_REPEAT_RATIO + TASK3_WEEK_REPEAT_ADDITION);
                break;
            case R.id.sb_data_Task4_onoff:
                sendCommand(KEY_TASK4_ONOFF, (seekBar.getProgress() + TASK4_ONOFF_OFFSET) * TASK4_ONOFF_RATIO + TASK4_ONOFF_ADDITION);
                break;
            case R.id.sb_data_Task4_type:
                sendCommand(KEY_TASK4_TYPE, (seekBar.getProgress() + TASK4_TYPE_OFFSET) * TASK4_TYPE_RATIO + TASK4_TYPE_ADDITION);
                break;
            case R.id.sb_data_Task4_Week_Repeat:
                sendCommand(KEY_TASK4_WEEK_REPEAT, (seekBar.getProgress() + TASK4_WEEK_REPEAT_OFFSET) * TASK4_WEEK_REPEAT_RATIO + TASK4_WEEK_REPEAT_ADDITION);
                break;
            case R.id.sb_data_Task5_onoff:
                sendCommand(KEY_TASK5_ONOFF, (seekBar.getProgress() + TASK5_ONOFF_OFFSET) * TASK5_ONOFF_RATIO + TASK5_ONOFF_ADDITION);
                break;
            case R.id.sb_data_Task5_type:
                sendCommand(KEY_TASK5_TYPE, (seekBar.getProgress() + TASK5_TYPE_OFFSET) * TASK5_TYPE_RATIO + TASK5_TYPE_ADDITION);
                break;
            case R.id.sb_data_Task5_Week_Repeat:
                sendCommand(KEY_TASK5_WEEK_REPEAT, (seekBar.getProgress() + TASK5_WEEK_REPEAT_OFFSET) * TASK5_WEEK_REPEAT_RATIO + TASK5_WEEK_REPEAT_ADDITION);
                break;
            case R.id.sb_data_Task1_day:
                sendCommand(KEY_TASK1_DAY, (seekBar.getProgress() + TASK1_DAY_OFFSET) * TASK1_DAY_RATIO + TASK1_DAY_ADDITION);
                break;
            case R.id.sb_data_Task1_minute:
                sendCommand(KEY_TASK1_MINUTE, (seekBar.getProgress() + TASK1_MINUTE_OFFSET) * TASK1_MINUTE_RATIO + TASK1_MINUTE_ADDITION);
                break;
            case R.id.sb_data_Task1_cycle_minute1:
                sendCommand(KEY_TASK1_CYCLE_MINUTE1, (seekBar.getProgress() + TASK1_CYCLE_MINUTE1_OFFSET) * TASK1_CYCLE_MINUTE1_RATIO + TASK1_CYCLE_MINUTE1_ADDITION);
                break;
            case R.id.sb_data_Task1_cycle_minute2:
                sendCommand(KEY_TASK1_CYCLE_MINUTE2, (seekBar.getProgress() + TASK1_CYCLE_MINUTE2_OFFSET) * TASK1_CYCLE_MINUTE2_RATIO + TASK1_CYCLE_MINUTE2_ADDITION);
                break;
            case R.id.sb_data_Task2_day:
                sendCommand(KEY_TASK2_DAY, (seekBar.getProgress() + TASK2_DAY_OFFSET) * TASK2_DAY_RATIO + TASK2_DAY_ADDITION);
                break;
            case R.id.sb_data_Task2_minute:
                sendCommand(KEY_TASK2_MINUTE, (seekBar.getProgress() + TASK2_MINUTE_OFFSET) * TASK2_MINUTE_RATIO + TASK2_MINUTE_ADDITION);
                break;
            case R.id.sb_data_Task2_cycle_minute1:
                sendCommand(KEY_TASK2_CYCLE_MINUTE1, (seekBar.getProgress() + TASK2_CYCLE_MINUTE1_OFFSET) * TASK2_CYCLE_MINUTE1_RATIO + TASK2_CYCLE_MINUTE1_ADDITION);
                break;
            case R.id.sb_data_Task2_cycle_minute2:
                sendCommand(KEY_TASK2_CYCLE_MINUTE2, (seekBar.getProgress() + TASK2_CYCLE_MINUTE2_OFFSET) * TASK2_CYCLE_MINUTE2_RATIO + TASK2_CYCLE_MINUTE2_ADDITION);
                break;
            case R.id.sb_data_Task3_day:
                sendCommand(KEY_TASK3_DAY, (seekBar.getProgress() + TASK3_DAY_OFFSET) * TASK3_DAY_RATIO + TASK3_DAY_ADDITION);
                break;
            case R.id.sb_data_Task3_minute:
                sendCommand(KEY_TASK3_MINUTE, (seekBar.getProgress() + TASK3_MINUTE_OFFSET) * TASK3_MINUTE_RATIO + TASK3_MINUTE_ADDITION);
                break;
            case R.id.sb_data_Task3_cycle_minute1:
                sendCommand(KEY_TASK3_CYCLE_MINUTE1, (seekBar.getProgress() + TASK3_CYCLE_MINUTE1_OFFSET) * TASK3_CYCLE_MINUTE1_RATIO + TASK3_CYCLE_MINUTE1_ADDITION);
                break;
            case R.id.sb_data_Task3_cycle_minute2:
                sendCommand(KEY_TASK3_CYCLE_MINUTE2, (seekBar.getProgress() + TASK3_CYCLE_MINUTE2_OFFSET) * TASK3_CYCLE_MINUTE2_RATIO + TASK3_CYCLE_MINUTE2_ADDITION);
                break;
            case R.id.sb_data_Task4_day:
                sendCommand(KEY_TASK4_DAY, (seekBar.getProgress() + TASK4_DAY_OFFSET) * TASK4_DAY_RATIO + TASK4_DAY_ADDITION);
                break;
            case R.id.sb_data_Task4_minute:
                sendCommand(KEY_TASK4_MINUTE, (seekBar.getProgress() + TASK4_MINUTE_OFFSET) * TASK4_MINUTE_RATIO + TASK4_MINUTE_ADDITION);
                break;
            case R.id.sb_data_Task4_cycle_minute1:
                sendCommand(KEY_TASK4_CYCLE_MINUTE1, (seekBar.getProgress() + TASK4_CYCLE_MINUTE1_OFFSET) * TASK4_CYCLE_MINUTE1_RATIO + TASK4_CYCLE_MINUTE1_ADDITION);
                break;
            case R.id.sb_data_Task4_cycle_minute2:
                sendCommand(KEY_TASK4_CYCLE_MINUTE2, (seekBar.getProgress() + TASK4_CYCLE_MINUTE2_OFFSET) * TASK4_CYCLE_MINUTE2_RATIO + TASK4_CYCLE_MINUTE2_ADDITION);
                break;
            case R.id.sb_data_Task5_day:
                sendCommand(KEY_TASK5_DAY, (seekBar.getProgress() + TASK5_DAY_OFFSET) * TASK5_DAY_RATIO + TASK5_DAY_ADDITION);
                break;
            case R.id.sb_data_Task5_minute:
                sendCommand(KEY_TASK5_MINUTE, (seekBar.getProgress() + TASK5_MINUTE_OFFSET) * TASK5_MINUTE_RATIO + TASK5_MINUTE_ADDITION);
                break;
            case R.id.sb_data_Task5_cycle_minute1:
                sendCommand(KEY_TASK5_CYCLE_MINUTE1, (seekBar.getProgress() + TASK5_CYCLE_MINUTE1_OFFSET) * TASK5_CYCLE_MINUTE1_RATIO + TASK5_CYCLE_MINUTE1_ADDITION);
                break;
            case R.id.sb_data_Task5_cycle_minute2:
                sendCommand(KEY_TASK5_CYCLE_MINUTE2, (seekBar.getProgress() + TASK5_CYCLE_MINUTE2_OFFSET) * TASK5_CYCLE_MINUTE2_RATIO + TASK5_CYCLE_MINUTE2_ADDITION);
                break;
            default:
                break;
        }
    }

    /*
     * ========================================================================
     * 菜单栏
     * ========================================================================
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.device_more, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_getHardwareInfo:
                if (mDevice.isLAN()) {
                    mDevice.getHardwareInfo();
                } else {
                    myToast("只允许在局域网下获取设备硬件信息！");
                }
                break;

            case R.id.action_getStatu:
                mDevice.getDeviceStatus();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Description:根据保存的的数据点的值来更新UI
     */
    protected void updateUI() {

        mCbDelay1.setChecked(data_OnOff1);
        mCbDelay2.setChecked(data_OnOff2);

        if (!data_OnOff1) {
            mRL1.setBackgroundColor(getResources().getColor(R.color.background_gray));
        } else {
            mRL1.setBackgroundColor(getResources().getColor(R.color.white));
        }

        if (!data_OnOff2) {
            mRL2.setBackgroundColor(getResources().getColor(R.color.background_gray));
        } else {
            mRL2.setBackgroundColor(getResources().getColor(R.color.white));
        }

        Log.e("==w", "ui data_Task1_minute :" + data_Task1_minute);
        Log.e("==w", "ui:" + timer1Bean.getTask_minute());
    }

    private void setEditText(EditText et, Object value) {
        et.setText(value.toString());
        et.setSelection(value.toString().length());
        et.clearFocus();
    }

    /**
     * Description:页面加载后弹出等待框，等待设备可被控制状态回调，如果一直不可被控，等待一段时间后自动退出界面
     */
    private void getStatusOfDevice() {
        // 设备是否可控
        if (isDeviceCanBeControlled()) {
            // 可控则查询当前设备状态
            mDevice.getDeviceStatus();
        } else {
            // 显示等待栏
            progressDialog.show();
            if (mDevice.isLAN()) {
                // 小循环10s未连接上设备自动退出
                mHandler.postDelayed(mRunnable, 10000);
            } else {
                // 大循环20s未连接上设备自动退出
                mHandler.postDelayed(mRunnable, 20000);
            }
        }
    }

    /**
     * 发送指令,下发单个数据点的命令可以用这个方法
     * <p>
     * <h3>注意</h3>
     * <p>
     * 下发多个数据点命令不能用这个方法多次调用，一次性多次调用这个方法会导致模组无法正确接收消息，参考方法内注释。
     * </p>
     *
     * @param key   数据点对应的标识名
     * @param value 需要改变的值
     */
    private void sendCommand(String key, Object value) {
        if (value == null) {
            return;
        }
        int sn = 5;
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(key, value);
        // 同时下发多个数据点需要一次性在map中放置全部需要控制的key，value值
        // hashMap.put(key2, value2);
        // hashMap.put(key3, value3);
        mDevice.write(hashMap, sn);
        Log.i("liang", "下发命令：" + hashMap.toString());
    }

    private boolean isDeviceCanBeControlled() {
        return mDevice.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceControlled;
    }

    private void toastDeviceNoReadyAndExit() {
        Toast.makeText(this, "设备无响应，请检查设备是否正常工作", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new EventBusMessage(1));
        finish();
    }

    private void toastDeviceDisconnectAndExit() {
        Toast.makeText(GosDeviceControlActivity.this, "连接已断开", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new EventBusMessage(1));
        finish();
    }

    /**
     * 展示设备硬件信息
     *
     * @param hardwareInfo
     */
    private void showHardwareInfo(String hardwareInfo) {
        String hardwareInfoTitle = "设备硬件信息";
        new AlertDialog.Builder(this).setTitle(hardwareInfoTitle).setMessage(hardwareInfo)
                .setPositiveButton(R.string.besure, null).show();
    }

    /**
     * Description:设置设备别名与备注
     */
    private void setDeviceInfo() {

        final Dialog mDialog = new AlertDialog.Builder(this).setView(new EditText(this)).create();
        mDialog.show();

        Window window = mDialog.getWindow();
        window.setContentView(R.layout.alert_gos_set_device_info);

        final EditText etAlias;
        final EditText etRemark;
        etAlias = (EditText) window.findViewById(R.id.etAlias);
        etRemark = (EditText) window.findViewById(R.id.etRemark);

        LinearLayout llNo, llSure;
        llNo = (LinearLayout) window.findViewById(R.id.llNo);
        llSure = (LinearLayout) window.findViewById(R.id.llSure);

        if (!TextUtils.isEmpty(mDevice.getAlias())) {
            setEditText(etAlias, mDevice.getAlias());
        }
        if (!TextUtils.isEmpty(mDevice.getRemark())) {
            setEditText(etRemark, mDevice.getRemark());
        }

        llNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        llSure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etRemark.getText().toString())
                        && TextUtils.isEmpty(etAlias.getText().toString())) {
                    myToast("请输入设备别名或备注！");
                    return;
                }
                mDevice.setCustomInfo(etRemark.getText().toString(), etAlias.getText().toString());
                mDialog.dismiss();
                String loadingText = (String) getText(R.string.loadingtext);
                progressDialog.setMessage(loadingText);
                progressDialog.show();
            }
        });

        mDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideKeyBoard();
            }
        });
    }

    /*
     * 获取设备硬件信息回调
     */
    @Override
    protected void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
                                      ConcurrentHashMap<String, String> hardwareInfo) {
        super.didGetHardwareInfo(result, device, hardwareInfo);
        StringBuffer sb = new StringBuffer();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            myToast("获取设备硬件信息失败：" + result.name());
        } else {
            sb.append("Wifi Hardware Version:" + hardwareInfo.get(WIFI_HARDVER_KEY) + "\r\n");
            sb.append("Wifi Software Version:" + hardwareInfo.get(WIFI_SOFTVER_KEY) + "\r\n");
            sb.append("MCU Hardware Version:" + hardwareInfo.get(MCU_HARDVER_KEY) + "\r\n");
            sb.append("MCU Software Version:" + hardwareInfo.get(MCU_SOFTVER_KEY) + "\r\n");
            sb.append("Wifi Firmware Id:" + hardwareInfo.get(WIFI_FIRMWAREID_KEY) + "\r\n");
            sb.append("Wifi Firmware Version:" + hardwareInfo.get(WIFI_FIRMWAREVER_KEY) + "\r\n");
            sb.append("Product Key:" + "\r\n" + hardwareInfo.get(PRODUCT_KEY) + "\r\n");

            // 设备属性
            sb.append("Device ID:" + "\r\n" + mDevice.getDid() + "\r\n");
            sb.append("Device IP:" + mDevice.getIPAddress() + "\r\n");
            sb.append("Device MAC:" + mDevice.getMacAddress() + "\r\n");
        }
        showHardwareInfo(sb.toString());
    }

    /*
     * 设置设备别名和备注回调
     */
    @Override
    protected void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
        super.didSetCustomInfo(result, device);
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
            //myToast("设置成功");
            progressDialog.cancel();
            //  finish();
        } else {
            // myToast("设置失败：" + result.name());
        }
    }

    /*
     * 设备状态改变回调，只有设备状态为可控才可以下发控制命令
     */
    @Override
    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
        super.didUpdateNetStatus(device, netStatus);
        if (netStatus == GizWifiDeviceNetStatus.GizDeviceControlled) {
            mHandler.removeCallbacks(mRunnable);
            progressDialog.cancel();
        } else {
            mHandler.sendEmptyMessage(handler_key.DISCONNECT.ordinal());
        }
    }

    /*
     * 设备上报数据回调，此回调包括设备主动上报数据、下发控制命令成功后设备返回ACK
     */
    @Override
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                  ConcurrentHashMap<String, Object> dataMap, int sn) {
        super.didReceiveData(result, device, dataMap, sn);
        Log.i("liang", "接收到数据:" + dataMap);
        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS && dataMap.get("data") != null) {
            getDataFromReceiveDataMap(dataMap);
            mHandler.sendEmptyMessage(handler_key.UPDATE_UI.ordinal());
        }
    }

}