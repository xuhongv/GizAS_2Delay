package com.gizwits.opensource.appkit.ControlModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.opensource.appkit.ControlModule.adapter.ListViewAdapter;
import com.gizwits.opensource.appkit.ControlModule.bean.TimerListInfBean;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.utils.EventBusMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TimerTaskActivity extends GosControlModuleBaseActivity {


    private ListView listView;

    private Button btnAdd;

    private ListViewAdapter adapter;

    private GizWifiDevice mDevice;


    private List<TimerListInfBean> listInfBeans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_timer);
        setActionBar(true, true, "定时");

        Intent intent = getIntent();
        mDevice = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        listInfBeans = getIntent().getParcelableArrayListExtra("_listInfBean");

        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {


        btnAdd = (Button) findViewById(R.id.btAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TimerTaskActivity.this, AddTimerTaskActivity.class);
                intent.putExtra("_device", mDevice);
                startActivity(intent);

            }
        });


        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListViewAdapter(this, listInfBeans);
        listView.setAdapter(adapter);
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

}
