package com.gizwits.opensource.appkit.ControlModule;

import android.os.Bundle;
import android.widget.NumberPicker;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.utils.EventBusMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TimerRecycleActivity extends GosControlModuleBaseActivity implements NumberPicker.Formatter {


    private NumberPicker mNpRecyHour = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_timer);
        setActionBar(true, true, "循环定时");
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        mNpRecyHour = (NumberPicker) findViewById(R.id.npRecyHour);
        mNpRecyHour.setFormatter(this);
        mNpRecyHour.setMinValue(0);//最小值
        mNpRecyHour.setMaxValue(23);//最大值
        mNpRecyHour.setValue(1);//设置初始选定值

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
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }
}
